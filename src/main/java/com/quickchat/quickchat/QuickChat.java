/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.quickchat.quickchat;

import java.util.Scanner;

public class QuickChat {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("=== Registration ===");
        System.out.print("Enter your first name: ");
        String firstName = input.nextLine().trim();
        System.out.print("Enter your last name: ");
        String lastName = input.nextLine().trim();

        String username;
        String password;
        String cellPhone;
        Login user;

        while (true) {
            System.out.print("Enter a username: ");
            username = input.nextLine().trim();
            user = new Login(firstName, lastName, username, "", "");
            System.out.println(user.usernameMessage());
            if (user.checkUserName()) {
                break;
            }
        }

        while (true) {
            System.out.print("Enter a password: ");
            password = input.nextLine();
            user = new Login(firstName, lastName, username, password, "");
            System.out.println(user.passwordMessage());
            if (user.checkPasswordComplexity()) {
                break;
            }
        }

        while (true) {
            System.out.print("Enter your cell phone number (e.g. +27838968976): ");
            cellPhone = input.nextLine().trim();
            user = new Login(firstName, lastName, username, password, cellPhone);
            System.out.println(user.cellPhoneMessage());
            if (user.checkCellPhoneNumber()) {
                break;
            }
        }

        System.out.println(user.registerUser());

        System.out.println();
        System.out.println("=== Login ===");
        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.print("Enter your username: ");
            String enteredUsername = input.nextLine().trim();
            System.out.print("Enter your password: ");
            String enteredPassword = input.nextLine();

            loggedIn = user.loginUser(enteredUsername, enteredPassword);
            System.out.println(user.returnLoginStatus(loggedIn));
        }

        input.close();
    }
}
