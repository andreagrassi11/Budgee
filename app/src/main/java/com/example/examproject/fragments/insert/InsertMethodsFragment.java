package com.example.examproject.fragments.insert;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.examproject.R;
import com.example.examproject.database.DatabaseManagerTry;
import com.example.examproject.fragments.Method.MethodsFragment;
import com.example.examproject.session.SessionManager;


public class InsertMethodsFragment extends Fragment {
    private DatabaseManagerTry dbManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_insert_method, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbManager = new DatabaseManagerTry(getContext());
        SessionManager sessionManager = new SessionManager(getContext());

        // Recupera dati utente
        String userId = sessionManager.getUserId();

        EditText editTextValue = view.findViewById(R.id.editTextText3);
        Button buttonInsert = view.findViewById(R.id.button3);

        buttonInsert.setOnClickListener(v -> {
            String value = editTextValue.getText().toString().trim();

            if(value.isEmpty())
                Toast.makeText(getContext(), "Please enter a value!", Toast.LENGTH_SHORT).show();
            else {
                long res = dbManager.getMethodsDAO().insertMethod(userId, value);

                if (res != -1) {
                    Toast.makeText(getContext(), "Inserted successfully!", Toast.LENGTH_SHORT).show();
                    MethodsFragment fragment = new MethodsFragment();
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainerView, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else
                    Toast.makeText(getContext(), "Insert failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}