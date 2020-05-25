package com.example.vinkelkampen;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.Objects;

public class Participant {
    private String participantName;
    private float score;
    private float currentGuess;

    public Participant(String name) {
        participantName = name;
        score = 0;
        currentGuess = 0;
    }

    public String getParticipantName() {
        return participantName;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public float getCurrentGuess() {
        return currentGuess;
    }

    public void setCurrentGuess(float currentGuess) {
        this.currentGuess = currentGuess;
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
