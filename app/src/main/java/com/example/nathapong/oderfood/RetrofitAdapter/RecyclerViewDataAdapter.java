package com.example.nathapong.oderfood.RetrofitAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nathapong.oderfood.ItemDecoration.VerticalSpaceItemDecoration;
import com.example.nathapong.oderfood.JsonModel.Category;
import com.example.nathapong.oderfood.JsonModel.FoodItem;
import com.example.nathapong.oderfood.R;
import com.example.nathapong.oderfood.RetrofitActivity;
import com.example.nathapong.oderfood.RetrofitGridLayoutActivity;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;

public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerViewDataAdapter.ItemRowHolder>{

    private Context mContext;
    private ArrayList<Category> dataList;


    public RecyclerViewDataAdapter(Context mContext, ArrayList<Category> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, null);
        ItemRowHolder mh = new ItemRowHolder(v);

        mh.recycler_view_list.setOnFlingListener(null);
        SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);
        snapHelperStart.attachToRecyclerView(mh.recycler_view_list);

        return mh;
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder itemRowHolder, final int i) {

        final String categoryName = dataList.get(i).getCategoryName();

        final ArrayList itemHorizontal = dataList.get(i).getItem();

            itemRowHolder.itemTitle.setText(categoryName);

            HorizontalAdapter itemListDataAdapter = new HorizontalAdapter(mContext, itemHorizontal);

            itemRowHolder.recycler_view_list.setHasFixedSize(true);
            itemRowHolder.recycler_view_list.setLayoutManager
                    (new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            //itemRowHolder.recycler_view_list.addItemDecoration(new VerticalSpaceItemDecoration(0,0,5,0));
            itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);

            itemRowHolder.btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent gridIntent = new Intent(mContext, RetrofitGridLayoutActivity.class);
                    gridIntent.putExtra("ITEMS", itemHorizontal);
                    mContext.startActivity(gridIntent);
                }
            });

        /*Glide.with(mContext).load(feedItem.getImageURL()).diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop().error(R.drawable.bg).into(feedListRowHolder.thumbView);*/

    }


    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView itemTitle;
        protected RecyclerView recycler_view_list;
        protected Button btnMore;

        public ItemRowHolder(View view) {
            super(view);

            this.itemTitle = (TextView) view.findViewById(R.id.itemTitle);
            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.recycler_view_list);
            this.btnMore= (Button) view.findViewById(R.id.btnMore);
        }
    }
}

