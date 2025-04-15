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

public class IncomeDAO {
    private final DatabaseHelper dbHelper;

    public IncomeDAO(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public long insertIncome(String userId, int categoryId, int methodId, double value, String name, String date) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("category_id", categoryId);
        values.put("method_id", methodId);
        values.put("amount", value);
        values.put("name", name);
        values.put("date", date);

        long result = db.insert("Income", null, values);
        db.close();
        return result;
    }

    public List<Map<String, String>> getUserIncomes(String userId) {
        List<Map<String, String>> incomeList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Income WHERE user_id = ?", new String[]{userId});

        if (cursor.moveToFirst()) {
            do {
                Map<String, String> item = new HashMap<>();

                item.put("ID", String.valueOf(cursor.getInt(0)));
                item.put("Categoria", String.valueOf(cursor.getInt(2)));
                item.put("Metodo", String.valueOf(cursor.getInt(3)));
                item.put("Amount", String.valueOf(cursor.getInt(4)));
                item.put("Name", cursor.getString(5));
                item.put("Date", cursor.getString(6));

                incomeList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return incomeList;
    }

    /**
     * Methods For update Record
     * @param incomeId
     * @param category
     * @param method
     * @param amount
     * @return
     */
    public boolean updateUserIncome(String incomeId, String category, String method, String amount, String name, String date) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("category_id", category);
        values.put("method_id", method);
        values.put("amount", amount);
        values.put("name", name);
        values.put("date", date);

        int rowsAffected = db.update("Income", values, "income_id = ?", new String[]{incomeId});
        db.close();

        return rowsAffected > 0;
    }


    public int deleteIncome(int incomeId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete("Income", "income_id = ?", new String[]{String.valueOf(incomeId)});
        db.close();
        return rowsDeleted;
    }

    public void close() {
        dbHelper.close();
    }
}
