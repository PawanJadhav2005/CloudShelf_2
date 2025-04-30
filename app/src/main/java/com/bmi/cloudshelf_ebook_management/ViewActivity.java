package com.bmi.cloudshelf_ebook_management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<EBook> ebookList;
    private EBookAdapter adapter;

    private FirebaseAuth auth;
    private FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ebookList = new ArrayList<>();
        adapter = new EBookAdapter(ebookList);
        recyclerView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loadEbooks();
    }

    private void loadEbooks() {

        FirebaseUser user = auth.getCurrentUser();
        String userId = user.getUid();

        db.collection("ebooks")
                .document(userId)
                .collection("userEbooks")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ebookList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        EBook ebook = doc.toObject(EBook.class);
                        ebookList.add(ebook);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                                Toast.makeText(this, "Failed to load eBooks!", Toast.LENGTH_SHORT).show()
                        );


    }
}