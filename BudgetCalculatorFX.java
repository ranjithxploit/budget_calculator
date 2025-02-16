import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class BudgetCalculatorFX extends Application {
    private double budget = 0;
    private double totalExpenses = 0;

    private Label budgetLabel = new Label("Total Budget: ₹0");
    private Label expenseLabel = new Label("Total Expenses: ₹0");
    private Label remainingLabel = new Label("Remaining: ₹0");

    private ObservableList<String> expenseList = FXCollections.observableArrayList();
    private ListView<String> expenseListView = new ListView<>(expenseList);

    @Override
    public void start(Stage stage) {
        TextField budgetField = new TextField();
        budgetField.setPromptText("Enter Budget");

        Button setBudgetButton = new Button("Set Budget");
        setBudgetButton.setOnAction(e -> {
            budget = Double.parseDouble(budgetField.getText());
            updateLabels();
        });

        TextField expenseNameField = new TextField();
        expenseNameField.setPromptText("Expense Name");

        TextField expenseAmountField = new TextField();
        expenseAmountField.setPromptText("Expense Amount");

        Button addExpenseButton = new Button("Add Expense");
        addExpenseButton.setOnAction(e -> {
            String name = expenseNameField.getText();
            if (name.isEmpty() || expenseAmountField.getText().isEmpty()) {
                System.out.println("Please enter both name and amount!");
                return;
            }
            try {
                double amount = Double.parseDouble(expenseAmountField.getText());
                totalExpenses += amount;
                expenseList.add(name + " - ₹" + amount);
                updateLabels();
                expenseNameField.clear();
                expenseAmountField.clear();
            } catch (NumberFormatException ex) {
                System.out.println("Invalid amount entered!");
            }
        });

        Button saveButton = new Button("Save to Log");
        saveButton.setOnAction(e -> saveExpensesToFile());

        // 🔹 Close Button
        Button closeButton = new Button("Close App");
        closeButton.setOnAction(e -> {
            System.out.println("Application Closed!");
            stage.close(); // Closes the application
        });

        VBox root = new VBox(10, budgetField, setBudgetButton, expenseNameField, expenseAmountField, addExpenseButton, expenseListView, saveButton, closeButton, budgetLabel, expenseLabel, remainingLabel);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 20px;");

        stage.setScene(new Scene(root, 350, 600));
        stage.setTitle("Budget Calculator");
        stage.show();
    }

    private void updateLabels() {
        budgetLabel.setText("Total Budget: ₹" + budget);
        expenseLabel.setText("Total Expenses: ₹" + totalExpenses);
        remainingLabel.setText("Remaining: ₹" + (budget - totalExpenses));
    }

    private void saveExpensesToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("log.txt"))) {
            writer.println("Budget: ₹" + budget);
            writer.println("Total Expenses: ₹" + totalExpenses);
            writer.println("Remaining: ₹" + (budget - totalExpenses));
            writer.println("\nExpenses:");
            for (String expense : expenseList) {
                writer.println(expense);
            }
            System.out.println("Expenses saved to log.txt!");
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
