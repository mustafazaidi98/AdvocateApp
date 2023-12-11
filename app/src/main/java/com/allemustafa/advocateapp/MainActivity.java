package com.allemustafa.advocateapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FusedLocationProviderClient mFusedLocationClient;
    private static final int LOCATION_REQUEST = 111;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private static String locationString = "Unspecified Location";
    private officialAdapter mAdapter;
    private RecyclerView rcV;
    private AppDataClass appData;
    private TextView location;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        location = findViewById(R.id.LocationHome);
        text = findViewById(R.id.NoData3);
        rcV = findViewById(R.id.recyclerView);
        rcV.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        activityResultLauncher = registerForActivityResult(
                new StartActivityForResult(),
                this::handleResult);
        mFusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);
        determineLocation();
    }

    private <O> void handleResult(O o) {
    }

    private void determineLocation() {
        // Check perm - if not then start the  request and return
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    // Got last known location. In some situations this can be null.
                    if (location != null) {
                        locationString = getPlace(location);
                        officialDataFetcher.downloadOfficials(this, locationString);
                    }
                })
                .addOnFailureListener(this, e ->
                        Toast.makeText(MainActivity.this,
                                e.getMessage(), Toast.LENGTH_LONG).show());
    }
    private void showLocationDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setView(et);
        builder.setPositiveButton("OK", (dialog, id) -> {
            String Location = et.getText().toString().
                    replaceAll(", ", ",");
            officialDataFetcher.downloadOfficials(this, Location);
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> {
            location.setText(locationString);
        });
        builder.setTitle("Enter the Location");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.search) {
            if (hasNetworkConnection())
                showLocationDialog();
        }
        else if(item.getItemId() == R.id.Info) {
            Intent intent = new Intent(this, AboutActivity.class);
            activityResultLauncher.launch(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    private String getPlace(Location loc) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        String address = "";

        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            address = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    determineLocation();
                } else {
                }
            }
        }
    }

    public boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean connected = (networkInfo != null && networkInfo.isConnectedOrConnecting());
        if (connected == false) {
            rcV.setVisibility(View.GONE);
            location.setText("No Internet Connection");
            text.setVisibility(View.VISIBLE);
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        else{
            rcV.setVisibility(View.VISIBLE);
            location.setText(locationString);
            text.setVisibility(View.GONE);
        }
        return connected;
    }

    public void InvalidLocationSelected(VolleyError error1) {
    }

    public void UpdateData(AppDataClass appData) {
        this.appData = appData;
        location.setText(appData.getAddress());
        mAdapter = new officialAdapter(appData.getOfficialsList(), this);
        rcV.setAdapter(mAdapter);
        rcV.setLayoutManager(new LinearLayoutManager(this));
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.official_menu, menu);
        return true;
    }
    @Override
    public void onClick(View v) {
        int pos = rcV.getChildLayoutPosition(v);
        officialClass nt = appData.getOfficialsList().get(pos);
        Intent intent = new Intent(this, officialDetailActivity.class);
        intent.putExtra("officialObject", nt);
        intent.putExtra("Location",location.getText().toString());
        activityResultLauncher.launch(intent);
    }
}