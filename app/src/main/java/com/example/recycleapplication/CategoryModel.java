package com.example.recycleapplication;

public class CategoryModel {


    private String title;
    private String imagePath;
    private int viewType;

    public CategoryModel(String title, String imagePath, int viewType) {
        this.title = title;
        this.imagePath = imagePath;
        this.viewType = viewType;
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

    public int getViewType() { return viewType; }

    public void setViewType(int viewType) { this.viewType = viewType; }

}
