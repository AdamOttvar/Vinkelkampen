package com.example.vinkelkampen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class DrawActivity extends AppCompatActivity {
    public static final String EXTRA_DRAWING_DATA = "com.example.vinkelkampen.EXTRA_DRAWING";
    AngleDrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        drawingView = findViewById(R.id.drawingView);
    }

    /** Called when the user hits the "guess angle" button **/
    public void guessAngle(View view) {
        Intent intent = new Intent(this, GuessActivity.class);
        startActivity(intent);
    }

    public void oppositeAngle(View view) {
        drawingView.toggleOppositeAngle();
    }

    public void randomAngle(View view) {
        Intent intent = new Intent(this, GuessActivity.class);
        drawingView.setRandomAngle();
        startActivity(intent);
    }

}
