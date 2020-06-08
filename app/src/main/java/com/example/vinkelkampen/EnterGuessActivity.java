package com.example.vinkelkampen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class EnterGuessActivity extends AppCompatActivity implements RecyclerAdapterGuesses.ItemClickListener {
    RecyclerView recyclerView;
    RecyclerAdapterGuesses adapter;
    TextView angleAnswer;
    float correctAngle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_guess);

        // Get the Intent that started this activity and extract the angle
        Intent intent = getIntent();
        correctAngle = intent.getFloatExtra(GuessActivity.EXTRA_CORRECT_ANGLE, 0);

        // Text view for displaying correct angle
        angleAnswer = findViewById(R.id.textAngleAnswer);
        angleAnswer.setText(String.format(MainActivity.locale, MainActivity.angleFormat, 0.0f));

        // Data to populate the RecyclerView with
        ArrayList<Participant> participants = MainActivity.getParticipants();

        // Set up the RecyclerView with adapter
        recyclerView = findViewById(R.id.recyclerGuesses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapterGuesses(participants);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    // For when the user clicks diff, in order to see the rounds result
    public void showDiff(View view) {
        angleAnswer.setText(String.format(MainActivity.locale, MainActivity.angleFormat, correctAngle));
        adapter.updateDiff(correctAngle);
        adapter.notifyDataSetChanged();
    }

    // Show the high score
    public void showHighScore(View view) {
        adapter.updateDiff(correctAngle);
        adapter.updateResult();
        adapter.resetGuesses();
        adapter.notifyDataSetChanged();
        Intent intent = new Intent(this, HighscoreActivity.class);
        startActivity(intent);
    }

    // Play a new round
    public void newAngle(View view) {
        adapter.updateDiff(correctAngle);
        adapter.updateResult();
        adapter.resetGuesses();
        adapter.notifyDataSetChanged();
        Intent intent = new Intent(this, DrawActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(View view, int position) {
        showDialogEnterGuess(position);
    }

    private void showDialogEnterGuess(int position) {
        final Participant player = adapter.getItem(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(EnterGuessActivity.this);
        builder.setTitle(R.string.add_guess);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);

        // Key Listener for if the user uses enter key on virtual keyboard
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER){
                    String guessInput = input.getText().toString();
                    player.setCurrentGuess(Float.parseFloat(guessInput));
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
                return false;
            }
        });

        // For if the user clicks positive button "OK"
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String guessInput = input.getText().toString();
                player.setCurrentGuess(Float.parseFloat(guessInput));
                adapter.notifyDataSetChanged();
            }
        });

        // For if the user clicks the negative button "Cancel"
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
