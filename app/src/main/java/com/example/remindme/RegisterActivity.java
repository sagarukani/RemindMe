package com.example.remindme;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Register user activity
 */
public class RegisterActivity extends AppCompatActivity {

    private Button btnRegister;
    private EditText etEmail,etPassword,etConfirmPassword, etName;
    private FirebaseAuth mAuth;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FirebaseDatabase mFirebaseDatabase;


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        users = mFirebaseDatabase.getReference("users");
        initView();
        onClick();
    }

    private void onClick() {
        btnRegister.setOnClickListener(v -> {
            //Creating new user
            mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etConfirmPassword.getText().toString())
                    .addOnSuccessListener(authResult -> {
                        //save user to the database
                        User user = new User();
                        user.setFullName(etName.getText().toString());
                        user.setEmail(etEmail.getText().toString());

                        //use phone as key

                        users.child(mAuth.getCurrentUser().getUid())
                                .setValue(user)
                                .addOnSuccessListener(aVoid -> Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_LONG).show())
                                .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_LONG).show());

                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Authentication Failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
        });
    }

    private void initView() {
        btnRegister = findViewById(R.id.btnRegister);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etName = findViewById(R.id.etName);
    }
}