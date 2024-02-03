package com.example.transportationapp.Customers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.transportationapp.Model.Cars;
import com.example.transportationapp.Prevalent.Prevalent;
import com.example.transportationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CarDetailsActivity extends AppCompatActivity {

    private Button addToWishListBtn;
    private ImageView car_image;
    private TextView car_name_details, car_description_details, car_type, sp_name, sp_phone, sp_acct_num;
    private String carID = "", status = "Normal";
    private String sName, sMatricId, sPhone, sEmail, sId, accountNum;
    private DatabaseReference CarsRef, spRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);

        spRef = FirebaseDatabase.getInstance().getReference().child("Cars");

        carID = getIntent().getStringExtra("cid");

        addToWishListBtn = (Button) findViewById(R.id.cd_add_to_wishlist_btn);
        car_image = (ImageView) findViewById(R.id.car_image);
        car_name_details = (TextView) findViewById(R.id.car_name_details);
        car_description_details = (TextView) findViewById(R.id.car_description_details);

        car_type = (TextView) findViewById(R.id.car_type);
        sp_name = (TextView) findViewById(R.id.sp_name);
        sp_phone = (TextView) findViewById(R.id.sp_phone);
        sp_acct_num = (TextView) findViewById(R.id.sp_acct_num);


        getCarDetails(carID);
        DatabaseReference entry = FirebaseDatabase.getInstance().getReference().child("Wish List").child("Booking Info").child(Prevalent.currentOnlineUser.getMatricId());
        entry.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    addToWishListBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {



//                            if (status.equals("Pending") || status.equals("Booking Accepted")) {
//                                Toast.makeText(CarDetailsActivity.this, "You can book more transport, once you complete your first final booking", Toast.LENGTH_LONG).show();
//
//                            }
//                            else {
                                addingToWishList();
//                            }
                        }
                    });
                }
                else if (snapshot.exists()){
                    addToWishListBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(CarDetailsActivity.this, "You allowed to add one car only at Wish List or You already booked a car", Toast.LENGTH_SHORT).show();
                            Toast.makeText(CarDetailsActivity.this, "Please check your Wish List", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        spRef.child(carID)
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

    @Override
    protected void onStart() {
        super.onStart();

        CheckBookingStatus();
    }

    private void addingToWishList() {
        String saveCurrentTime, saveCurrentDate;

        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());

        final DatabaseReference wishlistRef = FirebaseDatabase.getInstance().getReference().child("Wish List");

        final HashMap<String, Object> wishMap = new HashMap<>();
        wishMap.put("cid", carID);
        wishMap.put("carName",car_name_details.getText().toString());
        wishMap.put("carType", car_type.getText().toString());
        wishMap.put("date", saveCurrentDate);
        wishMap.put("time", saveCurrentTime);
        wishMap.put("description", car_description_details.getText().toString());

        wishMap.put("sname", sName);
        wishMap.put("smatricID", sMatricId);
        wishMap.put("sphone", sPhone);
        wishMap.put("semail", sEmail);
        wishMap.put("sid", sId);
        wishMap.put("accountNum", accountNum);


        wishlistRef.child("User View").child(Prevalent.currentOnlineUser.getMatricId())
                .child("Cars").child(carID)
                .updateChildren(wishMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            wishlistRef.child("Admin View").child(Prevalent.currentOnlineUser.getMatricId())
                                    .child("Cars").child(carID)
                                    .updateChildren(wishMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                wishlistRef.child("Booking Info").child(Prevalent.currentOnlineUser.getMatricId())
                                                        .updateChildren(wishMap)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Toast.makeText(CarDetailsActivity.this, "Added to Wish List", Toast.LENGTH_SHORT).show();

                                                                Intent intent = new Intent(CarDetailsActivity.this, HomeActivity.class);
                                                                startActivity(intent);

                                                            }
                                                        });
                                            }
                                        }
                                    });

                        }
                    }
                });


    }

    private void getCarDetails(String carID) {
        DatabaseReference carsRef = FirebaseDatabase.getInstance().getReference().child("Cars");
        carsRef.child(carID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Cars cars = snapshot.getValue(Cars.class);

                    car_name_details.setText("Car Name: " + cars.getCarName());
                    car_description_details.setText("Car Description: "+ "\n" + "\n" + cars.getDescription());
                    car_type.setText("Car Type: " + cars.getCarType());
                    sp_name.setText("Service Provider Name: " + cars.getSname());
                    sp_phone.setText("Service Provider Phone: " + cars.getSphone());
                    sp_acct_num.setText("Service Provider Account Detail: " + cars.getAccountNum());
                    Picasso.get().load(cars.getImage()).into(car_image);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void CheckBookingStatus() {
        DatabaseReference bookingsRef;
        bookingsRef = FirebaseDatabase.getInstance().getReference().child("Bookings").child(Prevalent.currentOnlineUser.getMatricId());

        bookingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String bookingstatus = snapshot.child("status").getValue().toString();

                    if (bookingstatus.equals("booked")) {
                        status = "Booking accepted";

                    }
                    else if (!(bookingstatus.equals("booked"))) {

                        status = "Booking is pending";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}