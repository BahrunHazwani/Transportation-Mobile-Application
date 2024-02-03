package com.example.transportationapp.ServiceProviders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.transportationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Sp_Forgot_Password extends AppCompatActivity {
    private EditText spEmail;
    private Button resetPassword;
    private ProgressDialog loadingBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_forgot_password);

        loadingBar = new ProgressDialog(this);

        spEmail = (EditText) findViewById(R.id.spEmail);
        resetPassword = (Button) findViewById(R.id.resetPassword);

        mAuth = FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = spEmail.getText().toString().trim();

        if(email.isEmpty()) {
            spEmail.setError("Email is required!");
            spEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            spEmail.setError("Please provide a valid email!");
            spEmail.requestFocus();
            return;
        }


        loadingBar.setTitle("Forgot Password");
        loadingBar.setMessage("Please wait, while we are checking the credentials.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                if (task.isSuccessful()) {

                    Toast.makeText(Sp_Forgot_Password.this, "Check your email to reset your password", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Sp_Forgot_Password.this, SPLoginActivity.class);
                    startActivity(intent);
                    loadingBar.dismiss();
                }
                else {
                    Toast.makeText(Sp_Forgot_Password.this, "This Email is not exist", Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }
            }
        });
    }
}