package com.example.nathapong.oderfood.ViewHolder;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.nathapong.oderfood.Common.Common;
import com.example.nathapong.oderfood.Interface.ItemClickListener;
import com.example.nathapong.oderfood.Model.Order;
import com.example.nathapong.oderfood.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class CartViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnCreateContextMenuListener {

    public TextView txtCartName, txtPrice;
    public ImageView imgCartCount;

    private ItemClickListener itemClickListener;

    public void setTxtCartName(TextView txtCartName) {
        this.txtCartName = txtCartName;
    }





    public CartViewHolder(View itemView) {
        super(itemView);

        txtCartName = (TextView)itemView.findViewById(R.id.cart_item_name);
        txtPrice = (TextView)itemView.findViewById(R.id.cart_item_price);
        imgCartCount = (ImageView) itemView.findViewById(R.id.cart_item_count);

        itemView.setOnCreateContextMenuListener(this);
    }



    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.setHeaderTitle("เลือกการทำงาน");
        menu.add(0,0,getAdapterPosition(), Common.DELETE);
    }
}


public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{


    private List<Order> listData = new ArrayList<>();
    private Context context;

    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_layout, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(""+listData.get(position).getQuantity(), Color.RED);
        holder.imgCartCount.setImageDrawable(drawable);

        Locale locale = new Locale("th", "TH");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(listData.get(position).getPrice())) * (Integer.parseInt(listData.get(position).getQuantity()));
        holder.txtPrice.setText(fmt.format(price));
        holder.txtCartName.setText(listData.get(position).getProductName());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
