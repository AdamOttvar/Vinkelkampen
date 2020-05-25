package com.example.vinkelkampen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class HighscoreActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerAdapterHighscore adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        // Data to populate the RecyclerView with
        ArrayList<Participant> participants = MainActivity.getParticipants();

        // Set up the RecyclerView with adapter
        recyclerView = findViewById(R.id.recyclerHighscore);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapterHighscore(this, participants);
        recyclerView.setAdapter(adapter);
    }

    // Play a new round
    public void highscoreNewAngle(View view) {
        Intent intent = new Intent(this, DrawActivity.class);
        startActivity(intent);
    }

}
