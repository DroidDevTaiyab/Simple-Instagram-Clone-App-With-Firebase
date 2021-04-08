package com.techpassmaster.simpleinstagramcloneapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.techpassmaster.simpleinstagramcloneapp.R;

/**
 * Created by Techpass Master on 8-April-21.
 * Website - https://techpassmaster.com/
 * Email id - hello@techpassmaster.com
 */

public class PhoneAuth_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoneauth);

        Button btn_phone_next = findViewById(R.id.btn_phone_next);
        final EditText edt_txt_mobile_no = findViewById(R.id.edt_txt_mobile_no);

        btn_phone_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = edt_txt_mobile_no.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10) {
                    edt_txt_mobile_no.setError("Valid number is required");
                    edt_txt_mobile_no.requestFocus();
                    return;
                }

                String phonenumber = "+91" + number;
                Intent intent = new Intent(PhoneAuth_Activity.this, VerifyPhoneActivity.class);
                intent.putExtra("code", phonenumber);
                startActivity(intent);
                finish();

            }
        });
    }
}