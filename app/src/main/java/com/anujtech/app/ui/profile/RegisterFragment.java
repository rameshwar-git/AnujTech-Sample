package com.anujtech.app.ui.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.anujtech.app.R;
import com.anujtech.app.datamodel.RegDataModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterFragment extends Fragment {


    EditText mname, mdob, memail, mphone, maddress, mpassword;
    Button msubmit, mback;
    FirebaseAuth mauth;
    ProgressBar progressBar;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference().child( "User Info" );

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_register, container, false );
        mback = view.findViewById( R.id.back );
        mback.setOnClickListener( this::onClick );
        msubmit = view.findViewById( R.id.submit );
        msubmit.setOnClickListener( this::onClick );

        mname = view.findViewById( R.id.uname );
        mdob = view.findViewById( R.id.dob );
        memail = view.findViewById( R.id.email );
        mpassword = view.findViewById( R.id.password );
        mphone = view.findViewById( R.id.phone );
        maddress = view.findViewById( R.id.address );
        progressBar = view.findViewById( R.id.regprogressBar );

        mauth = FirebaseAuth.getInstance();
        return view;
    }

    public void onClick(View view) {

        if (view.getId() == R.id.back) {
            getFragmentManager().beginTransaction().remove( this ).replace( R.id.fragment_register, new LoginFragment() ).commit();
        } else if (view.getId() == R.id.submit) {
            String inemail = memail.getText().toString().trim();
            String inpassword = mpassword.getText().toString().trim();

            if (TextUtils.isEmpty( inemail )) {
                memail.setError( "Email is required." );
                return;
            }
            if (TextUtils.isEmpty( inpassword )) {
                mpassword.setError( "Password is Required" );
                return;
            }
            if (mpassword.length() < 6) {
                mpassword.setError( "Password must be >=6 digit" );
                return;
            }
            progressBar.setVisibility( View.VISIBLE );
            mauth.createUserWithEmailAndPassword( inemail, inpassword ).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mauth.getCurrentUser();
                        assert firebaseUser != null;
                        String uid = firebaseUser.getUid();

                        RegDataModel regDataModel = new RegDataModel( mname.getText().toString(), mdob.getText().toString(), memail.getText().toString(), mphone.getText().toString(), maddress.getText().toString() );

                        databaseReference.child( uid ).setValue( regDataModel ).addOnFailureListener( new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make( view, "Error occure", Snackbar.LENGTH_LONG ).show();
                            }
                        } );
                        progressBar.setVisibility( View.INVISIBLE );
                        Snackbar.make( view, "Registered Successfully.", Snackbar.LENGTH_LONG ).show();
                        getFragmentManager().beginTransaction().replace( R.id.fragment_register, new LoginFragment() ).commit();

                    } else {
                        if (task.getException() instanceof com.google.firebase.auth.FirebaseAuthUserCollisionException) {
                            Toast.makeText( getActivity(), "Email Already Registered. ", Toast.LENGTH_SHORT ).show();
                            progressBar.setVisibility( View.INVISIBLE );
                        } else {
                            Toast.makeText( getActivity(), task.getException().toString(), Toast.LENGTH_SHORT ).show();
                            progressBar.setVisibility( View.INVISIBLE );
                        }
                    }
                }
            } );

        }
    }

}