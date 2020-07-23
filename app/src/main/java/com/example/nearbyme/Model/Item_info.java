package com.example.nearbyme.Model;

public class Item_info {
    private String user_id;
    private String item_id;
    private double latitude;
    private double longitude;
    private String category;
    private String sub_category;
    private String item_name;
    private String brand_name;
    private String condition;
    private int price;
    private String description;
    private boolean status;

    public Item_info() {
    }

    public Item_info(String user_id, double latitude, double longitude, String category, String sub_category, String item_name, String brand_name, String condition, int price, String description,boolean status) {
        this.user_id = user_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.sub_category = sub_category;
        this.item_name = item_name;
        this.brand_name = brand_name;
        this.condition = condition;
        this.price = price;
        this.description = description;
        this.status=status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
