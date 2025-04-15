package com.example.examproject.fragments.Profile;

import static com.example.examproject.util.Utils.openFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.examproject.LoginActivity;
import com.example.examproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Get references to the FrameLayouts
        FrameLayout languageFrame = view.findViewById(R.id.Transaction);
        FrameLayout currencyFrame = view.findViewById(R.id.Category);
        FrameLayout logoutFrame = view.findViewById(R.id.Ai);

        // Set click listeners to open the respective fragments
        languageFrame.setOnClickListener(v -> openFragment(new LanguageFragment(), getParentFragmentManager(), R.id.fragmentContainerView));
        currencyFrame.setOnClickListener(v -> openFragment(new CurrencyFragment(), getParentFragmentManager(), R.id.fragmentContainerView));
        logoutFrame.setOnClickListener(v -> {
            // Do logout
            FirebaseAuth.getInstance().signOut();

            // Check the logout
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            Intent loginIntent = new Intent(getActivity() , LoginActivity.class);

            if (currentUser == null) {
                // Redirect to LoginActivity if not signed in
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loginIntent);
                getActivity().finish();
            }
        });

        return view;

    }
}