package com.example.transportationapp.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.transportationapp.R;

public class AdminManageUser extends AppCompatActivity {

    private ImageView customer;
    private ImageView sp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_user);


        customer = (ImageView) findViewById(R.id.customer);
        sp = (ImageView) findViewById(R.id.sp);


        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminManageUser.this, AdminManageCustomer.class);
                startActivity(intent);
            }
        });

        sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminManageUser.this, AdminManageSP.class);
                startActivity(intent);
            }
        });
    }
}