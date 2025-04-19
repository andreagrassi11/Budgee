package com.example.examproject.fragments.login;

import static com.example.examproject.util.Utils.openFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.examproject.R;

public class WelcomeFragment extends Fragment {
    private Button loginButton;
    private TextView signUpTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        loginButton = view.findViewById(R.id.firstButton);
        signUpTextView = view.findViewById(R.id.signUpTextView);

        /* Listener */
        loginButton.setOnClickListener(v -> openFragment(new LoginFragment(), getParentFragmentManager(), R.id.fragmentWelcome));
        signUpTextView.setOnClickListener(v -> openFragment(new SignupFragment(), getParentFragmentManager(), R.id.fragmentWelcome));

        return view;
    }
}