package com.example.vinkelkampen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_KP_PLAYING = "com.example.vinkelkampen.EXTRA_KP";
    private static Map<String, Integer> mapOfParticipants = new HashMap<>();
    private static List<String> KPStyret = Arrays.asList("Adam", "Dany", "Jocke", "Tobbe", "Victor");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public static void populateWithKP(){
        for (int i = 0; i < KPStyret.size(); i++) {
            mapOfParticipants.put(KPStyret.get(i), 0);
        }
    }

    public static void clearParticipants(){
        mapOfParticipants.clear();
    }

    public static ArrayList<String> getParticipantsAsArray() {
        Set<String> keys = mapOfParticipants.keySet();
        return new ArrayList<String>(keys);
    }

    /** Called when the user hits the "new round" button **/
    public void newRound(View view) {
        Intent intent = new Intent(this, ParticipantsActivity.class);
        if (view.getId() == R.id.button_kp) {
            intent.putExtra(EXTRA_KP_PLAYING, true);
        }
        else {
            intent.putExtra(EXTRA_KP_PLAYING, false);
        }
        startActivity(intent);
    }
}
