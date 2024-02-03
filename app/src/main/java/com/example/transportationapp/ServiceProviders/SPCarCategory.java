package com.example.transportationapp.ServiceProviders;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.transportationapp.R;

public class SPCarCategory extends AppCompatActivity {

    private ImageView rental;
    private ImageView cab;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_car_category);


        rental = (ImageView) findViewById(R.id.rental);
        cab = (ImageView) findViewById(R.id.cab);


        rental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SPCarCategory.this, SPAddNewCar.class);
                intent.putExtra("carType", "Rental");
                startActivity(intent);
            }
        });

        cab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SPCarCategory.this, SPAddNewCar.class);
                intent.putExtra("carType", "Cab");
                startActivity(intent);
            }
        });
    }
}