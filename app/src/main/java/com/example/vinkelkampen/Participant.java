package com.example.vinkelkampen;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.Objects;

public class Participant {
    private String participantName;
    private float totalScore;
    private float currentGuess;
    private float currentScore;

    public Participant(String name) {
        participantName = name;
        totalScore = 0;
        currentGuess = 0;
        currentScore = 0;
    }

    public String getParticipantName() {
        return participantName;
    }

    public float getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(float totalScore) {
        this.totalScore = totalScore;
    }

    public float getCurrentGuess() {
        return currentGuess;
    }

    public void setCurrentGuess(float currentGuess) {
        this.currentGuess = currentGuess;
    }

    public float getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(float currentScore) {
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
}
