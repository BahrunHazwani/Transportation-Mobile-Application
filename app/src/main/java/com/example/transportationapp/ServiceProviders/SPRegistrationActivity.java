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

import com.example.transportationapp.Customers.RegisterActivity;
import com.example.transportationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SPRegistrationActivity extends AppCompatActivity {
    private Button sp_already_hvacct_btn, sp_register_btn;
    private EditText spName, spMatricId, spPhone, spAcctNum, spEmailRegister, spPassRegister;
    private TextView login_link;

    private FirebaseAuth mAuth;

    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_registration);

        mAuth = FirebaseAuth.getInstance();

        login_link = (TextView) findViewById(R.id.login_link);
        sp_register_btn = (Button) findViewById(R.id.sp_register_btn);

        spName = (EditText) findViewById(R.id.spName);
        spMatricId = (EditText) findViewById(R.id.spMatricId);
        spPhone = (EditText) findViewById(R.id.spPhone);
        spEmailRegister = (EditText) findViewById(R.id.spEmailRegister);
        spPassRegister = (EditText) findViewById(R.id.spPassRegister);
        spAcctNum = (EditText) findViewById(R.id.spAcctNum);

        loadingBar = new ProgressDialog(this);


        login_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SPRegistrationActivity.this, SPLoginActivity.class);
                startActivity(intent);
            }
        });

        sp_register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerSP();
            }
        });
    }

    private void registerSP() {
       final String name = spName.getText().toString();
       final String matricId = spMatricId.getText().toString();
       final String phone = spPhone.getText().toString();
       final String email = spEmailRegister.getText().toString();
       final String password = spPassRegister.getText().toString();
       final String acctNum = spAcctNum.getText().toString();

        if (name.isEmpty()) {
            spName.setError("Name is Required!");
            spName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            spEmailRegister.setError("Email is Required!");
            spEmailRegister.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            spEmailRegister.setError("Please provide valid email!");
            spEmailRegister.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            spPassRegister.setError("Password is Required!");
            spPassRegister.requestFocus();
            return;
        }

        if (password.length() < 6) {
            spPassRegister.setError("Minimum password length should be 6 characters!");
            spPassRegister.requestFocus();
            return;
        }



        if (matricId.isEmpty()) {
            spMatricId.setError("Matric ID is Required!");
            spMatricId.requestFocus();
            return;
        }

        if (acctNum.isEmpty()) {
            spAcctNum.setError("Bank Name and Account Number are Required!");
            spAcctNum.requestFocus();
            return;
        }


        if (phone.isEmpty()) {
            spPhone.setError("Phone Number is Required!");
            spPhone.requestFocus();
            return;
        }


        if (!name.equals("") && !matricId.equals("") && !phone.equals("") && !acctNum.equals("") && !email.equals("") && !password.equals("")) {
            loadingBar.setTitle("Creating Service Provider Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final DatabaseReference rootRef;
                                rootRef = FirebaseDatabase.getInstance().getReference();

                                String sid = mAuth.getUid();

                                HashMap<String, Object> serviceproviderMap = new HashMap<>();
                                serviceproviderMap.put("sid", sid);
                                serviceproviderMap.put("sname", name);
                                serviceproviderMap.put("smatricID", matricId);
                                serviceproviderMap.put("semail", email);
                                serviceproviderMap.put("sphone", phone);
                                serviceproviderMap.put("accountNum", acctNum);
                                serviceproviderMap.put("spassword", password);

                                rootRef.child("ServiceProvider").child(sid).updateChildren(serviceproviderMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                loadingBar.dismiss();
                                                Toast.makeText(SPRegistrationActivity.this, "You are registered successfully.", Toast.LENGTH_SHORT).show();
                                                Toast.makeText(SPRegistrationActivity.this, "Please update your profile after login process", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(SPRegistrationActivity.this, SPHomeActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                            }
                        }
                    });
        }
        else {
            Toast.makeText(this, "Failed to register! Try again!", Toast.LENGTH_SHORT).show();
        }
    }
}