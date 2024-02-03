package com.example.transportationapp.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.transportationapp.Model.Users;
import com.example.transportationapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminManageCustomer extends AppCompatActivity {

    private RecyclerView customers_list;
    private DatabaseReference manageUserRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_customer);

        manageUserRef = FirebaseDatabase.getInstance().getReference().child("Users");


        customers_list = findViewById(R.id.cust_lists);
        customers_list.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(manageUserRef, Users.class)
                        .build();

        FirebaseRecyclerAdapter<Users, UsersViewHolder> adapter =
                new FirebaseRecyclerAdapter<Users, UsersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull UsersViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull Users model) {

                        holder.Mname.setText("Name: " + model.getName());
                        holder.Memail.setText("Email: " + model.getEmail());
                        holder.MmatricID.setText("Matric ID: " + model.getMatricId());
                        holder.Mphone.setText("Phone: " + model.getPhone());
                        Picasso.get().load(model.getImage()).into(holder.MprofilePic);
                        String uID = getRef(position).getKey();
                        holder.MmatricID.setText("Matric ID: " + uID);



                        holder.MremoveBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                manageUserRef.child(uID).removeValue();
                                Toast.makeText(AdminManageCustomer.this, "This Customer has been removed successfully.", Toast.LENGTH_SHORT).show();
                            }
                        });



                    }

                    @NonNull
                    @Override
                    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_customer_layout, parent, false);
                        return new UsersViewHolder(view);
                    }
                };

        customers_list.setAdapter(adapter);
        adapter.startListening();
    }



    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        public TextView Mname, MmatricID, Memail, Mphone;
        public Button MremoveBtn;
        public ImageView MprofilePic;


        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);

            Mname = itemView.findViewById(R.id.Mname);
            MmatricID = itemView.findViewById(R.id.MmatricID);
            Memail = itemView.findViewById(R.id.Memail);
            Mphone = itemView.findViewById(R.id.Mphone);
            MremoveBtn = itemView.findViewById(R.id.MremoveBtn);
            MprofilePic = itemView.findViewById(R.id.MprofilePic);


        }
    }


}