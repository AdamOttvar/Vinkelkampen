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
    TextView angleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        drawingView = findViewById(R.id.drawingView);
        angleView = findViewById(R.id.textAngle);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        double currentAngle = drawingView.getCurrentAngle();
        Locale locale = new Locale("sv", "SE");
        angleView.setText(String.format(locale, "%.2f", currentAngle));

        return super.onTouchEvent(event);
    }

    /** Called when the user hits the "guess angle" button **/
    public void guessAngle(View view) {
        Intent intent = new Intent(this, GuessActivity.class);
        startActivity(intent);
    }

    public void oppositeAngle(View view) {
        drawingView.toggleOppositeAngle();
    }

}
