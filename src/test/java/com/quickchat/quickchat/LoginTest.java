/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.quickchat.quickchat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class LoginTest {

    private Login make(String username, String password, String cell) {
        return new Login("Kyle", "Smith", username, password, cell);
    }

    // ---- assertEquals tests: checking exact message text ----

    @Test
    void usernameCorrectlyFormatted_message() {
        assertEquals("Username successfully captured.", make("kyl_1", "Ch&&sec@ke99!", "+27838968976").usernameMessage());
    }

    @Test
    void usernameIncorrectlyFormatted_message() {
        assertEquals("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.",
                make("kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976").usernameMessage());
    }

    @Test
    void passwordMeetsComplexity_message() {
        assertEquals("Password successfully captured.", make("kyl_1", "Ch&&sec@ke99!", "+27838968976").passwordMessage());
    }

    @Test
    void passwordFailsComplexity_message() {
        assertEquals("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.",
                make("kyl_1", "password", "+27838968976").passwordMessage());
    }

    @Test
    void cellPhoneCorrectlyFormatted_message() {
        assertEquals("Cell number successfully captured.", make("kyl_1", "Ch&&sec@ke99!", "+27838968976").cellPhoneMessage());
    }

    @Test
    void cellPhoneIncorrectlyFormatted_message() {
        assertEquals("Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.",
                make("kyl_1", "Ch&&sec@ke99!", "08966553").cellPhoneMessage());
    }

    @Test
    void registerUser_success() {
        assertEquals("User has been registered successfully.", make("kyl_1", "Ch&&sec@ke99!", "+27838968976").registerUser());
    }

    @Test
    void loginStatus_messages() {
        Login user = make("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Welcome Kyle, Smith it is great to see you again.", user.returnLoginStatus(true));
        assertEquals("Username or password incorrect, please try again.", user.returnLoginStatus(false));
    }

    // ---- assertTrue / assertFalse tests: checking the boolean methods ----

    @Test
    void loginSuccessful() {
        assertTrue(make("kyl_1", "Ch&&sec@ke99!", "+27838968976").loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    void loginFailed() {
        assertFalse(make("kyl_1", "Ch&&sec@ke99!", "+27838968976").loginUser("kyl_1", "wrongPass1!"));
    }

    @Test
    void usernameCorrectlyFormatted_boolean() {
        assertTrue(make("kyl_1", "x", "x").checkUserName());
    }

    @Test
    void usernameIncorrectlyFormatted_boolean() {
        assertFalse(make("kyle!!!!!!!", "x", "x").checkUserName());
    }

    @Test
    void passwordMeetsComplexity_boolean() {
        assertTrue(make("x", "Ch&&sec@ke99!", "x").checkPasswordComplexity());
    }

    @Test
    void passwordFailsComplexity_boolean() {
        assertFalse(make("x", "password", "x").checkPasswordComplexity());
    }

    @Test
    void cellPhoneCorrectlyFormatted_boolean() {
        assertTrue(make("x", "x", "+27838968976").checkCellPhoneNumber());
    }

    @Test
    void cellPhoneIncorrectlyFormatted_boolean() {
        assertFalse(make("x", "x", "08966553").checkCellPhoneNumber());
    }
}
