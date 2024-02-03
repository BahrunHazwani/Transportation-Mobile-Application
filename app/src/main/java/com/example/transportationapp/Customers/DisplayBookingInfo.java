package com.example.transportationapp.Customers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.transportationapp.Prevalent.Prevalent;
import com.example.transportationapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class DisplayBookingInfo extends AppCompatActivity {

    private TextView spName, spPhone, dDestination, dDuration, dPassenger, dPayment, dPhone, dTime, dDate, dstatus, dcarname, dcartype, dPaymentMethod;
    private Button back_to_home_btn, cancel_btn;
    private RelativeLayout r1;
    private DatabaseReference spView,adminView,customerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_booking_info);

        spName = (TextView) findViewById(R.id.spName);
        spPhone = (TextView) findViewById(R.id.spPhone);
        dDestination = (TextView) findViewById(R.id.dDestination);
        dDuration = (TextView) findViewById(R.id.dDuration);
        dPassenger = (TextView) findViewById(R.id.dPassenger);
        dPayment = (TextView) findViewById(R.id.dPayment);
        dPaymentMethod = (TextView) findViewById(R.id.dPaymentMethod);
        dPhone = (TextView) findViewById(R.id.dPhone);
        dTime = (TextView) findViewById(R.id.dTime);
        dDate = (TextView) findViewById(R.id.dDate);
        dstatus = (TextView) findViewById(R.id.dstatus);
        dcarname = (TextView) findViewById(R.id.dcarname);
        dcartype = (TextView) findViewById(R.id.dcartype);
        back_to_home_btn = (Button) findViewById(R.id.back_to_home_btn);
        cancel_btn = (Button) findViewById(R.id.cancel_btn);
        r1 = (RelativeLayout) findViewById(R.id.r1);

        spView = FirebaseDatabase.getInstance().getReference().child("Wish List").child("Booking Info");

        customerView = FirebaseDatabase.getInstance().getReference().child("Bookings");




        DatabaseReference secure = FirebaseDatabase.getInstance().getReference().child("Wish List").child("Booking Info").child(Prevalent.currentOnlineUser.getMatricId()).child("status");
        secure.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DatabaseReference infoRef2 = FirebaseDatabase.getInstance().getReference().child("Wish List").child("Booking Info").child(Prevalent.currentOnlineUser.getMatricId());
                    infoRef2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                r1.setVisibility(View.VISIBLE);
                                back_to_home_btn.setVisibility(View.VISIBLE);
                                cancel_btn.setVisibility(View.VISIBLE);

                                back_to_home_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(DisplayBookingInfo.this, HomeActivity.class);
                                        startActivity(intent);
                                    }
                                });

                                cancel_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        spView.child(Prevalent.currentOnlineUser.getMatricId())
                                                .child("status")
                                                .setValue("Booking Canceled by Customer");

                                        customerView.child(Prevalent.currentOnlineUser.getMatricId()).removeValue();

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        bookingInfoDisplay(spName, spPhone, dDestination, dDuration, dPassenger, dPayment, dPaymentMethod, dPhone, dTime, dDate, dstatus, dcarname, dcartype);

    }

    private void bookingInfoDisplay( final TextView spName, final TextView spPhone, final TextView dDestination, final TextView dDuration, final TextView dPassenger, final TextView dPayment, final TextView dPaymentMethod, final TextView dPhone, final TextView dTime, final TextView dDate, final TextView dstatus, final TextView dcarname, final TextView dcartype) {

        DatabaseReference infoRef = FirebaseDatabase.getInstance().getReference().child("Wish List").child("Booking Info").child(Prevalent.currentOnlineUser.getMatricId());

        infoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                if (snapshot.child("sname").exists()) {
                    String SPName = "SP Name: "+ snapshot.child("sname").getValue().toString();
                    spName.setText(SPName);
                }
                if (snapshot.child("sphone").exists()) {
                    String SPPhone = "SP Phone: "+ snapshot.child("sphone").getValue().toString();
                    spPhone.setText(SPPhone);
                }
                if (snapshot.child("bpayment").exists()) {
                    String Dpayment = "Payment: "+ snapshot.child("bpayment").getValue().toString();
                    dPayment.setText(Dpayment);
                }
                if (snapshot.child("paymentMethod").exists()) {
                    String DpaymentMethod = "Payment Method: "+ snapshot.child("paymentMethod").getValue().toString();
                    dPaymentMethod.setText(DpaymentMethod);
                }
                if (snapshot.child("bphone").exists()) {
                    String Dphone = "Phone: "+ snapshot.child("bphone").getValue().toString();
                    dPhone.setText(Dphone);
                }
                if (snapshot.child("btime").exists()) {
                    String Dtime = "Time: "+ snapshot.child("btime").getValue().toString();
                    dTime.setText(Dtime);
                }

                if (snapshot.child("bdate").exists()) {
                    String Ddate = "Date: "+ snapshot.child("bdate").getValue().toString();
                    dDate.setText(Ddate);
                }
                if (snapshot.child("status").exists()) {
                    String Dstatus = "Status: "+ snapshot.child("status").getValue().toString();
                    dstatus.setText(Dstatus);
                }

                if (snapshot.child("carName").exists()) {
                    String DcarName = snapshot.child("carName").getValue().toString();
                    dcarname.setText(DcarName);
                }
                if (snapshot.child("carType").exists()) {
                    String DcarType = snapshot.child("carType").getValue().toString();
                    dcartype.setText(DcarType);
                }
                if (snapshot.child("bpassenger").exists()) {
                    String Dpassenger = "No. of Passenger: " + snapshot.child("bpassenger").getValue().toString();
                    dPassenger.setText(Dpassenger);
                }
                if (snapshot.child("bdestination").exists()) {
                    String Ddestination = "Destination: "+ snapshot.child("bdestination").getValue().toString();
                    dDestination.setText(Ddestination);
                }
                if (snapshot.child("bduration").exists()) {
                    String Dduration = "Duration: "+ snapshot.child("bduration").getValue().toString();
                    dDuration.setText(Dduration);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

