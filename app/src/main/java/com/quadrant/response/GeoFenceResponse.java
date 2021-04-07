package com.quadrant.response;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class GeoFenceResponse {
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

        @SerializedName("zonas")
        @Expose
        private List<Zona> zonas = null;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("active")
        @Expose
        private Boolean active;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("vehicleType")
        @Expose
        private String vehicleType;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("deleted_at")
        @Expose
        private Object deletedAt;
        @SerializedName("ServiceId")
        @Expose
        private Integer serviceId;
        @SerializedName("SiteId")
        @Expose
        private Integer siteId;
        @SerializedName("RateSharing")
        @Expose
        private List<Object> rateSharing = null;

        public List<Zona> getZonas() {
            return zonas;
        }

        public void setZonas(List<Zona> zonas) {
            this.zonas = zonas;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Boolean getActive() {
            return active;
        }

        public void setActive(Boolean active) {
            this.active = active;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVehicleType() {
            return vehicleType;
        }

        public void setVehicleType(String vehicleType) {
            this.vehicleType = vehicleType;
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

        public Object getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(Object deletedAt) {
            this.deletedAt = deletedAt;
        }

        public Integer getServiceId() {
            return serviceId;
        }

        public void setServiceId(Integer serviceId) {
            this.serviceId = serviceId;
        }

        public Integer getSiteId() {
            return siteId;
        }

        public void setSiteId(Integer siteId) {
            this.siteId = siteId;
        }

        public List<Object> getRateSharing() {
            return rateSharing;
        }

        public void setRateSharing(List<Object> rateSharing) {
            this.rateSharing = rateSharing;
        }

    }
    public class Zona {

        @SerializedName("points")
        @Expose
        private List<List<Double>> points = null;
        @SerializedName("exclude")
        @Expose
        private Boolean exclude;

        public List<List<Double>> getPoints() {
            return points;
        }

        public void setPoints(List<List<Double>> points) {
            this.points = points;
        }

        public Boolean getExclude() {
            return exclude;
        }

        public void setExclude(Boolean exclude) {
            this.exclude = exclude;
        }
    }


}

