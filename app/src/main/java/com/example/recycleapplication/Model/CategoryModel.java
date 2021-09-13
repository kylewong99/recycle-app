package com.example.recycleapplication.Model;

public class CategoryModel {


    private String title;
    private String imagePath;
    private String id;
    private int viewType;
    private int noClicked;

    public CategoryModel(String title, String imagePath, String id, int viewType) {
        this.title = title;
        this.imagePath = imagePath;
        this.id = id;
        this.viewType = viewType;
    }

    public CategoryModel(String title, String imagePath, String id, int noClicked, int viewType) {
        this.title = title;
        this.imagePath = imagePath;
        this.id = id;
        this.viewType = viewType;
        this.noClicked = noClicked;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public int getNoClicked() {
        return noClicked;
    }

    public void setNoClicked(int noClicked) {
        this.noClicked = noClicked;
    }
}
