package com.example.examproject.fragments.login;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.examproject.MainActivity;
import com.example.examproject.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.example.examproject.session.SessionManager;

import static com.example.examproject.util.Utils.openActivity;
import static com.example.examproject.util.Utils.openFragment;

public class LoginFragment extends Fragment {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private TextView title;
    private FirebaseAuth mAuth;
    private Button loginButton;
    private TextView resetPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        /* Inizialized Firebase*/
        FirebaseApp.initializeApp(getContext());
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        title = view.findViewById(R.id.textView4);
        usernameEditText = view.findViewById(R.id.editText);
        passwordEditText = view.findViewById(R.id.editTextTextPassword2);
        loginButton = view.findViewById(R.id.loginButton);
        resetPassword = view.findViewById(R.id.resetPassword);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginButton.setOnClickListener(v -> loginUser());
        resetPassword.setOnClickListener(v ->
                openFragment(new ResetPasswordFragment(), getParentFragmentManager(), R.id.fragmentWelcome)
        );
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            sendToMain(currentUser);
        }
    }

    private void loginUser() {
        String email = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            usernameEditText.setError("Enter a valid email");
            usernameEditText.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Enter a valid password");
            passwordEditText.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user != null) {
                                sendToMain(user);
                            }
                        } else {
                            Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            usernameEditText.setText("");
                            passwordEditText.setText("");
                        }
                    }
                });
    }

    private void sendToMain(FirebaseUser user) {
        String userId = user.getUid();
        String userEmail = user.getEmail();
        String userName = user.getDisplayName();

        SessionManager sessionManager = new SessionManager(getContext());
        sessionManager.saveUserSession(userId, userEmail, userName);

        openActivity(requireActivity(), MainActivity.class);
    }
}