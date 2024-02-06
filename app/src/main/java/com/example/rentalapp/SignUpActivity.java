package com.example.rentalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    TextView logoText;
    ImageView logowhite;

    TextInputLayout fullName, userName, email, phone, password;
    MaterialCardView signup;
    Button login;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Hooks
        login = findViewById(R.id.login);
        fullName = findViewById(R.id.fullName);
        userName = findViewById(R.id.userName);
        logowhite = findViewById(R.id.logowhite);
        logoText = findViewById(R.id.logoText);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        signup = findViewById(R.id.signup);


        //Save data in FireBase on button click
        signup.setOnClickListener(view -> {

            Pair[] pairs = new Pair[7];
            pairs[0] = new Pair<android.view.View, String>(logowhite, "logo_image");
            pairs[1] = new Pair<android.view.View, String>(logoText, "logo_text");
            pairs[2] = new Pair<android.view.View, String>(email, "email_tran");
            pairs[3] = new Pair<android.view.View, String>(password, "password_tran");
            pairs[4] = new Pair<android.view.View, String>(signup, "button_tran");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this, pairs);

            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("users");
            String noWhiteSpace = "(?=\\s*\\S).*$";
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            String passwordVal = "^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$";

            if(TextUtils.isEmpty(fullName.getEditText().getText().toString()))
            {
                fullName.setError("Field cannot be empty");
                return;
            }
            else
            {
                fullName.setError(null);
                fullName.setErrorEnabled(false);
            }

            if(TextUtils.isEmpty(userName.getEditText().getText().toString()))
            {
                userName.setError("Field cannot be empty");
                return;
            }
            else if (userName.getEditText().getText().toString().length() > 15)
            {
                userName.setError("Username too long");
                return;
            }
            else if (userName.getEditText().getText().toString().length() < 5)
            {
                userName.setError("Username too short");
                return;
            }
            else if (!userName.getEditText().getText().toString().matches(noWhiteSpace))
            {
                userName.setError("White spaces are not allowed");
                return;
            }
            else
            {
                userName.setError(null);
                userName.setErrorEnabled(false);
            }

            if(TextUtils.isEmpty(email.getEditText().getText().toString()))
            {
                email.setError("Field cannot be empty");
                return;
            }
            else if (!email.getEditText().getText().toString().matches(emailPattern))
            {
                email.setError("Invalid email address");
                return;
            }
            else
            {
                email.setError(null);
                email.setErrorEnabled(false);
            }
            if (TextUtils.isEmpty(phone.getEditText().getText().toString()))
            {
                phone.setError("Field cannot be empty");
                return;
            }
            else if (phone.getEditText().getText().toString().length() != 10)
            {
                phone.setError("Invalid phone number");
                return;
            }
            else
            {
                phone.setError(null);
                phone.setErrorEnabled(false);
            }
            if (TextUtils.isEmpty(password.getEditText().getText().toString()))
            {
                password.setError("Field cannot be empty");
                return;
            }
            else if (!password.getEditText().getText().toString().matches(passwordVal))
            {
                password.setError("Password is too weak");
                return;
            }
            else
            {
                password.setError(null);
                password.setErrorEnabled(false);
            }


            //Get all the values
            String name = fullName.getEditText().getText().toString();
            String user = userName.getEditText().getText().toString();
            String mail = email.getEditText().getText().toString();
            String phoneNo = phone.getEditText().getText().toString();
            String pass = password.getEditText().getText().toString();

            Intent intent = new Intent(SignUpActivity.this, VerifyPhoneNumberActivity.class);
            intent.putExtra("phoneNo", phoneNo);
            startActivity(intent, options.toBundle());

            //Store the data in Firebase
            UserHelperClass helperClass = new UserHelperClass(name, user, mail, phoneNo, pass);
            reference.child(phoneNo).setValue(helperClass);
        });

        login.setOnClickListener(v -> {
            startActivity(new android.content.Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });

    }
}