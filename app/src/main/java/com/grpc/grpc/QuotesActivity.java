package com.grpc.grpc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * QuotesActivity.java
 *
 * This activity serves as a hub for generating various types of pest control service quotations.
 * Users can select from 4pt, 6pt, 8pt, and 12pt contracts, which direct them to the respective quotation generation activity.
 * The username is retrieved from the intent and passed to the selected quotation activity.
 *
 * Features:
 * - Displays a welcome message with the user's name
 * - Provides navigation to different quotation generation activities
 * - Ensures USER_NAME is always passed to the next activity
 * - Supports multiple service contract types (4pt, 6pt, 8pt, 12pt)
 *
 * Author: James Scott
 */


public class QuotesActivity extends AppCompatActivity {

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_quote);

        // Retrieve the username from the intent
        userName = getIntent().getStringExtra("USER_NAME");

        // Set the welcome message
        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        welcomeTextView.setText("Welcome, " + userName + "!");

        // Initialize buttons
        Button button8ptContact = findViewById(R.id.button8ptContact);
        Button button6ptContract = findViewById(R.id.button6ptContract);
        Button button4ptContract = findViewById(R.id.button4ptContract);
        Button button12ptContract = findViewById(R.id.button12ptContract);

        // Open General8ptActivity
        button8ptContact.setOnClickListener(view -> {
            Intent intent = new Intent(QuotesActivity.this, General8ptActivity.class);
            intent.putExtra("USER_NAME", userName);
            startActivity(intent);
        });

        // Open General6ptActivity
        button6ptContract.setOnClickListener(view -> {
            Intent intent = new Intent(QuotesActivity.this, General6ptActivity.class);
            intent.putExtra("USER_NAME", userName);
            startActivity(intent);
        });

        // Open General4ptActivity
        button4ptContract.setOnClickListener(view -> {
            Intent intent = new Intent(QuotesActivity.this, General4ptActivity.class);
            intent.putExtra("USER_NAME", userName);
            startActivity(intent);
        });

        // Open General12ptActivity
        button12ptContract.setOnClickListener(view -> {
            Intent intent = new Intent(QuotesActivity.this, General12ptActivity.class);
            intent.putExtra("USER_NAME", userName);
            startActivity(intent);
        });
    }
}
