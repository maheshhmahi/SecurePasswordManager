package com.example.myapplication;

import java.security.SecureRandom;

public class PasswordGenerator {

    static final String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
    static final String numbers = "0123456789";
    static final String specialCharacters = "!@#$%^&*()-_=+<>?";
    static final String combinedChars = upperCaseLetters + lowerCaseLetters + numbers + specialCharacters;

    public static String generateStrongPassword() {

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        password.append(upperCaseLetters.charAt(random.nextInt(upperCaseLetters.length())));
        password.append(lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length())));
        password.append(numbers.charAt(random.nextInt(numbers.length())));
        password.append(specialCharacters.charAt(random.nextInt(specialCharacters.length())));

        for (int i = 4; i < 12; i++) { // Generating a password of length 12
            password.append(combinedChars.charAt(random.nextInt(combinedChars.length())));
        }

        return shuffleString(password.toString());
    }

    public static String shuffleString(String input) {
        char[] characters = input.toCharArray();
        SecureRandom random = new SecureRandom();
        for (int i = characters.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            char temp = characters[index];
            characters[index] = characters[i];
            characters[i] = temp;
        }

        return new String(characters);
    }
}