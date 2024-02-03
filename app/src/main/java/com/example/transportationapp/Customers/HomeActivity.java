package com.example.transportationapp.Customers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transportationapp.Admin.AdminManageCar;
import com.example.transportationapp.Model.Cars;
import com.example.transportationapp.Prevalent.Prevalent;
import com.example.transportationapp.R;
import com.example.transportationapp.ViewHolder.CarViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private DatabaseReference CarsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private String type = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            type = getIntent().getExtras().get("Admin").toString();
        }

        CarsRef = FirebaseDatabase.getInstance().getReference().child("Cars");

        Paper.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
 //       setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!type.equals("Admin")) {
                    Intent intent = new Intent(HomeActivity.this, WishListActivity.class);
                    startActivity(intent);
                }

            }
        });

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

        if (!type.equals("Admin")) {
            userNameTextView.setText(Prevalent.currentOnlineUser.getName());
            Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);
        }

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
                .setQuery(CarsRef.orderByChild("carStatus").equalTo("Approved"), Cars.class)
                .build();

        FirebaseRecyclerAdapter<Cars, CarViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cars, CarViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CarViewHolder holder, int position, @NonNull Cars model) {
                        holder.car_name.setText(model.getCarName());
//                        holder.car_description.setText(model.getDescription());
                        holder.car_type.setText("Car Type: " + model.getCarType());
                        Picasso.get().load(model.getImage()).into(holder.car_image);


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (type.equals("Admin")) {
                                    Intent intent = new Intent(HomeActivity.this, AdminManageCar.class);
                                    intent.putExtra("cid",model.getCid());
                                    startActivity(intent);
                                }
                                else {

                                    Intent intent = new Intent(HomeActivity.this, CarDetailsActivity.class);
                                    intent.putExtra("cid",model.getCid());
                                    startActivity(intent);
                                }



                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_home_cars_layout, parent, false);
                        CarViewHolder holder = new CarViewHolder(view);
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    @SuppressWarnings("StatementWithEmptyBody")


    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.wish_list)
        {
            if (!type.equals("Admin")) {
                Intent intent = new Intent(HomeActivity.this, WishListActivity.class);
                startActivity(intent);
            }
        }
        else if (id == R.id.nav_search)
        {
            if (!type.equals("Admin")) {
                Intent intent = new Intent(HomeActivity.this, SearchCarActivity.class);
                startActivity(intent);
            }

        }
        else if (id == R.id.nav_displayInfo)
        {
            Intent intent = new Intent(HomeActivity.this, DisplayBookingInfo.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_settings)
        {
            if (!type.equals("Admin")) {
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
            }

        }

        else if (id == R.id.nav_logout)
        {
            if (!type.equals("Admin")) {
                Paper.book().destroy();

                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

