package com.plaza.hotelmanagement.models.room;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FilterRoom {
    private String roomStatus;
    private String roomStyle;
    private String priceSort;

    // Constructor
    public FilterRoom(String roomStatus, String roomStyle, String priceSort){
        this.roomStatus = roomStatus;
        this.roomStyle = roomStyle;
        this.priceSort = priceSort;
    }

    //Preparing SQL query
    public String prepareQuery(){
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM room ");
        List<String> conditions = new ArrayList<>();
        if(roomStatus==null){
            conditions.add("RoomStatus = 'Available'");
        }else if(!Objects.equals(roomStatus, "All rooms")){
            conditions.add("RoomStatus = '" + roomStatus+"'");
        }
        conditions.add("RoomStatus = 'Available'");
        if (!Objects.equals(roomStyle, "All style")) {
            conditions.add("RoomStyle = '"+ roomStyle+"'");
        }

        if (!conditions.isEmpty()) {
            queryBuilder.append(" WHERE ").append(String.join(" AND ", conditions));
        }
        if(Objects.equals(priceSort, "Expensive")){
            queryBuilder.append(" ORDER BY BookingPrice DESC");
        }else if(Objects.equals(priceSort, "Cheap")){
            queryBuilder.append(" ORDER BY BookingPrice ASC");
        }
        System.out.println(queryBuilder);
        return queryBuilder.toString();
    }

    // Getter Setters
    public String getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }

    public String getRoomStyle() {
        return roomStyle;
    }

    public void setRoomStyle(String roomStyle) {
        this.roomStyle = roomStyle;
    }

    public String getPriceSort() {
        return priceSort;
    }

    public void setPriceSort(String priceSort) {
        this.priceSort = priceSort;
    }
}
