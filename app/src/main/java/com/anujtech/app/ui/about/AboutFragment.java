package com.anujtech.app.ui.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.anujtech.app.R;
import com.anujtech.app.ui.profile.LoginFragment;

public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        view.findViewById(R.id.about_profile).setOnClickListener(this::onClick);
        view.findViewById(R.id.app_rating).setOnClickListener(this::onClick);
        view.findViewById(R.id.about_policy).setOnClickListener(this::onClick);
        view.findViewById(R.id.about_share).setOnClickListener(this::onClick);
        return view;
    }
    private void onClick(View view) {
        if (view.getId()==R.id.about_profile) {
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().replace(R.id.fragment_about,new LoginFragment()).commit();
        } else if (view.getId() == R.id.app_rating) {
            String packagName=view.getContext().getPackageName();
            try {
                Uri marketUri = Uri.parse("market://details?id=" + packagName);
                Intent intent=new Intent(Intent.ACTION_VIEW,marketUri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
                        |Intent.FLAG_ACTIVITY_NEW_DOCUMENT
                        |Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                view.getContext().startActivity(intent);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else if (view.getId()==R.id.about_policy) {
            getFragmentManager().beginTransaction().replace(R.id.fragment_about, new TermAndPolicyFragment()).commit();
        }
        else if (view.getId()==R.id.about_share) {
            try {
                // Get package name and construct link
                String packageName = view.getContext().getPackageName(); // Or BuildConfig.APPLICATION_ID
                String playStoreLink = "https://play.google.com/store/apps/details?id=" + packageName;

                // Create the Intent
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain"); // Set MIME type for text

                // --- Customize your share message ---
                String subject = "Anuj Tech"; // Optional: Subject for email apps
                String shareBody = "Hey! Stay Updated for New Vacancy Through this App:\n\n" + playStoreLink;
                // ------------------------------------

                shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject); // Add the subject
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);   // Add the message body (with link)

                // Create a chooser Intent to always show the share options
                Intent chooserIntent = Intent.createChooser(shareIntent, "Share App via");

                // Verify that the intent will resolve to an activity
                if (chooserIntent.resolveActivity(view.getContext().getPackageManager()) != null) {
                    view.getContext().startActivity(chooserIntent);
                } else {
                    Toast.makeText(view.getContext(), "No app found to share.", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                // Handle any unexpected exceptions
                e.printStackTrace(); // Log the error
                Toast.makeText(view.getContext(), "Unable to share app.", Toast.LENGTH_SHORT).show();
            }
        }

    }

}