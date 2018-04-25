package com.example.nathapong.oderfood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.nathapong.oderfood.ItemDecoration.VerticalSpaceItemDecoration;
import com.example.nathapong.oderfood.JsonModel.Category;
import com.example.nathapong.oderfood.JsonModel.FoodItem;
import com.example.nathapong.oderfood.RetrofitAdapter.GridAdapter;
import com.example.nathapong.oderfood.RetrofitAdapter.HorizontalAdapter;
import com.example.nathapong.oderfood.RetrofitAdapter.RecyclerViewDataAdapter;

import java.util.ArrayList;

public class RetrofitGridLayoutActivity extends AppCompatActivity {

    private ArrayList<FoodItem> itemsList;

    RecyclerView grid_recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_grid_layout);

        grid_recyclerView = (RecyclerView)findViewById(R.id.grid_recyclerView);

        Bundle bundle = getIntent().getExtras();
        itemsList = bundle.getParcelableArrayList("ITEMS");

        //Log.d("ITEMS",itemsList.get(0).getName());


        grid_recyclerView.setHasFixedSize(true);

        GridAdapter adapter = new GridAdapter(this, itemsList);

        grid_recyclerView.setLayoutManager(new GridLayoutManager(this,4));

        //grid_recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(50,20,20,50));

        grid_recyclerView.setAdapter(adapter);

    }
}
