package com.anujtech.app.adminpages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.anujtech.app.R;

public class Adminpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        EdgeToEdge.enable( this );
        setContentView( R.layout.activity_adminpage );
        findViewById( R.id.btn_upload_interview ).setOnClickListener( this::onClick );
        findViewById(R.id.btn_cv_upload).setOnClickListener(this::onClick);
        findViewById(R.id.btn_update_interview).setOnClickListener(this::onClick);
        findViewById(R.id.btn_cv_update).setOnClickListener(this::onClick);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btn_upload_interview) {
            Intent intent = new Intent( this, InterviewUpload.class );
            startActivity( intent );
        } else if (view.getId() == R.id.btn_cv_upload) {
            Intent intent = new Intent(this, CVUpload.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btn_update_interview) {
            Intent intent = new Intent(this, UpdateClinteInterview.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btn_cv_update) {
            Intent intent = new Intent(this, UpdateCVInterview.class);
            startActivity( intent );
        }
    }
}