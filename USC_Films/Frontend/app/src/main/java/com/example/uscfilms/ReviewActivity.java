package com.example.uscfilms;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReviewActivity extends AppCompatActivity {

    private String reviewInfo;
    private String reviewRate;
    private String reviewContent;

    TextView reviewInfoText;
    TextView reviewRateText;
    TextView reviewContentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_USCFilms);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        getInputParams();
        getUI();

        reviewInfoText.setText(reviewInfo);
        reviewRateText.setText(reviewRate);
        reviewContentText.setText(reviewContent);
    }


    private void getInputParams() {
        this.reviewInfo = getIntent().getStringExtra("REVIEW_INFO");
        this.reviewRate = getIntent().getStringExtra("REVIEW_RATE");
        this.reviewContent = getIntent().getStringExtra("REVIEW_CONTENT");
    }

    private void getUI() {
        reviewInfoText = findViewById(R.id.reviewInfo2);
        reviewRateText = findViewById(R.id.reviewRate);
        reviewContentText = findViewById(R.id.reviewContent2);
    }

}