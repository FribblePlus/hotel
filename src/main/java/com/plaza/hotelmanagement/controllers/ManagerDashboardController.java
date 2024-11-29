package com.plaza.hotelmanagement.controllers;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.InputMismatchException;

public class ManagerDashboardController {

    @FXML
    private Button issueCardButton;

    @FXML
    private Button updateEmployeeButton;

    @FXML
    private GridPane roomGrid;

    @FXML
    public void initialize() {
        issueCardButton.setOnAction(event -> showIssueCardForm());
        updateEmployeeButton.setOnAction(event -> showUpdateEmployeeForm());
    }

    private void showIssueCardForm() {

            // Clear previous content
            roomGrid.getChildren().clear();

            // Add form for entering employee details
            VBox issueCardForm = new VBox(10);
            issueCardForm.getChildren().add(new Label("Issue Employee Card"));

            // Input fields
            TextField fullNameField = new TextField();
            fullNameField.setPromptText("Enter full name");

           TextField positionField = new TextField();
           positionField.setPromptText("Enter position");

            TextField emailField = new TextField();
            emailField.setPromptText("Enter email address");

            PasswordField passwordField = new PasswordField();
            passwordField.setPromptText("Enter password");

            // Submit button
            Button submitButton = new Button("Generate Card");
            submitButton.setOnAction(event -> {
                // Capture input values
                String fullName = fullNameField.getText();
                String role = positionField.getText();
                String email = emailField.getText();
                String password = passwordField.getText();

                // Validation
                if (fullName.isEmpty() || role == null || email.isEmpty() || password.isEmpty()) {
                    issueCardForm.getChildren().add(new Label("Error: All fields must be filled!"));
                    return;
                }

                // Generate the employee card
                VBox card = createEmployeeCard(fullName, role, email, password);

                // Display the card
                roomGrid.getChildren().clear();
                roomGrid.add(card, 0, 0);
            });

            // Add components to the form
            issueCardForm.getChildren().addAll(
                    new Label("Full Name:"), fullNameField,
                    new Label("Role:"), positionField,
                    new Label("Email:"), emailField,
                    new Label("Password:"), passwordField,
                    submitButton
            );

            roomGrid.add(issueCardForm, 0, 0); // Add to the GridPane


    }
    private VBox createEmployeeCard(String fullName, String role, String email, String password) {
        VBox card = new VBox(20);
        card.setStyle("-fx-border-color: black; -fx-padding: 20; -fx-alignment: center;");

        Label hotelName = new Label("THE PLAZA");
        hotelName.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-alignment: center;");


        Rectangle photoPlaceholder = new Rectangle(100, 120);
        photoPlaceholder.setFill(Color.GRAY);
        photoPlaceholder.setStroke(Color.BLACK);

        // Employee details
        Label nameLabel = new Label("Full Name: " + fullName);
        Label roleLabel = new Label("Role: " + role);
        Label emailLabel = new Label("Email: " + email);
        Label passwordLabel = new Label("Password: " + password);

        // Styling details
        nameLabel.setStyle("-fx-font-weight: bold;");
        roleLabel.setStyle("-fx-font-weight: bold;");
        emailLabel.setStyle("-fx-font-weight: bold;");
        passwordLabel.setStyle("-fx-font-weight: bold;");

        // Add all components to the card
        card.getChildren().addAll(
                hotelName,
                photoPlaceholder,
                nameLabel,
                roleLabel,
                emailLabel,
                passwordLabel
        );

        return card;
    }





    private void showUpdateEmployeeForm() {
            // Clear previous content
            roomGrid.getChildren().clear();

            // Add employee update form dynamically
            VBox updateForm = new VBox(10);
            updateForm.getChildren().add(new Label("Add New Employee"));


            TextField nameField = new TextField();
            nameField.setPromptText("Enter employee name");


            TextField surnameField = new TextField();
            surnameField.setPromptText("Enter employee surname");

            TextField ageField = new TextField();
            ageField.setPromptText("Enter employee age");

            TextField positionField = new TextField();
            positionField.setPromptText("Enter employee position");

            TextField idField = new TextField();
            idField.setPromptText("Enter employee ID");

            // Submit button
            Button submitButton = new Button("Submit");
            submitButton.setOnAction(event -> {
                try {

                    String name = nameField.getText();
                    String surname = surnameField.getText();
                    String age = ageField.getText();
                    String position = positionField.getText();
                    String id = idField.getText();

                    // Ensure all fields are filled
                    if (name.isEmpty() || surname.isEmpty() || age.isEmpty() || position.isEmpty() || id.isEmpty()) {
                        throw new IllegalArgumentException("All fields must be filled!");
                    }

                    // Parse and validate age
                    int parsedAge;
                    try {
                        parsedAge = Integer.parseInt(age);
                        if (parsedAge <= 0) {
                            throw new InputMismatchException("Age must be a positive number!");
                        }
                    } catch (NumberFormatException e) {
                        throw new InputMismatchException("Age must be a valid number!");
                    }

                    // Process the new employee data
                    System.out.println("New Employee Added:");
                    System.out.println("Name: " + name);
                    System.out.println("Surname: " + surname);
                    System.out.println("Age: " + parsedAge);
                    System.out.println("Position: " + position);
                    System.out.println("ID: " + id);

                    // Clear the form after submission
                    roomGrid.getChildren().clear();
                    roomGrid.add(new Label("Employee added successfully!"), 0, 0);

                } catch (IllegalArgumentException | InputMismatchException e) {
                    // Display error message
                    updateForm.getChildren().add(new Label("Error: " + e.getMessage()));
                }
            });

            // Add components to the form
            updateForm.getChildren().addAll(
                    new Label("Name:"), nameField,
                    new Label("Surname:"), surnameField,
                    new Label("Age:"), ageField,
                    new Label("Position:"), positionField,
                    new Label("Employee ID:"), idField,
                    submitButton
            );

            roomGrid.add(updateForm, 0, 0); // Add to the GridPane
        }

    }


