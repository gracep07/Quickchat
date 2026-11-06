package com.quickchat.quickchat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
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

    // Messages stored to send later (also saved to a JSON file)
    private static final ArrayList<Message> storedMessages = new ArrayList<>();
    private static final String JSON_FILE = "storedMessages.json";

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
     *
     * @return the new message ID
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
     * Checks the message ID is not more than ten characters.
     *
     * @return true if the ID is valid
     */
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    /**
     * Checks the recipient number: international code, then the number.
     * Reuses the same regex as Login.checkCellPhoneNumber() from Part 1.
     *
     * @param cell the recipient's cell number
     * @return the success or failure message
     */
    public static String checkRecipientCell(String cell) {
                // Source (adapted for +27 numbers): HowToDoInJava (n.d.) Regex for International Phone
        // Numbers in Java (E.164). Available at:
        // https://howtodoinjava.com/regex/java-regex-validate-international-phone-numbers
        // (Accessed: 6 November 2026).
        if (cell != null && cell.matches("^\\+27[0-9]{9}$")) {
            return CELL_OK;
        }
        return CELL_BAD;
    }

    /**
     * Checks the message is not more than 250 characters.
     *
     * @param text the message text to check
     * @return the success or failure message
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
                if (storeMessage()) {
                    return STORE_OK;
                }
                return "Message could not be stored.";
            default:
                return "Invalid choice. Please enter 1, 2 or 3.";
        }
    }

    /**
     * Saves this message, and all earlier stored messages, to a JSON file.
     *
     * @return true if the file was written successfully
     */
    public boolean storeMessage() {
        storedMessages.add(this);

        // Source: Google (n.d.) Gson User Guide. Available at:
        // https://google.github.io/gson/UserGuide.html (Accessed: 6 November 2026).
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            gson.toJson(storedMessages, writer);
            return true;
        } catch (IOException e) {
            System.out.println("Could not write the JSON file: " + e.getMessage());
            return false;
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