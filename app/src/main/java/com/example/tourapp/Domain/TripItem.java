package com.example.tourapp.Domain;

public class TripItem {

    private int id; // ✅ Newly added field to match database
    private String title;
    private String address;
    private int price;
    private String duration;
    private int bed;
    private String tourGuideName;
    private double score;
    private String pic;
    private String tourGuidePic;

    // Extra fields to match your database
    private String dateTour;
    private String description;
    private String distance;
    private String timeTour;
    private String tourGuidePhone;

    // নতুন timestamp ফিল্ড
    private long timestamp;

    public TripItem() {
        // Default constructor required for DataSnapshot.getValue(TripItem.class)
    }

    public TripItem(int id, String title, String address, int price, String duration, int bed,
                    String tourGuideName, double score, String pic, String tourGuidePic,
                    String dateTour, String description, String distance, String timeTour,
                    String tourGuidePhone, long timestamp) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.price = price;
        this.duration = duration;
        this.bed = bed;
        this.tourGuideName = tourGuideName;
        this.score = score;
        this.pic = pic;
        this.tourGuidePic = tourGuidePic;
        this.dateTour = dateTour;
        this.description = description;
        this.distance = distance;
        this.timeTour = timeTour;
        this.tourGuidePhone = tourGuidePhone;
        this.timestamp = timestamp;
    }

    // Getters

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAddress() { return address; }
    public int getPrice() { return price; }
    public String getDuration() { return duration; }
    public int getBed() { return bed; }
    public String getTourGuideName() { return tourGuideName; }
    public double getScore() { return score; }
    public String getPic() { return pic; }
    public String getTourGuidePic() { return tourGuidePic; }
    public String getDateTour() { return dateTour; }
    public String getDescription() { return description; }
    public String getDistance() { return distance; }
    public String getTimeTour() { return timeTour; }
    public String getTourGuidePhone() { return tourGuidePhone; }
    public long getTimestamp() { return timestamp; }

    // Setters

    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setAddress(String address) { this.address = address; }
    public void setPrice(int price) { this.price = price; }
    public void setDuration(String duration) { this.duration = duration; }
    public void setBed(int bed) { this.bed = bed; }
    public void setTourGuideName(String tourGuideName) { this.tourGuideName = tourGuideName; }
    public void setScore(double score) { this.score = score; }
    public void setPic(String pic) { this.pic = pic; }
    public void setTourGuidePic(String tourGuidePic) { this.tourGuidePic = tourGuidePic; }
    public void setDateTour(String dateTour) { this.dateTour = dateTour; }
    public void setDescription(String description) { this.description = description; }
    public void setDistance(String distance) { this.distance = distance; }
    public void setTimeTour(String timeTour) { this.timeTour = timeTour; }
    public void setTourGuidePhone(String tourGuidePhone) { this.tourGuidePhone = tourGuidePhone; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
