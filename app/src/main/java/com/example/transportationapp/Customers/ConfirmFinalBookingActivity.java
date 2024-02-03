package com.example.transportationapp.Customers;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.transportationapp.Prevalent.Prevalent;
import com.example.transportationapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class ConfirmFinalBookingActivity extends AppCompatActivity {

    private EditText booking_name, booking_phone, booking_date, booking_time, booking_payment, booking_destination, booking_passenger, booking_duration;
    private TextView txt_title, txt_cab_info, txt_rental_info, txt_identity_card, txt_license;
    private Button confirm_booking_btn;
    private ImageView identity_card_image, license_image;

    private Uri MyKadUri;
    private Uri LicenseUri;
    private String downloadImageUrl, downloadImageUrl2;
    private static final int GalleryPick = 2;
    private static final int GalleryPick2 = 1;
    private StorageReference licensePic;
    private StorageReference ICPic;

    private String carID = "";
    private String customerID = "";
    private DatabaseReference CarsRef, spRef;


    private String totalAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_booking);

        spRef = FirebaseDatabase.getInstance().getReference().child("Wish List").child("Booking Info");
        carID = getIntent().getStringExtra("cid");
        customerID = getIntent().getStringExtra("uid");


        licensePic = FirebaseStorage.getInstance().getReference().child("License pictures");
        ICPic = FirebaseStorage.getInstance().getReference().child("MyKad pictures");


        booking_name = (EditText) findViewById(R.id.booking_name);
        booking_phone = (EditText) findViewById(R.id.booking_phone);
        booking_date = (EditText) findViewById(R.id.booking_date);
        booking_time = (EditText) findViewById(R.id.booking_time);
        booking_payment = (EditText) findViewById(R.id.booking_payment);
        booking_destination = (EditText) findViewById(R.id.booking_destination);
        booking_passenger = (EditText) findViewById(R.id.booking_passenger);
        booking_duration = (EditText) findViewById(R.id.booking_duration);

        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_cab_info = (TextView) findViewById(R.id.txt_cab_info);
        txt_rental_info = (TextView) findViewById(R.id.txt_rental_info);
        txt_identity_card = (TextView) findViewById(R.id.txt_identity_card);
        txt_license = (TextView) findViewById(R.id.txt_license);

        confirm_booking_btn = (Button) findViewById(R.id.confirm_booking_btn);

        identity_card_image = (ImageView) findViewById(R.id.identity_card_image);
        license_image = (ImageView) findViewById(R.id.license_image);


        confirm_booking_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check();
            }
        });

        identity_card_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });

        license_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery2();
            }
        });


    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }

    private void OpenGallery2() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick2);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GalleryPick  && resultCode==RESULT_OK  &&  data!=null) {

            MyKadUri = data.getData();

            identity_card_image.setImageURI(MyKadUri);
            MyIC();

        }
        if (requestCode==GalleryPick2  && resultCode==RESULT_OK  &&  data!=null){
            LicenseUri = data.getData();
            license_image.setImageURI(LicenseUri);
            License();
        }


    }

    private void License() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading License");
        progressDialog.setMessage("Please wait, while we are uploading your information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        //License
        if (LicenseUri != null) {
            final StorageReference filepath = licensePic
                    .child(Prevalent.currentOnlineUser.getMatricId() + ".jpg");

            final UploadTask uploadTask2 = filepath.putFile(LicenseUri);


            uploadTask2.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    downloadImageUrl2 = filepath.getDownloadUrl().toString();
                    downloadImageUrl2 = task.getResult().toString();
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {

                        downloadImageUrl2 = task.getResult().toString();
//                        Toast.makeText(ConfirmFinalBookingActivity.this, "got the License image Url Successfully...", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }



                }
            });}

    }

    private void MyIC() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading MyKad");
        progressDialog.setMessage("Please wait, while we are uploading your information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (MyKadUri != null)     {
            //IC
            StorageReference fileref = ICPic.child(Prevalent.currentOnlineUser.getMatricId() + ".jpg");

            final UploadTask uploadTask = fileref.putFile(MyKadUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    String message = e.toString();
                    Toast.makeText(ConfirmFinalBookingActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(ConfirmFinalBookingActivity.this, " Car Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            downloadImageUrl = fileref.getDownloadUrl().toString();
                            downloadImageUrl = task.getResult().toString();
                            return fileref.getDownloadUrl();

                        }

                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {

                                downloadImageUrl = task.getResult().toString();
                                progressDialog.dismiss();
                            }



                        }
                    });

                }
            });}
    }

    private void Check() {
        if (TextUtils.isEmpty(booking_name.getText().toString())) {
            Toast.makeText(this, "Please provide your name.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(booking_phone.getText().toString())) {
            Toast.makeText(this, "Please provide your phone number.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(booking_date.getText().toString())) {
            Toast.makeText(this, "Please provide your booking date.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(booking_time.getText().toString())) {
            Toast.makeText(this, "Please provide your booking time.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(booking_payment.getText().toString())) {
            Toast.makeText(this, "Please state your payment amount.", Toast.LENGTH_SHORT).show();
        }
//        else if (TextUtils.isEmpty(booking_destination.getText().toString())) {
//            Toast.makeText(this, "Please provide your details.", Toast.LENGTH_SHORT).show();
//        }
//        else if (TextUtils.isEmpty(booking_passenger.getText().toString())) {
//            Toast.makeText(this, "Please provide your details.", Toast.LENGTH_SHORT).show();
//        }
//        else if (TextUtils.isEmpty(booking_duration.getText().toString())) {
//            Toast.makeText(this, "Please provide your details.", Toast.LENGTH_SHORT).show();
//        }
        else {
            ConfirmBooking();
        }

    }

    private void ConfirmBooking() {



        final String saveCurrentDate, saveCurrentTime;

        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());




        final DatabaseReference BookingsRef = FirebaseDatabase.getInstance().getReference()
                .child("Bookings")
                .child(Prevalent.currentOnlineUser.getMatricId());
        HashMap<String, Object> bookingsMap = new HashMap<>();
        bookingsMap.put("ic", downloadImageUrl);
        bookingsMap.put("bname", booking_name.getText().toString());
        bookingsMap.put("bphone", booking_phone.getText().toString());
        bookingsMap.put("bdate", booking_date.getText().toString());
        bookingsMap.put("btime", booking_time.getText().toString());
        bookingsMap.put("bpayment", booking_payment.getText().toString());
        bookingsMap.put("bdestination", booking_destination.getText().toString());
        bookingsMap.put("bpassenger", booking_passenger.getText().toString());
        bookingsMap.put("bduration", booking_duration.getText().toString());
        bookingsMap.put("license", downloadImageUrl2);
        bookingsMap.put("paymentMethod", "Physical Payment");
        bookingsMap.put("status", "Pending");

        bookingsMap.put("date", saveCurrentDate);
        bookingsMap.put("time", saveCurrentTime);


        BookingsRef.updateChildren(bookingsMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {


                    FirebaseDatabase.getInstance().getReference()
                            .child("Wish List")
                            .child("User View")
                            .child(Prevalent.currentOnlineUser.getMatricId())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        final DatabaseReference BookingsRef = FirebaseDatabase.getInstance().getReference()
                                                .child("Wish List")
                                                .child("Booking Info")
                                                .child(Prevalent.currentOnlineUser.getMatricId());
                                        HashMap<String, Object> bookingsMap = new HashMap<>();
                                        bookingsMap.put("ic", downloadImageUrl);
                                        bookingsMap.put("bname", booking_name.getText().toString());
                                        bookingsMap.put("bphone", booking_phone.getText().toString());
                                        bookingsMap.put("bdate", booking_date.getText().toString());
                                        bookingsMap.put("btime", booking_time.getText().toString());
                                        bookingsMap.put("bpayment", booking_payment.getText().toString());
                                        bookingsMap.put("bdestination", booking_destination.getText().toString());
                                        bookingsMap.put("bpassenger", booking_passenger.getText().toString());
                                        bookingsMap.put("bduration", booking_duration.getText().toString());
                                        bookingsMap.put("license", downloadImageUrl2);
                                        bookingsMap.put("paymentMethod", "Physical Payment");
                                        bookingsMap.put("status", "Pending");

                                        BookingsRef.updateChildren(bookingsMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    Toast.makeText(ConfirmFinalBookingActivity.this, "Your booking is confirmed", Toast.LENGTH_SHORT).show();

                                                    Intent intent = new Intent(ConfirmFinalBookingActivity.this, Payment.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }

                                            }
                                        });

                                    }
                                }
                            });
                }
            }
        });


    }
}

