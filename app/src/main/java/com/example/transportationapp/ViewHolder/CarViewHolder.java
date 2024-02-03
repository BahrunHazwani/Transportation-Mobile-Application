package com.example.transportationapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transportationapp.Interface.ItemClickListener;
import com.example.transportationapp.R;

public class CarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView car_name, car_description, car_type, sp_name, sp_phone, sp_acct_num;
    public ImageView car_image;
    public ItemClickListener listener;

    public CarViewHolder(@NonNull View itemView) {
        super(itemView);


        car_image = (ImageView) itemView.findViewById(R.id.car_image);
        car_name = (TextView) itemView.findViewById(R.id.car_name);
        car_description = (TextView) itemView.findViewById(R.id.car_description);
        car_type = (TextView) itemView.findViewById(R.id.car_type);
        sp_name = (TextView) itemView.findViewById(R.id.sp_name);
        sp_phone = (TextView) itemView.findViewById(R.id.sp_phone);
        sp_acct_num = (TextView) itemView.findViewById(R.id.sp_acct_num);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);

    }
}
