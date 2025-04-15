package com.example.examproject.fragments.Transaction;

import static com.example.examproject.fragments.Transaction.Util.getUserTransactionsByMonth;
import static com.example.examproject.fragments.Transaction.Util.setTransactions;
import static com.example.examproject.util.Utils.openDetailFragment;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examproject.R;
import com.example.examproject.adapter.TransactionAdapterNew;
import com.example.examproject.adapter.model.Transaction;
import com.example.examproject.TransactionAdapter;
import com.example.examproject.database.DAO.ExpenseDAO;
import com.example.examproject.database.DAO.IncomeDAO;
import com.example.examproject.database.DatabaseManagerTry;
import com.example.examproject.fragments.Detail.DetailFragment;
import com.example.examproject.fragments.Detail.DetailTransactionFragment;
import com.example.examproject.session.SessionManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private DatabaseManagerTry dbManagerNew;
    Spinner spinner;
    String userId;
    RecyclerView recyclerView;
    View rootView;
    private boolean isUserInteraction = false;
    Calendar calendar;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        SessionManager sessionManager = new SessionManager(getContext());
        calendar = Calendar.getInstance();

        // Recupera dati utente
        userId = sessionManager.getUserId();

        // Take Transaction
        dbManagerNew = new DatabaseManagerTry(getContext());
        recyclerView = rootView.findViewById(R.id.recyclerViewTransaction);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        /* Recupero Transaction Utente mese corrente */
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        String currentDate = String.format("%02d-%d", currentMonth, currentYear);

        List<Transaction> transactions = getUserTransactionsByMonth(userId, currentDate, getActivity(), dbManagerNew);

        /* Setto le Transaction */
        TransactionAdapterNew adapter = setTransactions(transactions, requireActivity());
        recyclerView.setAdapter(adapter);

        // Spinner
        List<String> dateList = new ArrayList<>();

        for (int year = 2025; year <= currentYear; year++) {
            int maxMonth = (year == currentYear) ? currentMonth : 12;
            for (int month = 1; month <= maxMonth; month++) {
                String formattedDate = String.format("%02d-%d", month, year);
                dateList.add(formattedDate);
            }
        }

        spinner = rootView.findViewById(R.id.spinner_transaction_month_year);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, dateList);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter3);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        String currentDate = String.format("%02d-%d", currentMonth, currentYear);

        List<Transaction> transactions = getUserTransactionsByMonth(userId, currentDate, getActivity(), dbManagerNew);

        /* Setto le Transaction */
        TransactionAdapterNew adapter = setTransactions(transactions, requireActivity());
        recyclerView.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!isUserInteraction) {
                    isUserInteraction = true;
                    return;
                }

                String selectedItem = parent.getItemAtPosition(position).toString();
                List<Transaction> transactions = getUserTransactionsByMonth(userId, selectedItem, getActivity(), dbManagerNew);

                /* Setto le Transaction */
                TransactionAdapterNew adapter = setTransactions(transactions, requireActivity());
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        String currentDate = String.format("%02d-%d", currentMonth, currentYear);

        List<Transaction> transactions = getUserTransactionsByMonth(userId, currentDate, getActivity(), dbManagerNew);

        /* Setto le Transaction */
        TransactionAdapterNew adapter = setTransactions(transactions, requireActivity());
        recyclerView.setAdapter(adapter);
    }

}