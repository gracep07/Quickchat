package com.quickchat.quickchat;

import java.util.Scanner;

/**
 * Runs the QuickChat menu after a successful login.
 */
public class QuickChatMenu {

    public static void run(Scanner scanner) {
        System.out.println("Welcome to QuickChat.");

        // The user sets how many messages they want to enter
        int numMessages = askNumberOfMessages(scanner);
        boolean allEntered = false;
        boolean running = true;

        // Application loop: runs until the user chooses Quit
        while (running) {
            System.out.println("\nPlease choose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    if (allEntered) {
                        System.out.println("You have already entered all " + numMessages + " messages.");
                    } else {
                        enterMessages(scanner, numMessages);
                        allEntered = true;
                    }
                    break;
                case "2":
                    System.out.println("Coming Soon.");
                    break;
                case "3":
                    System.out.println("Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please enter 1, 2 or 3.");
            }
        }
    }

    /**
     * Keeps asking until the user enters a whole number greater than 0.
     */
    private static int askNumberOfMessages(Scanner scanner) {
        while (true) {
            System.out.print("How many messages do you wish to enter? ");
            try {
                int number = Integer.parseInt(scanner.nextLine().trim());
                if (number > 0) {
                    return number;
                }
            } catch (NumberFormatException e) {
                // fall through to the error message below
            }
            System.out.println("Please enter a whole number greater than 0.");
        }
    }

    /**
     * Loops once for every message the user said they would enter.
     */
    /**
     * Loops once for every message the user said they would enter.
     */
    private static void enterMessages(Scanner scanner, int numMessages) {
        for (int i = 0; i < numMessages; i++) {
            int messageNumber = i; // starts at 0, matches the hash example 00:0:...
            System.out.println("\n--- Message " + (messageNumber + 1) + " of " + numMessages + " ---");

            // Recipient: keep asking until the number is valid
            String recipient;
            while (true) {
                System.out.print("Enter the recipient's cell number (e.g. +27718693002): ");
                recipient = scanner.nextLine().trim();
                String cellResult = Message.checkRecipientCell(recipient);
                System.out.println(cellResult);
                if (cellResult.equals(Message.CELL_OK)) {
                    break;
                }
            }

            // Message text: keep asking until it is 250 characters or less
            String text;
            while (true) {
                System.out.print("Enter your message: ");
                text = scanner.nextLine().trim();
                String lengthResult = Message.checkMessageLength(text);
                System.out.println(lengthResult);
                if (lengthResult.equals(Message.LENGTH_OK)) {
                    break;
                }
            }

            Message message = new Message(messageNumber, recipient, text);
            System.out.println("Message ID generated: " + message.getMessageID());
                        System.out.println("Message Hash: " + message.createMessageHash());
        }
    }
}