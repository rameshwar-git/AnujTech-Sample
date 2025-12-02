package com.anujtech.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
    ImageView plane,earth,circle,text;
    //TextView text;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_view);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //background
        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 5 seconds
                    sleep( 2500);

                    // After 5 seconds redirect to another intent
                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(i);

                    //Remove activity
                    finish();
                } catch (Exception e) {
                }
            }
        };
        // start thread
        background.start();

        plane=findViewById(R.id.imageViewPlane);
        earth=findViewById(R.id.imageViewEarth);
        text=findViewById(R.id.imageViewtext);
        circle=findViewById(R.id.imageViewCircle);
        //animation call
        plane.startAnimation(AnimationUtils.loadAnimation(this,R.anim.plane));
        earth.startAnimation(AnimationUtils.loadAnimation(this,R.anim.fade));
        text.startAnimation(AnimationUtils.loadAnimation(this,R.anim.fade));
        circle.startAnimation(AnimationUtils.loadAnimation(this,R.anim.fade));
    }
}
