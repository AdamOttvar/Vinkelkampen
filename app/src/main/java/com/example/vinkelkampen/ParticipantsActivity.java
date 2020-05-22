package com.example.vinkelkampen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ParticipantsActivity extends AppCompatActivity implements RecyclerAdapterParticipants.ItemClickListener {
    RecyclerAdapterParticipants adapter;
    EditText newParticipantName;

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

        // Data to populate the RecyclerView with
        ArrayList<Participant> participants = MainActivity.getParticipants();

        // Set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerParticipants);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapterParticipants(this, participants);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        // Set up name input
        newParticipantName = findViewById(R.id.editNewName);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    public void addParticipant(View view) {
        String name = newParticipantName.getText().toString();
        if (!name.equals("")) {
            MainActivity.addParticipant(name);
        }
        newParticipantName.setText("");
    }

    /** Called when the user hits the "start round" button **/
    public void startRound(View view) {
        Intent intent = new Intent(this, DrawActivity.class);
        startActivity(intent);
    }

}
