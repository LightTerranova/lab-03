package com.example.listycitylab3;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {
    interface AddCityDialogListener { // Passes the input back to Main Activity
        void addCity(City city);
    }

    private AddCityDialogListener listener; // Need to save what activity is calling us
    private City cityToEdit;                // var for city to edit

    // from lab hints
    static AddCityFragment newInstance(City city) {
        Bundle args = new Bundle();
        args.putSerializable("city", city);

        AddCityFragment fragment = new AddCityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context); // We want on attach to do everything it did, but add more functionality below
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context; // We are casting the context to our interface now our listener is our interface
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener"); // Cant work with an activity that doesnt implement our interface
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view =
                LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        // Checking if arguments are passed before we get them
        // This was causing a crash
        Bundle args = getArguments();
        if (args != null) {
            cityToEdit = (City) args.getSerializable("city");
            if (cityToEdit != null) {
                editCityName.setText(cityToEdit.getName());
                editProvinceName.setText(cityToEdit.getProvince());
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder // functions return a reference to the builder
                .setView(view)
                .setTitle(cityToEdit == null ? "Add a city" : "Edit city") // using tertiary to set the right dialogue
                .setNegativeButton("Cancel", null)
                .setPositiveButton(cityToEdit == null ? "Add" : "Save", (dialog, which) -> { // same as above
                    String cityName = editCityName.getText().toString();
                    String provinceName = editProvinceName.getText().toString();
                    if (cityToEdit == null) {
                        // Adding
                        listener.addCity(new City(cityName, provinceName));
                    } else {
                        // Editing
                        cityToEdit.setName(cityName);
                        cityToEdit.setProvince(provinceName);
                        listener.addCity(cityToEdit); // reused from lab
                    }
                })
                .create();
    }
}