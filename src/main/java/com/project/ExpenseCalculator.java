package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ExpenseCalculator {
    private List<Expense> expenses;
    private JTextField categoryField;
    private JTextField amountField;
    private JTextField deleteField;
    private JTextField filterField;
    private JTextArea reportArea;

    public ExpenseCalculator() {
        Locale.setDefault(Locale.US);
        expenses = new ArrayList<>();
        JFrame frame = new JFrame("Calculadora de Despesas Mensais");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new FlowLayout());

        // Interface Components
        categoryField = new JTextField(15);
        amountField = new JTextField(15);
        deleteField = new JTextField(5);
        filterField = new JTextField(15);
        reportArea = new JTextArea(10, 45);
        reportArea.setEditable(false);

        JButton addButton = new JButton("Adicionar");
        JButton deleteButton = new JButton("Excluir");
        JButton saveButton = new JButton("Salvar");
        JButton loadButton = new JButton("Carregar");
        JButton filterButton = new JButton("Filtrar");
        JButton generateButton = new JButton("Gerar Relatório");

        // Add components to frame
        frame.add(new JLabel("Categoria:"));
        frame.add(categoryField);
        frame.add(new JLabel("Valor:"));
        frame.add(amountField);
        frame.add(addButton);
        frame.add(new JLabel("Índice da Despesa para excluir:"));
        frame.add(deleteField);
        frame.add(deleteButton);
        frame.add(new JLabel("Filtrar por categoria:"));
        frame.add(filterField);
        frame.add(filterButton);
        frame.add(generateButton);
        frame.add(saveButton);
        frame.add(loadButton);
        frame.add(new JScrollPane(reportArea));

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addExpense();
                generateReport();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteExpense();
                generateReport();
            }
        });

        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterExpenses();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveExpenses();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadExpenses();
                generateReport();
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
            if (amount < 0) {
                JOptionPane.showMessageDialog(null, "O valor deve ser positivo.");
                return;
            }
            expenses.add(new Expense(category, amount));
            categoryField.setText("");
            amountField.setText("");
            JOptionPane.showMessageDialog(null, "Despesa adicionada com sucesso!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor, insira um valor válido.");
        }
    }

    private void deleteExpense() {
        String indexText = deleteField.getText();

        try {
            int index = Integer.parseInt(indexText);
            if (index < 0 || index >= expenses.size()) {
                JOptionPane.showMessageDialog(null, "Índice inválido.");
                return;
            }
            expenses.remove(index - 1);
            deleteField.setText("");
            JOptionPane.showMessageDialog(null, "Despesa excluída com sucesso!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor, insira um índice válido.");
        }
    }

    private void filterExpenses() {
        String category = filterField.getText();
        StringBuilder report = new StringBuilder();
        double total = 0;

        report.append("Relatório de Despesas para a Categoria: ").append(category).append("\n");
        for (Expense expense : expenses) {
            if (expense.getCategory().equalsIgnoreCase(category)) {
                report.append(String.format("Categoria: %s, Valor: %.2f%n", expense.getCategory(), expense.getAmount()));
                total += expense.getAmount();
            }
        }
        report.append(String.format("Total de despesas: %.2f%n", total));

        reportArea.setText(report.toString());
    }

    private void saveExpenses() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("expenses.txt"))) {
            for (Expense expense : expenses) {
                writer.printf("%s,%.2f%n", expense.getCategory(), expense.getAmount());
            }
            JOptionPane.showMessageDialog(null, "Despesas salvas com sucesso!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao valvar as despesas: " + e.getMessage());
        }
    }

    private void loadExpenses() {
        try (BufferedReader reader = new BufferedReader(new FileReader("expenses.txt"))) {
            String line;
            expenses.clear();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String category = parts[0];
                    double amount = Double.parseDouble(parts[1]);
                    expenses.add(new Expense(category, amount));
                }
            }
            JOptionPane.showMessageDialog(null, "Despesas carregadas com sucesso!");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Erro ao processar o arquivo de despesas.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar as despesas: " + e.getMessage());
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
        report.append(String.format("\nTotal de despesas: %.2f%n", total));

        reportArea.setText(report.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExpenseCalculator::new);
    }
}
