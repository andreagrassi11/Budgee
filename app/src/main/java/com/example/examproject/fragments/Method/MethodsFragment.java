package com.example.examproject.fragments.Method;

import static com.example.examproject.util.Utils.openDetailFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.examproject.R;
import com.example.examproject.adapter.model.Transaction;
import com.example.examproject.adapter.TransactionAdapterNew;
import com.example.examproject.database.DatabaseManagerTry;
import com.example.examproject.fragments.Detail.DetailFragment;
import com.example.examproject.session.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MethodsFragment extends Fragment {

    private DatabaseManagerTry dbManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_methods, container, false);
        SessionManager sessionManager = new SessionManager(getContext());

        // Recupera dati utente
        String userId = sessionManager.getUserId();

        // Take Transaction
        dbManager = new DatabaseManagerTry(getContext());
        RecyclerView recyclerView = rootView.findViewById(R.id.methodsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Transaction> transactions = new ArrayList<>();

        /* Recupero Methods */
        List<Map<String, String>> methods = dbManager.getMethodsDAO().getUserMethods(userId);

        for (Map<String, String> method : methods) {
            transactions.add(new Transaction(Integer.parseInt(method.get("ID")), method.get("Name"), null, null, null, null));
        }

        /* Setto le Transaction */
        TransactionAdapterNew adapter = new TransactionAdapterNew(transactions, transaction -> {
            Bundle bundle = new Bundle();
            bundle.putInt("id", transaction.getId());
            bundle.putString("title", transaction.getTitle());
            bundle.putString("type", "method"); // Methods or Category
            openDetailFragment(new DetailFragment(), transaction, requireActivity(), bundle);
        });

        recyclerView.setAdapter(adapter);

        return rootView;
    }
}