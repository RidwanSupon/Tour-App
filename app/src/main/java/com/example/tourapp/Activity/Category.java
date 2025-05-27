package com.example.tourapp.Activity;

public class Category {
    private int Id;
    private String ImagePath;
    private String Name;

    public Category() {
        // Default constructor required for Firebase
    }

    public Category(int id, String name, String imagePath) {
        this.Id = id;
        this.Name = name;
        this.ImagePath = imagePath;
    }

    public int getId() {
        return Id;
    }
    public void setId(int id) {
        this.Id = id;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        this.Name = name;
    }
    public String getImagePath() {
        return ImagePath;
    }
    public void setImagePath(String imagePath) {
        this.ImagePath = imagePath;
    }
}
