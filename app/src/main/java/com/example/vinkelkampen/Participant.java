package com.example.vinkelkampen;

import androidx.annotation.NonNull;

public class Participant {
    private String participantName;
    private float score;

    public Participant(String name) {
        participantName = name;
        score = 0;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    @NonNull
    @Override
    public String toString() {
        return participantName;
    }
}
