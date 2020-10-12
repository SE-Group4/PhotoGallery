package com.example.photogallery;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {
    public static final int FILTER_APPLIED = 1;
    public static final int FILTER_CLEARED = -1;
    static final double[] FALLBACK_COORDINATES = {49.220509, -123.007111};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        try {
            Calendar calendar = Calendar.getInstance();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date now = calendar.getTime();
            String todayStr = new SimpleDateFormat("yyy-MM-dd", Locale.getDefault()).format(now);
            Date today = format.parse((String) todayStr);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            String tomorrowStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
            Date tomorrow = format.parse((String) tomorrowStr);

            // set default values
            ((EditText) findViewById(R.id.date_from_text)).setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(today));
            ((EditText) findViewById(R.id.date_to_text)).setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(tomorrow));
            ((EditText) findViewById(R.id.lat_text)).setText(String.valueOf(getCoordinates()[0]));
            ((EditText) findViewById(R.id.lng_text)).setText(String.valueOf(getCoordinates()[1]));
        } catch (Exception ex) {
        }


        // invoke search logic
        Button applySearchButton = (Button) findViewById(R.id.btnApplySearch);
        applySearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();

                // find search fields by id
                EditText from = findViewById(R.id.date_from_text);
                EditText to = findViewById(R.id.date_to_text);
                EditText keywords = findViewById(R.id.keyword_search_text);
                EditText lat = findViewById(R.id.lat_text);
                EditText lng = findViewById(R.id.lng_text);

                // save their values into the intent
                i.putExtra("STARTTIMESTAMP", from.getText() != null ? from.getText().toString() : "");
                i.putExtra("ENDTIMESTAMP", to.getText() != null ? to.getText().toString() : "");
                i.putExtra("KEYWORDS", keywords.getText() != null ? keywords.getText().toString() : "");
                i.putExtra("LAT", lat.getText() != null ? lat.getText().toString() : "");
                i.putExtra("LNG", lng.getText() != null ? lng.getText().toString() : "");

                setResult(FILTER_APPLIED, i);
                finish();
            }
        });

        // clear input fields
        Button clearButton = (Button) findViewById(R.id.btnClear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                EditText from = findViewById(R.id.date_from_text);
                EditText to = findViewById(R.id.date_to_text);
                EditText keywords = findViewById(R.id.keyword_search_text);
                EditText lat = findViewById(R.id.lat_text);
                EditText lng = findViewById(R.id.lng_text);
                from.setText(null);
                to.setText(null);
                keywords.setText(null);
                lat.setText(null);
                lng.setText(null);
                i.putExtra("STARTTIMESTAMP", "");
                i.putExtra("ENDTIMESTAMP", "");
                i.putExtra("KEYWORDS", "");
                i.putExtra("LAT", "");
                i.putExtra("LNG", "");

                setResult(FILTER_CLEARED, i);
                finish();
            }
        });
    }

    // TODO: move out to a helper class
    private double[] getCoordinates() {
        try {
            LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double[] coordinates = {0, 0};
            if (location != null) {
                coordinates[0] = location.getLatitude();
                coordinates[1] = location.getLongitude();
                return coordinates;
            }
        } catch (SecurityException e) {
            Log.e("getLocationFailed", e.getMessage());
        }
        return FALLBACK_COORDINATES;
    }
}
