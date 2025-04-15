package com.example.examproject.database;

import android.content.Context;

import com.example.examproject.database.DAO.CategoryDAO;
import com.example.examproject.database.DAO.ExpenseDAO;
import com.example.examproject.database.DAO.IncomeDAO;
import com.example.examproject.database.DAO.MethodsDAO;
import com.example.examproject.database.DAO.UserDAO;

public class DatabaseManagerTry {
    private final UserDAO userDAO;
    private final IncomeDAO incomeDAO;
    private final ExpenseDAO expenseDAO;
    private final MethodsDAO methodsDAO;
    private final CategoryDAO categoryDAO;

    public DatabaseManagerTry(Context context) {
        this.userDAO = new UserDAO(context);
        this.incomeDAO = new IncomeDAO(context);
        this.expenseDAO = new ExpenseDAO(context);
        this.methodsDAO = new MethodsDAO(context);
        this.categoryDAO = new CategoryDAO(context);
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public IncomeDAO getIncomeDAO() {
        return incomeDAO;
    }

    public ExpenseDAO getExpenseDAO() {
        return expenseDAO;
    }

    public MethodsDAO getMethodsDAO() {
        return methodsDAO;
    }

    public CategoryDAO getCategoryDAO() {
        return categoryDAO;
    }

    public void close() {
        userDAO.close();
        incomeDAO.close();
        expenseDAO.close();
        methodsDAO.close();
        categoryDAO.close();
    }
}
