package com.allemustafa.advocateapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
public class officialDetailActivity extends AppCompatActivity {

    ImageView picture;
    ImageView party;
    ImageView FBphoto;
    ImageView TWphoto;
    ImageView YTphoto;
    TextView name;
    TextView partyName;
    TextView address;
    TextView office;
    TextView website;
    TextView phone;
    TextView websiteLabel;
    TextView locationDetail;
    TextView email;
    TextView emailLabel;
    String FB;
    String YT;
    String TW;
    private String websiteLink;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private officialClass nt;
    private boolean OpenActivity = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_detail);
        picture = findViewById(R.id.ofcPicture);
        party = findViewById(R.id.partyPicture);
        FBphoto = findViewById(R.id.facebook);
        YTphoto = findViewById(R.id.Youtube);
        TWphoto = findViewById(R.id.Twitter);
        name = findViewById(R.id.Name);
        partyName = findViewById(R.id.Party);
        address = findViewById(R.id.Address);
        office = findViewById(R.id.Office);
        website = findViewById(R.id.website);
        websiteLabel = findViewById(R.id.weblabel);
        locationDetail = findViewById(R.id.locatoionDetail);
        emailLabel = findViewById(R.id.emaillabel);
        email = findViewById(R.id.Email);
        phone = findViewById(R.id.Phone);
        officialClass obj = (officialClass) getIntent().getSerializableExtra("officialObject");
        locationDetail.setText(getIntent().getStringExtra("Location"));
        name.setText(obj.getName());
        partyName.setText(obj.getParty());
        email.setText(obj.getEmail());
        office.setText(obj.getOffice());
        websiteLink = obj.getUrl();
        nt = obj;
        if (obj.getAddress().isEmpty() == false)
            address.setText(obj.getAddress());
        if (obj.getUrl().isEmpty() == false)
            website.setText(obj.getUrl());
        if (obj.getPhone().isEmpty() == false)
            phone.setText(obj.getPhone());
        FB = obj.getFacebook();
        TW = obj.getTwitter();
        YT = obj.getYoutube();
        if (FB.isEmpty())
            FBphoto.setVisibility(View.GONE);
        if (YT.isEmpty())
            YTphoto.setVisibility(View.GONE);
        if (TW.isEmpty())
            TWphoto.setVisibility(View.GONE);
        if(obj.getUrl().isEmpty()){
            website.setVisibility(View.GONE);
            websiteLabel.setVisibility(View.GONE);
        }
        if(obj.getEmail().isEmpty()){
            email.setVisibility(View.GONE);
            emailLabel.setVisibility(View.GONE);
        }
        if (obj.getPhotoUrl().isEmpty() == false) {
            Picasso.get().load(obj.getPhotoUrl()).error(R.drawable.brokenimage)
                    .into(picture);
        }
        else{
            OpenActivity = false;
        }
        View root = findViewById(R.id.Cons);
        if (obj.getParty().equalsIgnoreCase("Republic Party")) {
            root.setBackgroundColor(getResources().getColor(R.color.red));
            party.setImageResource(R.drawable.rep_logo);
        } else if (obj.getParty().equalsIgnoreCase("Democratic Party")) {
            root.setBackgroundColor(getResources().getColor(R.color.blue));
            party.setImageResource(R.drawable.dem_logo);
        } else {
            root.setBackgroundColor(getResources().getColor(android.R.color.black));
            party.setVisibility(View.GONE);
        }
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleResult);
    }

    private <O> void handleResult(O o) {
    }

    public void photoClick(View v) {
        if(OpenActivity==false)return;
        Intent intent = new Intent(this, photoActivity.class);
        intent.putExtra("officialObject", nt);
        intent.putExtra("location", locationDetail.getText().toString());
        activityResultLauncher.launch(intent);
    }
    public void sendEmail(View v) {
        String[] addresses = new String[]{email.getText().toString(), "some.person@somemail.edu"};

        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));

        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, "This comes from EXTRA_SUBJECT");
        intent.putExtra(Intent.EXTRA_TEXT, "Email text body from EXTRA_TEXT...");

        // Check if there is an app that can handle mailto intents
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles SENDTO (mailto) intents");
        }
    }
    public void openWebsite(View v) {
        OpenLink(websiteLink);
    }
    public void openWebSite2(View v){
        if (nt.getParty().equalsIgnoreCase("Republic Party")) {
            OpenLink("https://www.gop.com/");
        } else if (nt.getParty().equalsIgnoreCase("Democratic Party")) {
            OpenLink("https://democrats.org/");
        }
    }
    private void OpenLink(String link) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles ACTION_VIEW (https) intents");
        }
    }

    public void openFacebook(View v){
        String FACEBOOK_URL = "https://www.facebook.com/"+FB;
        Intent intent;
        if (isPackageInstalled("com.facebook.katana")) {
            String urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToUse));
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK_URL));
        }

        // Check if there is an app that can handle fb or https intents
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles ACTION_VIEW (fb/https) intents");
        }

    }
    public void youTubeClicked(View v) {;
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + YT));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/" + YT)));
        }
    }

        public boolean isPackageInstalled(String packageName) {
        try {
            return getPackageManager().getApplicationInfo(packageName, 0).enabled;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    private void makeErrorAlert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg);
        builder.setTitle("No App Found");

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void twitterClicked(View v){
        Intent intent = null;
        try {
            // get the Twitter app if possible
            this.getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id="+TW));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/"+TW));
        }
        this.startActivity(intent);
    }
    public void clickMap(View v) {
        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address.getText().toString()));
        Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles ACTION_VIEW (geo) intents");
        }
    }
    public void clickCall(View v) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone.getText().toString()));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles ACTION_DIAL (tel) intents");
        }
    }
}