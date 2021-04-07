package com.quadrant.request;

public class ProfileRequest {
    private String surname,name,cf,identity_id,birth,residence,cap,residence_city,residence_country,tel;


    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentity_id() {
        return identity_id;
    }

    public void setIdentity_id(String identity_id) {
        this.identity_id = identity_id;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getResidence_city() {
        return residence_city;
    }

    public void setResidence_city(String residence_city) {
        this.residence_city = residence_city;
    }

    public String getResidence_country() {
        return residence_country;
    }

    public void setResidence_country(String residence_country) {
        this.residence_country = residence_country;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public String getTelephone() {
        return tel;
    }

    public void setTelephone(String telephone) {
        this.tel = telephone;
    }
}
