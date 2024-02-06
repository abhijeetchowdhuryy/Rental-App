package com.example.rentalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextView signup, signinToContinue, logoText;
    ImageView logowhite;
    TextInputLayout email, password;

    FirebaseAuth auth;

    ProgressDialog progressDialog;

    MaterialCardView loggin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN, android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        signinToContinue = findViewById(R.id.signinToContinue);
        logoText = findViewById(R.id.logoText);
        logowhite = findViewById(R.id.logowhite);
        signup = findViewById(R.id.signup);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        auth = FirebaseAuth.getInstance();
        loggin = findViewById(R.id.loggin);
        signup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);

            Pair[] pairs = new Pair[7];
            pairs[0] = new Pair<android.view.View, String>(logowhite, "logo_image");
            pairs[1] = new Pair<android.view.View, String>(logoText, "logo_text");
            pairs[2] = new Pair<android.view.View, String>(signinToContinue, "signin_text");
            pairs[3] = new Pair<android.view.View, String>(email, "email_tran");
            pairs[4] = new Pair<android.view.View, String>(password, "password_tran");
            pairs[5] = new Pair<android.view.View, String>(signup, "button_tran");
            pairs[6] = new Pair<android.view.View, String>(findViewById(R.id.login), "login_signup_tran");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
            startActivity(intent, options.toBundle());
        });

        loggin.setOnClickListener(v -> {
            String emailText = email.getEditText().getText().toString();
            String passwordText = password.getEditText().getText().toString();
            if(TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText))
            {
                android.widget.Toast.makeText(LoginActivity.this, "Please fill all the fields", android.widget.Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.setMessage("Logging in...");
                progressDialog.show();
                loginUser(emailText, passwordText);
            }
        });
    }

    private void loginUser(String emailText, String passwordText) {
            auth.signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(task -> {
                if(task.isSuccessful())
                {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, UserProfileActivity.class));
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                }
            });
    }
}