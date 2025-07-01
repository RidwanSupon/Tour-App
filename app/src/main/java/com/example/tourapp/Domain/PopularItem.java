package com.example.tourapp.Domain;

public class PopularItem {
    private String title;
    private String address;
    private int bed; // 🔄 আগের bedCount → bed
    private String duration;
    private double score;
    private int price;
    private String pic;
    private String tourGuideName;
    private String tourGuidePic;

    // 🔸 Default constructor (Firebase requires this)
    public PopularItem() {
    }

    // 🔸 Optional: Full constructor (useful for testing or manual creation)
    public PopularItem(String title, String address, int bed, String duration, double score, int price,
                       String pic, String tourGuideName, String tourGuidePic) {
        this.title = title;
        this.address = address;
        this.bed = bed;
        this.duration = duration;
        this.score = score;
        this.price = price;
        this.pic = pic;
        this.tourGuideName = tourGuideName;
        this.tourGuidePic = tourGuidePic;
    }

    // 🔸 Getters
    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public int getBed() {
        return bed;
    }

    public String getDuration() {
        return duration;
    }

    public double getScore() {
        return score;
    }

    public int getPrice() {
        return price;
    }

    public String getPic() {
        return pic;
    }

    public String getTourGuideName() {
        return tourGuideName;
    }

    public String getTourGuidePic() {
        return tourGuidePic;
    }

    // 🔸 Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBed(int bed) {
        this.bed = bed;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setTourGuideName(String tourGuideName) {
        this.tourGuideName = tourGuideName;
    }

    public void setTourGuidePic(String tourGuidePic) {
        this.tourGuidePic = tourGuidePic;
    }
}
