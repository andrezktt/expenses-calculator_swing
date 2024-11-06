package com.project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * ExpenseCalculator is a Java Swing application that manages monthly expenses.
 * It allows users to add, filter, delete, sort, save, and load expense data.
 * The application displays the expenses in a table and calculates the total value.
 */
public class ExpenseCalculator {
    private JFrame frame;
    private JTextField titleField;
    private JTextField categoryField;
    private JTextField amountField;
    private JTextField filterField;
    private JLabel totalLabel;
    private DefaultTableModel tableModel;
    private List<Expense> expenses;

    /**
     * Constructs the ExpenseCalculator application and initializes the GUI components.
     */
    public ExpenseCalculator() {
        Locale.setDefault(Locale.US);

        expenses = new ArrayList<>();
        frame = new JFrame("Calculadora de Despesas Mensais");
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.BOTH;

        // Title field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.1;
        inputPanel.add(new JLabel("Título:"), gbc);

        gbc.gridx = 1;
        titleField = new JTextField(20);
        inputPanel.add(titleField, gbc);

        // Category field
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Categoria:"), gbc);

        gbc.gridx = 1;
        categoryField = new JTextField(20);
        inputPanel.add(categoryField, gbc);

        // Amount field
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Valor:"), gbc);

        gbc.gridx = 1;
        amountField = new JTextField(20);
        inputPanel.add(amountField, gbc);

        // Add button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton addButton = new JButton("Adicionar");
        inputPanel.add(addButton, gbc);

        // Report panel
        JPanel reportPanel = new JPanel(new BorderLayout());

        // Expense table
        String[] columnNames = { "índice", "Título", "Categoria", "Valor"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable expenseTable = new JTable(tableModel);

        JScrollPane tableScrollPane = new JScrollPane(expenseTable);
        reportPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Total label
        totalLabel = new JLabel("Total: 0.00");
        reportPanel.add(totalLabel, BorderLayout.SOUTH);

        // Action panel with filters and buttons
        JPanel actionPanel = new JPanel();
        filterField = new JTextField(10);
        JButton filterButton = new JButton("Filtrar");
        JButton deleteButton = new JButton("Excluir");
        JButton saveButton = new JButton("Salvar");
        JButton loadButton = new JButton("Carregar");
        JButton sortByTitleButton = new JButton("Ordenar por Título");
        JButton sortByValueButton = new JButton("Ordenar por Valor");

        actionPanel.add(sortByTitleButton);
        actionPanel.add(sortByValueButton);
        actionPanel.add(new JLabel("Filtrar Categoria"));
        actionPanel.add(filterField);
        actionPanel.add(filterButton);
        actionPanel.add(deleteButton);
        actionPanel.add(saveButton);
        actionPanel.add(loadButton);

        // Add panels to the main frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(reportPanel, BorderLayout.CENTER);
        frame.add(actionPanel, BorderLayout.SOUTH);

        // Action listeners for buttons
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

        sortByTitleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                expenses.sort((e1, e2) -> e1.getTitle().compareToIgnoreCase(e2.getTitle()));
                generateReport(expenses);
            }
        });

        sortByValueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                expenses.sort((e1, e2) -> Double.compare(e1.getAmount(), e2.getAmount()));
                generateReport(expenses);
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Adds a new expense to the list and validates input data.
     */
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

            DecimalFormat df = new DecimalFormat("#.00");
            amount = Double.parseDouble(df.format(amount));

            expenses.add(new Expense(title, category, amount));
            titleField.setText("");
            categoryField.setText("");
            amountField.setText("");
            JOptionPane.showMessageDialog(null, "Despesa adicionada com sucesso!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor, insira um valor válido.");
        }
    }

    /**
     * Deletes an expense from the list based on the selected index in the table.
     *
     * @param index the index of the expense to delete
     */
    private void deleteExpense(int index) {
        if (index >= 0 && index < expenses.size()) {
            expenses.remove(index);
            generateReport(expenses);
        } else {
            JOptionPane.showMessageDialog(frame, "Selecione uma despesa para deletar.");
        }
    }

    /**
     * Filters expenses based on a specified category and displays the filtered list.
     *
     * @param category the category to filter by
     */
    private void filterExpenses(String category) {
        List<Expense> filteredExpenses = expenses.stream()
                .filter(expense -> expense.getCategory().equalsIgnoreCase(category) | category.isEmpty())
                .toList();
        generateReport(filteredExpenses);
    }

    /**
     * Saves the list of expenses to a file named "expenses.txt".
     */
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

    /**
     * Loads the list of expenses from a file named "expenses.txt".
     */
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

    /**
     * Generates and displays a report of the expenses in the table and updates the total amount.
     *
     * @param expenseList the list of expenses to display
     */
    private void generateReport(List<Expense> expenseList) {
        tableModel.setRowCount(0);
        double total = 0;
        for (int i = 0; i < expenseList.size(); i++) {
            Expense expense = expenseList.get(i);
            tableModel.addRow(new Object[]{i + 1, expense.getTitle(), expense.getCategory(), String.format("%.2f%n", expense.getAmount())});
            total += expense.getAmount();
        }
        totalLabel.setText(String.format("Total: R$%.2f%n", total));
    }

    /**
     * Main method that launches the ExpenseCalculator application.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExpenseCalculator::new);
    }
}

