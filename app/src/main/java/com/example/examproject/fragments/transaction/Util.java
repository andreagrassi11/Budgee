package com.example.examproject.fragments.transaction;

import static com.example.examproject.util.Utils.openDetailFragment;

import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.examproject.R;
import com.example.examproject.adapter.TransactionAdapterNew;
import com.example.examproject.adapter.model.Transaction;
import com.example.examproject.database.DatabaseManagerTry;
import com.example.examproject.fragments.detail.DetailTransactionFragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Util {

    public static List<Transaction> getUserTransactionsByMonth(String userId, String monthYear, FragmentActivity fr, DatabaseManagerTry dbManagerNew) {

        List<Transaction> monthTransaction = new ArrayList<>();
        double sumIncome = 0;
        double sumExpense = 0;

        // Ottieni entrate e spese
        List<Transaction> transactions = Util.takeTransaction(userId, dbManagerNew);

        String[] parts = monthYear.split("-");
        String formattedYearMonth = parts[1] + "-" + parts[0];

        for (Transaction transaction : transactions) {
            String date = transaction.getDate();

            if (date != null) {
                String yearMonthFromDb = date.substring(0, 7);

                if (yearMonthFromDb.equals(formattedYearMonth)) {

                    String value = transaction.getAmount().replace("€", "").trim();
                    double amount = Double.parseDouble(value.replace("-", "").trim());

                    if(transaction.getAmount().charAt(0) == '-')
                        sumExpense = sumExpense + amount;
                    else
                        sumIncome = sumIncome + amount;

                    monthTransaction.add(transaction);
                }
            }
        }

        /* Setto Totali */
        TextView expense = fr.findViewById(R.id.expensesAmount);
        TextView income = fr.findViewById(R.id.incomeAmount);

        expense.setText(String.valueOf(sumExpense) + "€");
        income.setText(String.valueOf(sumIncome) + "€");

        return monthTransaction;
    }

    public static List<Transaction> takeTransaction (String userId, DatabaseManagerTry dbManagerNew) {
        List<Transaction> transactions = new ArrayList<>();

        /* Recupero Income Utente */
        List<Map<String, String>> userIncome = dbManagerNew.getIncomeDAO().getUserIncomes(userId);

        for (Map<String, String> income : userIncome) {
            String method = dbManagerNew.getMethodsDAO().getMethodNameById(userId, Integer.parseInt(income.get("Metodo")));
            String category = dbManagerNew.getCategoryDAO().getCategoryNameById(userId, Integer.parseInt(income.get("Categoria")));
            transactions.add(new Transaction(Integer.parseInt(income.get("ID")), income.get("Name"), income.get("Date"), income.get("Amount")+"€", method, category));
        }

        /* Recupero Expense Utente */
        List<Map<String, String>> userExpenses = dbManagerNew.getExpenseDAO().getUserExpenses(userId);

        for (Map<String, String> expense : userExpenses) {
            String method = dbManagerNew.getMethodsDAO().getMethodNameById(userId, Integer.parseInt(expense.get("Metodo")));
            String category = dbManagerNew.getCategoryDAO().getCategoryNameById(userId, Integer.parseInt(expense.get("Categoria")));
            transactions.add(new Transaction(Integer.parseInt(expense.get("ID")), expense.get("Name"), expense.get("Date"), "-"+expense.get("Amount")+"€", method, category));
        }

        /* Ordino in base alla data */
        Collections.sort(transactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction t1, Transaction t2) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date1 = LocalDate.parse(t1.getDate(), formatter);
                LocalDate date2 = LocalDate.parse(t2.getDate(), formatter);

                return date2.compareTo(date1);
            }
        });

        return transactions;
    }

    public static TransactionAdapterNew setTransactions (List<Transaction> transactions, FragmentActivity fr) {
        TransactionAdapterNew adapter = new TransactionAdapterNew(transactions, transaction -> {

            String value = transaction.getAmount().replace("€", "").trim();
            String amount = value.replace("-", "").trim();

            Bundle bundle = new Bundle();
            bundle.putInt("id", transaction.getId());
            bundle.putString("title", transaction.getTitle());
            bundle.putString("date", transaction.getDate());
            bundle.putString("amount", amount);
            bundle.putString("method", transaction.getMethod());
            bundle.putString("category", transaction.getCategory());

            openDetailFragment(new DetailTransactionFragment(), transaction, fr, bundle);
        });

        return adapter;
    }
}
