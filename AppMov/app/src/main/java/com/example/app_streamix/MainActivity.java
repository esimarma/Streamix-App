package com.example.app_streamix;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.app_streamix.fragments.AccountFragment;
import com.example.app_streamix.fragments.ChangePasswordFragment;
import com.example.app_streamix.fragments.HomeFragment;
import com.example.app_streamix.fragments.ListsFragment;
import com.example.app_streamix.fragments.LoginFragment;
import com.example.app_streamix.fragments.ProfileFragment;
import com.example.app_streamix.fragments.RegisterFragment;
import com.example.app_streamix.fragments.SearchFragment;
import com.example.app_streamix.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ImageView appIcon, settingsIcon;
    private TextView headerTitle;
    private View topHeader; // Reference to the header container
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applySavedLanguage(); // Apply saved language before UI loads
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize header views
        appIcon = findViewById(R.id.app_icon);
        settingsIcon = findViewById(R.id.menu_icon);
        headerTitle = findViewById(R.id.header_title);
        topHeader = findViewById(R.id.top_header); // Get reference to the header layout
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

        // Listen for back stack changes to properly manage header visibility
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment currentFragment = getCurrentFragment();
            updateHeader(currentFragment);
        });
    }

    // Method to update the header dynamically based on the current fragment
    // Update the method to hide the header for Login and Register fragments
    private void updateHeader(Fragment fragment) {
        if (fragment instanceof SettingsFragment ||
                fragment instanceof AccountFragment ||
                fragment instanceof ChangePasswordFragment ||
                fragment instanceof LoginFragment ||
                fragment instanceof RegisterFragment) {

            hideHeader(); // Hide header in these screens
        } else {
            showHeader(); // Show header for all other screens

            appIcon.setVisibility(View.GONE); // Hide App Icon by default
            settingsIcon.setVisibility(View.GONE); // Hide Settings Icon by default

            if (fragment instanceof HomeFragment) {
                headerTitle.setText(getString(R.string.home_title));
                appIcon.setVisibility(View.VISIBLE);
                settingsIcon.setVisibility(View.VISIBLE);
            } else if (fragment instanceof SearchFragment) {
                headerTitle.setText(getString(R.string.search_title));
            } else if (fragment instanceof ListsFragment) {
                headerTitle.setText(getString(R.string.lists_title));
            } else if (fragment instanceof ProfileFragment) {
                headerTitle.setText(getString(R.string.profile_title));
                settingsIcon.setVisibility(View.VISIBLE);
            }
        }
    }

    // Method to hide the header
    public void hideHeader() {
        if (topHeader != null) {
            topHeader.setVisibility(View.GONE);

            // Force a layout recalculation
            findViewById(R.id.fragmentContainer).post(() -> {
                findViewById(R.id.fragmentContainer).requestLayout();
            });
        }
    }

    // Method to show the header
    public void showHeader() {
        if (topHeader != null) {
            topHeader.setVisibility(View.VISIBLE);
        }
    }

    // Method to get the currently active fragment
    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
    }

    public void navigateToSearch() {
        bottomNavigationView.setSelectedItemId(R.id.nav_search);
    }

    public void navigateToSettings() {
        // Navigate to SettingsFragment and hide the header
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new SettingsFragment())
                .addToBackStack(null) // Allows back navigation
                .commit();
        hideHeader(); // Hide header when opening Settings
    }
    private void applySavedLanguage() {
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        String languageCode = preferences.getString("language_preference", Locale.getDefault().getLanguage());

        setLocale(languageCode); // Apply the saved locale
    }

    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

}
