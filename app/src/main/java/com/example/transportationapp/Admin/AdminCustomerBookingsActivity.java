package com.example.transportationapp.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.transportationapp.Model.Wish;
import com.example.transportationapp.R;
import com.example.transportationapp.ViewHolder.WishViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminCustomerBookingsActivity extends AppCompatActivity {
    private RecyclerView carsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference wishListRef;

    private String customerID = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_customer_bookings);

        customerID = getIntent().getStringExtra("uid");

        carsList = findViewById(R.id.book_list);
        carsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        carsList.setLayoutManager(layoutManager);

        wishListRef = FirebaseDatabase.getInstance().getReference()
                .child("Wish List").child("Admin View").child(customerID).child("Cars");
    }


    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Wish> options =
                new FirebaseRecyclerOptions.Builder<Wish>()
                        .setQuery(wishListRef, Wish.class)
                        .build();

        FirebaseRecyclerAdapter<Wish, WishViewHolder> adapter = new FirebaseRecyclerAdapter<Wish, WishViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull WishViewHolder holder, int position, @NonNull Wish model) {
                holder.wl_car_name.setText(model.getCarName());
                holder.wl_car_type.setText(model.getCarType());
                holder.wl_sname.setText("Service Provider Name: " + model.getSname());
                holder.wl_sphone.setText("Service Provide Phone: " + model.getSphone());
                holder.wl_sAccNum.setText("Service Provider Account No.: " + model.getAccountNum());
            }

            @NonNull
            @Override
            public WishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wish_cars_layout, parent, false);
                WishViewHolder holder = new WishViewHolder(view);
                return holder;
            }
        };

        carsList.setAdapter(adapter);
        adapter.startListening();
    }
}