package com.example.examproject.fragments.Profile;

import static androidx.core.app.ActivityCompat.recreate;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.res.Configuration;
import java.util.Locale;
import com.example.examproject.R;
import androidx.annotation.Nullable;

public class LanguageFragment extends Fragment {

    private Spinner languageSpinner;
    private TextView languageText;
    private String[] languages = {"English", "Italiano"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_language, container, false);

        languageSpinner = view.findViewById(R.id.language_spinner);
        languageText = view.findViewById(R.id.language_text);
        Button button = view.findViewById(R.id.button6);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        button.setOnClickListener(v -> {
            String languageCode = languageSpinner.getSelectedItem().toString();

            switch (languageCode) {
                case "English": setLocale("en"); break;
                case "Italiano": setLocale("it"); break;
            }
        });

        return view;
    }

    private void setLocale(String langCode) {
        requireActivity()
                .getSharedPreferences("settings", Context.MODE_PRIVATE)
                .edit()
                .putString("lang", langCode)
                .apply();

        // Update locale
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        requireActivity().getResources().updateConfiguration(config, requireActivity().getResources().getDisplayMetrics());

        // Recreate the activity to apply changes
        requireActivity().recreate();
    }

}