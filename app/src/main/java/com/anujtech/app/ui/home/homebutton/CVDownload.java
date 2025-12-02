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
import com.anujtech.app.ads.CVInterstialAd;
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

public class CVDownload extends AppCompatActivity {
    //Array
    private final ArrayList<String> mFileName = new ArrayList<>();
    private final ArrayList<String> mFileUri = new ArrayList<>();
    private final ArrayList<String> mContact1 = new ArrayList<>();
    private final ArrayList<String> mContact2 = new ArrayList<>();
    private final ArrayList<String> mDate = new ArrayList<>();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://dheeraj-2e570-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private DatabaseReference databaseReference = mDatabase.getReference("Interview");
    private DownloadAdapter mAdapter;
    private ProgressBar progressBar;
    private TextView info;
    AdRequest adRequest = new AdRequest.Builder().build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_download);

        //declaration
        RecyclerView mRecyclerView = findViewById(R.id.interviewRecycler);
        mAdapter = new DownloadAdapter(this, mFileName, mFileUri, mContact1, mContact2, mDate);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        progressBar = findViewById(R.id.interviewProgress);
        info=findViewById(R.id.info);
        progressBar.setVisibility(View.VISIBLE);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        new CVInterstialAd(this,this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        AdView mAdViewBottom = findViewById(R.id.adview_banner_interview_bottom);
        AdView mAdViewTop = findViewById(R.id.adview_banner_interview_top);
        new ClientInterstialAd(this,this);
        if(mAdViewBottom!=null) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                mAdViewBottom.loadAd(new AdRequest.Builder().build());
            }, 700); // Small delay helps avoid UI congestion
        }
        if (mAdViewTop != null) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                mAdViewTop.loadAd(new AdRequest.Builder().build());
            }, 1200); // Small delay helps avoid UI congestion
        }
        info=findViewById(R.id.info);
        progressBar = findViewById(R.id.interviewProgress);
        progressBar.setVisibility(View.VISIBLE);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            databaseReference.child("CV").addValueEventListener(new ValueEventListener() {
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