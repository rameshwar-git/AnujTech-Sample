package com.anujtech.app.adminpages;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anujtech.app.R;
import com.anujtech.app.adapter.CVUpdateAdapter;
import com.anujtech.app.datamodel.ImageDataModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UpdateCVInterview extends AppCompatActivity {

    //Array
    private final ArrayList<String> mFileName = new ArrayList<>();
    private final ArrayList<String> mFileUri = new ArrayList<>();
    private final ArrayList<String> mContact1 = new ArrayList<>();
    private final ArrayList<String> mContact2 = new ArrayList<>();
    private final ArrayList<String> mDate = new ArrayList<>();
    private final ArrayList<String> mTimeStamp = new ArrayList<>();
    //firebase

    FirebaseDatabase mDatabase;
    DatabaseReference databaseReference;
    private ProgressBar progressBar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_download);
        progressBar = findViewById(R.id.interviewProgress);
        progressBar.setVisibility(View.VISIBLE);
        //Recycler View
        RecyclerView mRecyclerView = findViewById(R.id.interviewRecycler);
        CVUpdateAdapter mAdapter = new CVUpdateAdapter(this, mFileName, mFileUri, mContact1, mContact2, mDate, mTimeStamp);
        mRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //database
        mDatabase= FirebaseDatabase.getInstance();
        databaseReference=mDatabase.getReference();
        databaseReference.child("Interview")
                .child("CV")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    ImageDataModel downloadDataModel = snap.getValue(ImageDataModel.class);
                    mFileName.add(downloadDataModel.getFilename());
                    mFileUri.add(downloadDataModel.getImageUrl());
                    mTimeStamp.add(downloadDataModel.getmTimeStamp());
                    mDate.add(downloadDataModel.getmDate());
                    mContact1.add(downloadDataModel.getMob1());
                    mContact2.add(downloadDataModel.getMob2());
                    mAdapter.notifyDataSetChanged();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
