package com.example.securepasswordmanager;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.SecureRandom;

public class MainActivity extends AppCompatActivity {

    private EditText usernameField, passwordField;
    private Button generateButton;

    private PasswordGenerator passwordGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        passwordGenerator = new PasswordGenerator();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize fields
        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        generateButton = findViewById(R.id.generateButton);
        CheckBox togglePasswordVisibility = findViewById(R.id.togglePasswordVisibility);

        // Set click listener for Generate Password button
        generateButton.setOnClickListener(v -> {
            String username = usernameField.getText().toString();

            if (TextUtils.isEmpty(username)) {
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
                return;
            }

            String generatedPassword = passwordGenerator.generateStrongPassword();
            passwordField.setText(generatedPassword);
            Toast.makeText(this, "Password Generated!", Toast.LENGTH_SHORT).show();
        });
        togglePasswordVisibility.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Show password
                passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                // Hide password
                passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
    }
}