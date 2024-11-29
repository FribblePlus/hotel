package com.plaza.hotelmanagement.models.booking;

import com.plaza.hotelmanagement.enums.BookingStatus;
import com.plaza.hotelmanagement.models.account.Account;
import com.plaza.hotelmanagement.models.room.Room;

import java.sql.*;
import java.time.LocalDate;

public class Booking {
    private Integer bookingID;
    private Integer accountID;
    private Integer roomID;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private BookingStatus bookingStatus;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/hotel";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";


    public Booking(Integer accountID, Integer roomID, LocalDate checkInDate, LocalDate checkOutDate, BookingStatus bookingStatus) {
        this.accountID = accountID;
        this.roomID = roomID;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bookingStatus = bookingStatus;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public void setRoomID(Integer roomID) {
        this.roomID = roomID;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public Integer getBookingID() {
        return bookingID;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public Integer getRoomID() {
        return roomID;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public boolean createRoomBooking(Account user, Room room){
        String sql = "INSERT INTO roombooking (AccountID, RoomID, CheckInDate, CheckOutDate, BookingStatus) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, user.getAccountId());
            pstmt.setInt(2, room.getRoomID());
            pstmt.setString(3, checkInDate.toString());
            pstmt.setString(4, checkOutDate.toString());
            pstmt.setString(5, bookingStatus.toString());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.accountID = generatedKeys.getInt(1);
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
