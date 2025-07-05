package com.grpc.grpc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * StoredReportsActivity.java
 *
 * This activity allows users to browse and manage stored reports in Firebase Storage.
 * Users can navigate through parent folders, access subfolders, and view stored report files.
 *
 * Features:
 * - Loads parent folders from Firebase Storage
 * - Supports navigation into subfolders for organized report access
 * - Displays files stored in a selected folder
 * - Allows users to open and view reports in PDF format
 * - Excludes backup folders from the listing
 * - Provides user-friendly dialogs for file selection
 *
 * Author: James Scott
 */


public class StoredReportsActivity extends AppCompatActivity {

    private RecyclerView folderRecyclerView;
    private FolderAdapter adapter;
    private List<String> folderList = new ArrayList<>();
    private Button buttonBack;
    private String selectedParentFolder = null; // Keeps track of the currently selected parent folder

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stored_reports);

        folderRecyclerView = findViewById(R.id.folderRecyclerView);
        folderRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> finish());

        // Load root folders from Firebase Storage
        loadParentFolders();
    }

    /**
     * Loads the parent folders (e.g., "ReportsXX/") from Firebase Storage.
     */
    private void loadParentFolders() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        storageRef.listAll().addOnSuccessListener(listResult -> {
            folderList.clear();
            for (StorageReference prefix : listResult.getPrefixes()) {
                String folderName = prefix.getName();
                if (!folderName.equals("backup")) { // Exclude "backup" folder
                    folderList.add(folderName);
                }
            }

            if (adapter == null) {
                adapter = new FolderAdapter(folderList, this::loadSubFolders);
                folderRecyclerView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }

        }).addOnFailureListener(e ->
                Toast.makeText(StoredReportsActivity.this, "Failed to load folders: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }

    /**
     * Loads subfolders within the selected parent folder.
     */
    private void loadSubFolders(String parentFolder) {
        selectedParentFolder = parentFolder; // Track the current parent folder
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference parentFolderRef = storage.getReference().child(parentFolder);

        parentFolderRef.listAll().addOnSuccessListener(listResult -> {
            List<String> subFolderList = new ArrayList<>();
            for (StorageReference prefix : listResult.getPrefixes()) {
                subFolderList.add(prefix.getName());
            }

            if (subFolderList.isEmpty()) {
                // If no subfolders exist, display the files instead
                loadFilesFromFolder(parentFolder);
            } else {
                // Show subfolder selection dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select Subfolder");

                builder.setItems(subFolderList.toArray(new String[0]), (dialog, which) -> {
                    String selectedSubFolder = subFolderList.get(which);
                    loadFilesFromFolder(parentFolder + "/" + selectedSubFolder);
                });

                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                builder.show();
            }

        }).addOnFailureListener(e ->
                Toast.makeText(this, "Failed to load subfolders: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }

    /**
     * Loads files from the selected subfolder.
     */
    private void loadFilesFromFolder(String folderPath) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference folderRef = storage.getReference().child(folderPath);

        folderRef.listAll().addOnSuccessListener(listResult -> {
            List<String> fileList = new ArrayList<>();
            for (StorageReference item : listResult.getItems()) {
                fileList.add(item.getName());
            }

            runOnUiThread(() -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Files in " + folderPath);

                if (fileList.isEmpty()) {
                    builder.setMessage("No files found in this folder.")
                            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                } else {
                    builder.setItems(fileList.toArray(new String[0]), (dialog, which) -> {
                        String selectedFile = fileList.get(which);
                        viewFile(folderPath, selectedFile);
                    });
                }
                builder.show();
            });
        }).addOnFailureListener(e ->
                runOnUiThread(() -> Toast.makeText(this, "Failed to load files: " + e.getMessage(), Toast.LENGTH_SHORT).show())
        );
    }

    /**
     * Opens a selected file from Firebase Storage.
     */
    private void viewFile(String folder, String fileName) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference fileRef = storage.getReference().child(folder + "/" + fileName);

        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Failed to open file: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }
}
