package com.example.transportationapp.Customers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.transportationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button register_btn;
    private EditText custName, custMatricId, custPhone, custEmailRegister, custPassRegister;
    private ProgressDialog loadingBar;
    private TextView login_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_btn = (Button) findViewById(R.id.register_btn);
        custName = (EditText) findViewById(R.id.custName);
        custMatricId = (EditText) findViewById(R.id.custMatricId);
        custPhone = (EditText) findViewById(R.id.custPhone);
        custEmailRegister = (EditText) findViewById(R.id.custEmailRegister);
        custPassRegister = (EditText) findViewById(R.id.custPassRegister);
        login_link = (TextView) findViewById(R.id.login_link);
        loadingBar = new ProgressDialog(this);

        login_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.putExtra("check", "login");
                startActivity(intent);
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_btn();
            }
        });
    }

    private void register_btn() {
        String name = custName.getText().toString();
        String matricId = custMatricId.getText().toString();
        String phone = custPhone.getText().toString();
        String email = custEmailRegister.getText().toString();
        String password = custPassRegister.getText().toString();


        if (TextUtils.isEmpty(name)) {
                custName.setError("Name is Required!");
                custName.requestFocus();
        } else if (TextUtils.isEmpty(matricId)) {
            custMatricId.setError("Matric ID is Required!");
            custMatricId.requestFocus();
        } else if (TextUtils.isEmpty(phone)) {
            custPhone.setError("Phone Number is Required!");
            custPhone.requestFocus();
        } else if (TextUtils.isEmpty(email)) {
            custEmailRegister.setError("Email is Required!");
           custEmailRegister.requestFocus();

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                custEmailRegister.setError("Please provide valid email!");
                custEmailRegister.requestFocus();
                return;
            }
        } else if (TextUtils.isEmpty(password)) {
            custPassRegister.setError("Password is Required!");
            custPassRegister.requestFocus();

            if (password.length() < 6) {
                custPassRegister.setError("Minimum password length should be 6 characters!");
                custPassRegister.requestFocus();
                return;
            }
        } else {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatematricId(name, matricId, phone, email, password);
        }
    }


    private void ValidatematricId(String name, String matricId, String phone, String email, String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Users").child(matricId).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("name", name);
                    userdataMap.put("matricId", matricId);
                    userdataMap.put("phone", phone);
                    userdataMap.put("email", email);
                    userdataMap.put("password", password);

                    RootRef.child("Users").child(matricId).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(RegisterActivity.this, "Please update your profile after login process", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Network Error: Please try again after some time..", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                } else {
                    Toast.makeText(RegisterActivity.this, "This " + matricId + " already exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try again using another Matric ID.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
