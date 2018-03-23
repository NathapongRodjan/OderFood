package com.example.nathapong.oderfood;

import android.app.Fragment;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nathapong.oderfood.Common.Common;
import com.example.nathapong.oderfood.Database.Database;
import com.example.nathapong.oderfood.Model.Order;
import com.example.nathapong.oderfood.Model.Request;
import com.example.nathapong.oderfood.ViewHolder.CartAdapter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference reference;

    FirebaseAuth myFirebaseAuth;

    public TextView txtTotalPrice;
    Button btnPlaceOrder;

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

    Place shippingAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Firebase
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Request");
        myFirebaseAuth = FirebaseAuth.getInstance();

        //Init
        recyclerView = (RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = (TextView)findViewById(R.id.total);
        btnPlaceOrder = (Button)findViewById(R.id.btnPlaceOrder);


        loadListFood();  // Load List Order in Cart


        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isConnectedToInternet(getBaseContext())) {

                    // Check item in Cart is not empty
                    if (cart.size() > 0) {

                        showAlertDialogAndPlaceOrder();

                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
                        alertDialog.setTitle("ท่านไม่มีรายการในรถเข็น !");
                        alertDialog.setMessage("กรุณาเลือกรายการอาหารที่สนใจ...");
                        alertDialog.setIcon(R.drawable.ic_remove_shopping_cart_black_24dp);
                        alertDialog.show();
                    }
                }
                else {
                    Toast.makeText(Cart.this,"โปรดตรวจสอบการเชื่อมต่ออินเตอร์เน็ต !",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void showAlertDialogAndPlaceOrder() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("กรุณากรอกที่อยู่ของท่าน...");
        alertDialog.setMessage("ที่อยู่สำหรับจัดส่ง : ");

        LayoutInflater inflater = this.getLayoutInflater();
        View order_address_comment = inflater.inflate(R.layout.order_address_comment,null);

        PlaceAutocompleteFragment edtAddress =(PlaceAutocompleteFragment)getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        // Hide search icon before fragment
        edtAddress.getView().findViewById(R.id.place_autocomplete_search_button).setVisibility(View.GONE);

        // Set Hint for Autocomplete Edit Text
        ((EditText)edtAddress.getView().findViewById(R.id.place_autocomplete_search_input))
                .setHint("แตะเพื่อกรอกที่อยู่ของท่าน");

        // Set Text Size
        ((EditText)edtAddress.getView().findViewById(R.id.place_autocomplete_search_input))
                .setTextSize(20);

        // Get Address from place autocomplete
        edtAddress.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                shippingAddress = place;
            }

            @Override
            public void onError(Status status) {

                Log.e("ERROR", status.getStatusMessage());
            }
        });

        final MaterialEditText edtPhone = (MaterialEditText)order_address_comment.findViewById(R.id.edtPhone);
        final MaterialEditText edtComment = (MaterialEditText)order_address_comment.findViewById(R.id.edtComment);

        alertDialog.setView(order_address_comment);    // Set view to dialog
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                //Create new Request
                Request request = new Request(
                        myFirebaseAuth.getCurrentUser().getEmail(),
                        edtPhone.getText().toString(),
                        myFirebaseAuth.getCurrentUser().getDisplayName(),
                        shippingAddress.getAddress().toString(),
                        txtTotalPrice.getText().toString(),
                        "0",  // Initial status of each order
                        edtComment.getText().toString(),
                        getOrderDate(),
                        String.format("%s,%s", shippingAddress.getLatLng().latitude, shippingAddress.getLatLng().longitude),
                        cart);

                reference.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(request);

                //Delete Cart
                new Database(getBaseContext()).cleanCart();

                Toast.makeText(Cart.this, "ขอบคุณท่านลูกค้า , คำสั่งซื้อของท่านถูกจัดส่งแล้ว", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        alertDialog.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                // Remove Fragment when close dialog for prevent app crash
                getFragmentManager().beginTransaction()
                        .remove(getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment))
                        .commit();
            }
        });

        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                getFragmentManager().beginTransaction()
                        .remove(getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment))
                        .commit();
            }
        });

        alertDialog.show();
    }
    

    public void loadListFood() {

        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart,this);
        adapter.notifyDataSetChanged();     // Refresh info after delete item in cart
        recyclerView.setAdapter(adapter);

        //Calculate total price
        int total = 0;
        for (Order order : cart){
            total += (Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
            Locale locale = new Locale("th", "TH");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

            txtTotalPrice.setText(fmt.format(total));
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle().equals(Common.DELETE)){
            deleteCart(item.getOrder());
        }
        return true;
    }

    private void deleteCart(int position) {

        // Remove item at List<Order> by position
        cart.remove(position);

        //After that, delete all old data from SQLite
        new Database(this).cleanCart();

        //And update new data from List<Order> to SQLite
        for (Order item : cart){
            new Database(this).addTOCart(item);
        }

        txtTotalPrice.setText("");
        loadListFood();  // After update load List Food again
    }


    private String getOrderDate(){

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
