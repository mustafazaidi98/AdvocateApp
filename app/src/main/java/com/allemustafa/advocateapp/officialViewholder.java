package com.allemustafa.advocateapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class officialViewholder extends RecyclerView.ViewHolder {
    ImageView picture;
    TextView name;
    TextView position;
    public officialViewholder(@NonNull View itemView) {
        super(itemView);
        this.name = itemView.findViewById(R.id.name);
        this.position = itemView.findViewById(R.id.position);
        this.picture = itemView.findViewById(R.id.picture);
    }
}
