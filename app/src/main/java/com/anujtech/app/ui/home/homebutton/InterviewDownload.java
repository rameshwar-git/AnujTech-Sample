package com.anujtech.app.ui.home.homebutton;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anujtech.app.R;
import com.anujtech.app.adapter.DownloadAdapter;
import com.anujtech.app.ads.ClientInterstialAd;
import com.anujtech.app.datamodel.ImageDataModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InterviewDownload extends AppCompatActivity {
    //Array
    private final ArrayList<String> mFileName = new ArrayList<>();
    private final ArrayList<String> mFileUri = new ArrayList<>();
    private final ArrayList<String> mContact1 = new ArrayList<>();
    private final ArrayList<String> mContact2 = new ArrayList<>();
    private final ArrayList<String> mDate = new ArrayList<>();
    //firebase
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReference = mDatabase.getReference("Interview");
    private DownloadAdapter mAdapter;
    private ProgressBar progressBar;
    private TextView info;
    private AdView mAdViewBottom;
    private AdView mAdViewTop;
    private ClientInterstialAd clientInterstialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_download);
        RecyclerView mRecyclerView = findViewById(R.id.interviewRecycler);
        mAdapter = new DownloadAdapter(this, mFileName, mFileUri, mContact1, mContact2, mDate);
        mRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        progressBar = findViewById(R.id.interviewProgress);
        progressBar.setVisibility(View.VISIBLE);
        mAdViewBottom = findViewById(R.id.adview_banner_interview_bottom);
        mAdViewTop = findViewById(R.id.adview_banner_interview_top);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        clientInterstialAd = new ClientInterstialAd(getApplicationContext(),this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(mAdViewBottom!=null) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                mAdViewBottom.loadAd(new AdRequest.Builder().build());
            }, 100); // Small delay helps avoid UI congestion
        }
        if (mAdViewTop != null) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                mAdViewTop.loadAd(new AdRequest.Builder().build());
            }, 200); // Small delay helps avoid UI congestion
        }
        info=findViewById(R.id.info);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            databaseReference.child("Clint").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ImageDataModel downloadDataModel=null;
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        downloadDataModel = snap.getValue(ImageDataModel.class);
                        mFileName.add(downloadDataModel.getFilename());
                        mFileUri.add(downloadDataModel.getImageUrl());
                        mDate.add(downloadDataModel.getmDate());
                        mContact1.add(downloadDataModel.getMob1());
                        mContact2.add(downloadDataModel.getMob2());
                    }
                    mAdapter.notifyDataSetChanged();
                    if (downloadDataModel==null)
                        info.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Cancelled", "Image Load Error From Internet");
                }
            });
        }, 500);
    }
}