package com.example.examproject;

import static com.example.examproject.util.Utils.setLocale;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.examproject.fragments.Category.CategoryFragment;
import com.example.examproject.fragments.Transaction.HomeFragment;
import com.example.examproject.fragments.Insert.InsertFragment;
import com.example.examproject.fragments.Profile.ProfileFragment;
import com.example.examproject.fragments.Method.MethodsFragment;
import com.example.examproject.session.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private TextView greetingText;
    FloatingActionButton insertButton;
    private DatabaseManagerTry dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        String lang = prefs.getString("lang", "en"); // en default language
        setLocale(this, lang);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_template);

        /* Definizione elementi UI */
        greetingText = findViewById(R.id.greetingText);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        insertButton = findViewById(R.id.insertButton);
        FrameLayout fr = findViewById(R.id.incomeExpenses);

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
                // Replace fragment_container with the new fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, new InsertFragment())
                        .addToBackStack(null)
                        .commit();
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


                // Replace the current fragment
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView, selectedFragment)
                            .commit();
                }

                return true;
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, new HomeFragment())
                    .commit();
        }
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

        String uid = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        /* Recupero info utente */
        db.collection("User").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("Name");
                        String email = documentSnapshot.getString("Email");
                        // Use the retrieved data as needed
                        Log.d("Firestore", "User data: Name = " + name + ", Email = " + email);
                    } else {
                        Log.d("Firestore", "No such document");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error getting document", e);
                });
    }
}