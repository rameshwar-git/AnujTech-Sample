package com.anujtech.app.ui.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anujtech.app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;


public class ForgetPassword extends AppCompatActivity {
    Button submit;
    EditText email;
    FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_forgetpassword );
        email = findViewById( R.id.forgetEmailId );
        submit = findViewById( R.id.forgetsubmit );
        submit.setOnClickListener( this::onClick );

    }

    private void onClick(View view) {
        if (view.getId() == R.id.submit) {
            firebaseAuth.sendPasswordResetEmail( email.getText().toString() )
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Snackbar.make( view, "Email Sent Successfully.", Snackbar.LENGTH_LONG ).show();
                        }
                    })
                    .addOnFailureListener( new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make( view, "Reset Unsuccessfully.", Snackbar.LENGTH_LONG ).show();
                        }
                    } );
            Snackbar.make( view, "Email Sent Sucessfully.", Snackbar.LENGTH_LONG ).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}