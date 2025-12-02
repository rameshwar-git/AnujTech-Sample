package com.anujtech.app.ui.notifications;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anujtech.app.R;
import com.anujtech.app.adapter.NotificationAdapter;
import com.anujtech.app.datamodel.ImageDataModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private final ArrayList<String> mFileName = new ArrayList<>();
    private final ArrayList<String> mDate = new ArrayList<>();
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://dheeraj-2e570-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference databaseReference = mDatabase.getReference("Interview");
    private NotificationAdapter mAdapter;
    private ProgressBar progressBar;
    public NotificationsFragment() {

    }
    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_notifications, container, false );
        view.findViewById(R.id.notification_recycler);
        //view.findViewById(R.id.button).setOnClickListener(this::onClick);
        //MyFirebaseMessageSender myFirebaseMessageSender= new MyFirebaseMessageSender();

        //bar
        progressBar = view.findViewById(R.id.notificationProgress);
        progressBar.setVisibility(View.VISIBLE);

        //declaration
        RecyclerView mRecyclerView = view.findViewById(R.id.notification_recycler);
        mAdapter = new NotificationAdapter(getContext(), mFileName, mDate);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        databaseReference.child("Clint").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    ImageDataModel downloadDataModel = snap.getValue(ImageDataModel.class);
                    assert downloadDataModel != null;
                    mFileName.add(downloadDataModel.getFilename());
                    mDate.add(downloadDataModel.getmDate());
                    mAdapter.notifyDataSetChanged();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Cancelled", "Image Load Error From Internet");
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }
}