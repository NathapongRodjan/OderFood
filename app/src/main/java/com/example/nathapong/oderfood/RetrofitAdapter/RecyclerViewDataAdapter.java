package com.example.nathapong.oderfood.RetrofitAdapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nathapong.oderfood.JsonModel.Category;
import com.example.nathapong.oderfood.R;

import java.util.ArrayList;

public class RecyclerViewDataAdapter
        extends RecyclerView.Adapter<RecyclerViewDataAdapter.ItemRowHolder>{

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
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, int i) {

        final String categoryName = dataList.get(i).getCategoryName();

        ArrayList itemHorizontal = dataList.get(i).getItem();

        itemRowHolder.itemTitle.setText(categoryName);

        HorizontalAdapter itemListDataAdapter = new HorizontalAdapter(mContext, itemHorizontal);

        itemRowHolder.recycler_view_list.setHasFixedSize(true);
        itemRowHolder.recycler_view_list.setLayoutManager
                (new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);

        itemRowHolder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(v.getContext(),
                        "click event on more, "+ categoryName , Toast.LENGTH_SHORT).show();
            }
        });

       /* Glide.with(mContext)
                .load(feedItem.getImageURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.bg)
                .into(feedListRowHolder.thumbView);*/
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

