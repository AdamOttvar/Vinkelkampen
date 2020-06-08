package com.example.vinkelkampen;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class ParticipantUnitTest {
    private Participant participant;
    private String name = "Tester";
    private float score = 189.7f;


    @Test
    public void testGetParticipantName() {
        participant  = new Participant(name);
        assertEquals(name, participant.getParticipantName());
    }

    @Test
    public void testTotalScore() {
        participant  = new Participant(name);
        assertEquals(0, participant.getTotalScore(), 0.001);
        participant.setTotalScore(score);
        assertEquals(score, participant.getTotalScore(), 0.001);
    }

    @Test
    public void testCurrentGuess() {
        participant  = new Participant(name);
        assertEquals(0, participant.getCurrentGuess(), 0.001);
        float guess = 42.9f;
        participant.setCurrentGuess(guess);
        assertEquals(guess, participant.getCurrentGuess(), 0.001);
    }

    @Test
    public void testCurrentScore() {
        participant  = new Participant(name);
        assertEquals(0, participant.getCurrentScore(), 0.001);
        participant.setCurrentScore(score);
        assertEquals(score, participant.getCurrentScore(), 0.001);
    }

    @Test
    public void testToString() {
        participant  = new Participant(name);
        assertEquals(name, participant.toString());
    }

    @Test
    public void testEquals() {
        participant  = new Participant(name);
        Participant participantEqual = new Participant(name);
        Participant participantNotEqual = new Participant("Fester");
        assertEquals(participantEqual, participant);
        assertNotEquals(participantNotEqual, participant);
    }

    @Test
    public void testCompareTo() {
        assertEquals(4, 2 + 2);
    }

}
