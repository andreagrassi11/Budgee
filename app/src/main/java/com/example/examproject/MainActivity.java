package com.example.examproject;

import static android.content.ContentValues.TAG;
import static com.example.examproject.util.Utils.openFragment;
import static com.example.examproject.util.Utils.setLocale;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.examproject.database.DatabaseManagerTry;
import com.example.examproject.fragments.category.CategoryFragment;
import com.example.examproject.fragments.insert.InsertReceiptFragment;
import com.example.examproject.fragments.transaction.HomeFragment;
import com.example.examproject.fragments.insert.InsertFragment;
import com.example.examproject.fragments.profile.ProfileFragment;
import com.example.examproject.fragments.method.MethodsFragment;
import com.example.examproject.session.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private TextView greetingText;
    private FloatingActionButton insertButton;
    private DatabaseManagerTry dbManager;
    private FrameLayout fr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_template);

        /* Language Preferences*/
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        String lang = prefs.getString("lang", "en"); // en default language
        setLocale(this, lang);

        /* Widget*/
        boolean openFragment = getIntent().getBooleanExtra("open_fragment", false);
        Log.d("MainActivity", "open_fragment: " + openFragment);
        if (openFragment) {
            openFragment(new InsertReceiptFragment(), getSupportFragmentManager(), R.id.fragmentContainerView);
        }

        /* Lock screen orientation */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /* Definizione elementi UI */
        greetingText = findViewById(R.id.greetingText);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        insertButton = findViewById(R.id.insertButton);
        fr = findViewById(R.id.incomeExpenses);

        /* DB Init */
        dbManager = new DatabaseManagerTry(this);

        /* Recupero info utente */
        SessionManager sessionManager = new SessionManager(this);
        String userId = sessionManager.getUserId();
        Map<String, String> info = dbManager.getUserDAO().getUserInfo(userId);
        String txt = getString(R.string.home_hello) + " " + info.get("name") + "!";
        greetingText.setText(txt);

        /* Gestione Fragment */
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.setVisibility(View.GONE);
                openFragment(new InsertFragment(), getSupportFragmentManager(), R.id.fragmentContainerView);
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                /* Select the fragment */
                if(item.getItemId() == R.id.home) {
                    selectedFragment = new HomeFragment();
                    fr.setVisibility(View.VISIBLE);
                }

                if(item.getItemId() == R.id.stats) {
                    selectedFragment = new MethodsFragment();
                    fr.setVisibility(View.GONE);
                }
                if(item.getItemId() == R.id.category) {
                    selectedFragment = new CategoryFragment();
                    fr.setVisibility(View.GONE);
                }

                if(item.getItemId() == R.id.profile) {
                    selectedFragment = new ProfileFragment();
                    fr.setVisibility(View.GONE);
                }

                if (selectedFragment != null) {
                    openFragment(selectedFragment, getSupportFragmentManager(), R.id.fragmentContainerView);
                }

                return true;
            }
        });

        /*if (savedInstanceState == null) {
            openFragment(new HomeFragment(), getSupportFragmentManager(), R.id.fragmentContainerView);
        }*/
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if the user is signed in
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            // Redirect to LoginActivity if not signed in
            Intent loginIntent = new Intent(this, LoginActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(loginIntent);
            finish();
        }

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);

        if (currentFragment.getClass().getSimpleName().equals("HomeFragment")) {
            fr.setVisibility(View.VISIBLE);
        } else {
            fr.setVisibility(View.GONE);
        }
    }
}