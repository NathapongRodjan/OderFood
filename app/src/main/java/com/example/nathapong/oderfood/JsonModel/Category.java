package com.example.nathapong.oderfood.JsonModel;

import java.util.ArrayList;

public class Category {

    private String categoryName;
    private ArrayList<FoodItem> item;

    public Category() {
    }

    public Category(String categoryName, ArrayList<FoodItem> item) {
        this.categoryName = categoryName;
        this.item = item;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ArrayList<FoodItem> getItem() {
        return item;
    }

    public void setItem(ArrayList<FoodItem> item) {
        this.item = item;
    }
}

