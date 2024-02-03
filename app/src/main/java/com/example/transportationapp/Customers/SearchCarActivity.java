package com.example.transportationapp.Customers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.transportationapp.Model.Cars;
import com.example.transportationapp.R;
import com.example.transportationapp.ViewHolder.CarViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class SearchCarActivity extends AppCompatActivity {
    private Button SearchBtn;
    private EditText search_car_name;
    private RecyclerView searchList;
    private String SearchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_car);

        SearchBtn = (Button) findViewById(R.id.SearchBtn);
        search_car_name = (EditText) findViewById(R.id.search_car_name);
        searchList = findViewById(R.id.search_list);
        searchList.setLayoutManager(new LinearLayoutManager(SearchCarActivity.this));

        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchInput = search_car_name.getText().toString();

                onStart();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Cars");


        FirebaseRecyclerOptions<Cars> options =
                new FirebaseRecyclerOptions.Builder<Cars>()
                        .setQuery(reference.orderByChild("carName").startAt(SearchInput), Cars.class)
                        .build();

        FirebaseRecyclerAdapter<Cars, CarViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cars, CarViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CarViewHolder holder, int position, @NonNull Cars model) {
                        holder.car_name.setText(model.getCarName());
                        holder.car_description.setText("Description: " + "\n" + "\n" + model.getDescription());
                        holder.car_type.setText("Car Type: " + model.getCarType());
                        Picasso.get().load(model.getImage()).into(holder.car_image);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(SearchCarActivity.this, CarDetailsActivity.class);
                                intent.putExtra("cid",model.getCid());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_layout, parent, false);
                        CarViewHolder holder = new CarViewHolder(view);
                        return holder;
                    }
                };

        searchList.setAdapter(adapter);
        adapter.startListening();
    }
}