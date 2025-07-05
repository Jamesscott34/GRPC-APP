package com.grpc.grpc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


/**
 * NonToxERAActivity.java
 *
 * This activity allows users to generate a Non-Toxic Environmental Risk Assessment (ERA) report.
 * Users can enter company details and capture a signature before generating a structured PDF report.
 *
 * Features:
 * - Input validation for company name, address, and email
 * - Captures a signature for inclusion in the report
 * - Generates a professionally formatted PDF document
 * - Clears input fields after generating the report
 * - Displays a confirmation message upon successful PDF creation
 *
 * Author: James Scott
 */


public class NonToxERAActivity extends AppCompatActivity {

    private EditText editCompanyName, editAddress, editEmail;

    private Bitmap signatureBitmap = null;
    private static final int REQUEST_SIGNATURE_CAPTURE = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nontox_era);

        // Initialize input fields
        editCompanyName = findViewById(R.id.editCompanyName);
        editAddress = findViewById(R.id.editAddress);
        editEmail = findViewById(R.id.editEmail);

        Button btnGeneratePDF = findViewById(R.id.btnGeneratePDF);


        // Generate PDF
        btnGeneratePDF.setOnClickListener(view -> generatePDF());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGNATURE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                signatureBitmap = (Bitmap) extras.get("data");

            }
        }
    }

    private void generatePDF() {
        String companyName = editCompanyName.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String email = editEmail.getText().toString().trim();

        if (companyName.isEmpty() || address.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String pdfPath = NonToxERAPDFGenerator.generateNonToxicEnvironmentalRiskAssessment(this, companyName, address, email, signatureBitmap);
        if (pdfPath != null) {
            Toast.makeText(this, "PDF Saved: " + pdfPath, Toast.LENGTH_LONG).show();

            // Clear all input fields
            editCompanyName.setText("");
            editAddress.setText("");
            editEmail.setText("");

        } else {
            Toast.makeText(this, "Error creating PDF", Toast.LENGTH_SHORT).show();
        }
    }

}
