package com.example.nathapong.oderfood.Model;

import java.util.List;

/**
 * Created by Nathapong on 14/3/2561.
 */

public class Request {

    private String email;
    private String phone;
    private String name;
    private String addressDetail;
    private String address;
    private String total;
    private String status;
    private String comment;
    private String orderDate;
    private String latlng;
    private String paymentStatus;
    private List<Order> foods;   // List of food order


    public Request() {
    }

    public Request(String email, String phone, String name,String addressDatail, String address, String total,
                   String status, String comment,String orderDate, String latlng, String paymentStatus,
                   List<Order> foods) {

        this.email = email;
        this.phone = phone;
        this.name = name;
        this.addressDetail = addressDatail;
        this.address = address;
        this.total = total;
        this.status = status;
        this.comment = comment;
        this.orderDate = orderDate;
        this.latlng = latlng;
        this.paymentStatus = paymentStatus;

        this.foods = foods;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }
}
