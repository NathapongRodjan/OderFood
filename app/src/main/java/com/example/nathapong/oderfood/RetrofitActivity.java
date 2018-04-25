package com.example.nathapong.oderfood;

import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nathapong.oderfood.ItemDecoration.VerticalSpaceItemDecoration;
import com.example.nathapong.oderfood.JsonApi.Client;
import com.example.nathapong.oderfood.JsonApi.Service;
import com.example.nathapong.oderfood.JsonModel.Category;
import com.example.nathapong.oderfood.JsonModel.DataList;
import com.example.nathapong.oderfood.JsonModel.FoodItem;
import com.example.nathapong.oderfood.RetrofitAdapter.RecyclerViewDataAdapter;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitActivity extends AppCompatActivity {

    ArrayList<Category> allCategory;
    RecyclerView my_recycler_view;
    TextView txtText;

    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        allCategory = new ArrayList<Category>();

        txtText = (TextView)findViewById(R.id.txtText);

        loadFood();

        my_recycler_view = (RecyclerView) findViewById(R.id.my_recycler_view);

        my_recycler_view.setHasFixedSize(true);

        RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(this, allCategory);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        my_recycler_view.setLayoutManager
                (new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        my_recycler_view.addItemDecoration(new VerticalSpaceItemDecoration(50,20,20,50));

        my_recycler_view.setAdapter(adapter);
    }


    private void loadFood(){

        Service service = Client.getClient();

        Call<DataList> call = service.getCategoryList();

        call.enqueue(new Callback<DataList>() {
            @Override
            public void onResponse(Call<DataList> call, Response<DataList> response) {

                DataList list = response.body();

                ArrayList<Category> value = list.getCategoryList();

                for (int i = 0; i < value.size(); i++){

                    Category categoryData = new Category();

                    categoryData.setCategoryName(value.get(i).getCategoryName());

                    ArrayList<FoodItem> foodItems = new ArrayList<FoodItem>();

                    for (int x = 0; x < value.get(i).getItem().size(); x++){

                        String name = value.get(i).getItem().get(x).getName();
                        String shotDetail = value.get(i).getItem().get(x).getShotDetail();
                        String image = value.get(i).getItem().get(x).getImage();

                        foodItems.add(new FoodItem(name, shotDetail, image));
                    }

                    if (foodItems.size() >= 1) {
                        categoryData.setItem(foodItems);
                        allCategory.add(categoryData);
                    }
                }

                txtText.setText("\n");
            }

            @Override
            public void onFailure(Call<DataList> call, Throwable t) {

                Log.d("ERROR",t.getMessage());
            }
        });
    }
}
