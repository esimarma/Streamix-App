package com.example.app_streamix.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.res.Configuration;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.app_streamix.R;
import com.example.app_streamix.adapters.SettingsAdapter;
import com.example.app_streamix.utils.SessionManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SettingsFragment extends Fragment {

    private SessionManager sessionManager;
    private static final String LANGUAGE_PREFERENCE = "language_preference";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        sessionManager = new SessionManager(requireContext());
        loadLanguagePreference();

        // Back Button
        view.findViewById(R.id.backButton).setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        // RecyclerView Setup
        RecyclerView recyclerView = view.findViewById(R.id.settingsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Populate Settings List
        List<String> settingsOptions = new ArrayList<>();
        settingsOptions.add(getString(R.string.language_option));
        settingsOptions.add(getString(R.string.github_option));
        if (sessionManager.isLoggedIn()) {
            settingsOptions.add(getString(R.string.account_option));
            settingsOptions.add(getString(R.string.logout_option));
        }

        SettingsAdapter adapter = new SettingsAdapter(settingsOptions, this::handleSettingClick);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void handleSettingClick(String option) {
        if (option.equals(getString(R.string.account_option))) {
            navigateToFragment(new AccountFragment());
        } else if (option.equals(getString(R.string.language_option))) {
            showLanguageDialog();
        } else if (option.equals(getString(R.string.github_option))) {
            openGitHubRepository();
        } else if (option.equals(getString(R.string.logout_option))) {
            performLogout();
        }
    }

    private void navigateToFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void showLanguageDialog() {
        String[] languages = {
                getString(R.string.language_portuguese), // Portuguese language option
                getString(R.string.language_english) // English language option
        };

        int checkedItem = getCurrentLanguageIndex(); // Get the currently selected language index

        // Inflate the custom title view
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View titleView = inflater.inflate(R.layout.custom_dialog_title, null);
        TextView titleTextView = titleView.findViewById(R.id.customDialogTitle);
        titleTextView.setText(getString(R.string.language_dialog_title));

        // Create an alert dialog for language selection
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setCustomTitle(titleView) // Set custom title
                .setSingleChoiceItems(languages, checkedItem, (dialogInterface, which) -> {
                    if (which == 0) {
                        changeLanguage("pt"); // Change language to Portuguese
                    } else if (which == 1) {
                        changeLanguage("en"); // Change language to English
                    }
                    dialogInterface.dismiss(); // Close the dialog after selection
                })
                .setNegativeButton(getString(R.string.cancel), (dialogInterface, which) -> dialogInterface.dismiss()) // Cancel button
                .create();

        dialog.setOnShowListener(d -> {
            // Make sure the title text is white (handled by custom layout)

            // Make sure list items are white
            ListView listView = dialog.getListView();
            if (listView != null) {
                for (int i = 0; i < listView.getChildCount(); i++) {
                    TextView item = (TextView) listView.getChildAt(i);
                    if (item != null) item.setTextColor(getResources().getColor(R.color.white));
                }
            }

            // Make sure the cancel button is white
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.white));
        });

        dialog.show(); // Show the dialog
        dialog.getWindow().setBackgroundDrawableResource(R.color.app_background);

        // Customize the cancel button color (keeping your existing logic)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
    }


    private void changeLanguage(String languageCode) {
        SharedPreferences preferences = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LANGUAGE_PREFERENCE, languageCode); // Save the selected language
        editor.apply();

        // Update the app's locale
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        requireContext().getResources().updateConfiguration(config, requireContext().getResources().getDisplayMetrics());

        restartFragment(); // Restart the fragment to apply changes
    }

    private int getCurrentLanguageIndex() {
        String currentLanguage = Locale.getDefault().getLanguage(); // Get system default language
        if (currentLanguage.equals("pt")) {
            return 0; // Portuguese
        } else if (currentLanguage.equals("en")) {
            return 1; // English
        }
        return -1; // Default case (should not happen)
    }

    private void loadLanguagePreference() {
        SharedPreferences preferences = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        String languageCode = preferences.getString(LANGUAGE_PREFERENCE, Locale.getDefault().getLanguage());

        // Apply the saved language setting
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        requireContext().getResources().updateConfiguration(config, requireContext().getResources().getDisplayMetrics());
    }

    private void restartFragment() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, new SettingsFragment()) // This forces a reload
                .commit();
    }

    private void openGitHubRepository() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/esimarma/Streamix-App"));
        startActivity(browserIntent);
    }

    private void performLogout() {
        sessionManager.logout(); // Clear session data
        Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, new LoginFragment())
                .commit();
    }
}
