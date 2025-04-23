package com.example.examproject.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "exam.db";
    private static final int DATABASE_VERSION = 6;

    // USER
    private static final String TABLE_CREATE_USER = "CREATE TABLE User ( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "id_firebase TEXT NOT NULL, " +
            "name TEXT NOT NULL, " +
            "surname TEXT NOT NULL, " +
            "email TEXT NOT NULL)";

    // INCOME
    private static final String TABLE_CREATE_INCOME = "CREATE TABLE Income ( " +
            "income_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "user_id INTEGER NOT NULL, " +
            "category_id INTEGER NOT NULL, " +
            "method_id INTEGER NOT NULL, " +
            "amount DOUBLE NOT NULL, "+
            "name TEXT NOT NULL, "+
            "date TEXT NOT NULL, "+
            "FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE, " +
            "FOREIGN KEY (category_id) REFERENCES Category(category_id) ON DELETE CASCADE, " +
            "FOREIGN KEY (method_id) REFERENCES Method(method_id) ON DELETE CASCADE)";

    // EXPENSE
    private static final String TABLE_CREATE_EXPENSE = "CREATE TABLE Expense ( " +
            "expense_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "user_id INTEGER NOT NULL, " +
            "category_id INTEGER NOT NULL, " +
            "method_id INTEGER NOT NULL, " +
            "amount DOUBLE NOT NULL, "+
            "name TEXT NOT NULL, "+
            "date TEXT NOT NULL, "+
            "FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE, " +
            "FOREIGN KEY (category_id) REFERENCES Category(category_id) ON DELETE CASCADE, " +
            "FOREIGN KEY (method_id) REFERENCES Method(method_id) ON DELETE CASCADE)";

    // CATEGORY
    private static final String TABLE_CREATE_CATEGORY = "CREATE TABLE Category ( " +
            "category_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "user_id INTEGER NOT NULL, " +
            "name TEXT NOT NULL, " +
            "FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE)";

    // METHOD
    private static final String TABLE_CREATE_METHOD = "CREATE TABLE Method ( " +
            "method_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "user_id INTEGER NOT NULL, " +
            "name TEXT NOT NULL, " +
            "FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_USER);
        db.execSQL(TABLE_CREATE_INCOME);
        db.execSQL(TABLE_CREATE_EXPENSE);
        db.execSQL(TABLE_CREATE_CATEGORY);
        db.execSQL(TABLE_CREATE_METHOD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Invece di cancellare i dati, potresti usare ALTER TABLE per aggiungere nuove colonne
        db.execSQL("DROP TABLE IF EXISTS User");
        db.execSQL("DROP TABLE IF EXISTS Income");
        db.execSQL("DROP TABLE IF EXISTS Expense");
        db.execSQL("DROP TABLE IF EXISTS Category");
        db.execSQL("DROP TABLE IF EXISTS Method");
        onCreate(db);
    }
}
