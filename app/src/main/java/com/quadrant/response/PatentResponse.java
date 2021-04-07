package com.quadrant.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PatentResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("error")
    @Expose
    private Object error;
    @SerializedName("data")
    @Expose
    private Data data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public class Data {

        @SerializedName("driving_authorities")
        @Expose
        private String drivingAuthorities;
        @SerializedName("driving_expiry_date")
        @Expose
        private String drivingExpiryDate;

        @SerializedName("driving_license_nationality")
        @Expose
        private String drivingLicenseNationality;

        @SerializedName("driving_license_number")
        @Expose
        private String drivingLicenseNumber;

        @SerializedName("driving_lincense_holder_name")
        @Expose
        private String drivingLincenseHolderName;

        @SerializedName("driving_release_date")
        @Expose
        private String drivingReleaseDate;
        @SerializedName("found_service_via")


        @Expose
        private String foundServiceVia;

        @SerializedName("guy")
        @Expose
        private String guy;

        @SerializedName("nationality")
        @Expose
        private String nationality;

        public String getDrivingAuthorities() {
            return drivingAuthorities;
        }

        public void setDrivingAuthorities(String drivingAuthorities) {
            this.drivingAuthorities = drivingAuthorities;
        }

        public String getDrivingExpiryDate() {
            return drivingExpiryDate;
        }

        public void setDrivingExpiryDate(String drivingExpiryDate) {
            this.drivingExpiryDate = drivingExpiryDate;
        }

        public String getDrivingLicenseNationality() {
            return drivingLicenseNationality;
        }

        public void setDrivingLicenseNationality(String drivingLicenseNationality) {
            this.drivingLicenseNationality = drivingLicenseNationality;
        }

        public String getDrivingLicenseNumber() {
            return drivingLicenseNumber;
        }

        public void setDrivingLicenseNumber(String drivingLicenseNumber) {
            this.drivingLicenseNumber = drivingLicenseNumber;
        }

        public String getDrivingLincenseHolderName() {
            return drivingLincenseHolderName;
        }

        public void setDrivingLincenseHolderName(String drivingLincenseHolderName) {
            this.drivingLincenseHolderName = drivingLincenseHolderName;
        }

        public String getDrivingReleaseDate() {
            return drivingReleaseDate;
        }

        public void setDrivingReleaseDate(String drivingReleaseDate) {
            this.drivingReleaseDate = drivingReleaseDate;
        }

        public String getFoundServiceVia() {
            return foundServiceVia;
        }

        public void setFoundServiceVia(String foundServiceVia) {
            this.foundServiceVia = foundServiceVia;
        }

        public String getGuy() {
            return guy;
        }

        public void setGuy(String guy) {
            this.guy = guy;
        }

        public String getNationality() {
            return nationality;
        }

        public void setNationality(String nationality) {
            this.nationality = nationality;
        }

    }

}



