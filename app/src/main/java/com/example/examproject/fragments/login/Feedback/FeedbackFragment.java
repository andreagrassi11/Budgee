package com.example.examproject.fragments.login.Feedback;

import static com.example.examproject.util.Utils.openFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.examproject.R;
import com.example.examproject.fragments.login.LoginFragment;

public class FeedbackFragment extends Fragment {

    private TextView tx;
    private TextView login;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        tx = view.findViewById(R.id.feedback);
        login = view.findViewById(R.id.textView26);
        Bundle bundle = getArguments();

        if (bundle != null) {
            String data = bundle.getString("From");
            if (data.equals("Reset"))
                tx.setText("A password reset link has been sent to your email. Please check your inbox (and spam folder) to reset your password.");

            if (data.equals("Signup")) {
                tx.setText("Congratulations! Your account has been created successfully.");
                login.setVisibility(View.VISIBLE);
            }
        }

        /* Listener */
        login.setOnClickListener(v -> openFragment(new LoginFragment(), getParentFragmentManager(), R.id.fragmentWelcome));

        return view;
    }
}