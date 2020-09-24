package com.example.photogallery;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SearchActivity extends AppCompatActivity {
    String keywordSearchText;
    String dateFromText;
    String dateToText;
    String leftLatText;
    String leftLongText;
    String rightLatText;
    String rightLongText;
    Intent intent;

    public static final int FILTER_APPLIED = 1;
    public static final int FILTER_CLEARED = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        // invoke search logic
        Button applySearchButton = (Button) findViewById(R.id.btnApplySearch);
        applySearchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText text = findViewById(R.id.keyword_search_text);
                    keywordSearchText = text.getText().toString();

                    text = findViewById(R.id.date_from_text);
                    dateFromText = text.getText().toString();

                    text = findViewById(R.id.date_to_text);
                    dateToText = text.getText().toString();

                    text = findViewById(R.id.top_left_lat_text);
                    leftLatText = text.getText().toString();

                    text = findViewById(R.id.top_left_long_text);
                    leftLongText = text.getText().toString();

                    text = findViewById(R.id.bottom_right_lat_text);
                    rightLatText = text.getText().toString();

                    text = findViewById(R.id.bottom_right_long_text);
                    rightLongText = text.getText().toString();

                    intent = new Intent();
                    setResult(FILTER_APPLIED, intent);
                    finish();
                }
        });

        // clear input fields
        Button clearButton = (Button) findViewById(R.id.btnClear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText text = findViewById(R.id.keyword_search_text);
                keywordSearchText = null;
                text.setText(keywordSearchText);

                text = findViewById(R.id.date_from_text);
                dateFromText = null;
                text.setText(dateFromText);

                text = findViewById(R.id.date_to_text);
                dateToText = null;
                text.setText(dateToText);

                text = findViewById(R.id.top_left_lat_text);
                leftLatText = null;
                text.setText(leftLatText);

                text = findViewById(R.id.top_left_long_text);
                leftLongText = null;
                text.setText(leftLongText);

                text = findViewById(R.id.bottom_right_lat_text);
                rightLatText = null;
                text.setText(rightLatText);

                text = findViewById(R.id.bottom_right_long_text);
                rightLongText = null;
                text.setText(rightLongText);

                intent = new Intent();
                setResult(FILTER_CLEARED, intent);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        EditText text = findViewById(R.id.keyword_search_text);
        text.setText(keywordSearchText);

        text = findViewById(R.id.date_from_text);
        text.setText(dateFromText);

        text = findViewById(R.id.date_to_text);
        text.setText(dateToText);

        text = findViewById(R.id.top_left_lat_text);
        text.setText(leftLatText);

        text = findViewById(R.id.top_left_long_text);
        text.setText(leftLongText);

        text = findViewById(R.id.bottom_right_lat_text);
        text.setText(rightLatText);

        text = findViewById(R.id.bottom_right_long_text);
        text.setText(rightLongText);
    }


    // cancel search
    public void cancel(View view) {
        finish();
    }
}
