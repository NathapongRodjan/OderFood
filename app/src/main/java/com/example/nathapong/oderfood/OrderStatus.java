package com.example.nathapong.oderfood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.nathapong.oderfood.Common.Common;
import com.example.nathapong.oderfood.Model.Request;
import com.example.nathapong.oderfood.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.Locale;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth myFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Request");
        myFirebaseAuth = FirebaseAuth.getInstance();

        recyclerView = (RecyclerView)findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(myFirebaseAuth.getCurrentUser().getEmail());
    }

    private void loadOrders(String email) {

        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                reference.orderByChild("email").equalTo(email)) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {

                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(convertCOdeToStatus(model.getStatus()));
                viewHolder.txtOrderPhone.setText(model.getPhone());
                viewHolder.txtOrderTotal.setText(model.getTotal());
                viewHolder.txtOrderPayment.setText(model.getPaymentStatus());
                viewHolder.txtOrderDate.setText(model.getOrderDate());
                viewHolder.txtOrderAddress.setText(model.getAddressDetail());


            }
        };

        recyclerView.setAdapter(adapter);
    }

    private String convertCOdeToStatus(String status) {

        if (status.equals("0")){
            return "รับคำสั่งซื้อแล้ว";
        }
        else if (status.equals("1")){
            return "กำลังจัดส่ง...";
        }
        else {
            return "จัดส่งแล้ว";
        }
    }
}
