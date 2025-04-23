package com.example.examproject.fragments.detail;

import static com.example.examproject.util.Utils.openFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.examproject.R;
import com.example.examproject.database.DatabaseManagerTry;
import com.example.examproject.fragments.category.CategoryFragment;
import com.example.examproject.fragments.method.MethodsFragment;
import com.example.examproject.session.SessionManager;

public class DetailFragment extends Fragment {

    private EditText titleView;
    private int id;
    private String title;
    private String type;
    private String userId;
    private DatabaseManagerTry dbManager;
    private Button update;
    private Button delete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        SessionManager sessionManager = new SessionManager(getContext());
        dbManager = new DatabaseManagerTry(getContext());

        titleView = view.findViewById(R.id.editTextText11);

        /* Recupera id utente */
        userId = sessionManager.getUserId();
        update = view.findViewById(R.id.buttonUpdate);
        delete = view.findViewById(R.id.buttonDelete);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        /* Recupero dati bundle */
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getInt("id");
            title = bundle.getString("title");
            type = bundle.getString("type");
            titleView.setText(title);
        }

        /* Update */
        update.setOnClickListener(v -> {
            String value = titleView.getText().toString().trim();

            if(value.isEmpty())
                Toast.makeText(getContext(), "Please enter a value!", Toast.LENGTH_SHORT).show();
            else {
                boolean res = false;
                Fragment newFrag = null;

                if(type.equals("method")){
                    res = dbManager.getMethodsDAO().updateMethod(String.valueOf(id), value);
                    newFrag = new MethodsFragment();
                }

                if(type.equals("category")) {
                    res = dbManager.getCategoryDAO().updateCategory(String.valueOf(id), value);
                    newFrag = new CategoryFragment();
                }

                if (res)
                    openFragment(newFrag, getParentFragmentManager(), R.id.fragmentContainerView);
                else
                    Toast.makeText(getContext(), "Update failed!", Toast.LENGTH_SHORT).show();
            }
        });

        /* Delete */
        delete.setOnClickListener(v -> {
            long res = -1;
            Fragment newFrag = null;

            if(type.equals("method")){
                res = dbManager.getMethodsDAO().deleteMethod(id);
                newFrag = new MethodsFragment();
            }

            if(type.equals("category")) {
                res = dbManager.getCategoryDAO().deleteCategory(id);
                newFrag = new CategoryFragment();
            }

            if (res != -1)
                openFragment(newFrag, getParentFragmentManager(), R.id.fragmentContainerView);
            else
                Toast.makeText(getContext(), "Delete failed!", Toast.LENGTH_SHORT).show();
        });
    }
}
