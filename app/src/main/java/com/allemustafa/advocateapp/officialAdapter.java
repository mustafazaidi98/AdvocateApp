package com.allemustafa.advocateapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class officialAdapter extends RecyclerView.Adapter<officialViewholder> {
    private final List<officialClass> officialsList;
    private final MainActivity mainAct;
    public officialAdapter(List<officialClass> officialsList, MainActivity mainAct) {
        this.officialsList = officialsList;
        this.mainAct = mainAct;
    }
    public void onClick(View view) {
    }
    @NonNull
    @Override
    public officialViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.official_ui, parent, false);
        itemView.setOnClickListener(mainAct);
        return new officialViewholder(itemView);
    }
    private static final String TAG = "MainActivity";
    @Override
    public void onBindViewHolder(@NonNull officialViewholder holder, int position) {
        officialClass ofc = officialsList.get(position);
        holder.position.setText(ofc.getOffice());
        holder.name.setText(ofc.getName()+" ("+ofc.getParty()+")");
        String imgUrl = ofc.getPhotoUrl();
        if(imgUrl.isEmpty()==false){
            Picasso.get().load(imgUrl).error(R.drawable.brokenimage)
                    .into(holder.picture, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "onSuccess: ");
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d(TAG, "onError: " + e);
                        }
                    });
        }
        else{
            holder.picture.setImageResource(R.drawable.missing);
        }
    }

    @Override
    public int getItemCount() {
        return officialsList.size();
    }
}
