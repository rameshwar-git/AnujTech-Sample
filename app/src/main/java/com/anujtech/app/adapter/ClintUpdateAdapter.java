package com.anujtech.app.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anujtech.app.R;
import com.anujtech.app.adminpages.UpdateClinteInterview;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ClintUpdateAdapter extends RecyclerView.Adapter<ClintUpdateAdapter.ViewHolder> {


    //Declaring variables
    public ArrayList<String> mFileName;
    public ArrayList<String> mFileUri;
    public Context mcontext;
    public ArrayList<String> mContact1;
    public ArrayList<String> mContact2;
    public ArrayList<String> mDate;
    public ArrayList<String> mTimestamp;

    public ClintUpdateAdapter(Context mcontext, ArrayList<String> mFileName, ArrayList<String> mFileUri, ArrayList<String> mContact1, ArrayList<String> mContact2, ArrayList<String> mDate, ArrayList<String> mTimestamp) {
        this.mcontext = mcontext;
        this.mFileName = mFileName;
        this.mFileUri = mFileUri;
        this.mContact1 = mContact1;
        this.mContact2 = mContact2;
        this.mDate = mDate;
        this.mTimestamp = mTimestamp;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FirebaseApp.initializeApp(parent.getContext());
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.update, parent, false);
        return new ViewHolder(view, this);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.date.setText(mDate.get(position));
        holder.text.setText(mTimestamp.get(position));
        holder.contact1.setText(mContact1.get(position));
        holder.contact2.setText(mContact2.get(position));
        //Glide Image
        Glide.with(mcontext)
                .load(Uri.parse(mFileUri.get(position)))
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mFileUri.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder {

        private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = mDatabase.getReference();

        final ClintUpdateAdapter mClintUpdateAdapter;
        private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        ImageView imageView;
        Button delete, update;
        EditText date, contact1, contact2;
        TextView text;


        public ViewHolder(@NonNull View itemView, ClintUpdateAdapter mClintUpdateAdapter) {
            super(itemView);
            this.mClintUpdateAdapter = mClintUpdateAdapter;
            text = itemView.findViewById(R.id.update_text);
            date = itemView.findViewById(R.id.update_date);
            imageView = itemView.findViewById(R.id.update_image_file);
            contact1 = itemView.findViewById(R.id.update_contact1);
            contact2 = itemView.findViewById(R.id.update_contact2);
            delete = itemView.findViewById(R.id.delete);
            update = itemView.findViewById(R.id.upload);

            for (Button button : Arrays.asList(delete, update)) {
                button.setOnClickListener(this::onClick);
            }
        }

        private void onClick(View view) {
            if (view.getId() == R.id.upload) {
                HashMap<String, Object> update = new HashMap<>();
                update.put("mDate", date.getText().toString());
                update.put("mob1", contact1.getText().toString());
                update.put("mob2", contact2.getText().toString());
                DatabaseReference upload = databaseReference.child("Interview").child("Clint")
                        .child(mClintUpdateAdapter.mTimestamp.get(getAdapterPosition()));
                upload.updateChildren(update)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //Toast.makeText(imageView.getContext(), "Updated", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(mClintUpdateAdapter.mcontext, UpdateClinteInterview.class);
                                view.getContext().startActivity(intent);
                            }
                        });
            } else if (view.getId() == R.id.delete) {
                DatabaseReference upload = databaseReference.child("Interview").child("Clint")
                        .child(mClintUpdateAdapter.mTimestamp.get(getAdapterPosition()));
                upload.removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                    StorageReference storageReference = storageRef.getStorage().getReferenceFromUrl(mClintUpdateAdapter.mFileUri.get(getPosition()));
                                    storageReference.delete();
                                    //Toast.makeText(imageView.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(mClintUpdateAdapter.mcontext, UpdateClinteInterview.class);
                                view.getContext().startActivity(intent);
                            }
                        });
            }
        }
    }

}
