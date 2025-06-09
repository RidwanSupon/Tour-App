package com.example.tourapp.Domain;

public class RecommendedItem {
    private String title;
    private String address;
    private int bedCount;
    private String duration;
    private double score;
    private int price;
    private String pic;
    private String tourGuideName;
    private String tourGuidePic;

    // No-arg constructor required by Firebase
    public RecommendedItem() {
    }

    public RecommendedItem(String title, String address, int bedCount, String duration, double score,
                           int price, String pic, String tourGuideName, String tourGuidePic) {
        this.title = title;
        this.address = address;
        this.bedCount = bedCount;
        this.duration = duration;
        this.score = score;
        this.price = price;
        this.pic = pic;
        this.tourGuideName = tourGuideName;
        this.tourGuidePic = tourGuidePic;
    }

    // Getters
    public String getTitle() { return title; }
    public String getAddress() { return address; }
    public int getBedCount() { return bedCount; }
    public String getDuration() { return duration; }
    public double getScore() { return score; }
    public int getPrice() { return price; }
    public String getPic() { return pic; }
    public String getTourGuideName() { return tourGuideName; }
    public String getTourGuidePic() { return tourGuidePic; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setAddress(String address) { this.address = address; }
    public void setBedCount(int bedCount) { this.bedCount = bedCount; }
    public void setDuration(String duration) { this.duration = duration; }
    public void setScore(double score) { this.score = score; }
    public void setPrice(int price) { this.price = price; }
    public void setPic(String pic) { this.pic = pic; }
    public void setTourGuideName(String tourGuideName) { this.tourGuideName = tourGuideName; }
    public void setTourGuidePic(String tourGuidePic) { this.tourGuidePic = tourGuidePic; }
}
