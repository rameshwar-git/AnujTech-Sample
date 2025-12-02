package com.anujtech.app.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.anujtech.app.R;
import com.anujtech.app.ui.home.homebutton.InterviewDownload;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    public Context mcontext;
    //Declaring variables
    public ArrayList<String> mFileName;
    public ArrayList<String> mDate;

    public NotificationAdapter(Context mcontext, ArrayList<String> mFileName, ArrayList<String> mDate) {
        this.mcontext = mcontext;
        this.mFileName = mFileName;
        this.mDate = mDate;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification, parent, false);
        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText("New Vacancy");
        holder.date.setText(mDate.get(position));
        //Glide Image

    }

    @Override
    public int getItemCount() {
        return mFileName.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        final NotificationAdapter mDownloadAdapter;
        TextView date,title;
        CardView cardView;

        public ViewHolder(@NonNull View itemView, NotificationAdapter downloadAdapter) {
            super(itemView);
            this.mDownloadAdapter = downloadAdapter;
            date = itemView.findViewById(R.id.notify_message);
            title=itemView.findViewById(R.id.notify_title);
            cardView=itemView.findViewById(R.id.notification_card);
            cardView.setOnClickListener(this::onClick);

        }

        private void onClick(View view) {
            Intent intent=new Intent(view.getContext(), InterviewDownload.class);
            view.getContext().startActivity(intent);
        }
    }
}
