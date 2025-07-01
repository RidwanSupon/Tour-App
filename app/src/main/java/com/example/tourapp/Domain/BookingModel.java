package com.example.tourapp.Domain;

public class BookingModel {
    public String id;
    public String userId;
    public String tripTitle;
    public String contactName;
    public String contactNumber;
    public int totalPeople;
    public int child;
    public String neededBedOrRoom;
    public long timestamp;

    public BookingModel() {
        // Default constructor needed for Firebase
    }

    public BookingModel(String id, String tripTitle, String contactName, String contactNumber,
                        int totalPeople, int child, String neededBedOrRoom, String userId, long timestamp) {
        this.id = id;
        this.tripTitle = tripTitle;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.totalPeople = totalPeople;
        this.child = child;
        this.neededBedOrRoom = neededBedOrRoom;
        this.userId = userId;
        this.timestamp = timestamp;
    }
}
