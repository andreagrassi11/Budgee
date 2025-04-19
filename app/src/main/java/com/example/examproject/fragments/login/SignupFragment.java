package com.example.examproject.fragments.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.examproject.R;
import com.example.examproject.database.DatabaseManagerTry;
import com.example.examproject.fragments.login.Feedback.FeedbackFragment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupFragment extends Fragment {

    private FirebaseAuth mAuth;
    private EditText nameText;
    private EditText surnameText;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private Button buttonSignup;
    private DatabaseManagerTry dbManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        /* Inizialized Firebase*/
        FirebaseApp.initializeApp(getContext());
        mAuth = FirebaseAuth.getInstance();

        // Find Views
        nameText = view.findViewById(R.id.editTextText9);
        surnameText = view.findViewById(R.id.editTextText10);
        editTextEmail = view.findViewById(R.id.editTextText6);
        editTextPassword = view.findViewById(R.id.editTextText7);
        editTextConfirmPassword = view.findViewById(R.id.editTextText8);
        buttonSignup = view.findViewById(R.id.button5);

        /* Listener */
        buttonSignup.setOnClickListener(v -> registerUser());

        return view;
    }

    private void registerUser() {
        String name = nameText.getText().toString().trim();
        String surname = surnameText.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (name.isEmpty() || surname.isEmpty()) {
            nameText.setError("Enter name");
            surnameText.setError("Enter surname");
            nameText.requestFocus();
            surnameText.requestFocus();
            return;
        }

        // Validate Email
        if (email.isEmpty()) {
            editTextEmail.setError("Enter email");
            editTextEmail.requestFocus();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        // Validate Password
        if (password.isEmpty() || confirmPassword.isEmpty()) {
            editTextPassword.setError("Enter password");
            editTextConfirmPassword.setError("Enter password");
            editTextPassword.requestFocus();
            editTextConfirmPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Password must be at least 6 characters");
            editTextConfirmPassword.setError("Password must be at least 6 characters");
            editTextPassword.requestFocus();
            editTextConfirmPassword.requestFocus();
            return;
        }

        if(!password.equals(confirmPassword)) {
            editTextPassword.setError("Passwords do not match");
            editTextConfirmPassword.setError("Passwords do not match");
            editTextPassword.requestFocus();
            editTextConfirmPassword.requestFocus();
            return;
        }

        dbManager = new DatabaseManagerTry(getContext());

        // Register User with Firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();
                            long res = dbManager.getUserDAO().insertUser(userId, name, surname, email);

                            if (res != -1) {
                                Bundle bundle = new Bundle();
                                bundle.putString("From", "Signup");

                                FeedbackFragment feedback = new FeedbackFragment();
                                feedback.setArguments(bundle);

                                getParentFragmentManager().beginTransaction()
                                        .replace(R.id.fragmentWelcome, feedback)
                                        .addToBackStack(null)
                                        .commit();
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}