package com.example.transportationapp.ServiceProviders;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;





import com.example.transportationapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class SPProfile extends AppCompatActivity {

    private CircleImageView settings_profile_image;
    private EditText settings_name, settings_phone_number, settings_acct_num, settings_email, settings_password;
    private TextView profileChangeTextBtn, closeTextBtn, saveTextButton;


    private Uri imageUri;
    private String myUrl = "";
    private static final int GalleryPick = 1;
    private StorageTask uploadTask;
    private StorageReference storageProfilePictureRef;
    private String checker = "";
    private FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_profile);
        auth = FirebaseAuth.getInstance();

        storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("SP Profile pictures");

        settings_profile_image = (CircleImageView) findViewById(R.id.settings_profile_image);
        settings_name = (EditText) findViewById(R.id.settings_name);
        settings_phone_number = (EditText) findViewById(R.id.settings_phone_number);
        settings_password = (EditText) findViewById(R.id.settings_password);
        settings_acct_num = (EditText) findViewById(R.id.settings_acct_num);
        settings_email = (EditText) findViewById(R.id.settings_email);
        profileChangeTextBtn = (TextView) findViewById(R.id.profile_image_change_btn);
        closeTextBtn = (TextView) findViewById(R.id.close_settings_btn);
        saveTextButton = (TextView) findViewById(R.id.update_settings_btn);


        userInfoDisplay(settings_profile_image, settings_name, settings_phone_number, settings_acct_num, settings_email, settings_password);

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



        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(SPProfile.this, SPHomeActivity.class);
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
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("ServiceProvider");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("sname", settings_name.getText().toString());
        userMap.put("sphone", settings_phone_number.getText().toString());
        userMap.put("accountNum", settings_acct_num.getText().toString());
        userMap.put("semail", settings_email.getText().toString());
        userMap.put("spassword", settings_password.getText().toString());

        ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(userMap);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updateEmail(settings_email.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email address updated.");
                        }
                    }
                });


        user.updatePassword(settings_password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Password updated.");
                        }
                    }
                });





        startActivity(new Intent(SPProfile.this, SPHomeActivity.class));
        Toast.makeText(SPProfile.this, "Profile Info updated successfully.", Toast.LENGTH_SHORT).show();
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

            startActivity(new Intent(SPProfile.this, SPProfile.class));
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
        else if (TextUtils.isEmpty(settings_acct_num.getText().toString())) {
            Toast.makeText(this, "Bank Name and Account Number are mandatory.", Toast.LENGTH_SHORT).show();
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
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg");
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

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("ServiceProvider");

                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap.put("sname", settings_name.getText().toString());
                                userMap.put("sphone", settings_phone_number.getText().toString());
                                userMap.put("accountNum", settings_acct_num.getText().toString());
                                userMap.put("semail", settings_email.getText().toString());
                                userMap.put("spassword", settings_password.getText().toString());
                                userMap.put("image", myUrl);

                                ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(userMap);

                                progressDialog.dismiss();

                                startActivity(new Intent(SPProfile.this, SPHomeActivity.class));
                                Toast.makeText(SPProfile.this, "Profile Info updated sucessfully.\nPlease logout and login again to notice the changes", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(SPProfile.this, "Error..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else {
            Toast.makeText(this, "Image is not selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoDisplay(final CircleImageView settings_profile_image, final EditText settings_name, final EditText settings_phone_number, final EditText settings_acct_num, final EditText settings_email, final EditText settings_password) {

        DatabaseReference ServiceProviderRef = FirebaseDatabase.getInstance().getReference().child("ServiceProvider").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        ServiceProviderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.child("image").exists()) {
                        String image = snapshot.child("image").getValue().toString();
                        String name = snapshot.child("sname").getValue().toString();
                        String phone = snapshot.child("sphone").getValue().toString();
                        String email = snapshot.child("semail").getValue().toString();
                        String password = snapshot.child("spassword").getValue().toString();
                        String accountNum = snapshot.child("accountNum").getValue().toString();

                        Picasso.get().load(image).into(settings_profile_image);
                        settings_name.setText(name);
                        settings_phone_number.setText(phone);
                        settings_email.setText(email);
                        settings_password.setText(password);
                        settings_acct_num.setText(accountNum);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}