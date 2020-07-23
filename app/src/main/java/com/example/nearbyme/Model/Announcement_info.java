package com.example.nearbyme.Model;

public class Announcement_info {
    private String user_id;
    private String ann_id;
    private double latitude;
    private double longitude;
    private String subject;
    private String description;
    private boolean status;

    public Announcement_info() {
    }

    public Announcement_info(String user_id, double latitude, double longitude, String subject, String description,boolean status) {
        this.user_id = user_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.subject = subject;
        this.description = description;
        this.status=status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAnn_id() {
        return ann_id;
    }

    public void setAnn_id(String ann_id) {
        this.ann_id = ann_id;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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
