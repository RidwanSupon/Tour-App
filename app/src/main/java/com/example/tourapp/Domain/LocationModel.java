package com.example.tourapp.Domain;

public class LocationModel {
    public int id;
    public String loc;

    // Default constructor প্রয়োজন Firebase এর জন্য
    public LocationModel() {
    }

    public LocationModel(int id, String loc) {
        this.id = id;
        this.loc = loc;
    }

    public String getLoc() {
        return loc;
    }
}
