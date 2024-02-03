package com.example.transportationapp.Admin;

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


import com.example.transportationapp.Model.ServiceProvider;
import com.example.transportationapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminManageSP extends AppCompatActivity {

    private RecyclerView sp_list;
    private DatabaseReference manageUserRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_sp);

        manageUserRef = FirebaseDatabase.getInstance().getReference().child("ServiceProvider");


        sp_list = findViewById(R.id.sp_lists);
        sp_list.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<ServiceProvider> options =
                new FirebaseRecyclerOptions.Builder<ServiceProvider>()
                        .setQuery(manageUserRef, ServiceProvider.class)
                        .build();

        FirebaseRecyclerAdapter<ServiceProvider, SPViewHolder> adapter =
                new FirebaseRecyclerAdapter<ServiceProvider, SPViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull SPViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull ServiceProvider model) {
                        holder.Mname.setText("Name: " + model.getSname());
                        holder.Memail.setText("Email: " + model.getSemail());
                        holder.MmatricID.setText("Matric ID: " + model.getSmatricID());
                        holder.Mphone.setText("Phone: " + model.getSphone());
                        Picasso.get().load(model.getImage()).into(holder.MprofilePic);
                        holder.MaccountNum.setText("Bank Name and Account Number: " + model.getAccountNum());
                        String uID = getRef(position).getKey();


                        holder.MremoveBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                manageUserRef.child(uID).removeValue();
                                Toast.makeText(AdminManageSP.this, "This Service Provider has been removed successfully.", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }

                    @NonNull
                    @Override
                    public SPViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_sp_layout, parent, false);
                        return new AdminManageSP.SPViewHolder(view);
                    }
                };

        sp_list.setAdapter(adapter);
        adapter.startListening();
    }



    public static class SPViewHolder extends RecyclerView.ViewHolder {

        public TextView Mname, MmatricID, Memail, Mphone, MaccountNum;
        public Button MremoveBtn;
        public ImageView MprofilePic;


        public SPViewHolder(@NonNull View itemView) {
            super(itemView);

            Mname = itemView.findViewById(R.id.Mname);
            MmatricID = itemView.findViewById(R.id.MmatricID);
            Memail = itemView.findViewById(R.id.Memail);
            Mphone = itemView.findViewById(R.id.Mphone);
            MremoveBtn = itemView.findViewById(R.id.MremoveBtn);
            MprofilePic = itemView.findViewById(R.id.MprofilePic);
            MaccountNum = itemView.findViewById(R.id.MaccountNum);



        }
    }


}