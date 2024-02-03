package com.example.transportationapp.ServiceProviders;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.transportationapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SPAddNewCar extends AppCompatActivity {

    private String carType, CarName, Description, saveCurrentDate, saveCurrentTime;
    private Button add_new_car;
    private ImageView select_car;
    private EditText car_name, car_description;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String carRandomKey, downloadImageUrl;
    private StorageReference CarImagesRef;
    private DatabaseReference CarsRef, spRef;
    private ProgressDialog loadingBar;

    private String sName, sMatricId, sPhone, sEmail, sId, accountNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_add_new_car);

        carType = getIntent().getExtras().get("carType").toString();
        CarImagesRef = FirebaseStorage.getInstance().getReference().child("Car Images");
        CarsRef = FirebaseDatabase.getInstance().getReference().child("Cars");
        spRef = FirebaseDatabase.getInstance().getReference().child("ServiceProvider");

        add_new_car = (Button) findViewById(R.id.add_new_car);
        select_car = (ImageView) findViewById(R.id.select_car);
        car_name = (EditText) findViewById(R.id.car_name);
        car_description = (EditText) findViewById(R.id.car_description);
        loadingBar = new ProgressDialog(this);

        select_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });

        add_new_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateCarData();
            }
        });

        spRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            sName = snapshot.child("sname").getValue().toString();
                            sMatricId = snapshot.child("smatricID").getValue().toString();
                            sEmail = snapshot.child("semail").getValue().toString();
                            sPhone = snapshot.child("sphone").getValue().toString();
                            sId = snapshot.child("sid").getValue().toString();
                            accountNum = snapshot.child("accountNum").getValue().toString();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  && resultCode==RESULT_OK  &&  data!=null) {

            ImageUri = data.getData();
            select_car.setImageURI(ImageUri);
        }
    }

    private void ValidateCarData(){
        Description = car_description.getText().toString();
        CarName = car_name.getText().toString();


        if (ImageUri == null) {
            Toast.makeText(this, "Car image is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description)){
            Toast.makeText(this, "Please write car's description", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(CarName)){
            Toast.makeText(this, "Please write car's name", Toast.LENGTH_SHORT).show();
        }
        else {
            StoreCarInformation();
        }
    }

    private void StoreCarInformation() {
        loadingBar.setTitle("Add New Car");
        loadingBar.setMessage("Please wait, while we are adding new car");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        carRandomKey = saveCurrentDate + saveCurrentTime;

        StorageReference filePath = CarImagesRef.child(ImageUri.getLastPathSegment() + carRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String message = e.toString();
                Toast.makeText(SPAddNewCar.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {

                            downloadImageUrl = task.getResult().toString();
                            SaveCarInfoToDatabase();
                        }



                    }
                });

            }
        });
    }

    private void SaveCarInfoToDatabase() {

        HashMap<String, Object> carMap = new HashMap<>();
        carMap.put("cid", carRandomKey);
        carMap.put("date", saveCurrentDate);
        carMap.put("time", saveCurrentTime);
        carMap.put("description", Description);
        carMap.put("image", downloadImageUrl);
        carMap.put("carType", carType);
        carMap.put("carName", CarName);

        carMap.put("sname", sName);
        carMap.put("smatricID", sMatricId);
        carMap.put("sphone", sPhone);
        carMap.put("semail", sEmail);
        carMap.put("sid", sId);
        carMap.put("accountNum", accountNum);
        carMap.put("carStatus", "Not Approved");

        CarsRef.child(carRandomKey).updateChildren(carMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(SPAddNewCar.this, SPHomeActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(SPAddNewCar.this, "Car is added successfully...", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(SPAddNewCar.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }




}