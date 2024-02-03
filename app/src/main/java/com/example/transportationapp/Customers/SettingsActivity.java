package com.example.transportationapp.Customers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.transportationapp.Prevalent.Prevalent;
import com.example.transportationapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;


import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView settings_profile_image;
    private EditText settings_name, settings_phone_number, settings_email, settings_password;
    private TextView profileChangeTextBtn, closeTextBtn, saveTextButton;
    private Button security_questions_btn;

    private Uri imageUri;
    private String myUrl = "";
    private static final int GalleryPick = 1;
    private StorageTask uploadTask;
    private StorageReference storageProfilePictureRef;
    private String checker = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");

        settings_profile_image = (CircleImageView) findViewById(R.id.settings_profile_image);
        settings_name = (EditText) findViewById(R.id.settings_name);
        settings_phone_number = (EditText) findViewById(R.id.settings_phone_number);
        settings_email = (EditText) findViewById(R.id.settings_email);
        settings_password = (EditText) findViewById(R.id.settings_password);
        profileChangeTextBtn = (TextView) findViewById(R.id.profile_image_change_btn);
        closeTextBtn = (TextView) findViewById(R.id.close_settings_btn);
        saveTextButton = (TextView) findViewById(R.id.update_settings_btn);
        security_questions_btn = (Button) findViewById(R.id.security_questions_btn);

        userInfoDisplay(settings_profile_image, settings_name, settings_phone_number, settings_email, settings_password);

        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "clicked";
                OpenGallery();
            }
        });

        settings_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "clicked";
                OpenGallery();
            }
        });

        security_questions_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check", "settings");
                startActivity(intent);
            }
        });


        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                startActivity(intent);

            }
        });

        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checker.equals("clicked")) {
                    userInfoSaved();
                }
                else {
                    updateOnlyUserInfo();
                }
            }
        });

    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }


    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", settings_name.getText().toString());
        userMap.put("phone", settings_phone_number.getText().toString());
        userMap.put("email", settings_email.getText().toString());
        userMap.put("password", settings_password.getText().toString());

        ref.child(Prevalent.currentOnlineUser.getMatricId()).updateChildren(userMap);

        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
        Toast.makeText(SettingsActivity.this, "Profile Info updated successfully.", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            imageUri = data.getData();
            settings_profile_image.setImageURI(imageUri);

        }
        else
        {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
            finish();
        }
    }

    private void userInfoSaved() {
        if (TextUtils.isEmpty(settings_name.getText().toString())) {
            Toast.makeText(this, "Name is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(settings_phone_number.getText().toString())) {
            Toast.makeText(this, "Phone Number is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(settings_email.getText().toString())) {
            Toast.makeText(this, "Email is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(settings_password.getText().toString())) {
            Toast.makeText(this, "Password is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if (imageUri == null) {
            Toast.makeText(this, "Profile Image is mandatory...", Toast.LENGTH_SHORT).show();
            uploadImage();
        }

        uploadImage();

    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null) {
            final StorageReference fileref = storageProfilePictureRef
                    .child(Prevalent.currentOnlineUser.getMatricId() + ".jpg");
            uploadTask = fileref.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileref.getDownloadUrl();
                }
            })
            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("name", settings_name.getText().toString());
                        userMap.put("phone", settings_phone_number.getText().toString());
                        userMap.put("email", settings_email.getText().toString());
                        userMap.put("password", settings_password.getText().toString());
                        userMap.put("image", myUrl);

                        ref.child(Prevalent.currentOnlineUser.getMatricId()).updateChildren(userMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                        Toast.makeText(SettingsActivity.this, "Profile Info updated sucessfully.\nPlease logout and login again to notice the changes", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, "Error..", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "Image is not selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoDisplay(final CircleImageView settings_profile_image, final EditText settings_name, final EditText settings_phone_number, final EditText settings_email, final EditText settings_password) {

        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getMatricId());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.child("image").exists()) {
                        String image = snapshot.child("image").getValue().toString();
                        String name = snapshot.child("name").getValue().toString();
                        String phone = snapshot.child("phone").getValue().toString();
                        String email = snapshot.child("email").getValue().toString();
                        String password = snapshot.child("password").getValue().toString();

                        Picasso.get().load(image).into(settings_profile_image);
                        settings_name.setText(name);
                        settings_password.setText(password);
                        settings_phone_number.setText(phone);
                        settings_email.setText(email);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}