package com.example.nathapong.oderfood;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nathapong.oderfood.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {

    MaterialEditText edtPhone, edtName, edtPassword;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtPhone = (MaterialEditText)findViewById(R.id.edtPhone);
        edtName = (MaterialEditText)findViewById(R.id.edtName);
        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);

        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                mDialog.setMessage("Please waiting...");
                mDialog.show();


                // Use addListenerForSingleValueEvent instead of addValueEventListener
                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                         //Check if already user's phone
                        if (dataSnapshot.child(edtPhone.getText().toString()).exists()){

                            mDialog.dismiss();
                            Toast.makeText(SignUp.this, "Phone number already register !", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            mDialog.dismiss();

                            User user = new User(edtName.getText().toString(), edtPassword.getText().toString());

                            table_user.child(edtPhone.getText().toString()).setValue(user);

                            Toast.makeText(SignUp.this, "Sign up successfully !", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
