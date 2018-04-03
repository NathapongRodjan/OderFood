package com.example.nathapong.oderfood.JsonApi;

import com.example.nathapong.oderfood.JsonModel.DataList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Service {

    String BASE_URL = "http://www.codemp.net/json/";

    @GET("foods.json")
    Call<DataList> getCategoryList();
}
