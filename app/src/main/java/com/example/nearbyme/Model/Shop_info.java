package com.example.nearbyme.Model;

public class Shop_info {
    private String user_id;
    private String shop_id;
    private double latitude;
    private double longitude;
    private String dimension;
    private int rent_amount;
    private int security_amount;
    private String floor;
    private String description;
    private boolean finishing;
    private boolean store;
    private boolean parking;
    private boolean status;

    public Shop_info() {
    }

    public Shop_info(String user_id, double latitude, double longitude, String dimension, int rent_amount, int security_amount, String floor, String description, boolean finishing, boolean store, boolean parking, boolean status) {
        this.user_id = user_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dimension = dimension;
        this.rent_amount = rent_amount;
        this.security_amount = security_amount;
        this.floor = floor;
        this.description = description;
        this.finishing = finishing;
        this.store = store;
        this.parking = parking;
        this.status=status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
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

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public int getRent_amount() {
        return rent_amount;
    }

    public void setRent_amount(int rent_amount) {
        this.rent_amount = rent_amount;
    }

    public int getSecurity_amount() {
        return security_amount;
    }

    public void setSecurity_amount(int security_amount) {
        this.security_amount = security_amount;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFinishing() {
        return finishing;
    }

    public void setFinishing(boolean finishing) {
        this.finishing = finishing;
    }

    public boolean isStore() {
        return store;
    }

    public void setStore(boolean store) {
        this.store = store;
    }

    public boolean isParking() {
        return parking;
    }

    public void setParking(boolean parking) {
        this.parking = parking;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
