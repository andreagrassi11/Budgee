package com.example.examproject.fragments.Profile;

import static com.example.examproject.util.Utils.openFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    private FrameLayout languageFrame, logoutFrame;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        languageFrame = view.findViewById(R.id.Transaction);
        logoutFrame = view.findViewById(R.id.Ai);

        languageFrame.setOnClickListener(v -> openFragment(new LanguageFragment(), getParentFragmentManager(), R.id.fragmentContainerView));
        logoutFrame.setOnClickListener(v -> { doLogout(); });
    }

    private void doLogout() {

        FirebaseAuth.getInstance().signOut();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent loginIntent = new Intent(getActivity() , LoginActivity.class);

        if (currentUser == null) {
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(loginIntent);
            getActivity().finish();
        }
    }
}