package com.plaza.hotelmanagement.models.room;

import com.plaza.hotelmanagement.enums.RoomStatus;
import com.plaza.hotelmanagement.enums.RoomStyle;

public class Room {
    private int roomID;
    private final RoomStyle roomStyle;
    private final double bookingPrice;
    private RoomStatus roomStatus;
    private final int roomNumber;
    private Boolean isClean;
    private final String imageURL;

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public void setClean(Boolean clean) {
        isClean = clean;
    }

    public void setRoomStatus(RoomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }

    public String getRoomStyle() {
        return roomStyle.toString();
    }

    public double getBookingPrice() {
        return bookingPrice;
    }

    public String getRoomStatus() {
        return roomStatus.toString();
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public Boolean getClean() {
        return isClean;
    }

    public String getImageURL() {
        return imageURL;
    }

    public Room(int roomID, String roomStyle, double bookingPrice, String roomStatus, int roomNumber, boolean isClean, String imageURL) {
        this.roomID = roomID;
        this.roomStatus = RoomStatus.valueOf(roomStatus);
        this.roomStyle = RoomStyle.valueOf(roomStyle);
        this.bookingPrice = bookingPrice;
        this.isClean = isClean;
        this.imageURL = imageURL;
        this.roomNumber = roomNumber;
    }
}