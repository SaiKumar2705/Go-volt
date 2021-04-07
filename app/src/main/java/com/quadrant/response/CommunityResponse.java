package com.quadrant.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommunityResponse {
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

    public class Coupon {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("unlimited_join")
        @Expose
        private Boolean unlimitedJoin;
        @SerializedName("unlimited_finish")
        @Expose
        private Boolean unlimitedFinish;
        @SerializedName("join_count")
        @Expose
        private Integer joinCount;
        @SerializedName("finish")
        @Expose
        private Long finish;
        @SerializedName("start")
        @Expose
        private Long start;
        @SerializedName("type")
        @Expose
        private Integer type;
        @SerializedName("value")
        @Expose
        private Integer value;
        @SerializedName("code")
        @Expose
        private String code;
        @SerializedName("cashback")
        @Expose
        private Object cashback;
        @SerializedName("isReferral")
        @Expose
        private Boolean isReferral;
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
        @SerializedName("UserId")
        @Expose
        private Object userId;
        @SerializedName("currencyValue")
        @Expose
        private Double currencyValue;

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

        public Boolean getUnlimitedJoin() {
            return unlimitedJoin;
        }

        public void setUnlimitedJoin(Boolean unlimitedJoin) {
            this.unlimitedJoin = unlimitedJoin;
        }

        public Boolean getUnlimitedFinish() {
            return unlimitedFinish;
        }

        public void setUnlimitedFinish(Boolean unlimitedFinish) {
            this.unlimitedFinish = unlimitedFinish;
        }

        public Integer getJoinCount() {
            return joinCount;
        }

        public void setJoinCount(Integer joinCount) {
            this.joinCount = joinCount;
        }

        public Long getFinish() {
            return finish;
        }

        public void setFinish(Long finish) {
            this.finish = finish;
        }

        public Long getStart() {
            return start;
        }

        public void setStart(Long start) {
            this.start = start;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Object getCashback() {
            return cashback;
        }

        public void setCashback(Object cashback) {
            this.cashback = cashback;
        }

        public Boolean getIsReferral() {
            return isReferral;
        }

        public void setIsReferral(Boolean isReferral) {
            this.isReferral = isReferral;
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

        public Object getUserId() {
            return userId;
        }

        public void setUserId(Object userId) {
            this.userId = userId;
        }

        public Double getCurrencyValue() {
            return currencyValue;
        }

        public void setCurrencyValue(Double currencyValue) {
            this.currencyValue = currencyValue;
        }

    }
    public class Data {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("UserId")
        @Expose
        private Integer userId;
        @SerializedName("CouponId")
        @Expose
        private Integer couponId;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("deleted_at")
        @Expose
        private Object deletedAt;
        @SerializedName("Coupon")
        @Expose
        private Coupon coupon;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getCouponId() {
            return couponId;
        }

        public void setCouponId(Integer couponId) {
            this.couponId = couponId;
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

        public Coupon getCoupon() {
            return coupon;
        }

        public void setCoupon(Coupon coupon) {
            this.coupon = coupon;
        }

    }
}
