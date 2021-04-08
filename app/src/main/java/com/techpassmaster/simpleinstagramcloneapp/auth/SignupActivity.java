package com.techpassmaster.simpleinstagramcloneapp.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.techpassmaster.simpleinstagramcloneapp.R;

import java.util.HashMap;

/**
 * Created by Techpass Master on 8-April-21.
 * Website - https://techpassmaster.com/
 * Email id - hello@techpassmaster.com
 */

public class SignupActivity extends AppCompatActivity {

    private TextView txt_login;
    private EditText username, fullname, email, password;
    private Button signup_btn;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    ProgressBar signUp_progress;

    String str_username, str_fullname, str_password, str_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        txt_login = findViewById(R.id.txtview_login);
        signUp_progress = findViewById(R.id.signUp_progress);
        username = findViewById(R.id.username);
        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signup_btn = findViewById(R.id.signup_btn);

        mAuth = FirebaseAuth.getInstance();

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SignupActivity.this, LoginActivity.class));

            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateUsername() | !validateFullname() | !validateEmail() | !validatePassword()) {
                    return;
                }

                //    progressbar VISIBLE
                signUp_progress.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(str_email, str_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            //    progressbar GONE

                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", str_username);
                            hashMap.put("fullname", str_fullname);
                            hashMap.put("profileImageUrl", "https://firebasestorage.googleapis.com/v0/b/flytant-assignment.appspot.com/o/user.png?alt=media&token=bb9071b2-81e8-45a0-8945-fe991911f99d");

                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

//                                        mAuth.getCurrentUser().sendEmailVerification()
//                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<Void> task) {
//                                                        if (task.isSuccessful()) {
//
//
//                                                        }
//                                                    }
//                                                });
                                        signUp_progress.setVisibility(View.GONE);
                                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                        Toast.makeText(SignupActivity.this, "SignUp Successfully", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        } else {
                            //    progressbar GONE
                            signUp_progress.setVisibility(View.GONE);
                            Toast.makeText(SignupActivity.this, "Check Email id or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });


    }

    private boolean validateUsername() {
        str_username = username.getText().toString().trim();
        if (TextUtils.isEmpty(str_username)) {
            Toast.makeText(SignupActivity.this, "Enter Your Username", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateFullname() {
        str_fullname = fullname.getText().toString().trim();
        if (TextUtils.isEmpty(str_fullname)) {
            Toast.makeText(SignupActivity.this, "Enter Your Full Name", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateEmail() {
        str_email = email.getText().toString().trim();
        if (TextUtils.isEmpty(str_email)) {
            Toast.makeText(SignupActivity.this, "Enter Your Email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(str_email).matches()) {
            Toast.makeText(SignupActivity.this, "Please enter valid Email", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validatePassword() {
        str_password = password.getText().toString().trim();

        if (TextUtils.isEmpty(str_password)) {
            Toast.makeText(SignupActivity.this, "Enter Your Password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (str_password.length() <= 6) {
            Toast.makeText(SignupActivity.this, "Password is Very Short", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}