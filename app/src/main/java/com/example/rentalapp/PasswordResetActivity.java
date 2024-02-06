package com.example.rentalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PasswordResetActivity extends AppCompatActivity {

    TextInputLayout resetmail, otp;

    String verificationCodeBySystem;
    MaterialCardView verify;
    FirebaseAuth auth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        resetmail = findViewById(R.id.resetmail);
        otp = findViewById(R.id.otp);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        auth = FirebaseAuth.getInstance();
        verify = findViewById(R.id.verify);
        String phoneNo = getIntent().getStringExtra("phoneNo");
        sendVerificationCodeToUser(phoneNo);
        verify.setOnClickListener(v -> {
            String emailText = resetmail.getEditText().getText().toString();
            String otpText = otp.getEditText().getText().toString();
            if(TextUtils.isEmpty(emailText) &&  TextUtils.isEmpty(otpText))
            {
                Toast.makeText(PasswordResetActivity.this, "Enter Email or Phone No. to reset password", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(emailText) && !TextUtils.isEmpty(otpText)) {
                if (otpText.isEmpty() || otpText.length() < 6) {
                    otp.setError("Wrong OTP...");
                    otp.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(otpText);

            } else {
                progressBar.setVisibility(View.VISIBLE);
                auth.sendPasswordResetEmail(emailText).addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(PasswordResetActivity.this, "Check your Email", Toast.LENGTH_SHORT).show();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(PasswordResetActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void sendVerificationCodeToUser(String phoneNo) {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+91" + phoneNo,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    (Activity) TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationCodeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(PasswordResetActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private  void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(PasswordResetActivity.this, UserProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                            Toast.makeText(PasswordResetActivity.this, "Verification Completed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PasswordResetActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
    }

}