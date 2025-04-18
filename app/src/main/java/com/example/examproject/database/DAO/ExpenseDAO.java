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

public class ExpenseDAO {
    private final DatabaseHelper dbHelper;

    public ExpenseDAO(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * Insert User Expense
     * @param userId
     * @param categoryId
     * @param methodId
     * @param value
     * @param name
     * @param date
     * @return long
     */
    public long insertExpense(String userId, int categoryId, int methodId, double value, String name, String date) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("category_id", categoryId);
        values.put("method_id", methodId);
        values.put("amount", value);
        values.put("name", name);
        values.put("date", date);

        long result = db.insert("Expense", null, values);
        db.close();
        return result;
    }

    /**
     * Get all User Expenses
     * @param userId
     * @return List<Map<String, String>>
     */
    public List<Map<String, String>> getUserExpenses(String userId) {
        List<Map<String, String>> expenseList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Expense WHERE user_id = ?", new String[]{userId});

        if (cursor.moveToFirst()) {
            do {
                Map<String, String> item = new HashMap<>();
                item.put("ID", String.valueOf(cursor.getInt(0)));
                item.put("Categoria", String.valueOf(cursor.getInt(2)));
                item.put("Metodo", String.valueOf(cursor.getInt(3)));
                item.put("Amount", String.valueOf(cursor.getInt(4)));
                item.put("Name", cursor.getString(5));
                item.put("Date", cursor.getString(6));

                expenseList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return expenseList;
    }

    /**
     * Update Expense
     * @param expenseId
     * @param category
     * @param method
     * @param amount
     * @param name
     * @param date
     * @return boolean
     */
    public boolean updateUserExpense(String expenseId, String category, String method, String amount, String name, String date) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("category_id", category);
        values.put("method_id", method);
        values.put("amount", amount);
        values.put("name", name);
        values.put("date", date);

        int rowsAffected = db.update("Expense", values, "expense_id = ?", new String[]{expenseId});
        db.close();

        return rowsAffected > 0;
    }

    /**
     * Delete Expense
     * @param expenseId
     * @return int
     */
    public int deleteExpense(int expenseId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete("Expense", "expense_id = ?", new String[]{String.valueOf(expenseId)});
        db.close();
        return rowsDeleted;
    }

    public void close() {
        dbHelper.close();
    }
}
