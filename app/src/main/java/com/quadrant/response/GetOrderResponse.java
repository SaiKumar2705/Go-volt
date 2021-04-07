package com.quadrant.response;

import android.support.constraint.Constraints;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetOrderResponse {



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

        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("data")
        @Expose
        private List<Datum> data = null;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public List<Datum> getData() {
            return data;
        }

        public void setData(List<Datum> data) {
            this.data = data;
        }

    }
    public class Datum {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("deleted_at")
        @Expose
        private Object deletedAt;
        @SerializedName("ShopItemId")
        @Expose
        private Integer shopItemId;
        @SerializedName("UserId")
        @Expose
        private Integer userId;
        @SerializedName("TransactionId")
        @Expose
        private Integer transactionId;
        @SerializedName("BonusId")
        @Expose
        private Object bonusId;
        @SerializedName("ServiceId")
        @Expose
        private Integer serviceId;
        @SerializedName("SiteId")
        @Expose
        private Integer siteId;
        @SerializedName("ShopItem")
        @Expose
        private ShopItem shopItem;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public Integer getShopItemId() {
            return shopItemId;
        }

        public void setShopItemId(Integer shopItemId) {
            this.shopItemId = shopItemId;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(Integer transactionId) {
            this.transactionId = transactionId;
        }

        public Object getBonusId() {
            return bonusId;
        }

        public void setBonusId(Object bonusId) {
            this.bonusId = bonusId;
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

        public ShopItem getShopItem() {
            return shopItem;
        }

        public void setShopItem(ShopItem shopItem) {
            this.shopItem = shopItem;
        }

    }
    public class ShopItem {

        @SerializedName("price")
        @Expose
        private Price price;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("userVisible")
        @Expose
        private Boolean userVisible;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("deleted_at")
        @Expose
        private Object deletedAt;
        @SerializedName("BonusTemplateId")
        @Expose
        private Integer bonusTemplateId;
        @SerializedName("ServiceId")
        @Expose
        private Integer serviceId;
        @SerializedName("SiteId")
        @Expose
        private Integer siteId;
        @SerializedName("BonusTemplate")
        @Expose
        private BonusTemplate bonusTemplate;

        public Price getPrice() {
            return price;
        }

        public void setPrice(Price price) {
            this.price = price;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Boolean getUserVisible() {
            return userVisible;
        }

        public void setUserVisible(Boolean userVisible) {
            this.userVisible = userVisible;
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

        public Integer getBonusTemplateId() {
            return bonusTemplateId;
        }

        public void setBonusTemplateId(Integer bonusTemplateId) {
            this.bonusTemplateId = bonusTemplateId;
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

        public BonusTemplate getBonusTemplate() {
            return bonusTemplate;
        }

        public void setBonusTemplate(BonusTemplate bonusTemplate) {
            this.bonusTemplate = bonusTemplate;
        }

    }

    public class BonusTemplate {

        @SerializedName("properties")
        @Expose
        private Properties properties;
        @SerializedName("limits")
        @Expose
        private Limits limits;
        @SerializedName("constraints")
        @Expose
        private Constraints constraints;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("hidden")
        @Expose
        private Boolean hidden;
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

        public Properties getProperties() {
            return properties;
        }

        public void setProperties(Properties properties) {
            this.properties = properties;
        }

        public Limits getLimits() {
            return limits;
        }

        public void setLimits(Limits limits) {
            this.limits = limits;
        }

        public Constraints getConstraints() {
            return constraints;
        }

        public void setConstraints(Constraints constraints) {
            this.constraints = constraints;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Boolean getHidden() {
            return hidden;
        }

        public void setHidden(Boolean hidden) {
            this.hidden = hidden;
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

    }
    public class Properties {


    }
    public class Limits {

        @SerializedName("price")
        @Expose
        private Price_ price;

        public Price_ getPrice() {
            return price;
        }

        public void setPrice(Price_ price) {
            this.price = price;
        }

    }
    public class Price {

        @SerializedName("currency")
        @Expose
        private String currency;
        @SerializedName("value")
        @Expose
        private Integer value;

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

    }

    public class Price_ {

        @SerializedName("currency")
        @Expose
        private String currency;
        @SerializedName("value")
        @Expose
        private Integer value;

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

    }

    public class Constraints {

        @SerializedName("validityTime")
        @Expose
        private List<ValidityTime> validityTime = null;
        @SerializedName("siteIds")
        @Expose
        private List<Integer> siteIds = null;

        public List<ValidityTime> getValidityTime() {
            return validityTime;
        }

        public void setValidityTime(List<ValidityTime> validityTime) {
            this.validityTime = validityTime;
        }

        public List<Integer> getSiteIds() {
            return siteIds;
        }

        public void setSiteIds(List<Integer> siteIds) {
            this.siteIds = siteIds;
        }

    }

    public class ValidityTime {

        @SerializedName("type")
        @Expose
        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

    }





}
