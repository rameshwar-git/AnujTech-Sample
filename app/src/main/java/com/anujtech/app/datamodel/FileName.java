package com.anujtech.app.datamodel;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.util.Objects;

public class FileName {
    private Context mContex;
    private Uri uri;

    public FileName() {
    }

    public FileName(Context mContext, Uri uri) {
        this.mContex = mContext;
        this.uri = uri;
    }

    public Context getmContex() {
        return mContex;
    }

    public Uri getUri() {
        return uri;
    }

    public String ImageName(Context context, Uri fileuri) {
        if (Objects.equals(fileuri.getScheme(), "content")) {
            try (Cursor cursor = context.getContentResolver().query(fileuri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    return cursor.getString(nameIndex);
                }
            }
        } else if (fileuri.getScheme().equals("file")) {
            String path = fileuri.getPath();
            if (path != null) {
                int cut = path.lastIndexOf('/');
                if (cut != -1) {
                    return path.substring(cut + 1);
                }
            }
        }
        return null;
    }
}
