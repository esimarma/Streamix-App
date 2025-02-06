package com.example.app_streamix;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.app_streamix.fragments.HomeFragment;
import com.example.app_streamix.fragments.ListsFragment;
import com.example.app_streamix.fragments.ProfileFragment;
import com.example.app_streamix.fragments.SearchFragment;
import com.example.app_streamix.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ImageView appIcon, settingsIcon;
    private TextView headerTitle;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize header views
        appIcon = findViewById(R.id.app_icon);
        settingsIcon = findViewById(R.id.menu_icon);

        headerTitle = findViewById(R.id.header_title);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set default fragment (Home)
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new HomeFragment()).commit();
        updateHeader(new HomeFragment());

        // Handle bottom navigation clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_search) {
                selectedFragment = new SearchFragment();
            } else if (item.getItemId() == R.id.nav_lists) {
                selectedFragment = new ListsFragment();
            } else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
                updateHeader(selectedFragment);
            }

            return true;
        });

        // Settings Button Click Listener
        settingsIcon.setOnClickListener(v -> navigateToSettings());
    }

    // Method to update the header dynamically
    private void updateHeader(Fragment fragment) {
        appIcon.setVisibility(View.GONE); // Hide App Icon by default
        settingsIcon.setVisibility(View.GONE); // Hide Settings Icon by default

        if (fragment instanceof HomeFragment) {
            headerTitle.setText(getString(R.string.home_title));
            appIcon.setVisibility(View.VISIBLE); // Show App Icon only on Home
            settingsIcon.setVisibility(View.VISIBLE); // Show Settings Icon
        } else if (fragment instanceof SearchFragment) {
            headerTitle.setText(getString(R.string.search_title));
        } else if (fragment instanceof ListsFragment) {
            headerTitle.setText(getString(R.string.lists_title));
        } else if (fragment instanceof ProfileFragment) {
            headerTitle.setText(getString(R.string.profile_title));
        }
    }


    public void navigateToSearch() {
        bottomNavigationView.setSelectedItemId(R.id.nav_search);
    }

    public void navigateToSettings() {
        // Navigate to SettingsFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new SettingsFragment()) // Replace with settings fragment
                .addToBackStack(null) // Allows back navigation
                .commit();
    }
}
