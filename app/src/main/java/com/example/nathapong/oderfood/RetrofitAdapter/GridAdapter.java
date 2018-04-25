package com.example.nathapong.oderfood.RetrofitAdapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.nathapong.oderfood.GlideApp;
import com.example.nathapong.oderfood.JsonModel.FoodItem;
import com.example.nathapong.oderfood.R;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.SingleItemGridHolder>{

    private Context mContext;
    private ArrayList<FoodItem> itemsList;

    public GridAdapter(Context mContext, ArrayList<FoodItem> itemsList) {
        this.mContext = mContext;
        this.itemsList = itemsList;
    }

    @Override
    public SingleItemGridHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_single_card, null);
        SingleItemGridHolder mh = new SingleItemGridHolder(view);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemGridHolder holder, int position) {

        FoodItem singleItem = itemsList.get(position);

        holder.tvTitle.setText(singleItem.getName());
        holder.tvDescription.setText(singleItem.getShotDetail());

        GlideApp
                .with(mContext)
                .load(singleItem.getImage())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .error(R.drawable.no_image_available)
                .into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }


    public class SingleItemGridHolder extends RecyclerView.ViewHolder {

        protected TextView tvTitle;
        protected TextView tvDescription;
        protected ImageView itemImage;


        public SingleItemGridHolder(View view) {
            super(view);

            this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            this.tvDescription = (TextView) view.findViewById(R.id.tvDescription);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(v.getContext(), tvTitle.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
