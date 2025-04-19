package com.example.examproject.fragments.category;

import static com.example.examproject.util.Utils.openDetailFragment;

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
import com.example.examproject.R;
import com.example.examproject.adapter.TransactionAdapterNew;
import com.example.examproject.adapter.model.Transaction;
import com.example.examproject.database.DatabaseManagerTry;
import com.example.examproject.fragments.detail.DetailFragment;
import com.example.examproject.session.SessionManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryFragment extends Fragment {

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
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String userId = sessionManager.getUserId();

        // Take Transactio
        RecyclerView recyclerViewCategory = view.findViewById(R.id.recyclerViewCategory);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Transaction> transactions = new ArrayList<>();

        /* Recupero Methods */
        List<Map<String, String>> methods = dbManager.getCategoryDAO().getUserCategories(userId);

        for (Map<String, String> method : methods) {
            transactions.add(new Transaction(Integer.parseInt(method.get("ID")), method.get("Name"), null, null, null, null));
        }

        /* Setto le Transaction */
        TransactionAdapterNew adapter = new TransactionAdapterNew(transactions, transaction -> {
            Bundle bundle = new Bundle();
            bundle.putInt("id", transaction.getId());
            bundle.putString("title", transaction.getTitle());
            bundle.putString("type", "category"); // Methods or Category
            openDetailFragment(new DetailFragment(), transaction, requireActivity(), bundle);
        });

        recyclerViewCategory.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dbManager != null) dbManager.close();
    }
}