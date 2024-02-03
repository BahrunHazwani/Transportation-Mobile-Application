package com.example.transportationapp.Customers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.transportationapp.Admin.AdminHome;
import com.example.transportationapp.Model.Users;
import com.example.transportationapp.Prevalent.Prevalent;
import com.example.transportationapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private EditText custMatricIdLog, custPassLog;
    private Button login_btn;
    private ProgressDialog loadingBar;
    private TextView AdminLink, NotAdminLink, forget_password_link, title;

    private String parentDbName = "Users";
    private CheckBox chkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_btn = (Button) findViewById(R.id.login_btn);
        custMatricIdLog = (EditText) findViewById(R.id.custMatricIdLog);
        custPassLog = (EditText) findViewById(R.id.custPassLog);
        AdminLink = (TextView) findViewById(R.id.admin_panel_link);
        NotAdminLink = (TextView) findViewById(R.id.not_admin_panel_link);
        forget_password_link = (TextView) findViewById(R.id.forget_password_link);
        title = (TextView) findViewById(R.id.title);
        loadingBar = new ProgressDialog(this);

        chkBoxRememberMe = (CheckBox) findViewById(R.id.remember_me_chk);
        Paper.init(this);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginCustomer();
            }
        });

        forget_password_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check", "login");
                startActivity(intent);
            }
        });

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_btn.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                forget_password_link.setVisibility(View.INVISIBLE);
                parentDbName = "Admin";
                title.setText("Admin Login");
                custMatricIdLog.setHint("Admin ID");

            }
        });

        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_btn.setText("Login");
                custMatricIdLog.setHint("Matric ID");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                forget_password_link.setVisibility(View.VISIBLE);
                title.setText("Customer Login");
                parentDbName = "Users";
            }
        });

    }


    private void LoginCustomer() {
        String matricId = custMatricIdLog.getText().toString();
        String password = custPassLog.getText().toString();

        if (TextUtils.isEmpty(matricId)) {
            if (parentDbName.equals("Users")) {
                if (matricId.isEmpty()) {
                    custMatricIdLog.setError("Please enter your Matric ID!");
                    custMatricIdLog.requestFocus();
                    return;
                }
            }
            if (parentDbName.equals("Admin")) {
                if (matricId.isEmpty()) {
                    custMatricIdLog.setError("Please enter your Admin ID!");
                    custMatricIdLog.requestFocus();
                    return;
                }
            }
        }
        else if (TextUtils.isEmpty(password)) {
            if(password.isEmpty()) {
                custPassLog.setError("Please enter your Password!");
                custPassLog.requestFocus();
                return;
            }

            if(password.length() < 6) {
                custPassLog.setError("Minimum password length is 6 characters!");
                custPassLog.requestFocus();
                return;
            }
        } else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            AllowAccessToAccount(matricId, password);
        }
    }

    private void AllowAccessToAccount(String matricId, String password) {

        if (chkBoxRememberMe.isChecked()) {
            Paper.book().write(Prevalent.CustMatricIdKey, matricId);
            Paper.book().write(Prevalent.CustPassKey, password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(parentDbName).child(matricId).exists()) {
                    Users usersData = snapshot.child(parentDbName).child(matricId).getValue(Users.class);

                    if (usersData.getMatricId().equals(matricId)) {

                        if (usersData.getPassword().equals(password)) {

                            if (parentDbName.equals("Admin")){
                                Toast.makeText(LoginActivity.this, "Welcome Admin, you are logged in Successfully... ", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, AdminHome.class);
                                startActivity(intent);
                            }
                            else if (parentDbName.equals("Users")){
                                Toast.makeText(LoginActivity.this, "Logged in Successfully... ", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                            }

                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (parentDbName == "Users") {
                        Toast.makeText(LoginActivity.this, "Account with this " + matricId + " do not exists", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "You need to create a new Account.", Toast.LENGTH_SHORT).show();
                    } else if (parentDbName == "Admin") {
                        Toast.makeText(LoginActivity.this, "Account with this Admin ID do not exists", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}