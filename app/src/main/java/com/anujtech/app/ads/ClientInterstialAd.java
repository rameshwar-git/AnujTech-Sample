package com.anujtech.app.ads;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class ClientInterstialAd {
    private InterstitialAd mInterstitialAd;
    AdRequest adRequest = new AdRequest.Builder().build();
    public ClientInterstialAd(Context context, Activity activity){
          new Thread(
                () -> {
            MobileAds.initialize(context, initializationStatus -> {
            });
        })
                .start();
        InterstitialAd.load(context, "ca-app-pub-4119321529958098/6027517645", adRequest,
                new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                mInterstitialAd.show(activity);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                mInterstitialAd = null;
            }
        });
    }
}
