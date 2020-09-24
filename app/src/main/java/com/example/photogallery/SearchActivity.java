package com.example.photogallery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {
    public static final int FILTER_APPLIED = 1;
    public static final int FILTER_CLEARED = -1;

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
            ((EditText) findViewById(R.id.date_from_text)).setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(today));
            ((EditText) findViewById(R.id.date_to_text)).setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(tomorrow));

        } catch (Exception ex) {
        }


        // invoke search logic
        Button applySearchButton = (Button) findViewById(R.id.btnApplySearch);
        applySearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                EditText from = findViewById(R.id.date_from_text);
                EditText to = findViewById(R.id.date_to_text);
                EditText keywords = findViewById(R.id.keyword_search_text);
                i.putExtra("STARTTIMESTAMP", from.getText() != null ? from.getText().toString() : "");
                i.putExtra("ENDTIMESTAMP", to.getText() != null ? to.getText().toString() : "");
                i.putExtra("KEYWORDS", keywords.getText() != null ? keywords.getText().toString() : "");
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
                from.setText(null);
                to.setText(null);
                keywords.setText(null);
                i.putExtra("STARTTIMESTAMP", "");
                i.putExtra("ENDTIMESTAMP", "");
                i.putExtra("KEYWORDS", "");
                setResult(FILTER_CLEARED, i);
                finish();
            }
        });
    }
}
