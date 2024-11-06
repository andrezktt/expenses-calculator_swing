package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ExpenseCalculator {
    private List<Expense> expenses;
    private JTextField categoryField;
    private JTextField amountField;
    private JTextArea reportArea;

    public ExpenseCalculator() {
        expenses = new ArrayList<>();
        JFrame frame = new JFrame("Calculadora de Despesas Mensais");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new FlowLayout());

        // Interface Components
        categoryField = new JTextField(15);
        amountField = new JTextField(10);
        reportArea = new JTextArea(10, 30);
        reportArea.setEditable(false);

        JButton addButton = new JButton("Adicionar Despesa");
        JButton generateButton = new JButton("Gerar Relatório");

        // Add components to frame
        frame.add(new JLabel("Categoria:"));
        frame.add(categoryField);
        frame.add(new JLabel("Valor:"));
        frame.add(amountField);
        frame.add(addButton);
        frame.add(generateButton);
        frame.add(new JScrollPane(reportArea));

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addExpense();
            }
        });

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });

        frame.setVisible(true);
    }

    private void addExpense() {
        String category = categoryField.getText();
        String amountText = amountField.getText();

        if (category.isEmpty() || amountText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            expenses.add(new Expense(category, amount));
            categoryField.setText("");
            amountField.setText("");
            JOptionPane.showMessageDialog(null, "Despesa adicionada com sucesso!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor, insira um valor válido.");
        }
    }

    private void generateReport() {
        StringBuilder report = new StringBuilder();
        double total = 0;

        report.append("Relatório de Despesas:\n");
        for (Expense expense : expenses) {
            report.append(String.format("Categoria: %s, Valor: %.2f%n", expense.getCategory(), expense.getAmount()));
            total += expense.getAmount();
        }
        report.append(String.format("Total de despesas: %.2f%n", total));

        reportArea.setText(report.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExpenseCalculator::new);
    }
}
