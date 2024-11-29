package com.plaza.hotelmanagement.controllers;

import com.plaza.hotelmanagement.database.DatabaseManager;
import com.plaza.hotelmanagement.enums.BookingStatus;
import com.plaza.hotelmanagement.models.account.AccountHandler;
import com.plaza.hotelmanagement.models.room.Room;
import com.plaza.hotelmanagement.models.booking.Booking;
import com.plaza.hotelmanagement.models.account.Account;
import com.plaza.hotelmanagement.models.room.FilterRoom;
import com.plaza.hotelmanagement.models.room.HandleRoom;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.text.Text;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.io.IOException;
import java.util.Objects;
import java.util.ResourceBundle;

public class UserdashboardController implements Initializable {

    @FXML
    private GridPane roomGrid;
    @FXML
    private Label accountNameLabel;
    @FXML
    private HBox sortBox;
    @FXML
    private Label accountEmailLabel;


    private Account loggedInUser;
    DatabaseManager databaseManager = new DatabaseManager();
    AccountHandler accountHandler = new AccountHandler(databaseManager);


    public void setUserData(Account user) {
        this.loggedInUser = user;
        updateAccountDetails();
    }

    private void updateAccountDetails() {
        accountEmailLabel.setText(loggedInUser.getEmail());
        accountNameLabel.setText(loggedInUser.getFullName());
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        displayFilter();
        loadRoomsFromDatabase("All rooms", "All style", "Sort");
    }

    private void loadRoomsFromDatabase(String availability, String roomStyle, String sort) {
        sortBox.setVisible(true);
        FilterRoom filterRoom = new FilterRoom(availability, roomStyle, sort);
        DatabaseManager dbManager = new DatabaseManager();
        HandleRoom handleRoom = new HandleRoom(dbManager);
        List<Room> rooms = handleRoom.fetchRooms(filterRoom.prepareQuery());
        displayRooms(rooms);
    }

    private void displayFilter(){
        // Create the first ComboBox for room style
        ComboBox<String> roomStyle = new ComboBox<>();
        roomStyle.setPrefWidth(150.0);
        roomStyle.setItems(FXCollections.observableArrayList("All style", "Standard", "Deluxe", "FamilySuite", "BusinessSuite"));
        roomStyle.setValue("All style");
        // Create the second ComboBox for sorting
        ComboBox<String> sort = new ComboBox<>();
        sort.setPrefWidth(150.0);
        sort.setItems(FXCollections.observableArrayList("Sort", "Expensive", "Cheap"));
        sort.setValue("Sort");
        // Set event handlers for the ComboBoxes
        roomStyle.setOnMouseClicked(e -> loadRoomsFromDatabase(null, roomStyle.getValue(), sort.getValue()));
        sort.setOnMouseClicked(e -> loadRoomsFromDatabase(null, roomStyle.getValue(), sort.getValue()));

        // Create the HBox and set its properties

        sortBox.setPrefHeight(22.0);
        sortBox.setPrefWidth(134.0);

        // Add margin for the 'sort' ComboBox
        sortBox.setMargin(sort, new Insets(0, 0, 0, 10.0));

        // Add the ComboBoxes to the HBox
        sortBox.getChildren().addAll(roomStyle, sort);
        System.out.println("Should be printed");
    }

    private void displayRooms(List<Room> rooms) {
        roomGrid.getChildren().clear();
        int column = 0;
        int row = 0;
        if(rooms.isEmpty()){
            Label noRoomLabel = new Label("No rooms found");
            noRoomLabel.getStyleClass().add("no-room-label");
            roomGrid.add(noRoomLabel, 0, 0); // Positioning the label at the top-left of the grid.
            GridPane.setColumnSpan(noRoomLabel, 2); // Spanning across columns if required.
            roomGrid.setAlignment(Pos.CENTER);
        }else{
            for (Room room : rooms) {
                VBox card = createRoomCard(room);
                roomGrid.add(card, column, row);

                column++;
                if (column == 2) {
                    column = 0;
                    row++;
                }
            }
        }
    }

    private VBox createRoomCard(Room room) {
        VBox card = new VBox(0);
        card.getStyleClass().add("room-card");
        card.setPrefWidth(280);
        card.setMaxWidth(280);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(280);
        imageView.setFitHeight(180);
        imageView.getStyleClass().add("room-card-image");

        try {
            Image image = new Image(room.getImageURL(), true);
            image.errorProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    imageView.setImage(new Image("/img/placeholder.png"));
                }
            });
            imageView.setImage(image);
        } catch (Exception e) {
            imageView.setImage(new Image("/img/placeholder.png"));
        }

        VBox contentBox = new VBox(8);
        contentBox.setPadding(new Insets(16, 5, 5, 5));
        contentBox.getStyleClass().add("room-card-content");

        Label roomLabel = new Label("#" + room.getRoomNumber() + " " + room.getRoomStyle());
        roomLabel.getStyleClass().add("room-card-title");

        Label priceLabel = new Label(String.format("$%.2f per night", room.getBookingPrice()));
        priceLabel.getStyleClass().add("room-card-price");

        HBox roomInfoBox = new HBox(5);
        roomInfoBox.setAlignment(Pos.CENTER_LEFT);

        Label roomStatusLabel = new Label(room.getRoomStatus());
        roomStatusLabel.getStyleClass().add("room-card-info");


        roomInfoBox.getChildren().addAll(roomStatusLabel);

        Button bookButton = new Button("Book");
        bookButton.getStyleClass().add("shadcn-button");
        bookButton.setOnMouseClicked(e -> displayForm(room));
        bookButton.setDisable(!room.getRoomStatus().equals("Available"));

        HBox buttonBox = new HBox(bookButton);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        buttonBox.setPadding(new Insets(8, 0, 0, 0));

        contentBox.getChildren().addAll(roomLabel, priceLabel, roomInfoBox, buttonBox);
        card.getChildren().addAll(imageView, contentBox);

        return card;
    }

    private void displayForm(Room room) {
        roomGrid.getChildren().clear();
        sortBox.setVisible(false);
        GridPane form = createForm(room);
        roomGrid.add(form, 0, 0);
    }

    private GridPane createForm(Room room) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
//
//        // Room Style ComboBox
//        Label roomStyleLabel = new Label("Room Style:");
//        ComboBox<RoomStyle> roomStyleComboBox = new ComboBox<>();
//        roomStyleComboBox.getItems().addAll(RoomStyle.values());
//        roomStyleComboBox.setValue(RoomStyle.Standard);  // Default value
//        grid.add(roomStyleLabel, 0, 0);
//        grid.add(roomStyleComboBox, 1, 0);

        // Check-in DatePicker
        Label checkInLabel = new Label("Check-in Date:");
        DatePicker checkInDatePicker = new DatePicker();
        checkInDatePicker.setValue(LocalDate.now());  // Default value as today's date
        grid.add(checkInLabel, 0, 1);
        grid.add(checkInDatePicker, 0, 2);

        // Check-out DatePicker
        Label checkOutLabel = new Label("Check-out Date:");
        DatePicker checkOutDatePicker = new DatePicker();
        checkOutDatePicker.setValue(LocalDate.now().plusDays(1));  // Default value as one day after check-in
        grid.add(checkOutLabel, 1, 1);
        grid.add(checkOutDatePicker, 1, 2);

        // Submit Button
        Button submitButton = new Button("Book");
        submitButton.getStyleClass().add("shadcn-button");
        grid.add(submitButton, 2, 2);
        submitButton.setOnMouseClicked(e -> submitForm(room, checkInDatePicker, checkOutDatePicker));


        // Cancel Button
        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("cancel-button");
        grid.add(cancelButton, 3, 2);
        cancelButton.setOnMouseClicked(e -> {loadRoomsFromDatabase("All rooms", "All style", "Sort");});
        return grid;
    }

    @FXML
    private void displayAccountForm() {
        roomGrid.getChildren().clear();
        sortBox.setVisible(false);
        GridPane form = createAccountForm();
        roomGrid.add(form, 0, 0);
    }

    private GridPane createAccountForm() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);
        // Full name
        Label fullNameLabel = new Label("Full name");
        TextField fullName = new TextField(loggedInUser.getFullName());
        fullName.getStyleClass().add("input");
        grid.add(fullNameLabel, 0, 1);
        grid.add(fullName, 0, 2);

        // Email
        Label emailLable = new Label("Email");
        TextField email = new TextField(loggedInUser.getEmail());
        email.getStyleClass().add("input");
        grid.add(emailLable, 1, 1);
        grid.add(email, 1, 2);

        // Previus password
        Label currentPassLabel = new Label("Current password");
        TextField currentPass = new TextField();
        currentPass.getStyleClass().add("input");
        grid.add(currentPassLabel, 0, 3);
        grid.add(currentPass, 0, 4);

        // Email
        Label newPassLabel = new Label("NewPassword");
        TextField newPass = new TextField();
        newPass.getStyleClass().add("input");
        grid.add(newPassLabel, 1, 3);
        grid.add(newPass, 1, 4);

        // Button holder
        HBox buttonHold = new HBox();
        buttonHold.setSpacing(10);
        grid.add(buttonHold, 0, 5);

        // save Button
        Button submitButton = new Button("Save");
        submitButton.getStyleClass().add("shadcn-button");
        buttonHold.getChildren().add(submitButton);

        submitButton.setOnMouseClicked(e -> {
            if(!currentPass.getText().isEmpty() && Objects.equals(currentPass.getText(), newPass.getText())){
                System.out.println("shouldn't be the same");
                Text error = new Text("Shouldn't be the same");
                grid.add(error, 1, 5);
            } else if (!currentPass.getText().isEmpty() && accountHandler.authenticate(loggedInUser.getEmail(), currentPass.toString())==null) {
                System.out.println("current pass is wrong");
                Text error = new Text("Current password is wrong");
                grid.add(error, 1, 5);
            } else {
                Account editedAccount = accountHandler.editAccount(loggedInUser, fullName.getText(), email.getText(), newPass.getText());
                if(editedAccount!=null){
                    setUserData(editedAccount);
                    System.out.println("Sucess");
                    Text error = new Text("Success");
                    grid.add(error, 1, 5);
                }else{
                    Text error = new Text("Error while saving");
                    grid.add(error, 1, 5);
                }
            }
        });

        // Cancel Button
        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("cancel-button");
        grid.add(cancelButton, 0, 5);
        buttonHold.getChildren().add(cancelButton);

        cancelButton.setOnMouseClicked(e -> {loadRoomsFromDatabase("All rooms", "All style", "Sort");});
        return grid;


    }

    public void submitForm(Room room, DatePicker checkInDate, DatePicker checkOutDate){
        Booking newBook = new Booking(loggedInUser.getAccountId(), room.getRoomID(), checkInDate.getValue(), checkOutDate.getValue(), BookingStatus.Requested);
        System.out.println(newBook.createRoomBooking(loggedInUser, room));
    }

    @FXML
    private void backToMain() throws IOException {
        App.setRoot("main");
    }
}

