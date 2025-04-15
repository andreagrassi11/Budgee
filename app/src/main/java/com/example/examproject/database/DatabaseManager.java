package com.example.examproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class DatabaseManager {
    private DatabaseHelper dbHelper;

    public DatabaseManager(Fragment fragment) {
        Context context = fragment.getContext();
        dbHelper = new DatabaseHelper(context);
    }


    // 📌 1️⃣ INSERIRE UN NUOVO UTENTE
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

    // 📌 2️⃣ OTTENERE TUTTI GLI UTENTI
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

    /* Insert INCOME */
    public long insertIncome(String userId, int categoryId, int methodId, double value) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("category_id", categoryId);
        values.put("method_id", methodId);
        values.put("amount", value);

        long result = db.insert("Income", null, values);
        db.close();
        return result;
    }

    /* Insert EXPENSE */
    public long insertExpense(String userId, int categoryId, int methodId, double value) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("category_id", categoryId);
        values.put("method_id", methodId);
        values.put("amount", value);

        long result = db.insert("Expense", null, values);
        db.close();
        return result;
    }

    /* OTTENERE TUTTE LE ENTRATE DI UN UTENTE */
    public List<Map<String, String>> getUserIncome(String userId) {

        List<Map<String, String>> incomeList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Income WHERE user_id = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Map<String, String> item = new HashMap<>();
                item.put("ID",  String.valueOf(cursor.getInt(0)));
                item.put("Categoria",  String.valueOf(cursor.getInt(2)));
                item.put("Metodo",  String.valueOf(cursor.getInt(3)));
                item.put("Amount",  String.valueOf(cursor.getInt(4)));

                incomeList.add(item);
                //String income = "Income ID: " + cursor.getInt(0) + " - Categoria: " + cursor.getInt(2) + " - Metodo: " + cursor.getInt(3) + " - Amount: " + cursor.getInt(4);
                //incomeList.add(income);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return incomeList;
    }

    /* OTTENERE TUTTE LE SPESE DI UN UTENTE */
    public List<Map<String, String>> getUserExpenses(String userId) {

        List<Map<String, String>> expenseList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Expense WHERE user_id = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                //String expense = "Expense ID: " + cursor.getInt(0) + " - Categoria: " + cursor.getInt(2) + " - Metodo: " + cursor.getInt(3);
                //expenseList.add(expense);
                Map<String, String> item = new HashMap<>();
                item.put("ID",  String.valueOf(cursor.getInt(0)));
                item.put("Categoria",  String.valueOf(cursor.getInt(2)));
                item.put("Metodo",  String.valueOf(cursor.getInt(3)));
                item.put("Amount",  String.valueOf(cursor.getInt(4)));

                expenseList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return expenseList;
    }

    // 📌 7️⃣ ELIMINARE UN'ENTRATA (INCOME)
    public int deleteIncome(int incomeId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete("Income", "income_id = ?", new String[]{String.valueOf(incomeId)});
        db.close();
        return rowsDeleted;
    }

    // 📌 8️⃣ ELIMINARE UNA SPESA (EXPENSE)
    public int deleteExpense(int expenseId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete("Expense", "expense_id = ?", new String[]{String.valueOf(expenseId)});
        db.close();
        return rowsDeleted;
    }
}
