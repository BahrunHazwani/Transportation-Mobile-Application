package com.example.transportationapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transportationapp.Interface.ItemClickListener;
import com.example.transportationapp.R;



public class SPCarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView car_name, car_description, car_status, car_type;
    public ImageView car_image;
    public ItemClickListener listener;

    public SPCarViewHolder(@NonNull View itemView) {
        super(itemView);


        car_image = (ImageView) itemView.findViewById(R.id.sp_car_image);
        car_name = (TextView) itemView.findViewById(R.id.sp_car_name);
        car_description = (TextView) itemView.findViewById(R.id.sp_car_description);
        car_status = (TextView) itemView.findViewById(R.id.sp_car_status);
        car_type = (TextView) itemView.findViewById(R.id.car_type);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);

    }
}
