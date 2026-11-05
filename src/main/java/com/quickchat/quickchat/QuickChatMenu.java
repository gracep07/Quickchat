package com.quickchat.quickchat;

import java.util.Scanner;

/**
 * Runs the QuickChat menu after a successful login.
 */
public class QuickChatMenu {

    public static void run(Scanner scanner) {
        System.out.println("Welcome to QuickChat.");

        boolean running = true;

        // Application loop: runs until the user chooses Quit
        while (running) {
            System.out.println("\nPlease choose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Option: ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    System.out.println("Send Messages selected (coming in the next step).");
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
}