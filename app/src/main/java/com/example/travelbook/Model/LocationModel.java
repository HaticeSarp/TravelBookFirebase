package com.example.travelbook.Model;

public class LocationModel {
    private double latitude,longitude;
    String id;

    public LocationModel()
    {}

    public LocationModel(String id,double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.id=id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
