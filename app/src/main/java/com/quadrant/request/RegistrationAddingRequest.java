package com.quadrant.request;

public class RegistrationAddingRequest {


    private  String birth,surname,name,identity_id,
            residence,cap,residence_city,residence_country,cf,sex,
            identity_expiration,
            identity_release;

  //  private  Long ;


    public void setIdentity_release(String identity_release) {
        this.identity_release = identity_release;
    }

    public void setIdentity_expiration(String identity_expiration) {
        this.identity_expiration = identity_expiration;
    }

    public String getIdentity_release() {
        return identity_release;
    }

    public String getIdentity_expiration() {
        return identity_expiration;
    }

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

   /* public Long getBirth() {
        return birth;
    }

    public void setBirth(Long birth) {
        this.birth = birth;
    }*/
}
