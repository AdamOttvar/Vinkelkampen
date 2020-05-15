package com.example.vinkelkampen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

public class GuessActivity extends AppCompatActivity {
    public static final String EXTRA_ANGLE = "com.example.vinkelkampen.EXTRA_ANGLE";
    AngleDrawingView guessingView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);

        guessingView = findViewById(R.id.guessingView);
        guessingView.hideAngle(true);

    }

}
