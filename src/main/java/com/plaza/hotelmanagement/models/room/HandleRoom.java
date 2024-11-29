package com.plaza.hotelmanagement.models.room;

import com.plaza.hotelmanagement.database.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HandleRoom {
    private final DatabaseManager dbManager;

    public HandleRoom(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public List<Room> fetchRooms(String query) {
        List<Room> rooms = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                Room room = new Room(
                        rs.getInt("RoomID"),
                        rs.getString("RoomStyle"),
                        rs.getDouble("BookingPrice"),
                        rs.getString("RoomStatus"),
                        rs.getInt("RoomNumber"),
                        rs.getBoolean("IsClean"),
                        rs.getString("ImageURL")
                );
                rooms.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error fetching rooms: " + e.getMessage());
        }

        if (rooms.isEmpty()) {
            System.out.println("No rooms fetched from the database.");
        }
        return rooms;
    }

    public boolean insertRoom(Room room) {
        String insertQuery = "INSERT INTO room (RoomStyle, BookingPrice, RoomStatus, RoomNumber, IsClean, ImageURL) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, room.getRoomID());
            pstmt.setString(2, room.getRoomStyle());
            pstmt.setDouble(3, room.getBookingPrice());
            pstmt.setString(4, room.getRoomStatus());
            pstmt.setInt(5, room.getRoomNumber());
            pstmt.setBoolean(6, room.getClean());
            pstmt.setString(7, room.getImageURL());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        room.setRoomID(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
        e.printStackTrace();
        System.err.println("Error deleting room from database: " + e.getMessage());
    }
       return false;
    }
}
