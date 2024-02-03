package com.example.transportationapp.ServiceProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.transportationapp.ServiceProviders.SPManageCar;
import com.example.transportationapp.Admin.AdminRecycleViewHome;
import com.example.transportationapp.Customers.MainActivity;
import com.example.transportationapp.Model.Cars;
import com.example.transportationapp.R;
import com.example.transportationapp.ViewHolder.SPCarViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import androidx.annotation.NonNull;

//import android.support.v4.app.FragmentTransaction;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.widget.Toast;

public class SPHomeActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unverifiedCarsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_home);

//        toolbar = getSupportActionBar();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        unverifiedCarsRef = FirebaseDatabase.getInstance().getReference().child("Cars");


        recyclerView = findViewById(R.id.sp_home_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



//        toolbar.setTitle("Home");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cars> options =
                new FirebaseRecyclerOptions.Builder<Cars>()
                        .setQuery(unverifiedCarsRef.orderByChild("sid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()), Cars.class)
                        .build();

        FirebaseRecyclerAdapter<Cars, SPCarViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cars, SPCarViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull SPCarViewHolder holder, int position, @NonNull Cars model) {
                        holder.car_name.setText(model.getCarName());
                        holder.car_description.setText("Description: " + "\n" + "\n" + model.getDescription());
                        holder.car_status.setText("Status: " + model.getCarStatus());
                        holder.car_type.setText("Car Type: " + model.getCarType());
                        Picasso.get().load(model.getImage()).into(holder.car_image);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final String carID = model.getCid();

                                CharSequence options[] = new CharSequence[] {
                                        "Delete Car",
                                        "Edit Car",
                                        "No"
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(SPHomeActivity.this);
                                builder.setTitle("Manage Car?");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int position) {
                                        if (position == 0) {
                                            deleteCar(carID);
                                        }
                                        if (position == 1) {
                                            Intent intent = new Intent(SPHomeActivity.this, SPManageCar.class);
                                            intent.putExtra("cid",model.getCid());
                                            startActivity(intent);
                                        }
                                        if (position == 2) {

                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public SPCarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sp_cars_view, parent, false);
                        SPCarViewHolder holder = new SPCarViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    private void deleteCar(String carID) {
        unverifiedCarsRef.child(carID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SPHomeActivity.this, "That Car has been deleted successfully.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intentHome = new Intent(SPHomeActivity.this, SPHomeActivity.class);
                    startActivity(intentHome);

                    return true;


                case R.id.navigation_add:
                    Intent intentCate = new Intent(SPHomeActivity.this, SPCarCategory.class);
                    startActivity(intentCate);

                    return true;

                case R.id.navigation_manageBooking:
                    Intent intentBook = new Intent(SPHomeActivity.this, SPManageBooking.class);
                    startActivity(intentBook);

                    return true;

                case R.id.navigation_profile:
                    Intent intentProfile = new Intent(SPHomeActivity.this, SPProfile.class);
                    startActivity(intentProfile);

                    return true;


                case R.id.navigation_logout:
                    final FirebaseAuth mAuth;
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();

                    Intent intentMain = new Intent(SPHomeActivity.this, MainActivity.class);
                    intentMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentMain);
                    finish();

                    return true;


            }
            return false;
        }
    };


}