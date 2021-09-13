package com.example.recycleapplication.Model;

import java.util.ArrayList;
import java.util.List;

public class QuizResultModel {

    private String quizTitle;
    private Integer noAttempt;
    private List<String> date = new ArrayList<>();
    private List<String> result = new ArrayList<>();


    public QuizResultModel(String quizTitle, Integer noAttempt, List<String> date, List<String> result) {
        this.quizTitle = quizTitle;
        this.noAttempt = noAttempt;
        this.date = date;
        this.result = result;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
    }

    public Integer getNoAttempt() {
        return noAttempt;
    }

    public void setNoAttempt(Integer noAttempt) {
        this.noAttempt = noAttempt;
    }

    public List<String> getDate() {
        return date;
    }

    public void setDate(List<String> date) {
        this.date = date;
    }

    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }
}
