package com.anujtech.app.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anujtech.app.R;

import java.util.ArrayList;

public class UploadAdapter extends RecyclerView.Adapter<UploadAdapter.ViewHolder> {

    //Declaring variables
    public ArrayList<Uri> mFileUri;
    private final Context mcontext;

    public UploadAdapter(Context mcontext, ArrayList<Uri> mFileUri) {
        this.mcontext = mcontext;
        this.mFileUri = mFileUri;
    }

    @NonNull
    @Override
    public UploadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.interview_upload, parent, false);
        return new ViewHolder( view, this );
    }

    @Override
    public void onBindViewHolder(@NonNull UploadAdapter.ViewHolder holder, int position) {
        holder.imageView.setImageURI(mFileUri.get(position));
    }

    @Override
    public int getItemCount() {
        return mFileUri.size();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        final UploadAdapter mUploadAdapter;

        public ViewHolder(@NonNull View itemView, UploadAdapter uploadAdapter) {
            super( itemView );
            imageView = itemView.findViewById(R.id.ufileimage);
            this.mUploadAdapter = uploadAdapter;
        }

    }
}
