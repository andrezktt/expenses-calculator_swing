package com.project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExpenseCalculator {
    private JFrame frame;
    private JTextField titleField;
    private JTextField categoryField;
    private JTextField amountField;
    private JTextField filterField;
    private JLabel totalLabel;
    private DefaultTableModel tableModel;

    private List<Expense> expenses;

    public ExpenseCalculator() {
        Locale.setDefault(Locale.US);

        expenses = new ArrayList<>();
        frame = new JFrame("Calculadora de Despesas Mensais");
        frame.setLayout(new BorderLayout());

        // Add components to frame
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo título
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Título:"), gbc);

        gbc.gridx = 1;
        titleField = new JTextField(15);
        inputPanel.add(titleField, gbc);

        // Campo categoria
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Categoria:"), gbc);

        gbc.gridx = 1;
        categoryField = new JTextField(15);
        inputPanel.add(categoryField, gbc);

        // Campo Valor
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Valor:"), gbc);

        gbc.gridx = 1;
        amountField = new JTextField(10);
        inputPanel.add(amountField, gbc);

        // Botão Adicionar
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton addButton = new JButton("Adicionar");
        inputPanel.add(addButton, gbc);

        //Painel de Relatório
        JPanel reportPanel = new JPanel(new BorderLayout());

        // Tabela de Despesas
        String[] columnNames = { "índice", "Título", "Categoria", "Valor"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable expenseTable = new JTable(tableModel);

        JScrollPane tableScrollPane = new JScrollPane(expenseTable);
        reportPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Total de Despesas
        totalLabel = new JLabel("Total: 0.00");
        reportPanel.add(totalLabel, BorderLayout.SOUTH);

        // PAINEL DE FILTROS E AÇÕES
        JPanel actionPanel = new JPanel();
        filterField = new JTextField(10);
        JButton filterButton = new JButton("Filtrar");
        JButton deleteButton = new JButton("Excluir");
        JButton saveButton = new JButton("Salvar");
        JButton loadButton = new JButton("Carregar");

        actionPanel.add(new JLabel("Filtrar Categoria"));
        actionPanel.add(filterField);
        actionPanel.add(filterButton);
        actionPanel.add(deleteButton);
        actionPanel.add(saveButton);
        actionPanel.add(loadButton);

        // ADICIONAR OS PAINÉIS AO FRAME PRINCIPAL
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(reportPanel, BorderLayout.CENTER);
        frame.add(actionPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addExpense();
                generateReport(expenses);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteExpense(expenseTable.getSelectedRow());
                generateReport(expenses);
            }
        });
//
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterExpenses(filterField.getText());
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
                generateReport(expenses);
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
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

    private void deleteExpense(int index) {
        if (index >= 0 && index < expenses.size()) {
            expenses.remove(index);
            generateReport(expenses);
        } else {
            JOptionPane.showMessageDialog(frame, "Selecione uma despesa para deletar.");
        }
    }

    private void filterExpenses(String category) {
        List<Expense> filteredExpenses = expenses.stream()
                .filter(expense -> expense.getCategory().equalsIgnoreCase(category) | category.isEmpty())
                .toList();
        generateReport(filteredExpenses);
    }

    private void saveExpenses() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("expenses.txt"))) {
            for (Expense expense : expenses) {
                writer.println(expense.getTitle() + "," + expense.getCategory() + "," + expense.getAmount());
            }
            JOptionPane.showMessageDialog(frame, "Despesas salvas com sucesso.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Erro ao salvar as despesas: " + e.getMessage());
        }
    }

    private void loadExpenses() {
        expenses.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("expenses.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String title = parts[0];
                    String category = parts[1];
                    double amount = Double.parseDouble(parts[2]);
                    expenses.add(new Expense(title, category, amount));
                }
            }
            JOptionPane.showMessageDialog(frame, "Despesas carregadas com sucesso.");
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Erro ao processar o arquivo de despesas.");
        }
    }

    private void generateReport(List<Expense> expenseList) {
        tableModel.setRowCount(0);
        double total = 0;
        for (int i = 0; i < expenseList.size(); i++) {
            Expense expense = expenseList.get(i);
            tableModel.addRow(new Object[]{i + 1, expense.getTitle(), expense.getCategory(), expense.getAmount()});
            total += expense.getAmount();
        }
        totalLabel.setText(String.format("Total: R$%.2f%n", total));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExpenseCalculator::new);
    }
}
