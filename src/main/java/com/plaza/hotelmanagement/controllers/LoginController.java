package com.plaza.hotelmanagement.controllers;
// libraries
import java.io.IOException;

import com.plaza.hotelmanagement.models.account.Account;
import com.plaza.hotelmanagement.models.account.AccountHandler;
import com.plaza.hotelmanagement.database.DatabaseManager;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.text.Text;
import javafx.fxml.FXMLLoader;

public class LoginController {

    @FXML
    private TextField lg_email;
    @FXML
    private TextField lg_pass;
    @FXML
    private Button lgn;
    @FXML
    private Text rg_error;
    @FXML
    private Button rg;
    @FXML
    private TextField rg_email;
    @FXML
    private TextField rg_pass;
    @FXML
    private TextField rg_repass;
    @FXML
    private TextField rg_name;
    @FXML
    private AnchorPane side_form;
    @FXML
    private Text invalid;
    @FXML
    private Text rg_success;

    DatabaseManager databaseManager = new DatabaseManager();

    // switching login/register forms
    public void switchForm(ActionEvent event) {
        TranslateTransition slider = new TranslateTransition();
        if (event.getSource() == lgn) {
            slider.setNode(side_form);
            slider.setToX(480);
            slider.setDuration(Duration.seconds(.5));
            slider.play();
        } else if (event.getSource() == rg) {
            slider.setNode(side_form);
            slider.setToX(0);
            slider.setDuration(Duration.seconds(.5));
            slider.play();
        }
    }

    // transferring user
    @FXML
    public void successlogin(Account loggedInUser) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/userdashboard.fxml"));
        Parent root = loader.load();
        UserdashboardController controller = loader.getController();
        controller.setUserData(loggedInUser); // Pass user data to controller
        Stage stage = (Stage) lg_email.getScene().getWindow(); // Get current stage
        stage.setScene(new Scene(root));
        stage.show();
    }

    // login function
    public void login() throws IOException {
        String email = lg_email.getText();
        String password = lg_pass.getText();
        AccountHandler accountHandler = new AccountHandler(databaseManager);
        Account user = accountHandler.authenticate(email, password);
        if (user != null) {
            successlogin(user);
        } else {
            invalid.setText("Invalid email or password");
            invalid.setVisible(true);
        }
    }

    // register function
    public void register(){
        rg_error.setVisible(false);
        rg_success.setVisible(false);

        String fullName = rg_name.getText();
        String email = rg_email.getText();
        String password = rg_pass.getText();
        String confirmPassword = rg_repass.getText();

        if (!password.equals(confirmPassword)) {
            rg_error.setText("Passwords don't match");
            rg_error.setVisible(true);
            return;
        }

        Account newUser = new Account(fullName, email, password, "user");
        AccountHandler accountHandler = new AccountHandler(databaseManager);
        if (accountHandler.saveAccount(newUser)) {
            rg_success.setText("Successfully registered!");
            rg_success.setVisible(true);
        } else {
            rg_error.setText("Registration failed. Please try again.");
            rg_error.setVisible(true);
        }
    }

    // back to main page
    @FXML
    private void backToMain() throws IOException {
        App.setRoot("main");
    }
}