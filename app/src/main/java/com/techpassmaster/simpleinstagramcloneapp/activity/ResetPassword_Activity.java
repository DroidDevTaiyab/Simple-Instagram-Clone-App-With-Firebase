package com.techpassmaster.simpleinstagramcloneapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.techpassmaster.simpleinstagramcloneapp.R;
import com.techpassmaster.simpleinstagramcloneapp.auth.LoginActivity;

/**
 * Created by Techpass Master on 8-April-21.
 * Website - https://techpassmaster.com/
 * Email id - hello@techpassmaster.com
 */

public class ResetPassword_Activity extends AppCompatActivity {

    private EditText edit_txt_resetEmail;
    private Button button_resetPassword;
    private ProgressBar resetPassword_progress;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        edit_txt_resetEmail = findViewById(R.id.edit_txt_resetEmail);
        button_resetPassword = findViewById(R.id.button_resetPassword);
        resetPassword_progress = findViewById(R.id.resetPassword_progress);

        firebaseAuth = FirebaseAuth.getInstance();

        button_resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetPassword_progress.setVisibility(View.VISIBLE);

                firebaseAuth.sendPasswordResetEmail(edit_txt_resetEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                resetPassword_progress.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResetPassword_Activity.this, "Password reset link sent to your Email", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ResetPassword_Activity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(ResetPassword_Activity.this, "Something error", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        });


    }
}