package com.plaza.hotelmanagement.controllers;

import com.plaza.hotelmanagement.database.DatabaseManager;
import com.plaza.hotelmanagement.models.room.Room;
import com.plaza.hotelmanagement.models.room.FilterRoom;
import com.plaza.hotelmanagement.models.room.HandleRoom;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;

import java.net.URL;
import java.util.List;
import java.io.IOException;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML //Main section of every page
    private GridPane roomGrid;
    @FXML //Sort section of page
    private HBox sortBox;

    // Initial function
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayFilter();
        loadRoomsFromDatabase("All rooms", "All style", "Sort");
    }

    // Loads from Database
    private void loadRoomsFromDatabase(String availability, String roomStyle, String sort) {
        FilterRoom filterRoom = new FilterRoom(availability, roomStyle, sort);
        DatabaseManager dbManager = new DatabaseManager();
        HandleRoom handleRoom = new HandleRoom(dbManager);
        List<Room> rooms = handleRoom.fetchRooms(filterRoom.prepareQuery());
        displayRooms(rooms);
    }

    // Displays filter section
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
        roomStyle.setOnAction(e -> loadRoomsFromDatabase(null, roomStyle.getValue(), sort.getValue()));
        sort.setOnAction(e -> loadRoomsFromDatabase(null, roomStyle.getValue(), sort.getValue()));

        // Create the HBox and set its properties

        sortBox.setPrefHeight(22.0);
        sortBox.setPrefWidth(134.0);

        // Add margin for the 'sort' ComboBox
        HBox.setMargin(sort, new Insets(0, 0, 0, 10.0));

        // Add the ComboBoxes to the HBox
        sortBox.getChildren().addAll(roomStyle, sort);

    }

    // Displays fetched rooms as a cards
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

    // Creates room cards
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
        bookButton.setOnAction(e -> handleBookRoom(room));
        bookButton.setDisable(!room.getRoomStatus().equals("Available"));

        HBox buttonBox = new HBox(bookButton);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        buttonBox.setPadding(new Insets(8, 0, 0, 0));

        contentBox.getChildren().addAll(roomLabel, priceLabel, roomInfoBox, buttonBox);
        card.getChildren().addAll(imageView, contentBox);

        return card;
    }

    // is called when book button is pressed
    private void handleBookRoom(Room room) {
        // Implement booking logic here
        System.out.println("Booking room: " + room.getRoomStyle());
    }


    @FXML //Changing page to login/register page
    private void loginRegister() throws IOException {
        App.setRoot("login");
    }
}