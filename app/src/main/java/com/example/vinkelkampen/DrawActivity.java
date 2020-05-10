package com.example.vinkelkampen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

public class DrawActivity extends AppCompatActivity {
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
        String stringAngle = Double.toString(currentAngle);
        angleView.setText(stringAngle);

        return super.onTouchEvent(event);
    }
}
