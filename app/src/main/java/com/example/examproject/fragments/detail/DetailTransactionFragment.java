package com.example.examproject.fragments.detail;

import static com.example.examproject.util.Utils.openFragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.examproject.R;
import com.example.examproject.database.DatabaseManagerTry;
import com.example.examproject.fragments.transaction.HomeFragment;
import com.example.examproject.session.SessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class DetailTransactionFragment extends Fragment {

    private EditText titleView;
    private EditText dateView;
    private EditText amountView;
    private EditText methodView;
    private EditText categoryView;
    private int id;
    private String amount;
    private String title;
    private String date;
    private String method;
    private String category;
    private String type;
    private String userId;
    private DatabaseManagerTry dbManager;
    private Button update;
    private Button delete;
    private Calendar calendar;
    private EditText etDate;
    private View view;
    private Spinner methodSpinner;
    private Spinner categorySpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_transaction_detail, container, false);
        SessionManager sessionManager = new SessionManager(getContext());
        dbManager = new DatabaseManagerTry(getContext());

        titleView = view.findViewById(R.id.editText11);
        amountView = view.findViewById(R.id.editTextText13);

        /* Recupera id utente */
        userId = sessionManager.getUserId();
        update = view.findViewById(R.id.buttonUpdate);
        delete = view.findViewById(R.id.buttonDelete);

        etDate = view.findViewById(R.id.etDate2);
        calendar = Calendar.getInstance();

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        etDate.setOnClickListener(v -> showDatePickerDialog());

        /* Recupero dati bundle */
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getInt("id");
            title = bundle.getString("title");
            date = bundle.getString("date");
            amount = bundle.getString("amount");
            method = bundle.getString("method");
            category = bundle.getString("category");

            if(amount.startsWith("-")) {
                type = "Expense";
            }else {
                type = "Income";
            }
        }

        titleView.setText(title);
        etDate.setText(date);
        amountView.setText(amount);

        /* Info to Fragment Home */
        Bundle result = new Bundle();
        result.putString("date", date);
        getParentFragmentManager().setFragmentResult("myKey", result);

        /* Update */
        update.setOnClickListener(v -> {
            String value = titleView.getText().toString().trim();
            String date = etDate.getText().toString().trim();
            String amount = amountView.getText().toString().trim();
            String category = categorySpinner.getSelectedItem().toString();
            int idCategory = dbManager.getCategoryDAO().getCategoryIdByName(userId, category);

            String method = methodSpinner.getSelectedItem().toString();
            int idMethod = dbManager.getMethodsDAO().getMethodIdByName(userId, method);

            if(value.isEmpty())
                Toast.makeText(getContext(), "Please enter a value!", Toast.LENGTH_SHORT).show();
            else {
                boolean res = false;

                if(type.equals("Income"))
                    res = dbManager.getIncomeDAO().updateUserIncome(String.valueOf(id), String.valueOf(idCategory), String.valueOf(idMethod), amount, value, date);

                if(type.equals("Expense"))
                    res = dbManager.getExpenseDAO().updateUserExpense(String.valueOf(id), String.valueOf(idCategory), String.valueOf(idMethod), "-"+amount, value, date);

                if (res)
                    openFragment(new HomeFragment(), getParentFragmentManager(), R.id.fragmentContainerView);
                else
                    Toast.makeText(getContext(), "Update failed!", Toast.LENGTH_SHORT).show();
            }
        });

        /* Delete */
        delete.setOnClickListener(v -> {
            long res = -1;

            if(type.equals("Income"))
                res = dbManager.getIncomeDAO().deleteIncome(id);

            if(type.equals("Expense"))
                res = dbManager.getExpenseDAO().deleteExpense(id);

            if (res != -1)
                openFragment(new HomeFragment(), getParentFragmentManager(), R.id.fragmentContainerView);
            else
                Toast.makeText(getContext(), "Delete failed!", Toast.LENGTH_SHORT).show();
        });
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
        categorySpinner = view.findViewById(R.id.spinner);
        categorySpinner.setAdapter(adapter);

        int selectedCategory = categoryNames.indexOf(category);

        if (selectedCategory != -1) {
            categorySpinner.setSelection(selectedCategory);
        }

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
        methodSpinner = view.findViewById(R.id.spinner4);
        methodSpinner.setAdapter(adapterMethod);

        int selectedMethod = methodName.indexOf(method);

        if (selectedMethod != -1) {
            methodSpinner.setSelection(selectedMethod);
        }
    }

}
