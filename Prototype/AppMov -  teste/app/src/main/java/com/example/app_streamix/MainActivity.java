package com.example.app_streamix;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        ConstraintLayout header = findViewById(R.id.header); // Reference the header layout
        //Escolher o primeiro fragment que aparece no inicia da app
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new HomeFragment()).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if(item.getItemId() == R.id.nav_home){
                selectedFragment = new HomeFragment();
                header.setVisibility(View.VISIBLE); // Show the header in HomeFragment
            } else if (item.getItemId() == R.id.nav_search) {
                selectedFragment = new SearchFragment();
                header.setVisibility(View.GONE); // Hide the header
            } else if (item.getItemId() == R.id.nav_lists) {
                selectedFragment = new ListsFragment();
                header.setVisibility(View.GONE); // Hide the header
            }else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
                header.setVisibility(View.GONE); // Hide the header
            }

            if (selectedFragment != null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
            }

            return true;
        });
    }
    public void navigateToSearch() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_search);
    }

}