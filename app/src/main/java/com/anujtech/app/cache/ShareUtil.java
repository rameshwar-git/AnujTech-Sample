package com.anujtech.app.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ShareUtil {

    /**
     * Shares an image from a given URL using an Intent.
     * This operation is performed in the background.
     *
     * @param context The context (e.g., Activity or Application context).
     * @param imageUrl The URL of the image to share.
     */
    public static Uri shareImageFromUrl(final Context context, final String imageUrl) {
        // Using AsyncTask for simplicity, but consider modern alternatives
        new AsyncTask<String, Void, Uri>() {
            @Override
            protected Uri doInBackground(String... urls) {
                String urlString = urls[0];
                Bitmap bitmap = null;
                Uri imageUri = null;

                try {
                    // 1. Download the image
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(input);
                    if (bitmap != null) {
                        // 2. Save the image locally (to app's cache directory)
                        File cachePath = new File(context.getCacheDir(), "images");
                        cachePath.mkdirs(); // Create the directory if it doesn't exist
                        File file = new File(cachePath, "shared_image.png");
                        FileOutputStream stream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        stream.close();

                        // 3. Get a content URI using FileProvider
                        //    "YOUR_PACKAGE_NAME.fileprovider" must match the authority in your Manifest
                        imageUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle download or file saving errors
                }

                return imageUri;
            }

        }.execute();
        return Uri.parse(imageUrl);
    }
}