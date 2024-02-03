package com.example.transportationapp.ServiceProviders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.transportationapp.Admin.AdminCustomerBookingsActivity;
import com.example.transportationapp.Model.SPBookings;
import com.example.transportationapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SPManageBooking extends AppCompatActivity {

    private RecyclerView bookingList;
    private DatabaseReference spView, adminView, customerView;
    private String customerID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_manage_booking);
        customerID = getIntent().getStringExtra("matricId");
        spView = FirebaseDatabase.getInstance().getReference().child("Wish List").child("Booking Info");
        adminView = FirebaseDatabase.getInstance().getReference().child("Wish List").child("Admin View");
        customerView = FirebaseDatabase.getInstance().getReference().child("Bookings");
//        spView = FirebaseDatabase.getInstance().getReference().child("Bookings");


        bookingList = findViewById(R.id.bookings_list);
        bookingList.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<SPBookings> options =
                new FirebaseRecyclerOptions.Builder<SPBookings>()
                        .setQuery(spView.orderByChild("sid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()), SPBookings.class)
//                        .setQuery(spView.orderByChild("status").equalTo("not booked"), SPBookings.class)
                        .build();

        FirebaseRecyclerAdapter<SPBookings, SPBookingsViewHolder> adapter =
                new FirebaseRecyclerAdapter<SPBookings, SPBookingsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull SPBookingsViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull SPBookings model) {
                        holder.bName.setText("Name: " + model.getBname());
                        holder.bDestination.setText("Destination: " + model.getBdestination());
                        holder.bDuration.setText("Duration: " + model.getBduration());
                        holder.bPassenger.setText("Passenger: " + model.getBpassenger());
                        holder.bPayment.setText("Payment: " + model.getBpayment());
                        holder.bPaymentMethod.setText("Payment Method: " + model.getPaymentMethod());
                        holder.bPhone.setText("Phone: " + model.getBphone());
                        holder.bTime.setText("Time: " + model.getBtime());
                        holder.bDate.setText("Date: " + model.getBdate());
                        holder.status.setText("Status: " + model.getStatus());
                        Picasso.get().load(model.getIc()).into(holder.icImageView);
                        Picasso.get().load(model.getLicense()).into(holder.licenseImageView);
                        String uID = getRef(position).getKey();
                        holder.bMatricId.setText("Matric ID: " + uID);

                        holder.show_all_bookings_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                Intent intent = new Intent(SPManageBooking.this, AdminCustomerBookingsActivity.class);
                                intent.putExtra("uid",uID);
                                startActivity(intent);
                            }
                        });



                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                final String uID = getRef(position).getKey();

                                CharSequence options[] = new CharSequence[] {
                                        "Accept Booking",
                                        "Reject Booking",
                                        "Booking Completed"
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(SPManageBooking.this);
                                builder.setTitle("Are you sure with this booking?");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (i == 0) {
                                            String uID = getRef(position).getKey();

//                                            RemoveBooking(uID);
                                            ChangeStatusYes(uID);
                                        }
                                        else if (i == 1)
                                        {
                                            String uID = getRef(position).getKey();

//                                            RemoveBooking(uID);
                                            ChangeStatusNo(uID);
                                        }
                                        else if (i == 2) {
                                            String uID = getRef(position).getKey();

//                                            RemoveBooking(uID);
                                            ChangeStatusCompleted(uID);
                                        }
                                    }
                                });

                                builder.show();
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public SPBookingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sp_booking_layout, parent, false);
                        return new SPBookingsViewHolder(view);
                    }
                };

        bookingList.setAdapter(adapter);
        adapter.startListening();
    }

    private void ChangeStatusCompleted(String uID) {
        spView.child(uID)
                .child("status")
                .setValue("Booking Completed");
        spView.child(uID).removeValue();
        adminView.child(uID).removeValue();
    }

    private void ChangeStatusNo(String uID) {

        spView.child(uID)
                .child("status")
                .setValue("Booking Rejected");

        customerView.child(uID).removeValue();
    }

    private void ChangeStatusYes(String uID) {

        spView.child(uID)
                .child("status")
                .setValue("Booking Accepted");
        customerView.child(uID).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        if (task.isSuccessful()) {

                            Toast.makeText(SPManageBooking.this, "That booking has been approved.", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }


    public static class SPBookingsViewHolder extends RecyclerView.ViewHolder {

        public TextView bName, bMatricId, bDestination, bDuration, bPassenger, bPayment, bPaymentMethod, bPhone, bTime, bDate, status;
        public ImageView icImageView, licenseImageView;
        private Button show_all_bookings_btn;


        public SPBookingsViewHolder(@NonNull View itemView) {
            super(itemView);

            bName = itemView.findViewById(R.id.bName);
            bMatricId = itemView.findViewById(R.id.bMatricId);
            bDestination = itemView.findViewById(R.id.bDestination);
            bDuration = itemView.findViewById(R.id.bDuration);
            bPassenger = itemView.findViewById(R.id.bPassenger);
            bPayment = itemView.findViewById(R.id.bPayment);
            bPaymentMethod = itemView.findViewById(R.id.bPaymentMethod);
            bPhone = itemView.findViewById(R.id.bPhone);
            bTime = itemView.findViewById(R.id.bTime);
            bDate = itemView.findViewById(R.id.bDate);
            status = itemView.findViewById(R.id.status);
            icImageView = itemView.findViewById(R.id.icImageView);
            licenseImageView = itemView.findViewById(R.id.licenseImageView);
            show_all_bookings_btn = itemView.findViewById(R.id.show_all_bookings_btn);


        }
    }

//    private void RemoveBooking(String uID) {
//        spView.child(uID).removeValue();
//
////        spView.child("Admin View").child(uID).child("Cars").removeValue();
//    }
}