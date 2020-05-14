package com.example.nearbyme.Model;

public class MediaUpload {
    private String ImageUrl;

    public MediaUpload() {
    }

    public MediaUpload(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
