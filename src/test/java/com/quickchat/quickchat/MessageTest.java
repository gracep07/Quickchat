package com.quickchat.quickchat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Message class, using the Part 2 test data.
 */
public class MessageTest {

    // Test data message 1 (Send) and message 2 (Discard)
    private static final String RECIPIENT_1 = "+27718693002";
    private static final String TEXT_1 = "Hi Mike, can you join us for dinner tonight?";
    private static final String RECIPIENT_2 = "08575975889";
    private static final String TEXT_2 = "Hi Keegan, did you receive the payment?";

    @Test
    public void testMessageLengthSuccess() {
        assertEquals("Message ready to send.", Message.checkMessageLength(TEXT_1));
    }

    @Test
    public void testMessageLengthFailure() {
        String tooLong = "a".repeat(255);
        assertEquals("Message exceeds 250 characters by 5; please reduce the size.",
                Message.checkMessageLength(tooLong));
    }

    @Test
    public void testRecipientCorrectlyFormatted() {
        assertEquals("Cell phone number successfully captured.",
                Message.checkRecipientCell(RECIPIENT_1));
    }

    @Test
    public void testRecipientIncorrectlyFormatted() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain an "
                + "international code. Please correct the number and try again.",
                Message.checkRecipientCell(RECIPIENT_2));
    }

    @Test
    public void testMessageHashMessage1() {
        Message message = new Message("0012345678", 0, RECIPIENT_1, TEXT_1);
        assertEquals("00:0:HITONIGHT", message.createMessageHash());
    }

    @Test
    public void testMessageHashesInLoop() {
        String[] recipients = {RECIPIENT_1, RECIPIENT_2};
        String[] texts = {TEXT_1, TEXT_2};
        String[] expectedHashes = {"00:0:HITONIGHT", "00:1:HIPAYMENT"};

        for (int i = 0; i < texts.length; i++) {
            Message message = new Message("0012345678", i, recipients[i], texts[i]);
            assertEquals(expectedHashes[i], message.createMessageHash());
        }
    }

    @Test
    public void testMessageIDCreated() {
        Message message = new Message(0, RECIPIENT_1, TEXT_1);
        String id = message.getMessageID();
        assertTrue(id.matches("[0-9]{10}"));
        assertTrue(message.checkMessageID());
    }

    @Test
    public void testSendMessage() {
        Message message = new Message(0, RECIPIENT_1, TEXT_1);
        assertEquals("Message successfully sent.", message.SentMessage("1"));
    }

    @Test
    public void testDisregardMessage() {
        Message message = new Message(1, RECIPIENT_2, TEXT_2);
        assertEquals("Press 0 to delete the message.", message.SentMessage("2"));
    }

    @Test
    public void testStoreMessage() {
        Message message = new Message(0, RECIPIENT_1, TEXT_1);
        assertEquals("Message successfully stored.", message.SentMessage("3"));
    }

    @Test
    public void testTotalMessagesIncreasesWhenSent() {
        int before = Message.returnTotalMessages();
        Message message = new Message(0, RECIPIENT_1, TEXT_1);
        message.SentMessage("1");
        assertEquals(before + 1, Message.returnTotalMessages());
    }
}