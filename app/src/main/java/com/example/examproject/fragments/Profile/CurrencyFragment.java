package com.example.examproject.fragments.Profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.examproject.R;


public class CurrencyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_currency, container, false);
        ImageView imageView = view.findViewById(R.id.back_profile_fragment);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();

                // Check if there are any fragments in the back stack
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack(); // Pop the back stack to return to the previous fragment
                } else {
                    Toast.makeText(getContext(), "No fragments in back stack!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}