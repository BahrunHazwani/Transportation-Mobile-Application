package com.example.transportationapp.Customers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.transportationapp.Prevalent.Prevalent;
import com.example.transportationapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Payment extends AppCompatActivity {

    private ImageView maybank, bankIslam, cimb, ambank, hong_leong;
    private Button payment_method_btn, next_btn;
    private DatabaseReference spRef;
    private TextView txt_info, online_banking;
    private RelativeLayout r1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        spRef = FirebaseDatabase.getInstance().getReference().child("Wish List").child("Booking Info");


        maybank = (ImageView) findViewById(R.id.maybank);
        bankIslam = (ImageView) findViewById(R.id.bankIslam);
        cimb = (ImageView) findViewById(R.id.cimb);
        ambank = (ImageView) findViewById(R.id.ambank);
        hong_leong = (ImageView) findViewById(R.id.hong_leong);
        next_btn = (Button) findViewById(R.id.next_btn);
        txt_info = (TextView) findViewById(R.id.txt_info);
        online_banking = (TextView) findViewById(R.id.online_banking);
        r1 = (RelativeLayout) findViewById(R.id.r1);

        CharSequence options[] = new CharSequence[] {
                "Online Banking",
                "Physical Payment"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(Payment.this);
        builder.setTitle("Your Payment Method?");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    visible();

//                                           RemoveBooking(uID);

                }
                else
                {
                    physicalPayment();
                }
            }
        });

        builder.show();



        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Payment.this, "Your booking are completed successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Payment.this, DisplayBookingInfo.class);
                startActivity(intent);
            }
        });


        maybank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent link = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.maybank2u.com.my/home/m2u/common/login.do?action=Login"));
                startActivity(link);
            }
        });

        bankIslam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent link = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.bankislam.biz/"));
                startActivity(link);
            }
        });

        cimb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent link = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cimbclicks.com.my/clicks/#/"));
                startActivity(link);
            }
        });

        ambank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent link = new Intent(Intent.ACTION_VIEW, Uri.parse("https://ambank.amonline.com.my/web/"));
                startActivity(link);
            }
        });


        hong_leong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent link = new Intent(Intent.ACTION_VIEW, Uri.parse("https://s.hongleongconnect.my/rib/app/fo/login?icp=hlb-en-all-header-txt-connectweb"));
                startActivity(link);
            }
        });

    }

    private void physicalPayment() {
        spRef.child(Prevalent.currentOnlineUser.getMatricId())
                .child("paymentMethod")
                .setValue("Physical Payment");
        Intent intent = new Intent(Payment.this, DisplayBookingInfo.class);
        startActivity(intent);
    }

    private void visible() {
        spRef.child(Prevalent.currentOnlineUser.getMatricId())
                .child("paymentMethod")
                .setValue("Online Banking");

        maybank.setVisibility(View.VISIBLE);
        ambank.setVisibility(View.VISIBLE);
        cimb.setVisibility(View.VISIBLE);
        bankIslam.setVisibility(View.VISIBLE);
        hong_leong.setVisibility(View.VISIBLE);
        next_btn.setVisibility(View.VISIBLE);
        txt_info.setVisibility(View.VISIBLE);
        r1.setVisibility(View.VISIBLE);
        online_banking.setVisibility(View.VISIBLE);

    }

}