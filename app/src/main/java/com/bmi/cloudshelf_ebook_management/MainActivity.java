package com.bmi.cloudshelf_ebook_management;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    TextView welcomeText;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeText = findViewById(R.id.welcomeText);
        auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            welcomeText.setText("Welcome, " + user.getEmail());
        }
    }

    public void goToUpload(View view) {
        startActivity(new Intent(this, UploadActivity.class));
    }

    public void goToSearch(View view) {
        startActivity(new Intent(this, ViewActivity.class));
    }
}