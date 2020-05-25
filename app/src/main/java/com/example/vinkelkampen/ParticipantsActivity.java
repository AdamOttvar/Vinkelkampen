package com.example.vinkelkampen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ParticipantsActivity extends AppCompatActivity implements RecyclerAdapterParticipants.ItemClickListener {
    RecyclerView recyclerView;
    RecyclerAdapterParticipants adapter;
    EditText newParticipantName;

    private void showDialogRemove(int pos) {
        final Participant player = adapter.getItem(pos);
        AlertDialog.Builder builder = new AlertDialog.Builder(ParticipantsActivity.this);
        builder.setTitle(R.string.remove);
        builder.setMessage(player.getParticipantName() + "?");

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                MainActivity.removeParticipant(player);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        boolean isKP = intent.getBooleanExtra(MainActivity.EXTRA_KP_PLAYING, false);

        if (isKP) {
            MainActivity.clearParticipants();
            MainActivity.populateWithKP();
        }
        else {
            MainActivity.clearParticipants();
        }

        // Data to populate the RecyclerView with
        ArrayList<Participant> participants = MainActivity.getParticipants();

        // Set up the RecyclerView
        recyclerView = findViewById(R.id.recyclerParticipants);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapterParticipants(this, participants);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        // Set up name input
        newParticipantName = findViewById(R.id.editNewName);
    }

    @Override
    public void onItemClick(View view, int position) {
        showDialogRemove(position);
        recyclerView.invalidate();
    }

    public void addParticipant(View view) {
        String name = newParticipantName.getText().toString();
        if (!name.equals("")) {
            boolean added = MainActivity.addParticipant(name);
            if (!added)
                Toast.makeText(this, "Already exists", Toast.LENGTH_SHORT).show();
        }
        newParticipantName.setText("");
    }

    /** Called when the user hits the "start round" button **/
    public void startRound(View view) {
        Intent intent = new Intent(this, DrawActivity.class);
        startActivity(intent);
    }

}
