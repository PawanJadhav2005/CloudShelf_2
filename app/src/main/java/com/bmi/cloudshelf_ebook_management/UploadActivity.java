package com.bmi.cloudshelf_ebook_management;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity {
    Button chooseFileBtn, uploadBtn;
    Uri pdfUri;
    String pdfTitle;
    FirebaseStorage storage;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        chooseFileBtn = findViewById(R.id.chooseFile);
        uploadBtn = findViewById(R.id.uploadFile);
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        chooseFileBtn.setOnClickListener(v -> selectPdf());
        uploadBtn.setOnClickListener(v -> uploadPdf());
    }

    void selectPdf() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            pdfUri = data.getData();
            if (pdfUri != null) {
                Cursor cursor = getContentResolver().query(pdfUri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    pdfTitle = cursor.getString(nameIndex);
                    cursor.close();
                }
            }
        }
    }

    void uploadPdf() {
        if (pdfUri == null || pdfTitle == null || pdfTitle.isEmpty()) {
            Toast.makeText(this, "Please select a PDF file first.", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference ref = storage.getReference().child("ebooks/" + System.currentTimeMillis() + ".pdf");
        ref.putFile(pdfUri)
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return ref.getDownloadUrl();
                })
                .addOnSuccessListener(uri -> {
                    Map<String, Object> ebook = new HashMap<>();
                    ebook.put("title", pdfTitle);
                    ebook.put("url", uri.toString());
                    db.collection("ebooks").add(ebook);
                    Toast.makeText(this, "Uploaded", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Upload Failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }
}