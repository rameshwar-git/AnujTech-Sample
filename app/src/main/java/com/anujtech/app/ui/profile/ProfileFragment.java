package com.anujtech.app.ui.profile;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.anujtech.app.R;


public class ProfileFragment extends Fragment {

    public ProfileFragment() {

    }

    SharedPreferences sharedPreferences;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        view.findViewById(R.id.logout).setOnClickListener(this::onClick);
        sharedPreferences = getActivity().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        boolean isLoggedIn = checkLoggedIn();
        if (isLoggedIn) {
            getFragmentManager().beginTransaction().replace(R.id.fragment_profile, new LoginFragment()).commit();
        }
        return view;
    }

    public void onStart() {
        super.onStart();
        if (sharedPreferences.contains("uid")) {
            Toast.makeText(getActivity(), sharedPreferences.getString("uid", ""), Toast.LENGTH_SHORT).show();
        }
    }

    private void onClick(View view) {
        if (view.getId() == R.id.logout) {
            clearLoginData();
            getFragmentManager().beginTransaction().replace(R.id.fragment_profile, new LoginFragment()).commit();
        }
    }

    private void clearLoginData() {
        // Clear the saved login data from SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("saveLogin");
        editor.remove("userid");
        editor.apply();
    }

    private boolean checkLoggedIn() {
        // Check if the "saveLogin" flag is set to true in SharedPreferences
        return sharedPreferences.getBoolean("saveLogin", false);
    }
}