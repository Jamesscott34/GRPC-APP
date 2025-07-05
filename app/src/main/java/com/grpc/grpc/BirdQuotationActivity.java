package com.grpc.grpc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * BirdQuotationActivity.java
 *
 * This activity allows users to create a bird control service quotation.
 * Users can input customer details, add line items to the quotation,
 * and generate a PDF file containing all the details.
 *
 * Features:
 * - Input validation for customer details
 * - Ability to add multiple line items with descriptions and prices
 * - PDF generation for quotations
 * - Automatic field clearing after PDF generation
 * - Navigation back to the previous activity with user details
 *
 * Author: James Scott
 */

public class BirdQuotationActivity extends AppCompatActivity {

    private EditText addressInput, quoteDescriptionInput, userEmailInput, mobileNumberInput;
    private EditText itemDescriptionInput, itemPriceInput;
    private Button addItemButton, generatePdfButton;

    private String userName;

    private final List<String> descriptions = new ArrayList<>();
    private final List<Double> lineTotals = new ArrayList<>();

    /**
     * Initializes the activity, retrieves the username from intent,
     * and sets up UI elements. Handles button click events for adding
     * items and generating PDFs.
     *
     * @param savedInstanceState If the activity is being re-initialized
     *                           after previously being shut down, this
     *                           Bundle contains the most recent data.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bird_quotation);

        // Retrieve the user's name passed from ContractsActivity
        userName = getIntent().getStringExtra("USER_NAME");
        if (userName == null || userName.isEmpty()) {
            Toast.makeText(this, "Error: User name not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize inputs
        addressInput = findViewById(R.id.addressInput);
        quoteDescriptionInput = findViewById(R.id.quoteDescriptionInput);
        userEmailInput = findViewById(R.id.userEmailInput);
        mobileNumberInput = findViewById(R.id.mobileNumberInput);
        itemDescriptionInput = findViewById(R.id.itemDescriptionInput);
        itemPriceInput = findViewById(R.id.itemPriceInput);
        addItemButton = findViewById(R.id.addItemButton);
        generatePdfButton = findViewById(R.id.generatePdfButton);

        // Add Item Button Listener
        addItemButton.setOnClickListener(v -> addItem());

        // Generate PDF Button Listener
        generatePdfButton.setOnClickListener(v -> generatePdf());
    }

    /**
     * Adds a line item to the quotation, including a description and a price.
     * Validates the input fields before adding them to the list.
     */
    private void addItem() {
        String description = itemDescriptionInput.getText().toString().trim();
        String priceText = itemPriceInput.getText().toString().trim();

        if (description.isEmpty() || priceText.isEmpty()) {
            Toast.makeText(this, "Please fill in both description and price!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double price = Double.parseDouble(priceText);

            // Add the inputs to the lists
            descriptions.add(description);
            lineTotals.add(price);

            // Clear the input fields after adding
            itemDescriptionInput.setText("");
            itemPriceInput.setText("");

            Toast.makeText(this, "Item added! You can add another.", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Generates a PDF for the quotation with all the entered details.
     * Validates required fields, including email and mobile number format.
     * Calls the BirdQuotationPDFGenerator to create the file.
     */
    private void generatePdf() {
        String address = addressInput.getText().toString().trim();
        String quoteDescription = quoteDescriptionInput.getText().toString().trim();
        String userEmail = userEmailInput.getText().toString().trim();
        String mobileNumber = mobileNumberInput.getText().toString().trim();

        // Validate inputs
        if (address.isEmpty() || quoteDescription.isEmpty() || userEmail.isEmpty() || mobileNumber.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            Toast.makeText(this, "Please enter a valid email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!mobileNumber.matches("\\d{10,15}")) { // Check for a valid mobile number
            Toast.makeText(this, "Please enter a valid mobile number!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (descriptions.isEmpty() || lineTotals.isEmpty()) {
            Toast.makeText(this, "Please add at least one line item!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate the PDF
        BirdQuotationPDFGenerator.generateBirdQuotation(
                address,
                quoteDescription,
                descriptions,
                lineTotals,
                userEmail,
                mobileNumber,
                this
        );

        Toast.makeText(this, "PDF Generated Successfully!", Toast.LENGTH_SHORT).show();

        // Clear all fields after generating the PDF
        clearFields();

        // Navigate back to the previous activity with a result
        navigateBack();
    }

    /**
     * Clears all input fields and resets the lists of descriptions and prices.
     * This is called after a PDF is successfully generated.
     */
    private void clearFields() {
        addressInput.setText("");
        quoteDescriptionInput.setText("");
        userEmailInput.setText("");
        mobileNumberInput.setText("");
        itemDescriptionInput.setText("");
        itemPriceInput.setText("");
        descriptions.clear();
        lineTotals.clear();
    }

    /**
     * Navigates back to the previous activity, passing the username back
     * as a result to maintain user session.
     */
    private void navigateBack() {
        Intent backIntent = new Intent();
        backIntent.putExtra("USER_NAME", userName);
        setResult(RESULT_OK, backIntent);
        finish();
    }
}
