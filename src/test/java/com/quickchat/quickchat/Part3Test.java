package com.quickchat.quickchat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Part 3 unit tests, using the five test messages from the brief.
 */
public class Part3Test {

    /**
     * Clears the arrays and loads the Part 3 test data before every test.
     */
    @BeforeEach
    void setUp() {
        Message.clearAll();
        loadTestData();
    }

    /**
     * Fills the arrays with test messages 1 to 5 from the brief.
     */
    static void loadTestData() {
        // Message 1: Sent
        new Message("1000000001", 0, "+27834557896",
                "Did you get the cake?").SentMessage("1");

        // Message 2: Stored
        new Message("2000000002", 1, "+27838884567",
                "Where are you? You are late! I have asked you to be on time.").SentMessage("3");

        // Message 3: Disregard
        new Message("3000000003", 2, "+27834484567",
                "Yohoooo, I am at your gate.").SentMessage("2");

        // Message 4: Sent (the brief gives this recipient as 0838884567)
        new Message("4000000004", 3, "0838884567",
                "It is dinner time!").SentMessage("1");

        // Message 5: Stored
        new Message("5000000005", 4, "+27838884567",
                "Ok, I am leaving without you.").SentMessage("3");
    }

    @Test
    void sentMessagesArrayCorrectlyPopulated() {
        ArrayList<Message> sent = Message.getSentMessages();

        assertEquals(2, sent.size());
        assertEquals("Did you get the cake?", sent.get(0).getMessageText());
        assertEquals("It is dinner time!", sent.get(1).getMessageText());
    }

    @Test
    void allArraysCorrectlyPopulated() {
        assertEquals(2, Message.getSentMessages().size());
        assertEquals(1, Message.getDisregardedMessages().size());
        assertEquals(2, Message.getStoredMessages().size());
        assertEquals(5, Message.getMessageHashes().size());
        assertEquals(5, Message.getMessageIDs().size());
    }

    @Test
    void displayLongestMessage() {
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                Message.getLongestStoredMessage());
    }

    @Test
    void searchForMessageID() {
        assertEquals("Recipient: 0838884567\nMessage: It is dinner time!",
                Message.searchByMessageID("4000000004"));
    }

    @Test
    void searchForMessageIDNotFound() {
        assertEquals("Message ID not found.", Message.searchByMessageID("9999999999"));
    }

    @Test
    void searchMessagesForRecipient() {
        ArrayList<String> found = Message.getMessagesForRecipient("+27838884567");

        assertEquals(2, found.size());
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                found.get(0));
        assertEquals("Ok, I am leaving without you.", found.get(1));
    }

    @Test
    void deleteMessageUsingHash() {
        // Message 2 hash: first two digits of its ID, number 1, first and last words
        String result = Message.deleteByHash("20:1:WHERETIME");

        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\""
                + " successfully deleted.", result);
        assertEquals(1, Message.getStoredMessages().size());
        assertEquals(4, Message.getMessageHashes().size());
    }

    @Test
    void deleteMessageHashNotFound() {
        assertEquals("No stored message found with that hash.",
                Message.deleteByHash("00:0:NOTHERE"));
    }

    @Test
    void displaySentMessagesReport() {
        String report = Message.getSentReport();

        assertTrue(report.contains("10:0:DIDCAKE"));
        assertTrue(report.contains("+27834557896"));
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains("0838884567"));
        assertTrue(report.contains("It is dinner time!"));
    }

    @Test
    void displayStoredMessagesReport() {
        String report = Message.getStoredReport();

        assertTrue(report.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(report.contains("Ok, I am leaving without you."));
    }
}