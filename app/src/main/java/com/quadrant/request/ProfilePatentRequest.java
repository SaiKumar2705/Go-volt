package com.quadrant.request;

public class ProfilePatentRequest {

    private  String driving_lincense_holder_name,nationality;
    private String guy;
   private String driving_license_number;
   private String driving_release_date;
    private  String driving_expiry_date;
    private String driving_authorities;
    private  String driving_license_nationality;

    public String getGuy() {
        return guy;
    }

    public void setGuy(String guy) {
        this.guy = guy;
    }

    public String getDriving_license_number() {
        return driving_license_number;
    }

    public void setDriving_license_number(String driving_license_number) {
        this.driving_license_number = driving_license_number;
    }

    public String getDriving_release_date() {
        return driving_release_date;
    }

    public void setDriving_release_date(String driving_release_date) {
        this.driving_release_date = driving_release_date;
    }

    public String getDriving_expiry_date() {
        return driving_expiry_date;
    }

    public void setDriving_expiry_date(String driving_expiry_date) {
        this.driving_expiry_date = driving_expiry_date;
    }

    public String getDriving_authorities() {
        return driving_authorities;
    }

    public void setDriving_authorities(String driving_authorities) {
        this.driving_authorities = driving_authorities;
    }


   /* public String getHoldername() {
        return holdername;
    }

    public void setHoldername(String holdername) {
        this.holdername = holdername;
    }
*/
    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getDriving_lincense_holder_name() {
        return driving_lincense_holder_name;
    }

    public void setDriving_lincense_holder_name(String driving_lincense_holder_name) {
        this.driving_lincense_holder_name = driving_lincense_holder_name;
    }

    public String getDriving_license_nationality() {
        return driving_license_nationality;
    }

    public void setDriving_license_nationality(String driving_license_nationality) {
        this.driving_license_nationality = driving_license_nationality;
    }
}
