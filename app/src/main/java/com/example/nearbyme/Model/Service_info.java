package com.example.nearbyme.Model;

public class Service_info {
    private String user_id;
    private String service_id;
    private double latitude;
    private double longitude;
    private String service_type;
    private String speciality;
    private int experience;
    private String charge_type;
    private int charge_amount;
    private String description;
    private boolean status;

    public Service_info() {
    }

    public Service_info(String user_id, double latitude, double longitude, String service_type, String speciality, int experience, String charge_type, int charge_amount, String description, boolean status) {
        this.user_id = user_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.service_type = service_type;
        this.speciality = speciality;
        this.experience = experience;
        this.charge_type = charge_type;
        this.charge_amount = charge_amount;
        this.description = description;
        this.status=status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
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

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getCharge_type() {
        return charge_type;
    }

    public void setCharge_type(String charge_type) {
        this.charge_type = charge_type;
    }

    public int getCharge_amount() {
        return charge_amount;
    }

    public void setCharge_amount(int charge_amount) {
        this.charge_amount = charge_amount;
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
