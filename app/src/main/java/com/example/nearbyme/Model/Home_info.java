package com.example.nearbyme.Model;

public class Home_info {
    private String user_id;
    private String home_id;
    private double latitude;
    private double longitude;
    private float total_area;
    private float covered_area;
    private int rent_amount;
    private int security_amount;
    private String floor;
    private int rooms;
    private int bathrooms;
    private int kitchens;
    private String description;
    private boolean furnished;
    private boolean gas;
    private boolean water;
    private boolean garage;
    private boolean lawn;

    public Home_info() {
    }

    public Home_info(String user_id, double latitude, double longitude, float total_area, float covered_area, int rent_amount, int security_amount, String floor, int rooms, int bathrooms, int kitchens, String description, boolean furnished, boolean gas, boolean water, boolean garage, boolean lawn) {
        this.user_id = user_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.total_area = total_area;
        this.covered_area = covered_area;
        this.rent_amount = rent_amount;
        this.security_amount = security_amount;
        this.floor = floor;
        this.rooms = rooms;
        this.bathrooms = bathrooms;
        this.kitchens = kitchens;
        this.description = description;
        this.furnished = furnished;
        this.gas = gas;
        this.water = water;
        this.garage = garage;
        this.lawn = lawn;
    }

    public String getHome_id() {
        return home_id;
    }

    public void setHome_id(String home_id) {
        this.home_id = home_id;
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

    public float getTotal_area() {
        return total_area;
    }

    public void setTotal_area(float total_area) {
        this.total_area = total_area;
    }

    public float getCovered_area() {
        return covered_area;
    }

    public void setCovered_area(float covered_area) {
        this.covered_area = covered_area;
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

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(int bathrooms) {
        this.bathrooms = bathrooms;
    }

    public int getKitchens() {
        return kitchens;
    }

    public void setKitchens(int kitchens) {
        this.kitchens = kitchens;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFurnished() {
        return furnished;
    }

    public void setFurnished(boolean furnished) {
        this.furnished = furnished;
    }

    public boolean isGas() {
        return gas;
    }

    public void setGas(boolean gas) {
        this.gas = gas;
    }

    public boolean isWater() {
        return water;
    }

    public void setWater(boolean water) {
        this.water = water;
    }

    public boolean isGarage() {
        return garage;
    }

    public void setGarage(boolean garage) {
        this.garage = garage;
    }

    public boolean isLawn() {
        return lawn;
    }

    public void setLawn(boolean lawn) {
        this.lawn = lawn;
    }
}
