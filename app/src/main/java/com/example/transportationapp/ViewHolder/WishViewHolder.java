package com.example.transportationapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transportationapp.Interface.ItemClickListener;
import com.example.transportationapp.R;

public class WishViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView wl_car_name, wl_car_type, wl_sname, wl_sphone, wl_sAccNum;
    private ImageView wl_car_Image;
    private ItemClickListener itemClickListener;

    public WishViewHolder(@NonNull View itemView) {
        super(itemView);

        wl_car_name = itemView.findViewById(R.id.wl_car_name);
        wl_car_type = itemView.findViewById(R.id.wl_car_type);
        wl_sname = itemView.findViewById(R.id.wl_sname);
        wl_sphone = itemView.findViewById(R.id.wl_sphone);
        wl_sAccNum = itemView.findViewById(R.id.wl_sAccNum);


    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
