package com.example.transportationapp.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.transportationapp.Customers.MainActivity;
import com.example.transportationapp.Model.Cars;
import com.example.transportationapp.R;
import com.example.transportationapp.ViewHolder.CarViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class AdminHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener

{
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unverifiedCarsRef;

    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        unverifiedCarsRef = FirebaseDatabase.getInstance().getReference().child("Cars");


        recyclerView = findViewById(R.id.admin_cars_checklist);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();




        Paper.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        //       setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

//        userNameTextView.setText(Prevalent.currentOnlineUser.getName());
//        Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

//        if (id == R.id.action_settings)
//        {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.manage_car)
        {
            Intent intent = new Intent(AdminHome.this, AdminRecycleViewHome.class);
            startActivity(intent);
        }
        else if (id == R.id.check_new_bookings)
        {
            Intent intent = new Intent(AdminHome.this, AdminManageBookingActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.check_approve_new_car)
        {
            Intent intent = new Intent(AdminHome.this, AdminCheckNewCarsActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.manage_user)
        {
            Intent intent = new Intent(AdminHome.this, AdminManageUser.class);
            startActivity(intent);
        }

        else if (id == R.id.logout)
        {
            Intent intent = new Intent(AdminHome.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminHome.this);
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
                        Toast.makeText(AdminHome.this, "That Car has been approved, and it is now available for booking from the service provider.", Toast.LENGTH_SHORT).show();
                    }
                });
    }



}