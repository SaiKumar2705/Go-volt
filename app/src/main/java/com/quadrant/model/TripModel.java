package com.quadrant.model;

public class TripModel {
    private String created_at;
    private int  id;
    private String address_finish;
    private String address_start;
    private String startTime;
    private String finishTime;
    private String licenseplate;
    private String tempdate,scootertype;
    double price;

   private double lattitudestartpos,longitudestartpos,lattitudefinishpos,longitudefinishpos;


    public TripModel(int id, String created_at, double price, String licenseplate, String address_finish, String address_start, String startTime, String finishTime, String tempdate, double lattitudestartpos, double longitudestartpos, double lattitudefinishpos, double longitudefinishpos, String scootertype) {
        this.id = id;
        this.created_at = created_at;
        this.price = price;
        this.licenseplate = licenseplate;
        this.address_finish = address_finish;
        this.address_start = address_start;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.tempdate=tempdate;
        this.lattitudestartpos=lattitudestartpos;
        this.longitudestartpos=longitudestartpos;
        this.lattitudefinishpos=lattitudefinishpos;
        this.longitudefinishpos=longitudefinishpos;
        this.scootertype=scootertype;

    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public String getAddress_finish() {
        return address_finish;
    }

    public void setAddress_finish(String address_finish) {
        this.address_finish = address_finish;
    }

    public String getAddress_start() {
        return address_start;
    }

    public void setAddress_start(String address_start) {
        this.address_start = address_start;
    }


    public String getLicenseplate() {
        return licenseplate;
    }

    public void setLicenseplate(String licenseplate) {
        this.licenseplate = licenseplate;
    }

    public String getTempdate() {
        return tempdate;
    }

    public void setTempdate(String tempdate) {
        this.tempdate = tempdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public double getLattitudestartpos() {
        return lattitudestartpos;
    }

    public void setLattitudestartpos(double lattitudestartpos) {
        this.lattitudestartpos = lattitudestartpos;
    }

    public double getLongitudestartpos() {
        return longitudestartpos;
    }

    public void setLongitudestartpos(double longitudestartpos) {
        this.longitudestartpos = longitudestartpos;
    }

    public double getLattitudefinishpos() {
        return lattitudefinishpos;
    }

    public void setLattitudefinishpos(double lattitudefinishpos) {
        this.lattitudefinishpos = lattitudefinishpos;
    }

    public double getLongitudefinishpos() {
        return longitudefinishpos;
    }

    public void setLongitudefinishpos(double longitudefinishpos) {
        this.longitudefinishpos = longitudefinishpos;
    }

    public String getScootertype() {
        return scootertype;
    }

    public void setScootertype(String scootertype) {
        this.scootertype = scootertype;
    }
}
