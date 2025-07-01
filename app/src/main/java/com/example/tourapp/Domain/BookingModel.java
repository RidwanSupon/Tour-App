package com.example.tourapp.Domain;

public class BookingModel {

    private String bookingId;
    private String tripTitle;
    private String contactName;
    private String contactPhone;
    private int adultCount;
    private int childCount;
    private String userId;
    private long timestamp;

    // Default constructor required for Firebase
    public BookingModel() {
    }

    public BookingModel(String bookingId, String tripTitle,
                        String contactName, String contactPhone,
                        int adultCount, int childCount,
                        String userId, long timestamp) {
        this.bookingId = bookingId;
        this.tripTitle = tripTitle;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.adultCount = adultCount;
        this.childCount = childCount;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    // Getters and setters

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getTripTitle() {
        return tripTitle;
    }

    public void setTripTitle(String tripTitle) {
        this.tripTitle = tripTitle;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public int getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(int adultCount) {
        this.adultCount = adultCount;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
