package com.example.examproject.adapter.model;

public class Transaction {

        private final int id;
        private final String title;
        private final String date;
        private final String amount;
        private final String method;
        private final String category;

        public Transaction(int id, String title, String date, String amount, String method, String category) {
            this.id = id;
            this.title = title;
            this.date = date;
            this.amount = amount;
            this.method = method;
            this.category = category;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getDate() {
            return date;
        }

        public String getAmount() {
            return amount;
        }

        public String getMethod() {
            return method;
        }

        public String getCategory() {
            return category;
        }

}
