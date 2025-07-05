package com.grpc.grpc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * EnvironmentSelectionActivity.java
 *
 * This activity allows users to select between a Toxic or Non-Toxic Environment Risk Assessment (ERA).
 * It retrieves the username from the intent and displays a welcome message.
 * Users can choose between Toxic and Non-Toxic ERAs, which will open their respective activities.
 *
 * Features:
 * - Displays a welcome message with the user's name
 * - Allows selection between Toxic and Non-Toxic ERAs
 * - Passes the username to the selected ERA activity
 *
 * Author: James Scott
 */

public class EnvironmentSelectionActivity extends AppCompatActivity {

    private Button NonToxButton,ToxicButton;
    private String userEmail, userName;
    private TextView welcomeTextView;

    /**
     * Initializes the activity, retrieves the username from intent, sets up UI elements,
     * and handles button click events for navigating to the ERA selection screens.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the most recent data.
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_era);

        userName = getIntent().getStringExtra("USER_NAME");

        // Initialize the welcome TextView
        welcomeTextView = findViewById(R.id.welcomeTextView);
        welcomeTextView.setText("Welcome, " + userName + "!");

        ToxicButton = findViewById(R.id.ToxicButton);
        NonToxButton = findViewById(R.id.NonToxButton);

        // Set Button Click Listeners
        if (ToxicButton != null) {
            ToxicButton.setOnClickListener(view -> openActivity(ToxicERAActivity.class));
        }
        if (NonToxButton != null) {
            NonToxButton.setOnClickListener(view -> openActivity(NonToxERAActivity.class));


        }
    }

    /**
     * Opens the selected ERA activity and passes the username along.
     *
     * @param targetActivity The activity class to open (ToxicERAActivity or NonToxERAActivity).
     */
    private void openActivity(Class<?> targetActivity) {
        Log.d("MainActivity", "Opening " + targetActivity.getSimpleName() + " with USER_NAME: " + userName);
        Intent intent = new Intent(EnvironmentSelectionActivity.this, targetActivity);
        intent.putExtra("USER_NAME", userName);
        startActivity(intent);
    }

}
