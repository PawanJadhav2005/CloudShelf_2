package com.bmi.cloudshelf_ebook_management;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity {

    private static final int PICK_PDF_REQUEST = 1;
    private Uri pdfUri;
    private Button chooseFile, uploadFile;
    private ProgressDialog progressDialog;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        chooseFile = findViewById(R.id.chooseFile);
        uploadFile = findViewById(R.id.uploadFile);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("ebooks");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");

        chooseFile.setOnClickListener(v -> openFileChooser());
        uploadFile.setOnClickListener(v -> {
            if (pdfUri != null) {
                uploadPdf();
            } else {
                Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, PICK_PDF_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            pdfUri = data.getData();
            Toast.makeText(this, "File Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadPdf() {
        progressDialog.show();
        String fileName = System.currentTimeMillis() + ".pdf";
        StorageReference fileRef = storageReference.child(fileName);

        fileRef.putFile(pdfUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    progressDialog.dismiss();

                    FirebaseUser user = auth.getCurrentUser();
                    String userId = user.getUid();

                    Map<String, String> ebook = new HashMap<>();
                    ebook.put("title", fileName);
                    ebook.put("url", uri.toString());

                    db.collection("ebooks")
                            .document(userId)
                            .collection("userEbooks")
                            .add(ebook)
                            .addOnSuccessListener(documentReference ->
                                    Toast.makeText(UploadActivity.this, "Upload successful!", Toast.LENGTH_SHORT).show()
                            )
                            .addOnFailureListener(e ->
                                    Toast.makeText(UploadActivity.this, "Upload failed!", Toast.LENGTH_SHORT).show()
                            );
                }))
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}