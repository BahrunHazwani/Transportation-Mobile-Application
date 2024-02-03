package com.example.transportationapp.Customers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.transportationapp.Model.Wish;
import com.example.transportationapp.Prevalent.Prevalent;
import com.example.transportationapp.R;
import com.example.transportationapp.ViewHolder.WishViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WishListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private Button next_process_btn;
    private TextView  msg1, text1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);


        recyclerView = findViewById(R.id.wish_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        next_process_btn = (Button) findViewById(R.id.next_process_btn);
        msg1 = (TextView) findViewById(R.id.msg1);
        text1 = (TextView) findViewById(R.id.text1);

        next_process_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(WishListActivity.this, ConfirmFinalBookingActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        CheckBookingStatus();

        final DatabaseReference wishListRef = FirebaseDatabase.getInstance().getReference().child("Wish List");
        FirebaseRecyclerOptions<Wish> options =
                new FirebaseRecyclerOptions.Builder<Wish>()
                        .setQuery(wishListRef.child("User View")
                        .child(Prevalent.currentOnlineUser.getMatricId()).child("Cars"), Wish.class)
                        .build();

        FirebaseRecyclerAdapter<Wish, WishViewHolder> adapter
                = new FirebaseRecyclerAdapter<Wish, WishViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull WishViewHolder holder, int position, @NonNull Wish model) {
                holder.wl_car_name.setText(model.getCarName());
                holder.wl_car_type.setText(model.getCarType());
                holder.wl_sname.setText("Service Provider Name: " + model.getSname());
                holder.wl_sphone.setText("Service Provide Phone: " + model.getSphone());
                holder.wl_sAccNum.setText("Service Provider Account No.: " + model.getAccountNum());


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[] {
                          "View Car", "Remove"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(WishListActivity.this);
                        builder.setTitle("Wish List Options:");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    Intent intent = new Intent(WishListActivity.this, CarDetailsActivity.class);
                                    intent.putExtra("cid", model.getCid());
                                    startActivity(intent);
                                }
                                if (i == 1){
                                    wishListRef.child("User View")
                                            .child(Prevalent.currentOnlineUser.getMatricId())
//                                            .child("Cars")
//                                            .child(model.getCid())
                                            .removeValue();
                                    wishListRef.child("Admin View")
                                            .child(Prevalent.currentOnlineUser.getMatricId())
//                                            .child("Cars")
//                                            .child(model.getCid())
                                            .removeValue();
                                    wishListRef.child("Booking Info")
                                            .child(Prevalent.currentOnlineUser.getMatricId())
//                                            .child("Cars")
//                                            .child(model.getCid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(WishListActivity.this, "Car removed succesfully.", Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(WishListActivity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                    }

                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public WishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wish_cars_layout, parent, false);
                WishViewHolder holder = new WishViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void CheckBookingStatus() {
        DatabaseReference bookingsRef;
        bookingsRef = FirebaseDatabase.getInstance().getReference().child("Wish List").child("Booking Info").child(Prevalent.currentOnlineUser.getMatricId()).child("status");

        bookingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String bookingStatus = snapshot.getValue().toString();

                    if (bookingStatus.equals("Pending") || bookingStatus.equals("Booking Accepted") ||  bookingStatus.equals("Booking Completed")) {

                        recyclerView.setVisibility(View.GONE);

                        msg1.setVisibility(View.VISIBLE);
                        msg1.setText("Congratulations, your final booking has been done successfully.");
                        next_process_btn.setVisibility(View.GONE);
                        text1.setVisibility(View.GONE);

//                        Toast.makeText(WishListActivity.this, "You can book more transport, once you complete your first final booking", Toast.LENGTH_SHORT).show();
                    }
                    else if (bookingStatus.equals("Booking Rejected") ) {
                        recyclerView.setVisibility(View.GONE);

                        msg1.setVisibility(View.VISIBLE);
                        msg1.setText("Sorry, your booking has been rejected by service provider. Please wait till admin remove your previous booking detail");
                        next_process_btn.setVisibility(View.GONE);
                        text1.setVisibility(View.GONE);
                    }
                    else if (!(bookingStatus.equals("booked"))) {

                        recyclerView.setVisibility(View.GONE);

                        msg1.setVisibility(View.VISIBLE);

                        next_process_btn.setVisibility(View.GONE);

                        Toast.makeText(WishListActivity.this, "You can book more transport, once you complete or cancelled your first final booking", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}