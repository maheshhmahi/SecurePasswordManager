package com.example.securepasswordmanager;

import java.security.SecureRandom;

public class PasswordGenerator {

    final String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    final String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
    final String numbers = "0123456789";
    final String specialCharacters = "!@#$%^&*()-_=+<>?";
    final String combinedChars = upperCaseLetters + lowerCaseLetters + numbers + specialCharacters;

    public String generateStrongPassword() {

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

    public String shuffleString(String input) {
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
