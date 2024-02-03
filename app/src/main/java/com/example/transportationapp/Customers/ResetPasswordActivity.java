package com.example.transportationapp.Customers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.transportationapp.Prevalent.Prevalent;
import com.example.transportationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check = "";
    private TextView title_questions, page_title;
    private EditText find_matric_id, question_1, question_2;
    private Button verify_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        check = getIntent().getStringExtra("check");

        page_title = (TextView) findViewById(R.id.page_title);
        title_questions = (TextView) findViewById(R.id.title_questions);
        find_matric_id = (EditText) findViewById(R.id.find_matric_id);
        question_1 = (EditText) findViewById(R.id.question_1);
        question_2 = (EditText) findViewById(R.id.question_2);
        verify_btn = (Button) findViewById(R.id.verify_btn);


    }


    @Override
    protected void onStart() {
        super.onStart();

        find_matric_id.setVisibility(View.GONE);


        if (check.equals("settings")) {
            page_title.setText("Set Questions");
            title_questions.setText("Please set Answers for the following Security Questions");
            verify_btn.setText("Set");

            displayPreviousAnswers();

            verify_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setAnswers();

                }
            });
        }
        else if (check.equals("login")) {

            find_matric_id.setVisibility(View.VISIBLE);

            verify_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    verifyUser();
                }
            });
        }
    }

    private void setAnswers() {
        String answer1 = question_1.getText().toString().toLowerCase();
        String answer2 = question_2.getText().toString().toLowerCase();

        if (question_1.equals("") && question_2.equals("")) {
            Toast.makeText(ResetPasswordActivity.this, "Please answer both questions.", Toast.LENGTH_SHORT).show();
        }
        else {
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Users")
                    .child(Prevalent.currentOnlineUser.getMatricId());

            HashMap<String, Object> userdataMap = new HashMap<>();
            userdataMap.put("answer1", answer1);
            userdataMap.put("answer2",answer2);

            ref.child("Security Questions").updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ResetPasswordActivity.this, "You have answered security questions successfully.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ResetPasswordActivity.this, SettingsActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private void displayPreviousAnswers() {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(Prevalent.currentOnlineUser.getMatricId());

        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String ans1 = snapshot.child("answer1").getValue().toString();
                    String ans2 = snapshot.child("answer2").getValue().toString();

                    question_1.setText(ans1);
                    question_2.setText(ans2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void verifyUser() {
        final String matricid = find_matric_id.getText().toString();
        final String answer1 = question_1.getText().toString().toLowerCase();
        final String answer2 = question_2.getText().toString().toLowerCase();

        if (!matricid.equals("") && !answer1.equals("") && !answer2.equals("")) {
            final DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Users")
                    .child(matricid);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String mmatric = snapshot.child("matricId").getValue().toString();

                        if (snapshot.hasChild("Security Questions")) {
                            String ans1 = snapshot.child("Security Questions").child("answer1").getValue().toString();
                            String ans2 = snapshot.child("Security Questions").child("answer2").getValue().toString();

                            if (!ans1.equals(answer1)) {
                                Toast.makeText(ResetPasswordActivity.this, "Your first answer is wrong.", Toast.LENGTH_SHORT).show();
                            }
                            else if (!ans2.equals(answer2)) {
                                Toast.makeText(ResetPasswordActivity.this, "Your second answer is wrong.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New Password");

                                final EditText newPassword = new EditText(ResetPasswordActivity.this);
                                newPassword.setHint("Write New Password here...");
                                builder.setView(newPassword);

                                builder.setNegativeButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (!newPassword.getText().toString().equals("")) {
                                            ref.child("password")
                                                    .setValue(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(ResetPasswordActivity.this, "Password changed successfully.", Toast.LENGTH_SHORT).show();

                                                                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    });
                                        }

                                    }
                                });

                                builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });

                                builder.show();
                            }
                        }
                        else {
                            Toast.makeText(ResetPasswordActivity.this, "You have not set the security questions.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(ResetPasswordActivity.this, "This Matric ID is not exist.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            Toast.makeText(this, "Please complete the form.", Toast.LENGTH_SHORT).show();
        }


    }
}