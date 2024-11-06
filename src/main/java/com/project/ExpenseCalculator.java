package com.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ExpenseCalculator {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner scanner = new Scanner(System.in);
        List<Expense> expenses = new ArrayList<>();

        System.out.println("Bem-vindo(a) à Calculadora de Despesas Mensais!");

        while (true) {
            System.out.print("Digite a categoria da despesa (ou 'sair' para finalizar): ");
            String category = scanner.nextLine();

            if (category.equalsIgnoreCase("sair")) {
                break;
            }

            System.out.print("Digite o valor da despesa: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();

            expenses.add(new Expense(category, amount));
        }

        double total = 0;
        System.out.println("\nRelatório de Despesas:");
        for (Expense expense : expenses) {
            System.out.printf("Categoria: %s, Valor: %.2f%n", expense.getCategory(), expense.getAmount());
            total += expense.getAmount();
        }
        System.out.printf("Total de Despesas: %.2f%n", total);

        scanner.close();
    }
}
