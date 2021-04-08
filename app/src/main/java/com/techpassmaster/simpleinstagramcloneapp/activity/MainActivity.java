package com.techpassmaster.simpleinstagramcloneapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.techpassmaster.simpleinstagramcloneapp.R;
import com.techpassmaster.simpleinstagramcloneapp.tabs.home.HomeFragment;
import com.techpassmaster.simpleinstagramcloneapp.tabs.post.PostActivity;
import com.techpassmaster.simpleinstagramcloneapp.tabs.profile.ProfileFragment;

/**
 * Created by Techpass Master on 8-April-21.
 * Website - https://techpassmaster.com/
 * Email id - hello@techpassmaster.com
 */

public class MainActivity extends AppCompatActivity {

    Fragment selectFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.navigation_home:
                        selectFragment = new HomeFragment();
                        break;

                    case R.id.navigation_post:
                        selectFragment = null;
                        startActivity(new Intent(MainActivity.this, PostActivity.class));
                        break;

                    case R.id.navigation_profile:
                        selectFragment = new ProfileFragment();
                        break;

                }
                if (selectFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectFragment).commit();
                }

                return true;
            }
        });


    }

}