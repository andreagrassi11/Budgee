package com.example.examproject.database.DAO;

import android.content.Context;
import com.example.examproject.database.DatabaseHelper;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryDAO {
    private final DatabaseHelper dbHelper;

    public CategoryDAO(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * Method to insert category
     * @param userId
     * @param categoryName
     * @return long
     */
    public long insertCategory(String userId, String categoryName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("name", categoryName);

        long result = db.insert("Category", null, values);
        db.close();
        return result;
    }

    /**
     * Take all user Category
     * @param userId
     * @return List<Map<String, String>>
     */
    public List<Map<String, String>> getUserCategories(String userId) {
        List<Map<String, String>> categoryList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Category WHERE user_id = ?", new String[]{userId});

        if (cursor.moveToFirst()) {
            do {
                Map<String, String> item = new HashMap<>();
                item.put("ID", String.valueOf(cursor.getInt(0)));
                item.put("Name", cursor.getString(2));
                categoryList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return categoryList;
    }

    /**
     * Get id Category from Name
     * @param userId
     * @param categoryName
     * @return int
     */
    public int getCategoryIdByName(String userId, String categoryName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int categoryId = -1;

        Cursor cursor = db.rawQuery("SELECT category_id FROM Category WHERE user_id = ? AND name = ?",
                new String[]{userId, categoryName});

        if (cursor.moveToFirst()) {
            categoryId = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return categoryId;
    }

    /**
     * Get name Category from Id
     * @param userId
     * @param categoryId
     * @return String
     */
    public String getCategoryNameById(String userId, int categoryId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String categoryName = "";

        Cursor cursor = db.rawQuery("SELECT name FROM Category WHERE user_id = ? AND category_id = ?",
                new String[]{userId, String.valueOf(categoryId)});

        if (cursor.moveToFirst()) {
            categoryName = cursor.getString(0);
        }

        cursor.close();
        db.close();
        return categoryName;
    }

    /**
     * Method to update a category name
     * @param categoryId - ID of the category
     * @param categoryName - New category name
     * @return boolean
     */
    public boolean updateCategory(String categoryId, String categoryName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", categoryName);

        int rowsAffected = db.update("Category", values, "category_id = ?", new String[]{categoryId});
        db.close();

        return rowsAffected > 0;
    }

    /**
     * Method to delete a category
     * @param categoryId - ID of the category to be deleted
     * @return int
     */
    public int deleteCategory(int categoryId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete("Category", "category_id = ?", new String[]{String.valueOf(categoryId)});
        db.close();
        return rowsDeleted;
    }

    public void close() {
        dbHelper.close();
    }
}