package com.example.app_streamix.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app_streamix.MainActivity;
import com.example.app_streamix.R;
import com.example.app_streamix.models.User;
import com.example.app_streamix.utils.SessionManager;

public class AccountFragment extends Fragment {

    private EditText editTextName, editTextEmail;
    private TextView changePassword;
    private Button deleteAccountButton;
    private ImageView backButton, saveButton;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Get reference to MainActivity to control the header
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.hideHeader(); // Hide header when entering AccountFragment

        // Force layout recalculation AFTER hiding header
        view.postDelayed(() -> {
            view.requestLayout();
        }, 100); // Small delay to ensure recalculation

        // Initialize UI elements
        backButton = view.findViewById(R.id.back_button);
        saveButton = view.findViewById(R.id.save_button);
        editTextName = view.findViewById(R.id.editText_name);
        editTextEmail = view.findViewById(R.id.editText_email);
        changePassword = view.findViewById(R.id.change_password);
        deleteAccountButton = view.findViewById(R.id.delete_account_button);

        // Fetch user data from session
        SessionManager sessionManager = new SessionManager(requireContext());
        User user = sessionManager.getUser();

        if (user != null) {
            editTextName.setText(user.getName());
            editTextEmail.setText(user.getEmail());
        }

        // Set back button action
        backButton.setOnClickListener(v -> {
            mainActivity.showHeader(); // Restore header when returning to ProfileFragment
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Save button click action
        saveButton.setOnClickListener(v -> saveChanges());

        // Navigate to password change screen
        changePassword.setOnClickListener(v -> navigateToChangePassword());

        // Delete account button action
        deleteAccountButton.setOnClickListener(v -> showDeleteAccountWarning());

        return view;
    }

    private void saveChanges() {
        String newName = editTextName.getText().toString().trim();
        String newEmail = editTextEmail.getText().toString().trim();

        if (newName.isEmpty() || newEmail.isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        // Simulating a save operation
        Toast.makeText(getContext(), getString(R.string.changes_saved), Toast.LENGTH_SHORT).show();
    }

    private void navigateToChangePassword() {
        Fragment changePasswordFragment = new ChangePasswordFragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, changePasswordFragment)
                .addToBackStack(null)
                .commit();
    }

    private void showDeleteAccountWarning() {
        Toast.makeText(getContext(), getString(R.string.delete_account_message), Toast.LENGTH_LONG).show();
        // You can implement an alert dialog confirmation here.
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Ensure the header is only restored when returning to ProfileFragment, NOT ChangePasswordFragment
        if (requireActivity().getSupportFragmentManager().getBackStackEntryCount() == 0) {
            ((MainActivity) requireActivity()).showHeader();
        }
    }
}

