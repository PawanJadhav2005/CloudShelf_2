package com.bmi.cloudshelf_ebook_management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ViewActivity extends AppCompatActivity {
    EditText searchField;
    Button searchBtn;
    RecyclerView resultList;
    FirebaseFirestore db;
    List<EBook> ebooks = new ArrayList<>();
    EBookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        searchField = findViewById(R.id.searchField);
        searchField.setHint("Enter eBook title to search");
        searchBtn = findViewById(R.id.searchBtn);
        resultList = findViewById(R.id.recyclerView);
        db = FirebaseFirestore.getInstance();

        adapter = new EBookAdapter(ebooks);
        resultList.setLayoutManager(new LinearLayoutManager(this));
        resultList.setAdapter(adapter);

        searchBtn.setOnClickListener(v -> searchBooks(searchField.getText().toString()));
    }

    void searchBooks(String query) {
        db.collection("ebooks")
                .whereGreaterThanOrEqualTo("title", query)
                .whereLessThanOrEqualTo("title", query + "\uf8ff")
                .get()
                .addOnSuccessListener(docs -> {
                    ebooks.clear();
                    for (DocumentSnapshot doc : docs.getDocuments()) {
                        ebooks.add(doc.toObject(EBook.class));
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}