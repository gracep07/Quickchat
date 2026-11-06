package com.quickchat.quickchat;

import java.util.ArrayList;
import java.util.Random;

/**
 * Represents one QuickChat message.
 */
public class Message {

    public static final String CELL_OK = "Cell phone number successfully captured.";
    public static final String CELL_BAD = "Cell phone number is incorrectly formatted or does not "
            + "contain an international code. Please correct the number and try again.";
    public static final String LENGTH_OK = "Message ready to send.";
    public static final int MAX_LENGTH = 250;
        public static final String SEND_OK = "Message successfully sent.";
    public static final String DISCARD_MSG = "Press 0 to delete the message.";
    public static final String STORE_OK = "Message successfully stored.";

    // Every message sent while the program is running
    private static final ArrayList<Message> sentMessages = new ArrayList<>();

    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;

    public Message(int messageNumber, String recipient, String messageText) {
        this.messageID = generateMessageID();
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
    }

    /**
     * Builds a random ten-digit ID one digit at a time.
     */
    public static String generateMessageID() {
        Random random = new Random();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(random.nextInt(10));
        }
        return id.toString();
    }

    /**
     * True if the message ID is not more than ten characters.
     */
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    /**
     * Checks the recipient number: international code, then the number.
     * Returns the success or failure message.
     */
    public static String checkRecipientCell(String cell) {
        // TODO: paste the same regex pattern you used in Login.checkCellPhoneNumber()
        // and keep your reference comment for it here too.
        if (cell != null && cell.matches("^\\+\\d{1,3}\\d{1,10}$")) {
            return CELL_OK;
        }
        return CELL_BAD;
    }

    /**
     * Checks the message is not more than 250 characters.
     * Returns the success or failure message.
     */
    public static String checkMessageLength(String text) {
        int length = text.length();
        if (length <= MAX_LENGTH) {
            return LENGTH_OK;
        }
        return "Message exceeds 250 characters by " + (length - MAX_LENGTH)
                + "; please reduce the size.";
    }

        /**
     * Creates the message hash: first two digits of the ID, the message
     * number, then the first and last words of the message, in capitals.
     *
     * @return the message hash, e.g. 00:0:HITONIGHT
     */
    public String createMessageHash() {
        String firstTwo = messageID.substring(0, 2);

        // Split the message into words, ignoring extra spaces
        String[] words = messageText.trim().split("\\s+");

        // Remove punctuation such as commas and question marks
        String firstWord = words[0].replaceAll("[^A-Za-z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^A-Za-z0-9]", "");

        return (firstTwo + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }
        /**
     * Applies the user's choice for this message.
     *
     * @param choice "1" to send, "2" to disregard, "3" to store for later
     * @return the message to show the user
     */
    public String SentMessage(String choice) {
        switch (choice) {
            case "1":
                sentMessages.add(this);
                return SEND_OK;
            case "2":
                return DISCARD_MSG;
            case "3":
                return STORE_OK;
            default:
                return "Invalid choice. Please enter 1, 2 or 3.";
        }
    }
        /**
     * Builds the full details of this message in the required order.
     *
     * @return Message ID, Message Hash, Recipient and Message as text
     */
    public String getDetails() {
        return "Message ID: " + messageID
                + "\nMessage Hash: " + createMessageHash()
                + "\nRecipient: " + recipient
                + "\nMessage: " + messageText;
    }

    /**
     * Lists all the messages sent while the program is running.
     *
     * @return the details of every sent message
     */
    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages have been sent.";
        }
        StringBuilder all = new StringBuilder();
        for (Message m : sentMessages) {
            all.append(m.getDetails()).append("\n\n");
        }
        return all.toString().trim();
    }

    /**
     * Counts the messages sent while the program is running.
     *
     * @return the total number of sent messages
     */
    public static int returnTotalMessages() {
        return sentMessages.size();
    }
    public String getMessageID() { return messageID; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
}