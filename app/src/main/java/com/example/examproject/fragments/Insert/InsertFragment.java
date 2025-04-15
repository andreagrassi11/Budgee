package com.example.examproject.fragments.Insert;

import static com.example.examproject.util.Utils.openFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;

import com.example.examproject.R;

public class InsertFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_insert, container, false);

        // Get references to the FrameLayouts
        FrameLayout transactionFrame = view.findViewById(R.id.Transaction);
        FrameLayout categoryFrame = view.findViewById(R.id.Category);
        FrameLayout methodFrame = view.findViewById(R.id.Methods);
        FrameLayout aiFrame = view.findViewById(R.id.Ai);

        // Set click listeners to open the respective fragments
        transactionFrame.setOnClickListener(v -> openFragment(new InsertTransactionFragment(), getParentFragmentManager(), R.id.fragmentContainerView));
        categoryFrame.setOnClickListener(v -> openFragment(new InsertCategoryFragment(), getParentFragmentManager(), R.id.fragmentContainerView));
        methodFrame.setOnClickListener(v -> openFragment(new InsertMethodsFragment(), getParentFragmentManager(), R.id.fragmentContainerView));
        aiFrame.setOnClickListener(v -> openFragment(new InsertReceiptFragment(), getParentFragmentManager(), R.id.fragmentContainerView));

        return view;
    }
}