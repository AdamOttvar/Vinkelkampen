package com.example.vinkelkampen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ParticipantsActivity extends AppCompatActivity implements RecyclerAdapterParticipants.ItemClickListener {
    RecyclerAdapterParticipants adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        boolean isKP = intent.getBooleanExtra(MainActivity.EXTRA_KP_PLAYING, false);

        if (isKP) {
            MainActivity.populateWithKP();
        }
        else {
            MainActivity.clearParticipants();
        }

        // data to populate the RecyclerView with
        ArrayList<String> participantsNames = MainActivity.getParticipantsAsArray();

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerParticipants);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapterParticipants(this, participantsNames);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    /** Called when the user hits the "start round" button **/
    public void startRound(View view) {
        Intent intent = new Intent(this, DrawActivity.class);
        startActivity(intent);
    }

}
