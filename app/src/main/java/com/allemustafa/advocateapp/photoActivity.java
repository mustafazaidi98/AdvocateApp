package com.allemustafa.advocateapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class photoActivity extends AppCompatActivity {

    ImageView picture;
    ImageView party;
    TextView office;
    TextView name;
    TextView location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        picture = findViewById(R.id.photo1);
        party = findViewById(R.id.icon1);
        name = findViewById(R.id.name1);
        office = findViewById(R.id.office1);
        location = findViewById(R.id.Location);
        officialClass obj = (officialClass) getIntent().getSerializableExtra("officialObject");
        if (obj.getPhotoUrl().isEmpty() == false) {
            Picasso.get().load(obj.getPhotoUrl()).error(R.drawable.brokenimage)
                    .into(picture);
        }
        View root = party.getRootView();
        if (obj.getParty().equalsIgnoreCase("Republic Party")) {
            root.setBackgroundColor(getResources().getColor(R.color.red));
            party.setImageResource(R.drawable.rep_logo);
        } else if (obj.getParty().equalsIgnoreCase("Democratic Party")) {
            root.setBackgroundColor(getResources().getColor(R.color.blue));
            party.setImageResource(R.drawable.dem_logo);
        } else {
            root.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            party.setVisibility(View.GONE);
        }
        location.setText(getIntent().getStringExtra("location"));
        name.setText(obj.getName());
        office.setText(obj.getOffice());
    }
}