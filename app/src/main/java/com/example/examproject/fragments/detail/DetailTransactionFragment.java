package com.example.examproject.fragments.detail;

import static com.example.examproject.util.Utils.openFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.examproject.R;
import com.example.examproject.database.DatabaseManagerTry;
import com.example.examproject.fragments.transaction.HomeFragment;
import com.example.examproject.session.SessionManager;

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
    private DatabaseManagerTry dbManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_detail, container, false);
        SessionManager sessionManager = new SessionManager(getContext());
        dbManager = new DatabaseManagerTry(getContext());

        titleView = view.findViewById(R.id.editTextText11);
        dateView = view.findViewById(R.id.editTextText12);
        amountView = view.findViewById(R.id.editTextText13);
        methodView = view.findViewById(R.id.editTextText14);
        categoryView = view.findViewById(R.id.editTextText16);

        /* Recupero dati bundle */
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getInt("id");
            title = bundle.getString("title");
            date = bundle.getString("date");
            amount = bundle.getString("amount");
            method = bundle.getString("method");
            category = bundle.getString("category");

            if(amount.startsWith("-"))
                type = "Expense";
            else
                type = "Income";
        }

        titleView.setText(title);
        dateView.setText(date);
        amountView.setText(amount);
        methodView.setText(method);
        categoryView.setText(category);

        /* Recupera id utente */
        String userId = sessionManager.getUserId();

        Button update = view.findViewById(R.id.buttonUpdate);
        Button delete = view.findViewById(R.id.buttonDelete);

        /* Update */
        update.setOnClickListener(v -> {
            String value = titleView.getText().toString().trim();
            String date = dateView.getText().toString().trim();
            String amount = amountView.getText().toString().trim();
            String method = methodView.getText().toString().trim();
            String category = categoryView.getText().toString().trim();

            int IdCategory = dbManager.getCategoryDAO().getCategoryIdByName(userId, category);
            int IdMethod = dbManager.getMethodsDAO().getMethodIdByName(userId, method);

            if(value.isEmpty())
                Toast.makeText(getContext(), "Please enter a value!", Toast.LENGTH_SHORT).show();
            else {
                boolean res = false;

                if(type.equals("Income"))
                    res = dbManager.getIncomeDAO().updateUserIncome(String.valueOf(id), String.valueOf(IdCategory), String.valueOf(IdMethod), amount, value, date);

                if(type.equals("Expense"))
                    res = dbManager.getExpenseDAO().updateUserExpense(String.valueOf(id), String.valueOf(IdCategory), String.valueOf(IdMethod), amount, value, date);

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

        return view;
    }
}
