package com.example.nathapong.oderfood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nathapong.oderfood.Common.Common;
import com.example.nathapong.oderfood.Model.User;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {

    MaterialEditText edtEmail, edtName, edtPassword;
    Button btnSignUp;

    FirebaseDatabase database;
    DatabaseReference table_user;

    private FirebaseAuth myFirebaseAuth;

    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtEmail = (MaterialEditText)findViewById(R.id.edtEmail);
        edtName = (MaterialEditText)findViewById(R.id.edtName);
        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);

        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        myFirebaseAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createAccount(edtEmail.getText().toString(), edtPassword.getText().toString());
            }
        });

        /*btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isConnectedToInternet(getBaseContext())) {

                    final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                    mDialog.setMessage("Please waiting...");
                    mDialog.show();


                    // Use addListenerForSingleValueEvent instead of addValueEventListener
                    table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //Check if already user's phone
                            if (dataSnapshot.child(edtEmail.getText().toString()).exists()) {

                                mDialog.dismiss();
                                Toast.makeText(SignUp.this, "Phone number already register !", Toast.LENGTH_SHORT).show();
                            } else {
                                mDialog.dismiss();

                                User user = new User(edtName.getText().toString(), edtPassword.getText().toString());

                                table_user.child(edtEmail.getText().toString()).setValue(user);

                                Toast.makeText(SignUp.this, "Sign up successfully !", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    Toast.makeText(SignUp.this,"โปรดตรวจสอบการเชื่อมต่ออินเตอร์เน็ต !",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });*/
    }

    private boolean signUpValidateForm() {
        boolean valid = true;

        String name = edtEmail.getText().toString();
        if (TextUtils.isEmpty(name)) {
            edtEmail.setError("จำเป็นต้องระบุอีเมล์...");
            valid = false;
        } else {
            edtEmail.setError(null);
        }

        String email = edtName.getText().toString();
        if (TextUtils.isEmpty(email)) {
            edtName.setError("จำเป็นต้องระบุชื่อ...");
            valid = false;
        } else {
            edtName.setError(null);
        }

        String password = edtPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("จำเป็นต้องระบุรหัสผ่าน...");
            valid = false;
        } else {
            edtPassword.setError(null);
        }

        return valid;
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!signUpValidateForm()) {
            return;
        }

        if (password.length() < 6){
            Toast.makeText(SignUp.this,"รหัสผ่านต้องมากกว่า 6 ตัว ขึ้นไป",Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
        mDialog.setMessage("กรุณารอสักครู่...");
        mDialog.show();


        // [START create_user_with_email]
        myFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = myFirebaseAuth.getCurrentUser();

                            specifyUserProfile();
                            updateUI(user);
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "การเข้าสู่ระบบล้มเหลว",
                                    Toast.LENGTH_SHORT).show();

                            updateUI(null);
                        }

                        mDialog.dismiss();
                    }
                });
    }

    private void updateUI(FirebaseUser currentUser) {

        if (currentUser != null) {
            Intent homeIntent = new Intent(SignUp.this, Home.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(homeIntent);
            finish();
        }
    }

    private void specifyUserProfile(){

        FirebaseUser user = myFirebaseAuth.getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(edtName.getText().toString())
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        FirebaseUser currentUser = myFirebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }


}
