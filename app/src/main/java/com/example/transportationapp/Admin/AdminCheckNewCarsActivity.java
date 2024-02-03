package com.example.transportationapp.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.transportationapp.Interface.ItemClickListener;
import com.example.transportationapp.Model.Cars;
import com.example.transportationapp.R;
import com.example.transportationapp.ViewHolder.CarViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminCheckNewCarsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unverifiedCarsRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check_new_cars);

        unverifiedCarsRef = FirebaseDatabase.getInstance().getReference().child("Cars");


        recyclerView = findViewById(R.id.admin_cars_checklist);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Cars> options =
                new FirebaseRecyclerOptions.Builder<Cars>()
                .setQuery(unverifiedCarsRef.orderByChild("carStatus").equalTo("Not Approved"), Cars.class)
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
                        holder.sp_acct_num.setText("Service account No.: " + model.getAccountNum());
                        Picasso.get().load(model.getImage()).into(holder.car_image);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final String carID = model.getCid();

                                CharSequence options[] = new CharSequence[] {
                                        "Yes",
                                        "No"
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminCheckNewCarsActivity.this);
                                builder.setTitle("Do you want to Approve this Car. Are you sure?");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int position) {
                                        if (position == 0) {
                                            ChangeCarStatus(carID);
                                        }
                                        if (position == 1) {

                                        }
                                    }
                                });
                                builder.show();
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

    private void ChangeCarStatus(String carID) {
        unverifiedCarsRef.child(carID)
                .child("carStatus")
                .setValue("Approved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AdminCheckNewCarsActivity.this, "That Car has been approved, and it is now available for booking from the service provider.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}