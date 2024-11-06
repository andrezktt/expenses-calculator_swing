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
    private JTextField titleField;
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
        frame.setSize(500, 600);
        frame.setLayout(new GridBagLayout());

        // Interface Components
        titleField = new JTextField(15);
        categoryField = new JTextField(15);
        amountField = new JTextField(15);
        deleteField = new JTextField(5);
        filterField = new JTextField(15);
        reportArea = new JTextArea(15, 50);
        reportArea.setEditable(false);

        JButton addButton = new JButton("Adicionar");
        JButton deleteButton = new JButton("Excluir");
        JButton saveButton = new JButton("Salvar");
        JButton loadButton = new JButton("Carregar");
        JButton filterButton = new JButton("Filtrar");
        JButton generateButton = new JButton("Gerar Relatório");

        // Add components to frame
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        frame.add(new JLabel("Título:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        frame.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(new JLabel("Categoria:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        frame.add(categoryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(new JLabel("Valor:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        frame.add(amountField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(addButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        JScrollPane scrollPane = new JScrollPane(reportArea);
        frame.add(scrollPane, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(generateButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        frame.add(new JLabel("Índice para Excluir:"), gbc);

        gbc.gridx = 1;
        frame.add(deleteField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        frame.add(deleteButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        frame.add(new JLabel("Filtrar por Categoria:"), gbc);

        gbc.gridx = 1;
        frame.add(filterField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 9;
        frame.add(filterButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        frame.add(saveButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 10;
        frame.add(loadButton, gbc);

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
        String title = titleField.getText();
        String category = categoryField.getText();
        String amountText = amountField.getText();

        if (title.isEmpty() || category.isEmpty() || amountText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            if (amount < 0) {
                JOptionPane.showMessageDialog(null, "O valor deve ser positivo.");
                return;
            }
            expenses.add(new Expense(title, category, amount));
            titleField.setText("");
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
                report.append(String.format("%s - %s | Valor: R$%.2f%n", expense.getTitle(), expense.getCategory(), expense.getAmount()));
                total += expense.getAmount();
            }
        }
        report.append(String.format("\nTotal de despesas: R$%.2f%n", total));

        reportArea.setText(report.toString());
    }

    private void saveExpenses() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("expenses.txt"))) {
            for (Expense expense : expenses) {
                writer.printf("%s,%s,%.2f%n", expense.getTitle(), expense.getCategory(), expense.getAmount());
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
                if (parts.length == 3) {
                    String title = parts[0];
                    String category = parts[1];
                    double amount = Double.parseDouble(parts[2]);
                    expenses.add(new Expense(title, category, amount));
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

        report.append("Relatório de Despesas Mensais:\n").append("\n");
        for (int i = 0; i < expenses.size(); i++) {
            Expense expense = expenses.get(i);
            report.append(String.format("%d. %s - %s | Valor: R$%.2f%n", i + 1, expense.getTitle(), expense.getCategory(), expense.getAmount()));
            total += expense.getAmount();
        }
        report.append(String.format("\nTotal de despesas: R$%.2f%n", total));

        reportArea.setText(report.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExpenseCalculator::new);
    }
}
