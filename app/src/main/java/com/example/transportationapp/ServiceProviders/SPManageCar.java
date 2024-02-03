package com.example.transportationapp.ServiceProviders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.transportationapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

public class SPManageCar extends AppCompatActivity {

    private Button apply_changes, delete_car_btn;
    private EditText car_name_manage, car_description_manage;
    private ImageView car_image_manage;

    private String carID = "";
    private DatabaseReference carsRef;
    private static final int GalleryPick = 1;
    private String checker = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePictureRef;
    private Uri imageUri;
    private String myUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_manage_car);

        carID = getIntent().getStringExtra("cid");
        carsRef = FirebaseDatabase.getInstance().getReference().child("Cars").child(carID);
        storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("Car Images");

        apply_changes = (Button) findViewById(R.id.apply_changes);
        car_name_manage = (EditText) findViewById(R.id.car_name_manage);
        car_description_manage = (EditText) findViewById(R.id.car_description_manage);
        car_image_manage = (ImageView) findViewById(R.id.car_image_manage);
        delete_car_btn = (Button) findViewById(R.id.delete_car_btn);

        displaySpecificCarInfo(car_name_manage, car_description_manage, car_image_manage);

        car_image_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "clicked";
                OpenGallery();
            }
        });
        apply_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        delete_car_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteThisCar();
            }
        });
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            imageUri = data.getData();
            car_image_manage.setImageURI(imageUri);

        }
        else
        {

            startActivity(new Intent(SPManageCar.this, SPManageCar.class));
            finish();
        }
    }


    private void deleteThisCar() {
        carsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Intent intent = new Intent(SPManageCar.this, SPHomeActivity.class);
                startActivity(intent);
                finish();

                Toast.makeText(SPManageCar.this, "The Car is deleted successfully.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyChanges() {
        String cname = car_name_manage.getText().toString();
        String cdescription = car_description_manage.getText().toString();

        if (car_name_manage.equals("")) {
            Toast.makeText(this, "Write down Car Name.", Toast.LENGTH_SHORT).show();
        }
        else if (car_description_manage.equals("")) {
            Toast.makeText(this, "Write down Car Description.", Toast.LENGTH_SHORT).show();
        }
        else {
            HashMap<String, Object> carMap = new HashMap<>();
            carMap.put("cid", carID);
            carMap.put("description", cdescription);
            carMap.put("carName", cname);

            carsRef.updateChildren(carMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SPManageCar.this, "Changes applied successfully.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SPManageCar.this, SPHomeActivity.class);
                        startActivity(intent);
                        finish();


                    }
                }
            });
        }
    }


    private void uploadImage() {


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating Car Details");
        progressDialog.setMessage("Please wait, while we are updating the information");
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
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();

                        HashMap<String, Object> carMap = new HashMap<>();
                        carMap.put("cid", carID);
                        carMap.put("image", myUrl);

                        carsRef.updateChildren(carMap);
                        applyChanges();
                        progressDialog.dismiss();
                    }
                }
            });
        }
        else {
            applyChanges();
        }
    }

    private void displaySpecificCarInfo(final EditText car_name_manage, final EditText car_description_manage, final ImageView car_image_manage) {
        carsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String mcarName = snapshot.child("carName").getValue().toString();
                    String mdescription = snapshot.child("description").getValue().toString();
                    String mimage = snapshot.child("image").getValue().toString();


                    car_name_manage.setText(mcarName);
                    car_description_manage.setText(mdescription);
                    Picasso.get().load(mimage).into(car_image_manage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}