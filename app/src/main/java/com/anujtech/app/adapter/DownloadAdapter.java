package com.anujtech.app.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anujtech.app.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {


    public ArrayList<String> mFileUri;

    //Declaring variables
    public ArrayList<String> mFileName;
    public ArrayList<String> mContact1;
    public Context mcontext;
    public ArrayList<String> mContact2;
    public ArrayList<String> mDate;

    public DownloadAdapter(Context mcontext, ArrayList<String> mFileName, ArrayList<String> mFileUri, ArrayList<String> mContact1, ArrayList<String> mContact2, ArrayList<String> mDate) {
        this.mcontext = mcontext;
        this.mFileName = mFileName;
        this.mFileUri = mFileUri;
        this.mContact1 = mContact1;
        this.mContact2 = mContact2;
        this.mDate = mDate;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_download_photo, parent, false);
        return new ViewHolder( view, this );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String mCurrentImage = mFileUri.get(position);
        holder.date.setText(mDate.get(position));
        //Glide Image
        Glide.with( mcontext )
                .load(Uri.parse(mCurrentImage))
                .into( holder.imageView );

    }
    @Override
    public int getItemCount() {
        return mFileUri.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        final DownloadAdapter mDownloadAdapter;
        TextView date;
        ImageButton call1, call2, share;

        public ViewHolder(@NonNull View itemView, DownloadAdapter downloadAdapter) {
            super( itemView );
            this.mDownloadAdapter = downloadAdapter;
            date = itemView.findViewById(R.id.date);
            imageView = itemView.findViewById(R.id.fileimage);
            imageView.setOnClickListener(this::onClick);
            call1 = itemView.findViewById(R.id.call1);
            call2 = itemView.findViewById(R.id.call2);

            share = itemView.findViewById( R.id.share );

            for (ImageButton imageButton : Arrays.asList(call1, call2, share)) {
                imageButton.setOnClickListener( this::onClick );
            }
        }

        private void onClick(View view) {
            if (view.getId() == R.id.call1) {
                Intent phonecall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+91" + mDownloadAdapter.mContact1.get(getAdapterPosition())));
                view.getContext().startActivity(phonecall);
            }
            else if (view.getId() == R.id.call2) {
                Intent phonecall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+91" + mDownloadAdapter.mContact2.get(getAdapterPosition())));
                view.getContext().startActivity( phonecall );
            }
            else if (view.getId() == R.id.share) {
                try {
                    // Get package name and construct link
                    String packageName = view.getContext().getPackageName();
                    String playStoreLink = "https://play.google.com/store/apps/details?id=" + packageName;
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain"); // Set MIME type for text
                    String subject = "Anuj Tech"; // Optional: Subject for email apps
                    String shareBody = "Hey! Stay Updated for New Vacancy Through this App Download Now !!\n\n" + playStoreLink;
                   // Uri uri= ShareUtil.shareImageFromUrl(mDownloadAdapter.mcontext,mDownloadAdapter.mFileUri.get(getAdapterPosition()));

                    //shareIntent.setType("image/*");

                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject); // Add the subject
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

                    //shareIntent.putExtra(Intent.EXTRA_STREAM,uri);
                    //shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    // Add the message body (with link)
                    Intent chooserIntent = Intent.createChooser(shareIntent, "Share App via");

                    if (chooserIntent.resolveActivity(view.getContext().getPackageManager()) != null) {
                        view.getContext().startActivity(chooserIntent);
                    } else {
                        Toast.makeText(view.getContext(), "No app found to share.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    // Handle any unexpected exceptions
                    e.printStackTrace(); // Log the error
                    Toast.makeText(view.getContext(), "Unable to share app.", Toast.LENGTH_SHORT).show();
                }
            }
            else if (view.getId() == R.id.fileimage){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mDownloadAdapter.mFileUri.get(getAdapterPosition())));
                intent.setDataAndType(Uri.parse(mDownloadAdapter.mFileUri.get(getAdapterPosition())), "image/*");
                view.getContext().startActivity(intent);
            }
        }
       }
}
