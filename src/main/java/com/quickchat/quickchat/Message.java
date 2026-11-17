package com.quickchat.quickchat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
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

    // ---- The five required arrays (Part 3) ----

    // Every message sent while the program is running
    private static final ArrayList<Message> sentMessages = new ArrayList<>();

    // Every message the user disregarded
    private static final ArrayList<Message> disregardedMessages = new ArrayList<>();

    // Messages stored to send later (also saved to a JSON file)
    private static final ArrayList<Message> storedMessages = new ArrayList<>();

    // Message hashes and message IDs, in the order the messages were handled
    private static final ArrayList<String> messageHashes = new ArrayList<>();
    private static final ArrayList<String> messageIDs = new ArrayList<>();

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
     * Creates a message with a given ID, so unit tests can predict the hash.
     *
     * @param messageID the ID to use
     * @param messageNumber the message number
     * @param recipient the recipient's cell number
     * @param messageText the message text
     */
    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
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
     * Applies the user's choice for this message and records the message in
     * the correct arrays (sent, disregarded or stored), plus the hash and ID arrays.
     *
     * @param choice "1" to send, "2" to disregard, "3" to store for later
     * @return the message to show the user
     */
    public String SentMessage(String choice) {
        switch (choice) {
            case "1":
                sentMessages.add(this);
                recordIDAndHash();
                return SEND_OK;
            case "2":
                disregardedMessages.add(this);
                recordIDAndHash();
                return DISCARD_MSG;
            case "3":
                if (storeMessage()) {
                    recordIDAndHash();
                    return STORE_OK;
                }
                return "Message could not be stored.";
            default:
                return "Invalid choice. Please enter 1, 2 or 3.";
        }
    }

    /**
     * Adds this message's ID and hash to the ID and hash arrays.
     */
    private void recordIDAndHash() {
        messageIDs.add(messageID);
        messageHashes.add(createMessageHash());
    }

    /**
     * Saves this message, and all earlier stored messages, to a JSON file.
     *
     * @return true if the file was written successfully
     */
    public boolean storeMessage() {
        storedMessages.add(this);
        return writeStoredToFile();
    }

    /**
     * Writes the whole stored messages array to the JSON file.
     *
     * @return true if the file was written successfully
     */
    private static boolean writeStoredToFile() {
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
     * Reads the stored messages from the JSON file into the stored messages
     * array. Does nothing if the file does not exist yet.
     *
     * @return the number of stored messages loaded
     */
    public static int loadStoredMessages() {
        File file = new File(JSON_FILE);
        if (!file.exists()) {
            return 0;
        }

        // Source: Google (n.d.) Gson User Guide (reading a list with TypeToken).
        // Available at: https://google.github.io/gson/UserGuide.html
        // (Accessed: 17 November 2026).
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Message>>() { }.getType();

        try (FileReader reader = new FileReader(file)) {
            ArrayList<Message> loaded = gson.fromJson(reader, listType);
            if (loaded == null) {
                return 0;
            }

            // Replace the stored array with what is in the file
            storedMessages.clear();
            for (Message m : loaded) {
                storedMessages.add(m);

                // Make sure each stored message's ID and hash are in their arrays
                if (!messageIDs.contains(m.getMessageID())) {
                    messageIDs.add(m.getMessageID());
                    messageHashes.add(m.createMessageHash());
                }
            }
            return storedMessages.size();
        } catch (IOException e) {
            System.out.println("Could not read the JSON file: " + e.getMessage());
            return 0;
        }
    }

    // ---- Stored Messages features (Part 3) ----

    /**
     * Feature (a): lists the sender and recipient of every stored message.
     * The sender is the logged-in user.
     *
     * @return one line per stored message
     */
    public static String displaySendersAndRecipients() {
        if (storedMessages.isEmpty()) {
            return "No stored messages.";
        }
        StringBuilder result = new StringBuilder();
        for (Message m : storedMessages) {
            result.append("Sender: You (logged-in user) | Recipient: ")
                    .append(m.getRecipient()).append("\n");
        }
        return result.toString().trim();
    }

    /**
     * Feature (b): finds the longest stored message.
     *
     * @return the text of the longest stored message
     */
    public static String getLongestStoredMessage() {
        if (storedMessages.isEmpty()) {
            return "No stored messages.";
        }
        String longest = storedMessages.get(0).getMessageText();
        for (Message m : storedMessages) {
            if (m.getMessageText().length() > longest.length()) {
                longest = m.getMessageText();
            }
        }
        return longest;
    }

    /**
     * Feature (c): searches every array for a message ID.
     *
     * @param id the message ID to look for
     * @return the recipient and message, or a not found message
     */
    public static String searchByMessageID(String id) {
        ArrayList<Message> all = getAllMessages();
        for (Message m : all) {
            if (m.getMessageID().equals(id.trim())) {
                return "Recipient: " + m.getRecipient() + "\nMessage: " + m.getMessageText();
            }
        }
        return "Message ID not found.";
    }

    /**
     * Feature (d): finds every sent or stored message for one recipient.
     *
     * @param recipient the recipient's cell number
     * @return the matching message texts (empty if there are none)
     */
    public static ArrayList<String> getMessagesForRecipient(String recipient) {
        ArrayList<String> found = new ArrayList<>();
        for (Message m : sentMessages) {
            if (m.getRecipient().equals(recipient.trim())) {
                found.add(m.getMessageText());
            }
        }
        for (Message m : storedMessages) {
            if (m.getRecipient().equals(recipient.trim())) {
                found.add(m.getMessageText());
            }
        }
        return found;
    }

    /**
     * Feature (e): deletes a stored message using its hash. Also removes its
     * ID and hash from their arrays and updates the JSON file.
     *
     * @param hash the message hash to delete
     * @return a success or not found message
     */
    public static String deleteByHash(String hash) {
        for (int i = 0; i < storedMessages.size(); i++) {
            Message m = storedMessages.get(i);
            if (m.createMessageHash().equalsIgnoreCase(hash.trim())) {
                String text = m.getMessageText();
                storedMessages.remove(i);
                messageIDs.remove(m.getMessageID());
                messageHashes.remove(m.createMessageHash());
                writeStoredToFile();
                return "Message: \"" + text + "\" successfully deleted.";
            }
        }
        return "No stored message found with that hash.";
    }

    /**
     * Feature (f): builds a report of every stored message.
     *
     * @return the full details of all stored messages
     */
    public static String getStoredReport() {
        return buildReport(storedMessages, "stored");
    }

    /**
     * Builds a report of every sent message (hash, recipient, message).
     *
     * @return the details of all sent messages
     */
    public static String getSentReport() {
        return buildReport(sentMessages, "sent");
    }

    private static String buildReport(ArrayList<Message> list, String label) {
        if (list.isEmpty()) {
            return "No " + label + " messages.";
        }
        StringBuilder report = new StringBuilder();
        for (Message m : list) {
            report.append("Message ID: ").append(m.getMessageID())
                    .append("\nMessage Hash: ").append(m.createMessageHash())
                    .append("\nRecipient: ").append(m.getRecipient())
                    .append("\nMessage: ").append(m.getMessageText())
                    .append("\n\n");
        }
        return report.toString().trim();
    }

    /**
     * Joins the sent, disregarded and stored arrays into one list for searching.
     */
    private static ArrayList<Message> getAllMessages() {
        ArrayList<Message> all = new ArrayList<>();
        all.addAll(sentMessages);
        all.addAll(disregardedMessages);
        all.addAll(storedMessages);
        return all;
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

    /**
     * Empties all the arrays so unit tests can start fresh.
     */
    public static void clearAll() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
    }

    // ---- Getters for the arrays ----
    public static ArrayList<Message> getSentMessages() { return sentMessages; }
    public static ArrayList<Message> getDisregardedMessages() { return disregardedMessages; }
    public static ArrayList<Message> getStoredMessages() { return storedMessages; }
    public static ArrayList<String> getMessageHashes() { return messageHashes; }
    public static ArrayList<String> getMessageIDs() { return messageIDs; }

    public String getMessageID() { return messageID; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
}