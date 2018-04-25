package com.example.nathapong.oderfood.JsonModel;

import android.os.Parcel;
import android.os.Parcelable;

public class FoodItem implements Parcelable{

    private String name;
    private String shotDetail;
    private String image;

    public FoodItem(String name, String shotDetail, String image) {
        this.name = name;
        this.shotDetail = shotDetail;
        this.image = image;
    }

    protected FoodItem(Parcel in) {
        name = in.readString();
        shotDetail = in.readString();
        image = in.readString();
    }

    public static final Creator<FoodItem> CREATOR = new Creator<FoodItem>() {
        @Override
        public FoodItem createFromParcel(Parcel in) {
            return new FoodItem(in);
        }

        @Override
        public FoodItem[] newArray(int size) {
            return new FoodItem[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(shotDetail);
        dest.writeString(image);
    }
}
