package com.example.nearbyme.Model;

public class Restaurant_info {
    private String user_id;
    private double latitude;
    private double longitude;
    private String res_type;
    private String res_name;
    private int opening_hour;
    private int opening_minute;
    private int closing_hour;
    private int closing_minute;
    private String description;
    private boolean home_delivery;
    private boolean table_reservation;

    public Restaurant_info() {
    }

    public Restaurant_info(String user_id, double latitude, double longitude, String res_type, String res_name, int opening_hour, int opening_minute, int closing_hour, int closing_minute, String description, boolean home_delivery, boolean table_reservation) {
        this.user_id = user_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.res_type = res_type;
        this.res_name = res_name;
        this.opening_hour = opening_hour;
        this.opening_minute = opening_minute;
        this.closing_hour = closing_hour;
        this.closing_minute = closing_minute;
        this.description = description;
        this.home_delivery = home_delivery;
        this.table_reservation = table_reservation;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getRes_type() {
        return res_type;
    }

    public void setRes_type(String res_type) {
        this.res_type = res_type;
    }

    public String getRes_name() {
        return res_name;
    }

    public void setRes_name(String res_name) {
        this.res_name = res_name;
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

    public boolean isHome_delivery() {
        return home_delivery;
    }

    public void setHome_delivery(boolean home_delivery) {
        this.home_delivery = home_delivery;
    }

    public boolean isTable_reservation() {
        return table_reservation;
    }

    public void setTable_reservation(boolean table_reservation) {
        this.table_reservation = table_reservation;
    }
}
