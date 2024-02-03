package com.example.transportationapp.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transportationapp.Model.Cars;
import com.example.transportationapp.R;
import com.example.transportationapp.ViewHolder.CarViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class AdminRecycleViewHome extends AppCompatActivity

{
    private DatabaseReference CarsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    private String type = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_recycle_view_home);

        CarsRef = FirebaseDatabase.getInstance().getReference().child("Cars");

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cars> options =
                new FirebaseRecyclerOptions.Builder<Cars>()
                        .setQuery(CarsRef, Cars.class)
                        .build();

        FirebaseRecyclerAdapter<Cars, CarViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cars, CarViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CarViewHolder holder, int position, @NonNull Cars model) {
                        holder.car_name.setText(model.getCarName());
                        holder.car_description.setText(model.getDescription());
                        holder.car_type.setText("Car Type: " + model.getCarType());
                        holder.sp_name.setText("Service Provider Name: " + model.getSname());
                        holder.sp_phone.setText("Service Provider Phone: " + model.getSphone());
                        holder.sp_acct_num.setText("Service Provider Account No.: " + model.getAccountNum());
                        Picasso.get().load(model.getImage()).into(holder.car_image);


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                    Intent intent = new Intent(AdminRecycleViewHome.this, AdminManageCar.class);
                                    intent.putExtra("cid",model.getCid());
                                    startActivity(intent);

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cars_layout, parent, false);
                        CarViewHolder holder = new CarViewHolder(view);
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }


}

