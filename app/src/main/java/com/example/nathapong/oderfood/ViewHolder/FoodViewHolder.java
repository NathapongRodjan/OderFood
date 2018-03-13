package com.example.nathapong.oderfood.ViewHolder;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nathapong.oderfood.Interface.ItemClickListener;
import com.example.nathapong.oderfood.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView txtFoodName;
    public ImageView imgFood;

    // Interface
    private ItemClickListener itemClickListener;


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }



    public FoodViewHolder(View itemView) {
        super(itemView);


        txtFoodName = (TextView)itemView.findViewById(R.id.food_name);
        imgFood = (ImageView)itemView.findViewById(R.id.food_image);

        itemView.setOnClickListener(this);
    }




    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v, getAdapterPosition(),false);
    }
}
