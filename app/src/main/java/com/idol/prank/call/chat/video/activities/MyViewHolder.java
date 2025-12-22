package com.idol.prank.call.chat.video.activities;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.idol.prank.call.chat.video.R;

public class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;

    MyViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.image_view);
    }
}
