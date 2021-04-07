package com.quadrant.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SiteIDResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("error")
    @Expose
    private Object error;
    @SerializedName("data")
    @Expose
    private List<Data> data = null;

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

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("countryCode3")
        @Expose
        private String countryCode3;
        @SerializedName("topLatitude")
        @Expose
        private Double topLatitude;
        @SerializedName("bottomLatitude")
        @Expose
        private Double bottomLatitude;
        @SerializedName("leftLongitude")
        @Expose
        private Double leftLongitude;
        @SerializedName("rightLongitude")
        @Expose
        private Double rightLongitude;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("deleted_at")
        @Expose
        private String deletedAt;
        @SerializedName("ServiceId")
        @Expose
        private Integer serviceId;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCountryCode3() {
            return countryCode3;
        }

        public void setCountryCode3(String countryCode3) {
            this.countryCode3 = countryCode3;
        }

        public Double getTopLatitude() {
            return topLatitude;
        }

        public void setTopLatitude(Double topLatitude) {
            this.topLatitude = topLatitude;
        }

        public Double getBottomLatitude() {
            return bottomLatitude;
        }

        public void setBottomLatitude(Double bottomLatitude) {
            this.bottomLatitude = bottomLatitude;
        }

        public Double getLeftLongitude() {
            return leftLongitude;
        }

        public void setLeftLongitude(Double leftLongitude) {
            this.leftLongitude = leftLongitude;
        }

        public Double getRightLongitude() {
            return rightLongitude;
        }

        public void setRightLongitude(Double rightLongitude) {
            this.rightLongitude = rightLongitude;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(String deletedAt) {
            this.deletedAt = deletedAt;
        }

        public Integer getServiceId() {
            return serviceId;
        }

        public void setServiceId(Integer serviceId) {
            this.serviceId = serviceId;
        }

    }
}
