package com.anujtech.app.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.anujtech.app.R;
import com.anujtech.app.adminpages.Adminpage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    private EditText lemail, lpassword;
    private TextView forget;
    String email, pass;

    public LoginFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_login, container, false );
        view.findViewById( R.id.fragment_login );
        view.findViewById( R.id.signup ).setOnClickListener( this::onClick );
        view.findViewById( R.id.signin ).setOnClickListener( this::onClick );
        view.findViewById( R.id.forgetpassword ).setOnClickListener( this::onClick );
        lemail = view.findViewById( R.id.loginemail );
        lpassword = view.findViewById( R.id.loginpassword );


        //save user id password

        return view;
    }

    public void onClick(View view) {
        email = lemail.getText().toString();
        pass = lpassword.getText().toString();
        assert getFragmentManager() != null;
        if (view.getId() == R.id.signup) {
            getFragmentManager().beginTransaction().replace( R.id.fragment_login, new RegisterFragment() ).commit();
        } else if (view.getId() == R.id.signin) {
            if (TextUtils.isEmpty( email ) || TextUtils.isEmpty( pass )) {
                /*Intent intent = new Intent( getActivity(), Adminpage.class );
                startActivity( intent );*/
                Toast.makeText( getActivity(), "Email and Password Required", Toast.LENGTH_SHORT ).show();
            } else {
                mauthFireBase( email, pass );
            }
        } else if (view.getId() == R.id.forgetpassword) {
            Intent intent = new Intent( getActivity(), ForgetPassword.class );
            startActivity( intent );
        }
    }
    // for user authentication with google fire base
    public void mauthFireBase(String email, String pass) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword( email, pass ).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            if (FirebaseAuth.getInstance().getUid().equals("dPhFN0GuiWNg0JCVxkskKWZO69Y2")) {
                                Intent intent = new Intent( getActivity(), Adminpage.class );
                                startActivity( intent );
                            } else {
                                getFragmentManager().beginTransaction().replace( R.id.fragment_login, new ProfileFragment() ).commit();
                            }
                            Toast.makeText( getActivity(), "Login Sucessfull.", Toast.LENGTH_SHORT ).show();
                        } else {
                            Toast.makeText( getActivity(), task.getException().toString(), Toast.LENGTH_LONG ).show();
                        }
                    }
                } )
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof com.google.firebase.auth.FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText( getActivity(), "Enter Correct Email and Password", Toast.LENGTH_LONG ).show();
                        }
                    }
                } );
    }

}