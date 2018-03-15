package com.example.nathapong.oderfood;

import android.content.Intent;
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
import com.example.nathapong.oderfood.Interface.ItemClickListener;
import com.example.nathapong.oderfood.Model.Food;
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

        // Get Intent here
        if (getIntent() != null)
            CategoryId = getIntent().getStringExtra("CategoryId");

        if (!CategoryId.isEmpty() && CategoryId != null){

            if (Common.isConnectedToInternet(getBaseContext())) {
                loadListFood(CategoryId);
            }
            else {
                Toast.makeText(FoodList.this,"โปรดตรวจสอบการเชื่อมต่ออินเตอร์เน็ต !",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Search
        materialSearchBar = (MaterialSearchBar)findViewById(R.id.searchBar);
        materialSearchBar.setHint("ใส่ชื่อเมนูที่ท่านต้องการ...");

        loadSuggest();

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




    private void loadSuggest() {

        Foods.orderByChild("MenuId").equalTo(CategoryId).addValueEventListener(new ValueEventListener() {
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
                        foodDetail.putExtra("FoodId", adapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });
            }
        };

        // Set Adapter
        Log.d("TAG", "Number of Item is "+adapter.getItemCount());
        recyclerView.setAdapter(adapter);

    }
}
