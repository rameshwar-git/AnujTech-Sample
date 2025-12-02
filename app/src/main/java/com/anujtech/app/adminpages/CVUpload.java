package com.anujtech.app.adminpages;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anujtech.app.R;
import com.anujtech.app.datamodel.FileName;
import com.anujtech.app.datamodel.ImageDataModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class CVUpload extends AppCompatActivity {

    private static final int PICK_IMAGES_REQUEST = 1;
    //Firebase
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    StorageReference storageReference = storage.getReference();
    DatabaseReference databaseReference = mDatabase.getReference();
    FrameLayout frameLayout;
    Button uploadbtn;
    private ProgressBar progressBar;
    //
    private String mFileName;
    private Uri mFileUri;
    private ImageView imageView;
    private TextView mDate, mobile1, mobile2;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interview_upload);

        //
        progressBar = findViewById(R.id.interviewuploadprogress);
        Button select = findViewById(R.id.select);
        select.setOnClickListener(this::onClick);
        uploadbtn = findViewById(R.id.upload);
        uploadbtn.setEnabled(false);
        uploadbtn.setOnClickListener(this::onClick);
        //
        frameLayout = findViewById(R.id.framelayout);
        imageView = findViewById(R.id.ufileimage);
        mobile1 = findViewById(R.id.contact1);
        mobile2 = findViewById(R.id.contact2);
        mDate = findViewById(R.id.idate);
        mDate.setOnClickListener(this::onClick);
        //
        frameLayout.setVisibility(View.INVISIBLE);

    }


    private void onClick(@NonNull View view) {
        if (view.getId() == R.id.select) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGES_REQUEST);
            progressBar.setVisibility(View.INVISIBLE);
            frameLayout.setVisibility(View.VISIBLE);


        }
        else if (view.getId() == R.id.upload) {
            assert mFileUri != null;
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = currentDateTime.format(formatter);
            progressBar.setVisibility(View.VISIBLE);
            Snackbar.make(view, "Uploading...", Snackbar.LENGTH_LONG).show();
            StorageReference upload = storageReference.child("Images").child("CV");
            upload.child(mFileName.substring(mFileName.length()-8)).putFile(mFileUri)
                    .addOnSuccessListener(taskSnapshot -> taskSnapshot.getMetadata().getReference().getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();
                                ImageDataModel imageDataModel = new ImageDataModel(mFileName.substring(39), imageUrl, mobile1.getText().toString(), mobile2.getText().toString(), mDate.getText().toString(), formattedDateTime);
                                databaseReference.child("Interview").child("CV")
                                        .child(formattedDateTime).setValue(imageDataModel);
                            })
                    )
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.INVISIBLE);
                        Snackbar.make(view, "Uploaded Successfully.", Snackbar.LENGTH_LONG).show();
                        imageView.setImageDrawable(null);
                        mFileUri = null;
                    })
                    .addOnFailureListener(exception -> {
                        Log.e("UploadError", "Getting Download URL", exception);
                    });
            uploadbtn.setEnabled(false);
        }
        else if (view.getId()==R.id.idate) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create and show DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            monthOfYear++;
                            String day, month;
                            // Month is 0-indexed, so add 1
                            day = String.valueOf(dayOfMonth);
                            month = String.valueOf(monthOfYear);
                            if (dayOfMonth < 10)
                                day = "0" + dayOfMonth;
                            if (monthOfYear < 10)
                                month = "0" + monthOfYear;
                            mDate.setText(day + "/" + month + "/" + year);
                        }
                    }, year, month, day);
            datePickerDialog.show();

        }
    }
    //image selection
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "Image Not Selected", Toast.LENGTH_LONG).show();
            return;
        }
        if (requestCode == PICK_IMAGES_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                uploadbtn.setEnabled(true);
                mFileUri = data.getData();
                if (null != mFileUri) {
                    mFileName = new FileName(this, mFileUri).toString();
                    imageView.setImageURI(mFileUri);
                }
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}