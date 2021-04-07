package com.quadrant.model;

public class RideListItems {
    private String name;
    private String sub_name;
    private String distance;
    private String date;
    private String price;

    public RideListItems(String name, String sub_name, String distance, String date, String price) {
        this.name = name;
        this.sub_name = sub_name;
        this.distance = distance;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
