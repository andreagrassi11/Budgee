package com.example.examproject.fragments.insert;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.examproject.R;
import com.example.examproject.database.DatabaseManagerTry;
import com.example.examproject.fragments.transaction.HomeFragment;
import com.example.examproject.session.SessionManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class InsertAIFragment extends Fragment {

    private DatabaseManagerTry dbManager;
    private View view;
    private EditText etDate;
    private EditText editTextValue;
    private EditText editTextName;
    private EditText editTextDate;
    private Calendar calendar;
    private String name;
    private String date;
    private String total;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_insert_ai, container, false);

        etDate = view.findViewById(R.id.etDate);
        calendar = Calendar.getInstance(); // Get current date

        etDate.setOnClickListener(v -> showDatePickerDialog());

        return view;
    }

    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format selected date as "YYYY-MM-DD"
                    String selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    etDate.setText(selectedDate); // Display in EditText
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        dbManager = new DatabaseManagerTry(getContext());
        SharedPreferences prefs = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        String lang = prefs.getString("lang", "en"); // en default language

        // Recupera dati utente
        SessionManager sessionManager = new SessionManager(getContext());
        String userId = sessionManager.getUserId();


        /* Recupero Categorie Utente */
        List<Map<String, String>> categories = dbManager.getCategoryDAO().getUserCategories(userId);
        List<String> categoryNames = new ArrayList<>();

        if(lang.equals("en"))
            categoryNames.add("-- Select Category --");

        if(lang.equals("it"))
            categoryNames.add("-- Seleziona Categoria --");

        // Extract category names from the result
        for (Map<String, String> category : categories) {
            categoryNames.add(category.get("Name"));
        }

        // Set up the adapter to populate the Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner categorySpinner = view.findViewById(R.id.spinner2);
        categorySpinner.setAdapter(adapter);

        /* Recupero Metodi Utente */
        List<Map<String, String>> methods = dbManager.getMethodsDAO().getUserMethods(userId);
        List<String> methodName = new ArrayList<>();

        if(lang.equals("en"))
            methodName.add("-- Select Methods --");

        if(lang.equals("it"))
            methodName.add("-- Seleziona Metodo --");

        // Extract category names from the result
        for (Map<String, String> method : methods) {
            methodName.add(method.get("Name"));
        }

        // Set up the adapter to populate the Spinner
        ArrayAdapter<String> adapterMethod = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, methodName);
        adapterMethod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner methodSpinner = view.findViewById(R.id.spinner3);
        methodSpinner.setAdapter(adapterMethod);

        editTextValue = view.findViewById(R.id.editTextText);
        editTextName = view.findViewById(R.id.editTextText4);
        editTextDate = view.findViewById(R.id.etDate);

        SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getRecognizedTextList().observe(getViewLifecycleOwner(), recognizedTextList -> {
            if (recognizedTextList != null) {
                name = recognizedTextList.get(0);
                date = recognizedTextList.get(1);
                total = recognizedTextList.get(2);

                editTextValue.setText(total);
                editTextName.setText(name);
                editTextDate.setText(convertDateFormat(date));

            } else {
                Log.e("FragmentB", "Recognized text list is null");
            }
        });


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbManager = new DatabaseManagerTry(getContext());
        SessionManager sessionManager = new SessionManager(getContext());

        // Recupera dati utente
        String userId = sessionManager.getUserId();

        Spinner spinnerType = view.findViewById(R.id.spinnerType);
        Spinner spinnerCategory = view.findViewById(R.id.spinner2);
        Spinner spinnerMethods = view.findViewById(R.id.spinner3);
        editTextValue = view.findViewById(R.id.editTextText);
        editTextName = view.findViewById(R.id.editTextText4);
        Button buttonInsert = view.findViewById(R.id.button);


        buttonInsert.setOnClickListener(v -> {
            String type = spinnerType.getSelectedItem().toString();
            String category = spinnerCategory.getSelectedItem().toString();
            int idCategory = dbManager.getCategoryDAO().getCategoryIdByName(userId, category);

            String method = spinnerMethods.getSelectedItem().toString();
            int idMethod = dbManager.getMethodsDAO().getMethodIdByName(userId, method);

            String value = editTextValue.getText().toString().trim();
            String name = editTextName.getText().toString().trim();
            String date = etDate.getText().toString();

            if(value.isEmpty() || type.isEmpty() || category.isEmpty() || method.isEmpty() || name.isEmpty() || date.isEmpty())
                Toast.makeText(getContext(), "Please enter value!", Toast.LENGTH_SHORT).show();
            else {
                long res = 0;

                if(type.equals("Income"))
                    res = dbManager.getIncomeDAO().insertIncome(userId, idCategory, idMethod, Double.parseDouble(value), name, date);

                if(type.equals("Expense"))
                    res = dbManager.getExpenseDAO().insertExpense(userId, idCategory, idMethod, Double.parseDouble(value), name, date);

                if (res != -1) {
                    Toast.makeText(getContext(), "Inserted successfully!", Toast.LENGTH_SHORT).show();
                    HomeFragment fragment = new HomeFragment();
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainerView, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else
                    Toast.makeText(getContext(), "Insert failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static String convertDateFormat(String inputDate) {
        String[] possibleFormats = {"dd-MM-yyyy", "dd/MM/yyyy"};
        String outputFormat = "yyyy-MM-dd";

        for (String format : possibleFormats) {
            try {
                SimpleDateFormat inputFormatter = new SimpleDateFormat(format);
                SimpleDateFormat outputFormatter = new SimpleDateFormat(outputFormat);

                Date date = inputFormatter.parse(inputDate);
                return outputFormatter.format(date);
            } catch (ParseException ignored) {
            }
        }

        return "Invalid Date Format";
    }
}