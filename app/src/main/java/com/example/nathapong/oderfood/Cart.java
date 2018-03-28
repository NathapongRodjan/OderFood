package com.example.nathapong.oderfood;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import co.omise.android.models.Token;
import co.omise.android.ui.CreditCardActivity;

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

    int paymentSelected = 0;  // 1 = Credit , 2 = Money
    boolean isCreditCardSucceed = false;

    private static final String OMISE_PKEY = "pkey_test_5bdi3tzhhnw9apdgepf";
    private static final int REQUEST_CC = 100;


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

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("กรุณากรอกข้อมูลการสั่งซื้อ");
        //alertDialog.setMessage("ที่อยู่สำหรับจัดส่ง : ");

        LayoutInflater inflater = this.getLayoutInflater();
        View order_address_comment = inflater.inflate(R.layout.order_address_comment,null);

        final PlaceAutocompleteFragment edtAddress =(PlaceAutocompleteFragment)getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        // Hide search icon before fragment
        edtAddress.getView().findViewById(R.id.place_autocomplete_search_button).setVisibility(View.GONE);

        // Set Hint for Autocomplete Edit Text
        ((EditText)edtAddress.getView().findViewById(R.id.place_autocomplete_search_input))
                .setHint("แตะเพื่อระบุสถานที่ใกล้เคียง");

        // Set Text Size
        ((EditText)edtAddress.getView().findViewById(R.id.place_autocomplete_search_input))
                .setTextSize(16);

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

        final MaterialEditText edtName = (MaterialEditText)order_address_comment.findViewById(R.id.edtName);
        final MaterialEditText edtPhone = (MaterialEditText)order_address_comment.findViewById(R.id.edtPhone);
        final MaterialEditText edtAddressDetail = (MaterialEditText)order_address_comment.findViewById(R.id.edtAddressDetail);
        final MaterialEditText edtComment = (MaterialEditText)order_address_comment.findViewById(R.id.edtComment);
        final RadioGroup radioGroup = (RadioGroup)order_address_comment.findViewById(R.id.radioGroup);
        final RadioButton rdbCredit = (RadioButton)order_address_comment.findViewById(R.id.rdbCredit);
        final RadioButton rdbMoney = (RadioButton)order_address_comment.findViewById(R.id.rdbMoney);
        final Button btnOK = (Button) order_address_comment.findViewById(R.id.btnOK);
        final Button btnCancel = (Button) order_address_comment.findViewById(R.id.btnCancel);

        edtName.setText(myFirebaseAuth.getCurrentUser().getDisplayName());



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int radioButtonId) {
                switch (radioButtonId){
                    case R.id.rdbCredit :
                        paymentSelected = 1;
                        break;
                    case R.id.rdbMoney :
                        paymentSelected = 2;
                        break;
                    default :
                        paymentSelected = 0;
                        break;
                }
            }
        });

        alertDialog.setView(order_address_comment);    // Set view to dialog
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        //alertDialog.show();
        final AlertDialog showDialog = alertDialog.show();   // Show Dialog when press place order button


        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((edtName.getText().toString().isEmpty())
                        ||(edtPhone.getText().toString()).isEmpty()
                        ||(edtAddressDetail.getText().toString()).isEmpty()) {

                    if (edtName.getText().toString().isEmpty()) {
                        edtName.setError("กรุณากรอกชื่อลูกค้า !");
                    }
                    if (edtPhone.getText().toString().isEmpty()) {
                        edtPhone.setError("กรุณากรอกเบอร์โทรศัพท์ !");
                    }
                    if (edtAddressDetail.getText().toString().isEmpty()) {
                        edtAddressDetail.setError("กรุณากรอกที่อยู่ !");
                    }
                }
                else if (shippingAddress == null){
                    Toast.makeText(Cart.this, "กรุณาระบุสถานที่ใกล้เคียง", Toast.LENGTH_SHORT).show();
                }
                else {

                    switch (paymentSelected){
                        case 0 :
                            Toast.makeText(Cart.this, "กรุณาเลือกวิธีชำระเงิน", Toast.LENGTH_SHORT).show();
                            break;

                        // Credit
                        case 1 :
                            if (!isCreditCardSucceed){
                                showCreditCardForm();
                            }
                            else {
                                Request request = new Request(
                                        myFirebaseAuth.getCurrentUser().getEmail(),
                                        edtPhone.getText().toString(),
                                        edtName.getText().toString(),
                                        edtAddressDetail.getText().toString(),
                                        shippingAddress.getAddress().toString(),
                                        txtTotalPrice.getText().toString(),
                                        "0",  // Initial status of each order
                                        edtComment.getText().toString(),
                                        getOrderDate(),
                                        String.format("%s,%s", shippingAddress.getLatLng().latitude, shippingAddress.getLatLng().longitude),
                                        "ชำระด้วยบัตรเครดิต",
                                        cart);

                                reference.child(String.valueOf(System.currentTimeMillis()))
                                        .setValue(request);

                                //Delete Cart
                                new Database(getBaseContext()).cleanCart();

                                Toast.makeText(Cart.this, "ขอบคุณท่านลูกค้า , คำสั่งซื้อของท่านถูกจัดส่งแล้ว", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            break;

                        // Money
                        case 2 :
                            Request request = new Request(
                                    myFirebaseAuth.getCurrentUser().getEmail(),
                                    edtPhone.getText().toString(),
                                    edtName.getText().toString(),
                                    edtAddressDetail.getText().toString(),
                                    shippingAddress.getAddress().toString(),
                                    txtTotalPrice.getText().toString(),
                                    "0",  // Initial status of each order
                                    edtComment.getText().toString(),
                                    getOrderDate(),
                                    String.format("%s,%s", shippingAddress.getLatLng().latitude, shippingAddress.getLatLng().longitude),
                                    "ชำระด้วยเงินสด",
                                    cart);

                            reference.child(String.valueOf(System.currentTimeMillis()))
                                    .setValue(request);

                            //Delete Cart
                            new Database(getBaseContext()).cleanCart();

                            Toast.makeText(Cart.this, "ขอบคุณท่านลูกค้า , คำสั่งซื้อของท่านถูกจัดส่งแล้ว", Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            showDialog.dismiss();

                // Remove Fragment when close dialog for prevent app crash
                getFragmentManager().beginTransaction()
                        .remove(getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment))
                        .commit();
            }
        });

        // Remove Fragment when close dialog for prevent app crash when tap back press button
        showDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                getFragmentManager().beginTransaction()
                        .remove(getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment))
                        .commit();
            }
        });

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

    private void showCreditCardForm() {
        Intent intent = new Intent(this, CreditCardActivity.class);
        intent.putExtra(CreditCardActivity.EXTRA_PKEY, OMISE_PKEY);
        startActivityForResult(intent, REQUEST_CC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_CC :

                if (resultCode == CreditCardActivity.RESULT_CANCEL) {

                    isCreditCardSucceed = false;  // Failed
                    Toast.makeText(this, "ยกเลิกการทำรายการ", Toast.LENGTH_SHORT).show();
                    return;
                }

                Token token = data.getParcelableExtra(CreditCardActivity.EXTRA_TOKEN_OBJECT);
                isCreditCardSucceed = true;   // Succeed
                Toast.makeText(this, "ตรวจสอบบัตรเครดิตเรียบร้อย", Toast.LENGTH_SHORT).show();
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
