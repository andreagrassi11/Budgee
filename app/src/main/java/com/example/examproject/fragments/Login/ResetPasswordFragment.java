package com.example.examproject.fragments.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.examproject.R;
import com.example.examproject.fragments.Login.Feedback.FeedbackFragment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordFragment extends Fragment {

    private EditText emailEditText;
    private Button resetButton;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        resetButton = view.findViewById(R.id.button4);

        /* Listener */
        resetButton.setOnClickListener(v -> resetPassword(view));

        return view;
    }

    private void resetPassword(View view) {

        emailEditText = view.findViewById(R.id.editTextText5);
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailEditText.setError("Enter your email");
            emailEditText.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter a valid email");
            emailEditText.requestFocus();
            return;
        }

        /* Inizialized Firebase*/
        FirebaseApp.initializeApp(getContext());
        mAuth = FirebaseAuth.getInstance();

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    Bundle bundle = new Bundle();

                    if (task.isSuccessful())
                        bundle.putString("From", "Reset");
                    else
                        Toast.makeText(getContext(), "Please enter a valid Email", Toast.LENGTH_SHORT).show();

                    FeedbackFragment feedback = new FeedbackFragment();
                    feedback.setArguments(bundle);

                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragmentWelcome, feedback)
                            .addToBackStack(null)
                            .commit();
                });
    }
}