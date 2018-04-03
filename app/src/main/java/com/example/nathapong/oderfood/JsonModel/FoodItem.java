package com.example.nathapong.oderfood.JsonModel;

public class FoodItem {

    private String name;
    private String shotDetail;
    private String image;

    public FoodItem(String name, String shotDetail, String image) {
        this.name = name;
        this.shotDetail = shotDetail;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShotDetail() {
        return shotDetail;
    }

    public void setShotDetail(String shotDetail) {
        this.shotDetail = shotDetail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
