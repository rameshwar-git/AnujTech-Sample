package com.anujtech.app;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.anujtech.app.databinding.ActivityMainBinding;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AppUpdateManager appUpdateManager;
    private AppUpdateInfo appUpdateInfo; // Store to access later for flexible update install
    private ActivityResultLauncher<IntentSenderRequest> activityResultLauncher;
    // Choose either FLEXIBLE or IMMEDIATE
    private static final int UPDATE_TYPE = AppUpdateType.FLEXIBLE; // Or AppUpdateType.IMMEDIATE

    private final InstallStateUpdatedListener installStateUpdatedListener = state -> {
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            // Flexible update downloaded: notify user to install
            popupSnackbarForCompleteUpdate();
        } else if (state.installStatus() == InstallStatus.FAILED) {
            Log.e("UpdateCheck", "Flexible update installation failed. Reason: " + state.installErrorCode());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        // menu should be considered as top level destinations.
        binding = ActivityMainBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );
        BottomNavigationView navView = findViewById( R.id.nav_view );
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_notifications, R.id.navigation_about)
                .build();
        NavController navController = Navigation.findNavController( this, R.id.nav_host_fragment_activity_main );
        NavigationUI.setupActionBarWithNavController( this, navController, appBarConfiguration );
        NavigationUI. setupWithNavController( binding.navView, navController );
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseApp.initializeApp(getApplicationContext());
        MobileAds.initialize(getApplicationContext());
        // Passing each menu ID as a set of Ids because each
        askNotificationPermission();
        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());

        // Register the activity result launcher
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartIntentSenderForResult(),
                result -> {
                    // Handle the result of the update flow
                    if (result.getResultCode() != Activity.RESULT_OK) {
                        Log.w("UpdateCheck", "Update flow failed! Result code: " + result.getResultCode());
                        // Optional: Handle cancellation or failure. You could retry the check.
                    } else {
                        Log.d("UpdateCheck", "Update flow successful or accepted.");
                        // If it was an IMMEDIATE update, the app probably restarts before reaching here.
                        // If it was FLEXIBLE, the download starts in the background.
                    }
                });

        // Check for updates
        checkForUpdate();

        if (UPDATE_TYPE == AppUpdateType.IMMEDIATE) {
            appUpdateManager.getAppUpdateInfo().addOnSuccessListener(info -> {
                if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    startUpdateFlow(info); // Restart the update flow
                }
            });
        }
        // Re-check for flexible updates that might have downloaded while the app was paused.
        if (UPDATE_TYPE == AppUpdateType.FLEXIBLE) {
            appUpdateManager.getAppUpdateInfo().addOnSuccessListener(info -> {
                if(info.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate();
                }
            });
        }
        // Register listener for flexible updates (only if using FLEXIBLE)
        if (UPDATE_TYPE == AppUpdateType.FLEXIBLE) {
            appUpdateManager.registerListener(installStateUpdatedListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the listener when the activity is destroyed (only if using FLEXIBLE)
        if (UPDATE_TYPE == AppUpdateType.FLEXIBLE) {
            try {
                appUpdateManager.unregisterListener(installStateUpdatedListener);
            } catch (Exception e) {
                Log.e("UpdateCheck", "Error unregistering listener", e);
            }
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if(isGranted){
                }
            });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                boolean hasPermission = ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED;

                if (!hasPermission) {
                    ActivityCompat.requestPermissions(
                            this,
                            new String[]{Manifest.permission.POST_NOTIFICATIONS},
                            0
                    );
            }
        }
    }
    private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar = Snackbar.make(
                findViewById(android.R.id.content), // Use a root view
                "An update has just been downloaded.",
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RESTART", view -> {
            if (appUpdateManager != null) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.snackbar_action_color)); // Use your color resource
        snackbar.show();
    }
    private void checkForUpdate() {
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(info -> {
            this.appUpdateInfo = info; // Save for later use if needed
            boolean isUpdateAvailable = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE;
            boolean isUpdateAllowed = info.isUpdateTypeAllowed(UPDATE_TYPE);

            Log.d("UpdateCheck", "UpdateAvailable: " + isUpdateAvailable + ", UpdateAllowed: " + isUpdateAllowed + ", Type: " + UPDATE_TYPE);

            if (isUpdateAvailable && isUpdateAllowed) {
                Log.d("UpdateCheck", "Update available and allowed. Starting update flow.");
                startUpdateFlow(info);
            } else {
                Log.d("UpdateCheck", "No update available or update type not allowed.");
            }
        }).addOnFailureListener(e -> {
            Log.e("UpdateCheck", "Failed to check for update.", e);
        });
    }
    private void startUpdateFlow(AppUpdateInfo info) {
        AppUpdateOptions options = AppUpdateOptions.newBuilder(UPDATE_TYPE)
                // Optionally, allow deleting asset packs if needed for storage
                // .setAllowAssetPackDeletion(true)
                .build();

        appUpdateManager.startUpdateFlowForResult(
                info,
                activityResultLauncher, // Use the launcher
                options
        );
    }

}