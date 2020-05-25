package com.example.vinkelkampen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class EnterGuessActivity extends AppCompatActivity implements RecyclerAdapterGuesses.ItemClickListener {
    RecyclerView recyclerView;
    RecyclerAdapterGuesses adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_guess);

        // Data to populate the RecyclerView with
        ArrayList<Participant> participants = MainActivity.getParticipants();

        // Set up the RecyclerView with adapter
        recyclerView = findViewById(R.id.recyclerGuesses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapterGuesses(this, participants);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
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

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: Better handling of inputs (strings, commas, etc)
                // or is it sufficient to have the input type set up correct?
                String guessInput = input.getText().toString();
                player.setCurrentGuess(Float.parseFloat(guessInput));
                adapter.notifyDataSetChanged();
                //recyclerView.invalidate();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
