package com.example.app_streamix.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.app_streamix.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FilterFragment extends BottomSheetDialogFragment {

    public interface FilterListener {
        void onFiltersApplied(String selectedCategory);
    }

    private FilterListener filterListener;
    private Spinner spinnerCategories;
    private String selectedCategory = "All"; // Default category

    public void setFilterListener(FilterListener listener) {
        this.filterListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        spinnerCategories = view.findViewById(R.id.spinner_categories);
        Button applyFiltersButton = view.findViewById(R.id.btn_apply_filters);
        Button removeFiltersButton = view.findViewById(R.id.btn_remove_filters);

        // Set up the spinner with category options
        String[] categories = {"All", "Ação", "Drama", "Comédia"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_selected_item, categories);
        adapter.setDropDownViewResource(R.layout.spinner_item); // Apply custom layout for dropdown items
        spinnerCategories.setAdapter(adapter);


        // Ensure spinner selection is detected
        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
                Toast.makeText(requireContext(), "Selected: " + selectedCategory, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = "All"; // Default to "All" if nothing is selected
            }
        });

        // Apply Filters
        applyFiltersButton.setOnClickListener(v -> {
            if (filterListener != null) {
                filterListener.onFiltersApplied(selectedCategory);
            }
            dismiss();
        });

        // Remove Filters
        removeFiltersButton.setOnClickListener(v -> {
            selectedCategory = "All"; // Reset to default
            spinnerCategories.setSelection(0); // Reset spinner to "All"
            dismiss();
        });

        return view;
    }
}
