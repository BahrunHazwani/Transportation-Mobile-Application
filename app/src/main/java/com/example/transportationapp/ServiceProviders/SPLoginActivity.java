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
import android.widget.TextView;
import android.widget.Toast;

import com.example.transportationapp.Customers.LoginActivity;
import com.example.transportationapp.Customers.ResetPasswordActivity;
import com.example.transportationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SPLoginActivity extends AppCompatActivity {
    private Button sp_login_btn;
    private EditText spEmailLog, spPassLog;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;
    private TextView forget_password_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_login);

        sp_login_btn = (Button) findViewById(R.id.sp_login_btn);
        spEmailLog = (EditText) findViewById(R.id.spEmailLog);
        spPassLog = (EditText) findViewById(R.id.spPassLog);
        forget_password_link = (TextView) findViewById(R.id.forget_password_link);

        loadingBar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        forget_password_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SPLoginActivity.this, Sp_Forgot_Password.class);
                intent.putExtra("check", "login");
                startActivity(intent);
            }
        });


        sp_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginSP();
            }
        });

    }

    private void loginSP() {
        final String email = spEmailLog.getText().toString();
        final String password = spPassLog.getText().toString();

        if(email.isEmpty()) {
            spEmailLog.setError("Email is required!");
            spEmailLog.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            spEmailLog.setError("Please enter a valid email!");
            spEmailLog.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            spPassLog.setError("Password is required!");
            spPassLog.requestFocus();
            return;
        }

        if(password.length() < 6) {
            spPassLog.setError("Minimum password length is 6 characters!");
            spPassLog.requestFocus();
            return;
        }

        if (!email.equals("") && !password.equals("")) {
            loadingBar.setTitle("Service Provider Account Login");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(SPLoginActivity.this, SPHomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(SPLoginActivity.this, "Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
        }
        else {
            Toast.makeText(this, "Please complete the login fields.", Toast.LENGTH_SHORT).show();
        }
    }
}