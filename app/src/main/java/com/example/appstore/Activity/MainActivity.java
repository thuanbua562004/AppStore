package com.example.appstore.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.appstore.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragmentManager = getSupportFragmentManager();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                // Check which item is selected
                if (item.getItemId() == R.id.nav_home) {
                    selectedFragment = new HomeF();
                } else if (item.getItemId() == R.id.nav_user) {
                    selectedFragment = new UsersF();
                }
                if (item.getItemId() == R.id.nav_product) {
                    selectedFragment = new ProductF();
                } else if (item.getItemId() == R.id.nav_notifi) {
                    selectedFragment = new NotifiF();
                }

                // Replace the fragment only if it is not null
                if (selectedFragment != null) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                } else {
                    // Handle the case where selectedFragment is null
                    // Optionally log an error or handle unexpected cases
                }
                return true;
            }
        });

        // Load default fragment if savedInstanceState is null
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home); // Set default fragment ID here
        }
    }
}
