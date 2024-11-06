package com.project;

public class Expense {
    private String title;
    private String category;
    private double amount;

    public Expense(String title, String category, double amount) {
        this.title = title;
        this.category = category;
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }
}
