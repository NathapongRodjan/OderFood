package com.example.nathapong.oderfood.Model;


public class Rating {

    private String userEmail;
    private String userName;
    private String foodId;
    private String rateValue;
    private String comment;
    private String date;

    public Rating() {
    }

    public Rating(String userEmail, String userName, String foodId, String rateValue, String comment, String date) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.foodId = foodId;
        this.rateValue = rateValue;
        this.comment = comment;
        this.date = date;
    }


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
