package com.grpc.grpc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * GenerateLeadsActivity.java
 *
 * This activity allows users to generate and manage leads for pest control services.
 * Users can input premise details, price quoted, and select a reason for the lead.
 * The commission is automatically calculated for contracts, and leads are stored in Firestore.
 * If the user is "user," they can assign the lead to another user.
 *
 * Features:
 * - Input validation for premise name, address, and price quoted
 * - Automatic commission calculation for contracts (10% of quoted price)
 * - Firebase Firestore integration for storing lead details
 * - Allows "user" to assign leads to other users
 * - Supports lead categorization based on "Job" or "Contract"
 * - Provides user-friendly alerts and error handling
 *
 * Author: James Scott
 */


public class GenerateLeadsActivity extends AppCompatActivity {

    private EditText premiseNameEditText, premiseAddressEditText, priceQuotedEditText;
    private TextView commissionTextView, dateTextView;
    private Spinner reasonSpinner;
    private Button addLeadButton, backButton;

    private FirebaseFirestore db;
    private String userName;
    private String selectedReason = "Job"; // Default reason

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_leads);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Retrieve the username from the intent
        userName = getIntent().getStringExtra("USER_NAME");
        if (userName == null || userName.isEmpty()) {
            Toast.makeText(this, "Error: User name not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize UI elements
        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        welcomeTextView.setText("Welcome, " + userName + "!");
        premiseNameEditText = findViewById(R.id.premiseNameEditText);
        premiseAddressEditText = findViewById(R.id.premiseAddressEditText);
        priceQuotedEditText = findViewById(R.id.priceQuotedEditText);
        commissionTextView = findViewById(R.id.commissionTextView);
        dateTextView = findViewById(R.id.dateTextView);
        reasonSpinner = findViewById(R.id.reasonSpinner);
        addLeadButton = findViewById(R.id.addLeadButton);
        backButton = findViewById(R.id.backButton);

        // Set the current date
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        dateTextView.setText("Date: " + currentDate);

        // Populate the spinner with options for Reason
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.reason_options, // Array of reasons defined in resources
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reasonSpinner.setAdapter(adapter);

        // Handle spinner selection changes
        reasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedReason = parent.getItemAtPosition(position).toString();

                // Adjust commission or TBC depending on the selected reason
                if ("Contract".equalsIgnoreCase(selectedReason)) {
                    String priceQuotedStr = priceQuotedEditText.getText().toString().trim();
                    if (!priceQuotedStr.isEmpty()) {
                        try {
                            double priceQuoted = Double.parseDouble(priceQuotedStr);
                            double commission = priceQuoted * 0.10; // 10% for Contract
                            commissionTextView.setText("Commission: €" + String.format(Locale.getDefault(), "%.2f", commission));
                        } catch (NumberFormatException e) {
                            commissionTextView.setText("Commission: €0.00");
                        }
                    } else {
                        commissionTextView.setText("Commission: €0.00");
                    }
                } else if ("Job".equalsIgnoreCase(selectedReason)) {
                    commissionTextView.setText("Commission: TBC");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedReason = "Job"; // Default to Job
            }
        });

        // Automatically calculate commission when price is entered or changed
        priceQuotedEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if ("Contract".equalsIgnoreCase(selectedReason)) {
                    String priceQuotedStr = s.toString().trim();
                    if (!priceQuotedStr.isEmpty()) {
                        try {
                            double priceQuoted = Double.parseDouble(priceQuotedStr);
                            double commission = priceQuoted * 0.10; // 10% for Contract
                            commissionTextView.setText("Commission: €" + String.format(Locale.getDefault(), "%.2f", commission));
                        } catch (NumberFormatException e) {
                            commissionTextView.setText("Commission: €0.00");
                            Toast.makeText(GenerateLeadsActivity.this, "Please enter a valid price.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        commissionTextView.setText("Commission: €0.00");
                    }
                } else if ("Job".equalsIgnoreCase(selectedReason)) {
                    commissionTextView.setText("Commission: TBC");
                }
            }
        });

        addLeadButton.setOnClickListener(view -> addLead(currentDate));

        // Back Button Listener
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(GenerateLeadsActivity.this, MainActivity.class);
            intent.putExtra("USER_NAME", userName); // Pass username to MainActivity
            startActivity(intent);
            finish();
        });
    }

    // Method to save the lead to Firestore
    private void saveLeadToFirestore(String premiseName, String premiseAddress, double priceQuoted, double commission, String date, String reason, String addedBy) {
        CollectionReference leadsCollection = db.collection("Leads");
        leadsCollection.add(createLeadObject(premiseName, premiseAddress, priceQuoted, commission, date, reason, addedBy))
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Lead added successfully", Toast.LENGTH_SHORT).show();
                    // Redirect to ViewLeadsActivity
                    Intent intent = new Intent(GenerateLeadsActivity.this, ViewLeadsActivity.class);
                    intent.putExtra("USER_NAME", userName); // Pass the username
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add lead: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    private void addLead(String currentDate) {
        String premiseName = premiseNameEditText.getText().toString().trim();
        String premiseAddress = premiseAddressEditText.getText().toString().trim();
        String priceQuotedStr = priceQuotedEditText.getText().toString().trim();

        if (premiseName.isEmpty() || premiseAddress.isEmpty() || priceQuotedStr.isEmpty()) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        double priceQuoted;
        try {
            priceQuoted = Double.parseDouble(priceQuotedStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid price.", Toast.LENGTH_SHORT).show();
            return;
        }

        double commission = "Contract".equalsIgnoreCase(selectedReason) ? priceQuoted * 0.10 : 0.0;

        if ("user".equalsIgnoreCase(userName)) {
            showAssignToDialog(premiseName, premiseAddress, priceQuoted, commission, currentDate, selectedReason);
        } else {
            saveLeadToFirestore(premiseName, premiseAddress, priceQuoted, commission, currentDate, selectedReason, userName);
        }
    }

    // Method to show the dialog for user to assign the lead
    private void showAssignToDialog(String premiseName, String premiseAddress, double priceQuoted, double commission, String date, String reason) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Assign Lead");

        // Input field for assigning the lead
        final EditText input = new EditText(this);
        input.setHint("Enter name to assign lead to");
        dialogBuilder.setView(input);

        dialogBuilder.setPositiveButton("Assign", null);

        dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = dialogBuilder.create();

        dialog.setOnShowListener(dialogInterface -> {
            Button assignButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            assignButton.setOnClickListener(v -> {
                String assignedTo = input.getText().toString().trim();

                if (assignedTo.isEmpty()) {
                    Toast.makeText(this, "You must provide a name to assign the lead.", Toast.LENGTH_SHORT).show();
                } else {
                    // Save the lead with the assigned name
                    saveLeadToFirestore(premiseName, premiseAddress, priceQuoted, commission, date, reason, assignedTo);
                    dialog.dismiss();
                }
            });
        });

        dialog.show();
    }


    private Map<String, Object> createLeadObject(String premiseName, String premiseAddress, double priceQuoted, double commission, String date, String reason, String userName) {
        Map<String, Object> lead = new HashMap<>();
        lead.put("Premise Name", premiseName);
        lead.put("Premise Address", premiseAddress);
        lead.put("Price Quoted", priceQuoted);
        lead.put("Commission", commission);
        lead.put("Date", date);
        lead.put("Reason", reason); // Add reason to the database object
        lead.put("Added By", userName); // Add username to the database object
        return lead;
    }

    private void openQuotationViewActivity() {
        Intent intent = new Intent(GenerateLeadsActivity.this, QuotationViewActivity.class);
        intent.putExtra("USER_NAME", userName);
        startActivity(intent);
    }
}
