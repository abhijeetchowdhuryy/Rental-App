package com.example.rentalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfileActivity extends AppCompatActivity {

    TextInputLayout name, email, phone, password;

    TextView fullNameLabel, usernameLabel;

    String user_username, user_name, user_email, user_phoneNo, user_password;

    DatabaseReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        reference = FirebaseDatabase.getInstance().getReference("users");

        //Hooks
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        fullNameLabel = findViewById(R.id.fullNameLabel);
        usernameLabel = findViewById(R.id.userNameLabel);

        showAllUserData();
    }

    private void showAllUserData() {
        Intent intent = getIntent();
        user_username = intent.getStringExtra("username");
        user_name = intent.getStringExtra("name");
        user_email = intent.getStringExtra("email");
        user_phoneNo = intent.getStringExtra("phoneNo");
        user_password = intent.getStringExtra("password");

        fullNameLabel.setText(user_name);
        usernameLabel.setText(user_username);
        name.getEditText().setText(user_name);
        email.getEditText().setText(user_email);
        phone.getEditText().setText(user_phoneNo);
        password.getEditText().setText(user_password);
    }

    public void update(View view) {
        if (isNameChanged() || isPasswordChanged() || isPhoneChanged()) {
            Toast.makeText(this, "Data has been updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Data is same and cannot be updated", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isPhoneChanged() {
        if (!user_phoneNo.equals(phone.getEditText().getText().toString())) {
            reference.child(user_username).child("phoneNo").setValue(phone.getEditText().getText().toString());
            user_phoneNo = phone.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isPasswordChanged() {
        if (!user_password.equals(password.getEditText().getText().toString())) {
            reference.child(user_username).child("password").setValue(password.getEditText().getText().toString());
            user_password = password.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isNameChanged() {
        if (!user_name.equals(name.getEditText().getText().toString())) {
            reference.child(user_username).child("name").setValue(name.getEditText().getText().toString());
            user_name = name.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }
}