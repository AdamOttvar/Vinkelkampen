package com.example.vinkelkampen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class GuessActivity extends AppCompatActivity {
    public static final String EXTRA_CORRECT_ANGLE = "com.example.vinkelkampen.EXTRA_ANGLE";
    AngleDrawingView guessingView;
    TextView angleAnswer;
    MediaPlayer mediaPlayer;

    final Handler handler = new Handler();
    final Runnable reveal = new Runnable() {
        public void run() {
            revealAngle();
        }
    };
    final Runnable guess = new Runnable() {
        public void run() {
            startEnterGuessActivity();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);

        mediaPlayer = MediaPlayer.create(this, R.raw.vinkelkampen);

        guessingView = findViewById(R.id.guessingView);
        guessingView.touchDisabled(true);
        guessingView.hideAngle(true);

        // TODO: Handle media player better.
        // Now it will start multiple media players if the device is flipped during playing.
        // It will also continue to play even if the user goes back to previous view.
        mediaPlayer.start();
        handler.postDelayed(reveal, 16000);
        handler.postDelayed(guess, 49000);
    }

    @Override
    public void onBackPressed()
    {
        guessingView.touchDisabled(false);
        guessingView.hideAngle(false);
        super.onBackPressed();
    }

    private void revealAngle() {
        guessingView.hideAngle(false);
    }

    public void resultAngle(View view) {
        guessingView.hideAngle(false);
        double currentAngle = guessingView.getCurrentAngle();
        angleAnswer = findViewById(R.id.textAngleAnswer);
        Locale locale = new Locale("sv", "SE");
        angleAnswer.setText(String.format(locale, "%.2f", currentAngle));
    }

    private void startEnterGuessActivity() {
        Intent intent = new Intent(this, EnterGuessActivity.class);
        double currentAngle = guessingView.getCurrentAngle();
        intent.putExtra(EXTRA_CORRECT_ANGLE, (float) currentAngle);
        startActivity(intent);
    }
}
