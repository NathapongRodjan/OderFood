package com.example.nathapong.oderfood;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nathapong.oderfood.Common.Common;
import com.example.nathapong.oderfood.Database.Database;
import com.example.nathapong.oderfood.Interface.ItemClickListener;
import com.example.nathapong.oderfood.Model.Food;
import com.example.nathapong.oderfood.Model.Order;
import com.example.nathapong.oderfood.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference Foods;
    String CategoryId = "";

    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    // search function
    FirebaseRecyclerAdapter<Food, FoodViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        database = FirebaseDatabase.getInstance();
        Foods = database.getReference("Food");

        recyclerView = (RecyclerView)findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(FoodList.this);
        recyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // Get Intent here
                if (getIntent() != null)
                    CategoryId = getIntent().getStringExtra("CategoryId");

                if (!CategoryId.isEmpty() && CategoryId != null){

                    if (Common.isConnectedToInternet(getBaseContext())) {

                        loadListFood(CategoryId);
                    }
                    else {
                        Toast.makeText(FoodList.this,"โปรดตรวจสอบการเชื่อมต่ออินเตอร์เน็ต !",Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);   // Disable refresh ring
                        return;
                    }
                }
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                // Get Intent here
                if (getIntent() != null)
                    CategoryId = getIntent().getStringExtra("CategoryId");

                if (!CategoryId.isEmpty() && CategoryId != null){

                    if (Common.isConnectedToInternet(getBaseContext())) {

                        loadListFood(CategoryId);  // Load List of foods
                        loadSuggest(CategoryId);   // Load List of Suggestions in Search Bar
                    }
                    else {
                        Toast.makeText(FoodList.this,"โปรดตรวจสอบการเชื่อมต่ออินเตอร์เน็ต !",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });



        // Search
        materialSearchBar = (MaterialSearchBar)findViewById(R.id.searchBar);
        materialSearchBar.setHint("ใส่ชื่อเมนูที่ท่านต้องการ...");
        //materialSearchBar.setSpeechMode(false);

        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //when user type texts will change suggest list
                List<String> suggest = new ArrayList<>();
                for (String search : suggestList){

                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())){
                        suggest.add(search);
                    }
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                // When Search Bar is close. Restore original adapter.

                if (!enabled){
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                // When search finish. Show result of search adapter

                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearch(CharSequence text) {

        searchAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                Foods.orderByChild("Name").equalTo(text.toString())) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {

                viewHolder.txtFoodName.setText(model.getName());

                Glide
                        .with(FoodList.this)
                        .load(model.getImage())
                        .into(viewHolder.imgFood);

                final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        // Start new Activity
                        Intent foodDetail = new Intent(FoodList.this, FoodDetail.class);
                        foodDetail.putExtra("FoodId", searchAdapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });
            }
        };

        recyclerView.setAdapter(searchAdapter); //Set adapter for Recycler View is search result
    }




    private void loadSuggest(String categoryId) {


        Foods.orderByChild("MenuId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()){

                    Food item = child.getValue(Food.class);
                    suggestList.add(item.getName());  // add name of food to suggest list
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadListFood(String categoryId){


        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>
                (Food.class, R.layout.food_item, FoodViewHolder.class,
                        Foods.orderByChild("MenuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, final Food model, final int position) {

                viewHolder.txtFoodName.setText(model.getName() + " : " + model.getPrice() + " บาท");

                Glide
                        .with(FoodList.this)
                        .load(model.getImage())
                        .into(viewHolder.imgFood);

                viewHolder.btn_quick_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Check current food is not add to cart
                        if (new Database(getBaseContext()).isCurrentFoodExistsInCart
                                (adapter.getRef(position).getKey()) <= 0){

                            new Database(getBaseContext()).addTOCart(new Order(
                                    adapter.getRef(position).getKey(),
                                    model.getName(),
                                    "1",     // Only quick cart
                                    model.getPrice(),
                                    model.getDiscount(),
                                    model.getImage()
                            ));
                        }
                        else {
                            Toast.makeText(FoodList.this, "ท่านเพิ่มสินค้านี้แล้ว กรุณาตรวจสอบในรถเข็น", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


                final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        // Start new Activity
                        Intent foodDetail = new Intent(FoodList.this, FoodDetail.class);
                        foodDetail.putExtra("FoodId", adapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });
            }
        };

        // Set Adapter
        Log.d("TAG", "Number of Item is "+adapter.getItemCount());
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setRefreshing(false);  // Disable refresh ring

    }
}
