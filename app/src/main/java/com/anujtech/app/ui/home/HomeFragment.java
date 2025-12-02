package com.anujtech.app.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anujtech.app.R;
import com.anujtech.app.ui.home.homebutton.CVDownload;
import com.anujtech.app.ui.home.homebutton.InterviewDownload;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class HomeFragment extends Fragment {
    public HomeFragment() {
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_home, container, false );
        view.findViewById( R.id.btn_interview ).setOnClickListener( this::onClick );
        view.findViewById( R.id.btn_cvinterview).setOnClickListener( this::onClick );
        EdgeToEdge.enable(getActivity());
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        View view=getView();
        assert view != null;
        AdView madview=view.findViewById(R.id.adview_banner);
        AdView medium=view.findViewById(R.id.banner_ad_medium);
        AdRequest adRequest=new AdRequest.Builder().build();
        if(madview!=null)
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                madview.loadAd(adRequest);
            }, 500);
        if (medium != null) {
            medium.loadAd(adRequest);
        }
    }
    public void onClick(@NonNull View view) {
        assert getFragmentManager() != null;
        if (view.getId() == R.id.btn_interview) {
            Intent intent = new Intent(getActivity(), InterviewDownload.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btn_cvinterview) {
            Intent intent = new Intent(getActivity(), CVDownload.class);
            startActivity(intent);
        }
    }
}