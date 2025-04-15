package com.example.examproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examproject.R;
import com.example.examproject.adapter.model.Transaction;

import java.util.List;

public class TransactionAdapterNew extends RecyclerView.Adapter<TransactionAdapterNew.ViewHolder> {

    private List<Transaction> transactionList;
    private OnItemClickListener onItemClickListener;

    // Define interface for click listener
    public interface OnItemClickListener {
        void onItemClick(Transaction transaction);
    }

    // Constructor with click listener
    public TransactionAdapterNew(List<Transaction> transactionList, OnItemClickListener onItemClickListener) {
        this.transactionList = transactionList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.title.setText(transaction.getTitle());
        holder.subtitle.setText(transaction.getDate());
        holder.amount.setText(transaction.getAmount());

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(transaction);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle, amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            amount = itemView.findViewById(R.id.amount);
        }
    }
}
