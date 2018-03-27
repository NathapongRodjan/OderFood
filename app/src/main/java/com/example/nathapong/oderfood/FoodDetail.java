package com.example.nathapong.oderfood;

import android.app.DownloadManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.nathapong.oderfood.Common.Common;
import com.example.nathapong.oderfood.Database.Database;
import com.example.nathapong.oderfood.Model.Food;
import com.example.nathapong.oderfood.Model.Order;
import com.example.nathapong.oderfood.Model.Rating;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class FoodDetail extends AppCompatActivity implements RatingDialogListener{

    TextView food_name, food_price, food_description;
    ImageView img_food;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnRating;
    CounterFab btnCart;
    ElegantNumberButton numberButton;
    RatingBar ratingBar;

    String foodId = "";

    FirebaseDatabase database;
    DatabaseReference food;        // For access food in Firebase
    DatabaseReference ratingRef;   // For access rating in Firebase
    FirebaseAuth myFirebaseAuth;

    Food currentFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        database = FirebaseDatabase.getInstance();
        food = database.getReference("Food");
        ratingRef = database.getReference("Rating");
        myFirebaseAuth = FirebaseAuth.getInstance();

        // Init View Object
        numberButton = (ElegantNumberButton)findViewById(R.id.number_button);
        btnCart = (CounterFab) findViewById(R.id.btncart);
        btnRating = (FloatingActionButton)findViewById(R.id.btn_rating);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        img_food = (ImageView) findViewById(R.id.img_food);
        food_name = (TextView)findViewById(R.id.food_name);
        food_description = (TextView)findViewById(R.id.food_description);
        food_price = (TextView)findViewById(R.id.food_price);
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpendeddAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addTOCart(new Order(
                        foodId,
                        currentFood.getName(),
                        numberButton.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getDiscount(),
                        currentFood.getImage()
                ));

                btnCart.setCount(new Database(FoodDetail.this).getCountCart());
                Toast.makeText(FoodDetail.this, "เพิ่มในรถเข็นแล้ว !", Toast.LENGTH_SHORT).show();
            }
        });

        btnCart.setCount(new Database(this).getCountCart());


        // Get FoodId from Intent
        if (getIntent() != null)
            foodId = getIntent().getStringExtra("FoodId");
        if (!foodId.isEmpty()){

            if (Common.isConnectedToInternet(getBaseContext())) {
                getDetailFood(foodId);
                getRatingFood(foodId);
            }
            else {
                Toast.makeText(FoodDetail.this,"โปรดตรวจสอบการเชื่อมต่ออินเตอร์เน็ต !",Toast.LENGTH_SHORT).show();
                return;
            }
        }


    }

    private void getRatingFood(final String foodId) {

        Query foodRating = ratingRef.orderByChild("foodId").equalTo(foodId);

        foodRating.addValueEventListener(new ValueEventListener() {

            int count = 0, sum = 0;  //For calculate rating average

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                    Rating item = postSnapshot.getValue(Rating.class);
                    sum += Integer.parseInt(item.getRateValue());
                    count++;
                }
                if (count != 0){
                    float average = sum / count;
                    ratingBar.setRating(average);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showRatingDialog() {

        new AppRatingDialog.Builder()
                .setPositiveButtonText("ส่ง")
                .setNegativeButtonText("ยกเลิก")
                .setNoteDescriptions(Arrays.asList("แย่","พอใช้","ปานกลาง","ดี","ดีมาก"))
                .setDefaultRating(1)
                .setTitle("ความพึงพอใจ")
                .setDescription("กรุณาให้คะแนนดาว และส่งความคิดเห็นของท่าน")
                .setTitleTextColor(R.color.normalText)
                .setDescriptionTextColor(R.color.normalText)
                .setHint("กรุณาเขียนความคิดเห็น...")
                .setHintTextColor(R.color.normalText)
                .setCommentTextColor(R.color.normalText)
                .setCommentBackgroundColor(R.color.ratingCommentBackground)
                .setWindowAnimation(R.style.RatingDialogFadeAnimate)
                .create(FoodDetail.this)
                .show();

    }

    private void getDetailFood(String foodId) {

        food.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                currentFood = dataSnapshot.getValue(Food.class);

                Glide
                        .with(FoodDetail.this)
                        .load(currentFood.getImage())
                        .into(img_food);

                collapsingToolbarLayout.setTitle(currentFood.getName());

                food_name.setText(currentFood.getName());
                food_price.setText(currentFood.getPrice());
                food_description.setText(currentFood.getDescription());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public void onPositiveButtonClicked(int rateValue, String comment) {

        // Get Rating and upload to Firebase
        final Rating rating = new Rating(
                                myFirebaseAuth.getCurrentUser().getEmail(),
                                myFirebaseAuth.getCurrentUser().getDisplayName(),
                                foodId,
                                String.valueOf(rateValue),
                                comment,
                                getDate());

        final String Key_Rating = ratingRef.push().getKey(); // Create auto Key

        ratingRef.child(Key_Rating).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(Key_Rating).exists()){

                    // Remove old Value
                    ratingRef.child(Key_Rating).removeValue();

                    // Update new Value
                    ratingRef.child(Key_Rating).setValue(rating);
                }
                else {
                    // Update new Value
                    ratingRef.child(Key_Rating).setValue(rating);
                }
                Toast.makeText(FoodDetail.this, "ขอบคุณสำหรับความคิดเห็นของท่าน", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    private String getDate(){

        String thaiMonths[] = {
                "ม.ค.", "ก.พ.", "มี.ค.", "เม.ย.",
                "พ.ค.", "มิ.ย.", "ก.ค.", "ส.ค.",
                "ก.ย.", "ต.ค.", "พ.ย.", "ธ.ค."};

        final Calendar c = Calendar.getInstance();
        int Year = c.get(Calendar.YEAR) + 543;
        int Month = c.get(Calendar.MONTH);
        int Day = c.get(Calendar.DAY_OF_MONTH);

        DateFormat df = new SimpleDateFormat("HH:mm");
        String time = df.format(Calendar.getInstance().getTime()) + " น.";   // Get Time

        String orderDate = Day + " " + thaiMonths[Month] + " " + Year + " เวลา " + time;

        return orderDate;
    }

}
