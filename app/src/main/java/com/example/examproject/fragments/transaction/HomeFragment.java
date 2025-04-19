package com.example.examproject.fragments.transaction;

import static com.example.examproject.fragments.transaction.Util.getUserTransactionsByMonth;
import static com.example.examproject.fragments.transaction.Util.setTransactions;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.examproject.R;
import com.example.examproject.adapter.TransactionAdapterNew;
import com.example.examproject.adapter.model.Transaction;
import com.example.examproject.database.DatabaseManagerTry;
import com.example.examproject.session.SessionManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    private Spinner spinner;
    private String userId;
    private RecyclerView recyclerView;
    private View rootView;
    private boolean isUserInteraction = false;
    private Calendar calendar;
    private DatabaseManagerTry dbManager;
    private SessionManager sessionManager;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sessionManager = new SessionManager(getContext());
        dbManager = new DatabaseManagerTry(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        /* Variable */
        calendar = Calendar.getInstance();
        userId = sessionManager.getUserId();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        recyclerView = rootView.findViewById(R.id.recyclerViewTransaction);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        spinner = rootView.findViewById(R.id.spinner_transaction_month_year);
        /* Recupero Transaction Utente mese corrente */
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        String currentDate = String.format("%02d-%d", currentMonth, currentYear);

        List<Transaction> transactions = getUserTransactionsByMonth(userId, currentDate, getActivity(), dbManager);

        /* Setto le Transaction */
        TransactionAdapterNew adapter = setTransactions(transactions, requireActivity());
        recyclerView.setAdapter(adapter);

        /* Spinner */
        List<String> dateList = new ArrayList<>();

        for (int year = 2025; year <= currentYear; year++) {
            int maxMonth = (year == currentYear) ? currentMonth : 12;
            for (int month = 1; month <= maxMonth; month++) {
                String formattedDate = String.format("%02d-%d", month, year);
                dateList.add(formattedDate);
            }
        }

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, dateList);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter3);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!isUserInteraction) {
                    isUserInteraction = true;
                    return;
                }

                String selectedItem = parent.getItemAtPosition(position).toString();
                List<Transaction> transactions = getUserTransactionsByMonth(userId, selectedItem, getActivity(), dbManager);

                /* Setto le Transaction */
                TransactionAdapterNew adapter = setTransactions(transactions, requireActivity());
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("");
            }
        });

    }

        @Override
    public void onStart() {
        super.onStart();


        /*int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        String currentDate = String.format("%02d-%d", currentMonth, currentYear);

        List<Transaction> transactions = getUserTransactionsByMonth(userId, currentDate, getActivity(), dbManager);

        /* Setto le Transaction */
       /* TransactionAdapterNew adapter = setTransactions(transactions, requireActivity());
        recyclerView.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!isUserInteraction) {
                    isUserInteraction = true;
                    return;
                }

                String selectedItem = parent.getItemAtPosition(position).toString();
                List<Transaction> transactions = getUserTransactionsByMonth(userId, selectedItem, getActivity(), dbManager);

                /* Setto le Transaction */
            /*    TransactionAdapterNew adapter = setTransactions(transactions, requireActivity());
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dbManager != null) dbManager.close();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

}