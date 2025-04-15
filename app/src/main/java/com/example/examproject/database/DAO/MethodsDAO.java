package com.example.examproject.database.DAO;

import android.content.Context;
import com.example.examproject.database.DatabaseHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.examproject.database.DatabaseHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodsDAO {
    private final DatabaseHelper dbHelper;

    public MethodsDAO(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public long insertMethod(String userId, String methodName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("name", methodName);

        long result = db.insert("Method", null, values);
        db.close();
        return result;
    }

    // Get Methods for a specific user
    public List<Map<String, String>> getUserMethods(String userId) {
        List<Map<String, String>> methodList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Method WHERE user_id = ?", new String[]{userId});

        if (cursor.moveToFirst()) {
            do {
                Map<String, String> item = new HashMap<>();
                item.put("ID", String.valueOf(cursor.getInt(0))); // Method ID
                item.put("Name", cursor.getString(2)); // Method Name
                methodList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return methodList;
    }

    public int getMethodIdByName(String userId, String methodName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int methodId = -1; // Default value if not found

        Cursor cursor = db.rawQuery("SELECT method_id FROM Method WHERE user_id = ? AND name = ?",
                new String[]{userId, methodName});

        if (cursor.moveToFirst()) {
            methodId = cursor.getInt(0); // Get the ID from the first column
        }

        cursor.close();
        db.close();
        return methodId;
    }

    public String getMethodNameById(String userId, int methodId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String methodName = ""; // Default value if not found

        Cursor cursor = db.rawQuery("SELECT name FROM Method WHERE user_id = ? AND method_id = ?",
                new String[]{userId, String.valueOf(methodId)});

        if (cursor.moveToFirst()) {
            methodName = cursor.getString(0); // Get the ID from the first column
        }

        cursor.close();
        db.close();
        return methodName;
    }


    // Update an existing Method
    public boolean updateMethod(String methodId, String methodName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", methodName);

        int rowsAffected = db.update("Method", values, "method_id = ?", new String[]{methodId});
        db.close();

        return rowsAffected > 0;
    }

    // Delete a Method
    public int deleteMethod(int methodId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete("Method", "method_id = ?", new String[]{String.valueOf(methodId)});
        db.close();
        return rowsDeleted;
    }

    public void close() {
        dbHelper.close();
    }
}