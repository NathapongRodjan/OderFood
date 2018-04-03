package com.example.nathapong.oderfood.JsonModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataList {

    @SerializedName("data")
    private ArrayList<Category> categoryList;

    public DataList(ArrayList<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public ArrayList<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(ArrayList<Category> categoryList) {
        this.categoryList = categoryList;
    }
}
