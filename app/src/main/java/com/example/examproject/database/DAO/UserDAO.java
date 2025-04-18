package com.example.examproject.database.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.examproject.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDAO {
    private final DatabaseHelper dbHelper;

    public UserDAO(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * Insert User
     * @param id
     * @param name
     * @param surname
     * @param email
     * @return long
     */
    public long insertUser(String id, String name, String surname, String email) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id_firebase", id);
        values.put("name", name);
        values.put("surname", surname);
        values.put("email", email);

        long result = db.insert("User", null, values);
        db.close();
        return result;
    }

    /**
     * User Info
     * @param userId
     * @return Map<String, String>
     */
    public Map<String, String> getUserInfo(String userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String id = "", name ="", surname = "", email = "";
        Map<String, String> item = new HashMap<>();

        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE id_firebase = ?",
                new String[]{userId});

        if (cursor.moveToFirst()) {
            item.put("id", String.valueOf(cursor.getString(1)));
            item.put("name", String.valueOf(cursor.getString(2)));
            item.put("surname", String.valueOf(cursor.getString(3)));
            item.put("email", String.valueOf(cursor.getString(4)));
        }

        cursor.close();
        db.close();
        return item;
    }

    /**
     * Get all users
     * @return List<String>
     */
    public List<String> getAllUsers() {
        List<String> users = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM User", null);

        if (cursor.moveToFirst()) {
            do {
                String user = cursor.getString(1) + " " + cursor.getString(2) + " - " + cursor.getString(3);
                users.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return users;
    }

    public void close() {
        dbHelper.close();
    }
}
