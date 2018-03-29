package com.example.nathapong.oderfood.ViewHolder;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.nathapong.oderfood.Interface.ItemClickListener;
import com.example.nathapong.oderfood.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtOrderId, txtOrderStatus, txtOrderPhone, txtOrderAddress, txtOrderTotal, txtOrderDate, txtOrderPayment;

    // Interface
    private ItemClickListener itemClickListener;


    public OrderViewHolder(View itemView) {
        super(itemView);

        txtOrderId = (TextView)itemView.findViewById(R.id.order_id);
        txtOrderStatus = (TextView)itemView.findViewById(R.id.order_status);
        txtOrderPhone = (TextView)itemView.findViewById(R.id.order_phone);
        txtOrderTotal = (TextView)itemView.findViewById(R.id.order_total);
        txtOrderDate = (TextView)itemView.findViewById(R.id.order_date);
        txtOrderAddress = (TextView)itemView.findViewById(R.id.order_address);
        txtOrderPayment = (TextView)itemView.findViewById(R.id.order_payment);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v, getAdapterPosition(),false);
    }
}
