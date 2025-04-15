package com.example.examproject.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Salvare i dati dell'utente dopo il login
    public void saveUserSession(String userId, String name, String email) {
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    // Ottenere l'ID utente
    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    // Ottenere il nome utente
    public String getUserName() {
        return sharedPreferences.getString(KEY_NAME, null);
    }

    // Ottenere l'email utente
    public String getUserEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }

    // Eliminare la sessione (Logout)
    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}
