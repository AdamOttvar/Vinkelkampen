package se.ottvar.vinkelkampen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_KP_PLAYING = "com.example.vinkelkampen.EXTRA_KP";
    public static Locale locale;
    public static String angleFormat;
    private static ArrayList<Participant> listOfParticipants = new ArrayList<>();
    private static List<String> KPStyret = Arrays.asList("Adam", "Dany", "Jocke", "Tobbe", "Victor");

    Switch modeSwitch;
    private static boolean hardMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locale = new Locale("sv", "SE");
        angleFormat = "%.0f";

        modeSwitch = findViewById(R.id.switchMode);
        modeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hardMode = isChecked;
                angleFormat = hardMode ? "%.2f" : "%.0f";
            }
        });
    }

    public static boolean isEasyMode() {
        return !hardMode;
    }

    public static void populateWithKP(){
        for (int i = 0; i < KPStyret.size(); i++) {
            listOfParticipants.add(new Participant(KPStyret.get(i)));
        }
    }

    public static boolean addParticipant(String name) {
        Participant comparePart = new Participant(name);
        if (!listOfParticipants.contains(comparePart)) {
            listOfParticipants.add(new Participant(name));
            return true;
        }

        return false;
    }

    public static void removeParticipant(Participant player) {
        listOfParticipants.remove(player);
    }

    public static void clearParticipants(){
        listOfParticipants.clear();
    }

    public static ArrayList<Participant> getParticipants() {
        return listOfParticipants;
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
