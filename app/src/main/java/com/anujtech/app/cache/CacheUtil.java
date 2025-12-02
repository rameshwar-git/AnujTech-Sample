package com.anujtech.app.cache;

import android.content.Context;

import java.io.File;

public class CacheUtil {

    // Method to clear the app's internal cache
    public static void clearInternalCache(Context context) {
        try {
            File cacheDir = context.getCacheDir();
            deleteDir(cacheDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to clear the app's external cache
    // Requires READ_EXTERNAL_STORAGE and WRITE_EXTERNAL_STORAGE permissions
    // and consideration forScoped Storage in newer Android versions.
    public static void clearExternalCache(Context context) {
        try {
            File externalCacheDir = context.getExternalCacheDir();
            if (externalCacheDir != null && externalCacheDir.exists()) {
                deleteDir(externalCacheDir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Helper method to delete a directory and its contents
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}