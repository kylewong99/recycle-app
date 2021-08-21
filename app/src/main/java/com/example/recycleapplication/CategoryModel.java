package com.example.recycleapplication;

public class CategoryModel {

    private String quizId;
    private String title;

    public CategoryModel(String quizId, String title) {
        this.quizId = quizId;
        this.title = title;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
