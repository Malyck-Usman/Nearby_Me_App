package com.example.nearbyme.Model;

public class Home_Store_info {
    private String user_id;
    private String homestore_id;
    private double latitude;
    private double longitude;
    private String store_type;
    private String store_name;
    private int opening_hour;
    private int opening_minute;
    private int closing_hour;
    private int closing_minute;
    private String description;
    private boolean status;

    public Home_Store_info() {
    }

    public Home_Store_info(String user_id, double latitude, double longitude, String store_type, String store_name, int opening_hour, int opening_minute, int closing_hour, int closing_minute, String description,boolean status) {
        this.user_id = user_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.store_type = store_type;
        this.store_name = store_name;
        this.opening_hour = opening_hour;
        this.opening_minute = opening_minute;
        this.closing_hour = closing_hour;
        this.closing_minute = closing_minute;
        this.description = description;
        this.status=status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getHomestore_id() {
        return homestore_id;
    }

    public void setHomestore_id(String homestore_id) {
        this.homestore_id = homestore_id;
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

    public String getStore_type() {
        return store_type;
    }

    public void setStore_type(String store_type) {
        this.store_type = store_type;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public int getOpening_hour() {
        return opening_hour;
    }

    public void setOpening_hour(int opening_hour) {
        this.opening_hour = opening_hour;
    }

    public int getOpening_minute() {
        return opening_minute;
    }

    public void setOpening_minute(int opening_minute) {
        this.opening_minute = opening_minute;
    }

    public int getClosing_hour() {
        return closing_hour;
    }

    public void setClosing_hour(int closing_hour) {
        this.closing_hour = closing_hour;
    }

    public int getClosing_minute() {
        return closing_minute;
    }

    public void setClosing_minute(int closing_minute) {
        this.closing_minute = closing_minute;
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
