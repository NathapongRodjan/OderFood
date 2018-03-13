package com.example.nathapong.oderfood;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btnSignUp, btnSignIn;
    TextView txtSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignUp = (Button)findViewById(R.id.btnSignUp);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        txtSlogan = (TextView)findViewById(R.id.txtSlogan);

        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
        txtSlogan.setTypeface(face);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent SignUpIntent = new Intent(MainActivity.this, SignUp.class);
                startActivity(SignUpIntent);
            }
        });


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent SignInIntent = new Intent(MainActivity.this, SignIn.class);
                startActivity(SignInIntent);
            }
        });


    }
}
