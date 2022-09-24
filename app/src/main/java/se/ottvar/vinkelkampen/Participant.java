package se.ottvar.vinkelkampen;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.Objects;

public class Participant implements Comparable<Participant> {
    private String participantName;
    private float totalScore;
    private float currentGuess;
    private float currentScore;

    Participant(String name) {
        participantName = name;
        totalScore = 0;
        currentGuess = 0;
        currentScore = 0;
    }

    String getParticipantName() {
        return participantName;
    }

    float getTotalScore() {
        return totalScore;
    }

    void setTotalScore(float totalScore) {
        this.totalScore = totalScore;
    }

    float getCurrentGuess() {
        return currentGuess;
    }

    void setCurrentGuess(float currentGuess) {
        this.currentGuess = currentGuess;
    }

    float getCurrentScore() {
        return currentScore;
    }

    void setCurrentScore(float currentScore) {
        this.currentScore = currentScore;
    }

    @NonNull
    @Override
    public String toString() {
        return participantName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return participantName.equals(that.participantName);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(participantName);
    }

    @Override
    public int compareTo(@NonNull Participant o) {
        float compareTo=((Participant) o).getTotalScore();
        return Math.round(this.totalScore-compareTo);
    }
}
