package com.example.recycleapplication;

public class CategoryModel {


    private String title;
    private String imagePath;

    public CategoryModel(String title, String imagePath) {
        this.title = title;
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

}
