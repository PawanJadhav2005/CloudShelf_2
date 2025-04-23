package com.bmi.cloudshelf_ebook_management;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignInActivity extends AppCompatActivity {
    EditText email , password ;
    Button signin;
    TextView gotolog;

    FirebaseAuth firebaseAuth;

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        init();
        gotolog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, LogInActivity.class));
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emal = email.getText().toString();
                String pass = password.getText().toString();

                if(TextUtils.isEmpty(emal)|| TextUtils.isEmpty(pass)){
                    Toast.makeText(SignInActivity.this, "Please enter all the credentials", Toast.LENGTH_SHORT).show();
                }
                else{
                    registerUser(emal , pass);
                }


            }
        });

    }
    private void registerUser(String emal, String pass) {
        firebaseAuth.createUserWithEmailAndPassword(emal,pass).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                // User is successfully registered
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Toast.makeText(SignInActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();

                // Redirect to login or home activity
                // Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                // startActivity(intent);
                // finish();

            } else {
                // If registration fails, display a message to the user
                Toast.makeText(SignInActivity.this, "Registration failed: " + task.getException().getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e("tagg","Failed"+task.getException().getMessage());
            }
        });
    }
    void init(){
        email = findViewById(R.id.emailfield2);
        password = findViewById(R.id.passfield2);
        signin= findViewById(R.id.signinbtn);
        gotolog = findViewById(R.id.gotolog);
        firebaseAuth=  FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

    }
}