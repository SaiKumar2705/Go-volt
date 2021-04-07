package com.quadrant.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserInfoResponseData {

    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class UserSite {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("deleted_at")
        @Expose
        private Object deletedAt;
        @SerializedName("UserId")
        @Expose
        private Integer userId;
        @SerializedName("SiteId")
        @Expose
        private Integer siteId;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
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

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getSiteId() {
            return siteId;
        }

        public void setSiteId(Integer siteId) {
            this.siteId = siteId;
        }

    }


    public class Booking {


    }

    public class BookingsStats {


    }

    public class Service {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("domain")
        @Expose
        private String domain;
        @SerializedName("unlimitedBooking")
        @Expose
        private Boolean unlimitedBooking;
        @SerializedName("freeReBooking")
        @Expose
        private Boolean freeReBooking;
        @SerializedName("free")
        @Expose
        private Boolean free;
        @SerializedName("inTesting")
        @Expose
        private Boolean inTesting;
        @SerializedName("register")
        @Expose
        private Boolean register;
        @SerializedName("timeReBooking")
        @Expose
        private Integer timeReBooking;
        @SerializedName("price")
        @Expose
        private Double price;
        @SerializedName("priceReBooking")
        @Expose
        private Double priceReBooking;
        @SerializedName("manage")
        @Expose
        private Boolean manage;
        @SerializedName("url_manage")
        @Expose
        private Object urlManage;
        @SerializedName("external_validate")
        @Expose
        private Boolean externalValidate;
        @SerializedName("url_validate")
        @Expose
        private Object urlValidate;
        @SerializedName("canBook")
        @Expose
        private Boolean canBook;
        @SerializedName("tos_url")
        @Expose
        private String tosUrl;
        @SerializedName("owner_number")
        @Expose
        private String ownerNumber;
        @SerializedName("price_block")
        @Expose
        private String priceBlock;
        @SerializedName("type_validate")
        @Expose
        private String typeValidate;
        @SerializedName("callback_code")
        @Expose
        private String callbackCode;
        @SerializedName("callback_attribute")
        @Expose
        private String callbackAttribute;
        @SerializedName("callback_limit")
        @Expose
        private Boolean callbackLimit;
        @SerializedName("callback_mask")
        @Expose
        private Object callbackMask;
        @SerializedName("colour")
        @Expose
        private String colour;
        @SerializedName("personal")
        @Expose
        private Boolean personal;
        @SerializedName("oauth_provider")
        @Expose
        private Object oauthProvider;
        @SerializedName("token")
        @Expose
        private String token;
        @SerializedName("timeFreeBooking")
        @Expose
        private Integer timeFreeBooking;
        @SerializedName("userAssignVehicleDay")
        @Expose
        private Integer userAssignVehicleDay;
        @SerializedName("priceGeneratorType")
        @Expose
        private Integer priceGeneratorType;
        @SerializedName("enablePermission")
        @Expose
        private Boolean enablePermission;
        @SerializedName("multisite")
        @Expose
        private Boolean multisite;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("deleted_at")
        @Expose
        private Object deletedAt;
        @SerializedName("RateSharingId")
        @Expose
        private Object rateSharingId;
        @SerializedName("CouponId")
        @Expose
        private Object couponId;
        @SerializedName("ReferralCouponId")
        @Expose
        private Integer referralCouponId;
        @SerializedName("DefaultRateSharingId")
        @Expose
        private Object defaultRateSharingId;

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

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public Boolean getUnlimitedBooking() {
            return unlimitedBooking;
        }

        public void setUnlimitedBooking(Boolean unlimitedBooking) {
            this.unlimitedBooking = unlimitedBooking;
        }

        public Boolean getFreeReBooking() {
            return freeReBooking;
        }

        public void setFreeReBooking(Boolean freeReBooking) {
            this.freeReBooking = freeReBooking;
        }

        public Boolean getFree() {
            return free;
        }

        public void setFree(Boolean free) {
            this.free = free;
        }

        public Boolean getInTesting() {
            return inTesting;
        }

        public void setInTesting(Boolean inTesting) {
            this.inTesting = inTesting;
        }

        public Boolean getRegister() {
            return register;
        }

        public void setRegister(Boolean register) {
            this.register = register;
        }

        public Integer getTimeReBooking() {
            return timeReBooking;
        }

        public void setTimeReBooking(Integer timeReBooking) {
            this.timeReBooking = timeReBooking;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Double getPriceReBooking() {
            return priceReBooking;
        }

        public void setPriceReBooking(Double priceReBooking) {
            this.priceReBooking = priceReBooking;
        }

        public Boolean getManage() {
            return manage;
        }

        public void setManage(Boolean manage) {
            this.manage = manage;
        }

        public Object getUrlManage() {
            return urlManage;
        }

        public void setUrlManage(Object urlManage) {
            this.urlManage = urlManage;
        }

        public Boolean getExternalValidate() {
            return externalValidate;
        }

        public void setExternalValidate(Boolean externalValidate) {
            this.externalValidate = externalValidate;
        }

        public Object getUrlValidate() {
            return urlValidate;
        }

        public void setUrlValidate(Object urlValidate) {
            this.urlValidate = urlValidate;
        }

        public Boolean getCanBook() {
            return canBook;
        }

        public void setCanBook(Boolean canBook) {
            this.canBook = canBook;
        }

        public String getTosUrl() {
            return tosUrl;
        }

        public void setTosUrl(String tosUrl) {
            this.tosUrl = tosUrl;
        }

        public String getOwnerNumber() {
            return ownerNumber;
        }

        public void setOwnerNumber(String ownerNumber) {
            this.ownerNumber = ownerNumber;
        }

        public String getPriceBlock() {
            return priceBlock;
        }

        public void setPriceBlock(String priceBlock) {
            this.priceBlock = priceBlock;
        }

        public String getTypeValidate() {
            return typeValidate;
        }

        public void setTypeValidate(String typeValidate) {
            this.typeValidate = typeValidate;
        }

        public String getCallbackCode() {
            return callbackCode;
        }

        public void setCallbackCode(String callbackCode) {
            this.callbackCode = callbackCode;
        }

        public String getCallbackAttribute() {
            return callbackAttribute;
        }

        public void setCallbackAttribute(String callbackAttribute) {
            this.callbackAttribute = callbackAttribute;
        }

        public Boolean getCallbackLimit() {
            return callbackLimit;
        }

        public void setCallbackLimit(Boolean callbackLimit) {
            this.callbackLimit = callbackLimit;
        }

        public Object getCallbackMask() {
            return callbackMask;
        }

        public void setCallbackMask(Object callbackMask) {
            this.callbackMask = callbackMask;
        }

        public String getColour() {
            return colour;
        }

        public void setColour(String colour) {
            this.colour = colour;
        }

        public Boolean getPersonal() {
            return personal;
        }

        public void setPersonal(Boolean personal) {
            this.personal = personal;
        }

        public Object getOauthProvider() {
            return oauthProvider;
        }

        public void setOauthProvider(Object oauthProvider) {
            this.oauthProvider = oauthProvider;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Integer getTimeFreeBooking() {
            return timeFreeBooking;
        }

        public void setTimeFreeBooking(Integer timeFreeBooking) {
            this.timeFreeBooking = timeFreeBooking;
        }

        public Integer getUserAssignVehicleDay() {
            return userAssignVehicleDay;
        }

        public void setUserAssignVehicleDay(Integer userAssignVehicleDay) {
            this.userAssignVehicleDay = userAssignVehicleDay;
        }

        public Integer getPriceGeneratorType() {
            return priceGeneratorType;
        }

        public void setPriceGeneratorType(Integer priceGeneratorType) {
            this.priceGeneratorType = priceGeneratorType;
        }

        public Boolean getEnablePermission() {
            return enablePermission;
        }

        public void setEnablePermission(Boolean enablePermission) {
            this.enablePermission = enablePermission;
        }

        public Boolean getMultisite() {
            return multisite;
        }

        public void setMultisite(Boolean multisite) {
            this.multisite = multisite;
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

        public Object getRateSharingId() {
            return rateSharingId;
        }

        public void setRateSharingId(Object rateSharingId) {
            this.rateSharingId = rateSharingId;
        }

        public Object getCouponId() {
            return couponId;
        }

        public void setCouponId(Object couponId) {
            this.couponId = couponId;
        }

        public Integer getReferralCouponId() {
            return referralCouponId;
        }

        public void setReferralCouponId(Integer referralCouponId) {
            this.referralCouponId = referralCouponId;
        }

        public Object getDefaultRateSharingId() {
            return defaultRateSharingId;
        }

        public void setDefaultRateSharingId(Object defaultRateSharingId) {
            this.defaultRateSharingId = defaultRateSharingId;
        }

    }
    public class Token {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("code")
        @Expose
        private String code;
        @SerializedName("expire")
        @Expose
        private Integer expire;
        @SerializedName("clientType")
        @Expose
        private Integer clientType;
        @SerializedName("unlimited")
        @Expose
        private Boolean unlimited;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("deleted_at")
        @Expose
        private Object deletedAt;
        @SerializedName("NotificationDeviceId")
        @Expose
        private Object notificationDeviceId;
        @SerializedName("UserId")
        @Expose
        private Integer userId;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Integer getExpire() {
            return expire;
        }

        public void setExpire(Integer expire) {
            this.expire = expire;
        }

        public Integer getClientType() {
            return clientType;
        }

        public void setClientType(Integer clientType) {
            this.clientType = clientType;
        }

        public Boolean getUnlimited() {
            return unlimited;
        }

        public void setUnlimited(Boolean unlimited) {
            this.unlimited = unlimited;
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

        public Object getNotificationDeviceId() {
            return notificationDeviceId;
        }

        public void setNotificationDeviceId(Object notificationDeviceId) {
            this.notificationDeviceId = notificationDeviceId;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

    }
    public class User {

        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("freeMinutes")
        @Expose
        private Integer freeMinutes;
        @SerializedName("prepaidMinutes")
        @Expose
        private Integer prepaidMinutes;
        @SerializedName("validLicensePlate")
        @Expose
        private Integer validLicensePlate;
        @SerializedName("validDocument")
        @Expose
        private Integer validDocument;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("surname")
        @Expose
        private String surname;
        @SerializedName("role")
        @Expose
        private String role;
        @SerializedName("license_id")
        @Expose
        private String licenseId;
        @SerializedName("license_expiration")
        @Expose
        private String licenseExpiration;
        @SerializedName("license_release")
        @Expose
        private String licenseRelease;
        @SerializedName("license_place")
        @Expose
        private Object licensePlace;
        @SerializedName("license_country")
        @Expose
        private String licenseCountry;
        @SerializedName("identity_type")
        @Expose
        private Object identityType;
        @SerializedName("identity_id")
        @Expose
        private Object identityId;
        @SerializedName("identity_release")
        @Expose
        private Object identityRelease;
        @SerializedName("identity_expiration")
        @Expose
        private Object identityExpiration;
        @SerializedName("identity_issue_city")
        @Expose
        private Object identityIssueCity;
        @SerializedName("identity_issue_state")
        @Expose
        private Object identityIssueState;
        @SerializedName("cf")
        @Expose
        private String cf;
        @SerializedName("sex")
        @Expose
        private Object sex;
        @SerializedName("birth")
        @Expose
        private String birth;
        @SerializedName("residence")
        @Expose
        private String residence;
        @SerializedName("cap")
        @Expose
        private String cap;
        @SerializedName("tel")
        @Expose
        private String tel;
        @SerializedName("validSMS")
        @Expose
        private Boolean validSMS;
        @SerializedName("validEmail")
        @Expose
        private Boolean validEmail;
        @SerializedName("validID")
        @Expose
        private Boolean validID;
        @SerializedName("online")
        @Expose
        private Boolean online;
        @SerializedName("authy")
        @Expose
        private Boolean authy;
        @SerializedName("authy_id")
        @Expose
        private Object authyId;
        @SerializedName("oauth")
        @Expose
        private Boolean oauth;
        @SerializedName("paymentCreated")
        @Expose
        private Boolean paymentCreated;
        @SerializedName("oauth_id")
        @Expose
        private Object oauthId;
        @SerializedName("paymentRef")
        @Expose
        private Object paymentRef;
        @SerializedName("birth_country")
        @Expose
        private Object birthCountry;
        @SerializedName("birth_city")
        @Expose
        private Object birthCity;
        @SerializedName("birth_state")
        @Expose
        private Object birthState;
        @SerializedName("residence_country")
        @Expose
        private String residenceCountry;
        @SerializedName("residence_city")
        @Expose
        private String residenceCity;
        @SerializedName("residence_state")
        @Expose
        private Object residenceState;
        @SerializedName("company_name")
        @Expose
        private Object companyName;
        @SerializedName("type")
        @Expose
        private Object type;
        @SerializedName("sendRegisterCompleteEmail")
        @Expose
        private Boolean sendRegisterCompleteEmail;
        @SerializedName("block")
        @Expose
        private Boolean block;
        @SerializedName("block_reason")
        @Expose
        private Object blockReason;
        @SerializedName("accessForScooter")
        @Expose
        private Boolean accessForScooter;
        @SerializedName("accessForBike")
        @Expose
        private Boolean accessForBike;
        @SerializedName("accessForCar")
        @Expose
        private Boolean accessForCar;
        @SerializedName("lastPaymentStatus")
        @Expose
        private String lastPaymentStatus;
        @SerializedName("referal")
        @Expose
        private String referal;
        @SerializedName("freeMilliseconds")
        @Expose
        private Integer freeMilliseconds;
        @SerializedName("prepaidMilliseconds")
        @Expose
        private Integer prepaidMilliseconds;
        @SerializedName("cashbackCount")
        @Expose
        private Integer cashbackCount;
        @SerializedName("createdBy")
        @Expose
        private Integer createdBy;
        @SerializedName("fbAccountKitId")
        @Expose
        private Object fbAccountKitId;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("deleted_at")
        @Expose
        private Object deletedAt;
        @SerializedName("SiteId")
        @Expose
        private Integer siteId;
        @SerializedName("ServiceId")
        @Expose
        private Integer serviceId;
        @SerializedName("LicensePlate")
        @Expose
        private List<LicensePlate> licensePlate = null;
        @SerializedName("Document")
        @Expose
        private List<Object> document = null;
        @SerializedName("Avatar")
        @Expose
        private List<Object> avatar = null;
        @SerializedName("UserSites")
        @Expose
        private List<UserSite> userSites = null;
        @SerializedName("freeCredit")
        @Expose
        private Integer freeCredit;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Integer getFreeMinutes() {
            return freeMinutes;
        }

        public void setFreeMinutes(Integer freeMinutes) {
            this.freeMinutes = freeMinutes;
        }

        public Integer getPrepaidMinutes() {
            return prepaidMinutes;
        }

        public void setPrepaidMinutes(Integer prepaidMinutes) {
            this.prepaidMinutes = prepaidMinutes;
        }

        public Integer getValidLicensePlate() {
            return validLicensePlate;
        }

        public void setValidLicensePlate(Integer validLicensePlate) {
            this.validLicensePlate = validLicensePlate;
        }

        public Integer getValidDocument() {
            return validDocument;
        }

        public void setValidDocument(Integer validDocument) {
            this.validDocument = validDocument;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getLicenseId() {
            return licenseId;
        }

        public void setLicenseId(String licenseId) {
            this.licenseId = licenseId;
        }

        public String getLicenseExpiration() {
            return licenseExpiration;
        }

        public void setLicenseExpiration(String licenseExpiration) {
            this.licenseExpiration = licenseExpiration;
        }

        public String getLicenseRelease() {
            return licenseRelease;
        }

        public void setLicenseRelease(String licenseRelease) {
            this.licenseRelease = licenseRelease;
        }

        public Object getLicensePlace() {
            return licensePlace;
        }

        public void setLicensePlace(Object licensePlace) {
            this.licensePlace = licensePlace;
        }

        public String getLicenseCountry() {
            return licenseCountry;
        }

        public void setLicenseCountry(String licenseCountry) {
            this.licenseCountry = licenseCountry;
        }

        public Object getIdentityType() {
            return identityType;
        }

        public void setIdentityType(Object identityType) {
            this.identityType = identityType;
        }

        public Object getIdentityId() {
            return identityId;
        }

        public void setIdentityId(Object identityId) {
            this.identityId = identityId;
        }

        public Object getIdentityRelease() {
            return identityRelease;
        }

        public void setIdentityRelease(Object identityRelease) {
            this.identityRelease = identityRelease;
        }

        public Object getIdentityExpiration() {
            return identityExpiration;
        }

        public void setIdentityExpiration(Object identityExpiration) {
            this.identityExpiration = identityExpiration;
        }

        public Object getIdentityIssueCity() {
            return identityIssueCity;
        }

        public void setIdentityIssueCity(Object identityIssueCity) {
            this.identityIssueCity = identityIssueCity;
        }

        public Object getIdentityIssueState() {
            return identityIssueState;
        }

        public void setIdentityIssueState(Object identityIssueState) {
            this.identityIssueState = identityIssueState;
        }

        public String getCf() {
            return cf;
        }

        public void setCf(String cf) {
            this.cf = cf;
        }

        public Object getSex() {
            return sex;
        }

        public void setSex(Object sex) {
            this.sex = sex;
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

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public Boolean getValidSMS() {
            return validSMS;
        }

        public void setValidSMS(Boolean validSMS) {
            this.validSMS = validSMS;
        }

        public Boolean getValidEmail() {
            return validEmail;
        }

        public void setValidEmail(Boolean validEmail) {
            this.validEmail = validEmail;
        }

        public Boolean getValidID() {
            return validID;
        }

        public void setValidID(Boolean validID) {
            this.validID = validID;
        }

        public Boolean getOnline() {
            return online;
        }

        public void setOnline(Boolean online) {
            this.online = online;
        }

        public Boolean getAuthy() {
            return authy;
        }

        public void setAuthy(Boolean authy) {
            this.authy = authy;
        }

        public Object getAuthyId() {
            return authyId;
        }

        public void setAuthyId(Object authyId) {
            this.authyId = authyId;
        }

        public Boolean getOauth() {
            return oauth;
        }

        public void setOauth(Boolean oauth) {
            this.oauth = oauth;
        }

        public Boolean getPaymentCreated() {
            return paymentCreated;
        }

        public void setPaymentCreated(Boolean paymentCreated) {
            this.paymentCreated = paymentCreated;
        }

        public Object getOauthId() {
            return oauthId;
        }

        public void setOauthId(Object oauthId) {
            this.oauthId = oauthId;
        }

        public Object getPaymentRef() {
            return paymentRef;
        }

        public void setPaymentRef(Object paymentRef) {
            this.paymentRef = paymentRef;
        }

        public Object getBirthCountry() {
            return birthCountry;
        }

        public void setBirthCountry(Object birthCountry) {
            this.birthCountry = birthCountry;
        }

        public Object getBirthCity() {
            return birthCity;
        }

        public void setBirthCity(Object birthCity) {
            this.birthCity = birthCity;
        }

        public Object getBirthState() {
            return birthState;
        }

        public void setBirthState(Object birthState) {
            this.birthState = birthState;
        }

        public String getResidenceCountry() {
            return residenceCountry;
        }

        public void setResidenceCountry(String residenceCountry) {
            this.residenceCountry = residenceCountry;
        }

        public String getResidenceCity() {
            return residenceCity;
        }

        public void setResidenceCity(String residenceCity) {
            this.residenceCity = residenceCity;
        }

        public Object getResidenceState() {
            return residenceState;
        }

        public void setResidenceState(Object residenceState) {
            this.residenceState = residenceState;
        }

        public Object getCompanyName() {
            return companyName;
        }

        public void setCompanyName(Object companyName) {
            this.companyName = companyName;
        }

        public Object getType() {
            return type;
        }

        public void setType(Object type) {
            this.type = type;
        }

        public Boolean getSendRegisterCompleteEmail() {
            return sendRegisterCompleteEmail;
        }

        public void setSendRegisterCompleteEmail(Boolean sendRegisterCompleteEmail) {
            this.sendRegisterCompleteEmail = sendRegisterCompleteEmail;
        }

        public Boolean getBlock() {
            return block;
        }

        public void setBlock(Boolean block) {
            this.block = block;
        }

        public Object getBlockReason() {
            return blockReason;
        }

        public void setBlockReason(Object blockReason) {
            this.blockReason = blockReason;
        }

        public Boolean getAccessForScooter() {
            return accessForScooter;
        }

        public void setAccessForScooter(Boolean accessForScooter) {
            this.accessForScooter = accessForScooter;
        }

        public Boolean getAccessForBike() {
            return accessForBike;
        }

        public void setAccessForBike(Boolean accessForBike) {
            this.accessForBike = accessForBike;
        }

        public Boolean getAccessForCar() {
            return accessForCar;
        }

        public void setAccessForCar(Boolean accessForCar) {
            this.accessForCar = accessForCar;
        }

        public String getLastPaymentStatus() {
            return lastPaymentStatus;
        }

        public void setLastPaymentStatus(String lastPaymentStatus) {
            this.lastPaymentStatus = lastPaymentStatus;
        }

        public String getReferal() {
            return referal;
        }

        public void setReferal(String referal) {
            this.referal = referal;
        }

        public Integer getFreeMilliseconds() {
            return freeMilliseconds;
        }

        public void setFreeMilliseconds(Integer freeMilliseconds) {
            this.freeMilliseconds = freeMilliseconds;
        }

        public Integer getPrepaidMilliseconds() {
            return prepaidMilliseconds;
        }

        public void setPrepaidMilliseconds(Integer prepaidMilliseconds) {
            this.prepaidMilliseconds = prepaidMilliseconds;
        }

        public Integer getCashbackCount() {
            return cashbackCount;
        }

        public void setCashbackCount(Integer cashbackCount) {
            this.cashbackCount = cashbackCount;
        }

        public Integer getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(Integer createdBy) {
            this.createdBy = createdBy;
        }

        public Object getFbAccountKitId() {
            return fbAccountKitId;
        }

        public void setFbAccountKitId(Object fbAccountKitId) {
            this.fbAccountKitId = fbAccountKitId;
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

        public Integer getSiteId() {
            return siteId;
        }

        public void setSiteId(Integer siteId) {
            this.siteId = siteId;
        }

        public Integer getServiceId() {
            return serviceId;
        }

        public void setServiceId(Integer serviceId) {
            this.serviceId = serviceId;
        }

        public List<LicensePlate> getLicensePlate() {
            return licensePlate;
        }

        public void setLicensePlate(List<LicensePlate> licensePlate) {
            this.licensePlate = licensePlate;
        }

        public List<Object> getDocument() {
            return document;
        }

        public void setDocument(List<Object> document) {
            this.document = document;
        }

        public List<Object> getAvatar() {
            return avatar;
        }

        public void setAvatar(List<Object> avatar) {
            this.avatar = avatar;
        }

        public List<UserSite> getUserSites() {
            return userSites;
        }

        public void setUserSites(List<UserSite> userSites) {
            this.userSites = userSites;
        }

        public Integer getFreeCredit() {
            return freeCredit;
        }

        public void setFreeCredit(Integer freeCredit) {
            this.freeCredit = freeCredit;
        }

    }

    public class LicensePlate {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("mimetype")
        @Expose
        private String mimetype;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("size")
        @Expose
        private Integer size;
        @SerializedName("description")
        @Expose
        private Object description;
        @SerializedName("type")
        @Expose
        private Integer type;
        @SerializedName("ResourceType")
        @Expose
        private Integer resourceType;
        @SerializedName("ResourceId")
        @Expose
        private Integer resourceId;
        @SerializedName("ServiceId")
        @Expose
        private Integer serviceId;
        @SerializedName("code")
        @Expose
        private String code;
        @SerializedName("keys3")
        @Expose
        private String keys3;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("deleted_at")
        @Expose
        private Object deletedAt;

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

        public String getMimetype() {
            return mimetype;
        }

        public void setMimetype(String mimetype) {
            this.mimetype = mimetype;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public Integer getResourceType() {
            return resourceType;
        }

        public void setResourceType(Integer resourceType) {
            this.resourceType = resourceType;
        }

        public Integer getResourceId() {
            return resourceId;
        }

        public void setResourceId(Integer resourceId) {
            this.resourceId = resourceId;
        }

        public Integer getServiceId() {
            return serviceId;
        }

        public void setServiceId(Integer serviceId) {
            this.serviceId = serviceId;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getKeys3() {
            return keys3;
        }

        public void setKeys3(String keys3) {
            this.keys3 = keys3;
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

    }

    public class Data {

        @SerializedName("booking")
        @Expose
        private Booking booking;
        @SerializedName("user")
        @Expose
        private User user;
        @SerializedName("token")
        @Expose
        private Token token;
        @SerializedName("rent")
        @Expose
        private List<Object> rent = null;
        @SerializedName("service")
        @Expose
        private Service service;
        @SerializedName("bookingsStats")
        @Expose
        private BookingsStats bookingsStats;

        public Booking getBooking() {
            return booking;
        }

        public void setBooking(Booking booking) {
            this.booking = booking;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Token getToken() {
            return token;
        }

        public void setToken(Token token) {
            this.token = token;
        }

        public List<Object> getRent() {
            return rent;
        }

        public void setRent(List<Object> rent) {
            this.rent = rent;
        }

        public Service getService() {
            return service;
        }

        public void setService(Service service) {
            this.service = service;
        }

        public BookingsStats getBookingsStats() {
            return bookingsStats;
        }

        public void setBookingsStats(BookingsStats bookingsStats) {
            this.bookingsStats = bookingsStats;
        }

    }

    @SerializedName("booking")
    @Expose
    private Booking booking;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("token")
    @Expose
    private Token token;
    @SerializedName("rent")
    @Expose
    private List<Object> rent = null;
    @SerializedName("service")
    @Expose
    private Service service;
    @SerializedName("bookingsStats")
    @Expose
    private BookingsStats bookingsStats;

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public List<Object> getRent() {
        return rent;
    }

    public void setRent(List<Object> rent) {
        this.rent = rent;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public BookingsStats getBookingsStats() {
        return bookingsStats;
    }

    public void setBookingsStats(BookingsStats bookingsStats) {
        this.bookingsStats = bookingsStats;
    }
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("freeMinutes")
    @Expose
    private Integer freeMinutes;
    @SerializedName("prepaidMinutes")
    @Expose
    private Integer prepaidMinutes;
    @SerializedName("validLicensePlate")
    @Expose
    private Integer validLicensePlate;
    @SerializedName("validDocument")
    @Expose
    private Integer validDocument;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("surname")
    @Expose
    private String surname;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("license_id")
    @Expose
    private String licenseId;
    @SerializedName("license_expiration")
    @Expose
    private String licenseExpiration;
    @SerializedName("license_release")
    @Expose
    private String licenseRelease;
    @SerializedName("license_place")
    @Expose
    private Object licensePlace;
    @SerializedName("license_country")
    @Expose
    private String licenseCountry;
    @SerializedName("identity_type")
    @Expose
    private Object identityType;
    @SerializedName("identity_id")
    @Expose
    private Object identityId;
    @SerializedName("identity_release")
    @Expose
    private Object identityRelease;
    @SerializedName("identity_expiration")
    @Expose
    private Object identityExpiration;
    @SerializedName("identity_issue_city")
    @Expose
    private Object identityIssueCity;
    @SerializedName("identity_issue_state")
    @Expose
    private Object identityIssueState;
    @SerializedName("cf")
    @Expose
    private String cf;
    @SerializedName("sex")
    @Expose
    private Object sex;
    @SerializedName("birth")
    @Expose
    private String birth;
    @SerializedName("residence")
    @Expose
    private String residence;
    @SerializedName("cap")
    @Expose
    private String cap;
    @SerializedName("tel")
    @Expose
    private String tel;
    @SerializedName("validSMS")
    @Expose
    private Boolean validSMS;
    @SerializedName("validEmail")
    @Expose
    private Boolean validEmail;
    @SerializedName("validID")
    @Expose
    private Boolean validID;
    @SerializedName("online")
    @Expose
    private Boolean online;
    @SerializedName("authy")
    @Expose
    private Boolean authy;
    @SerializedName("authy_id")
    @Expose
    private Object authyId;
    @SerializedName("oauth")
    @Expose
    private Boolean oauth;
    @SerializedName("paymentCreated")
    @Expose
    private Boolean paymentCreated;
    @SerializedName("oauth_id")
    @Expose
    private Object oauthId;
    @SerializedName("paymentRef")
    @Expose
    private Object paymentRef;
    @SerializedName("birth_country")
    @Expose
    private Object birthCountry;
    @SerializedName("birth_city")
    @Expose
    private Object birthCity;
    @SerializedName("birth_state")
    @Expose
    private Object birthState;
    @SerializedName("residence_country")
    @Expose
    private String residenceCountry;
    @SerializedName("residence_city")
    @Expose
    private String residenceCity;
    @SerializedName("residence_state")
    @Expose
    private Object residenceState;
    @SerializedName("company_name")
    @Expose
    private Object companyName;
    @SerializedName("type")
    @Expose
    private Object type;
    @SerializedName("sendRegisterCompleteEmail")
    @Expose
    private Boolean sendRegisterCompleteEmail;
    @SerializedName("block")
    @Expose
    private Boolean block;
    @SerializedName("block_reason")
    @Expose
    private Object blockReason;
    @SerializedName("accessForScooter")
    @Expose
    private Boolean accessForScooter;
    @SerializedName("accessForBike")
    @Expose
    private Boolean accessForBike;
    @SerializedName("accessForCar")
    @Expose
    private Boolean accessForCar;
    @SerializedName("lastPaymentStatus")
    @Expose
    private String lastPaymentStatus;
    @SerializedName("referal")
    @Expose
    private String referal;
    @SerializedName("freeMilliseconds")
    @Expose
    private Integer freeMilliseconds;
    @SerializedName("prepaidMilliseconds")
    @Expose
    private Integer prepaidMilliseconds;
    @SerializedName("cashbackCount")
    @Expose
    private Integer cashbackCount;
    @SerializedName("createdBy")
    @Expose
    private Integer createdBy;
    @SerializedName("fbAccountKitId")
    @Expose
    private Object fbAccountKitId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("SiteId")
    @Expose
    private Integer siteId;
    @SerializedName("ServiceId")
    @Expose
    private Integer serviceId;
    @SerializedName("LicensePlate")
    @Expose
    private List<LicensePlate> licensePlate = null;
    @SerializedName("Document")
    @Expose
    private List<Object> document = null;
    @SerializedName("Avatar")
    @Expose
    private List<Object> avatar = null;
    @SerializedName("UserSites")
    @Expose
    private List<UserSite> userSites = null;
    @SerializedName("freeCredit")
    @Expose
    private Integer freeCredit;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getFreeMinutes() {
        return freeMinutes;
    }

    public void setFreeMinutes(Integer freeMinutes) {
        this.freeMinutes = freeMinutes;
    }

    public Integer getPrepaidMinutes() {
        return prepaidMinutes;
    }

    public void setPrepaidMinutes(Integer prepaidMinutes) {
        this.prepaidMinutes = prepaidMinutes;
    }

    public Integer getValidLicensePlate() {
        return validLicensePlate;
    }

    public void setValidLicensePlate(Integer validLicensePlate) {
        this.validLicensePlate = validLicensePlate;
    }

    public Integer getValidDocument() {
        return validDocument;
    }

    public void setValidDocument(Integer validDocument) {
        this.validDocument = validDocument;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public String getLicenseExpiration() {
        return licenseExpiration;
    }

    public void setLicenseExpiration(String licenseExpiration) {
        this.licenseExpiration = licenseExpiration;
    }

    public String getLicenseRelease() {
        return licenseRelease;
    }

    public void setLicenseRelease(String licenseRelease) {
        this.licenseRelease = licenseRelease;
    }

    public Object getLicensePlace() {
        return licensePlace;
    }

    public void setLicensePlace(Object licensePlace) {
        this.licensePlace = licensePlace;
    }

    public String getLicenseCountry() {
        return licenseCountry;
    }

    public void setLicenseCountry(String licenseCountry) {
        this.licenseCountry = licenseCountry;
    }

    public Object getIdentityType() {
        return identityType;
    }

    public void setIdentityType(Object identityType) {
        this.identityType = identityType;
    }

    public Object getIdentityId() {
        return identityId;
    }

    public void setIdentityId(Object identityId) {
        this.identityId = identityId;
    }

    public Object getIdentityRelease() {
        return identityRelease;
    }

    public void setIdentityRelease(Object identityRelease) {
        this.identityRelease = identityRelease;
    }

    public Object getIdentityExpiration() {
        return identityExpiration;
    }

    public void setIdentityExpiration(Object identityExpiration) {
        this.identityExpiration = identityExpiration;
    }

    public Object getIdentityIssueCity() {
        return identityIssueCity;
    }

    public void setIdentityIssueCity(Object identityIssueCity) {
        this.identityIssueCity = identityIssueCity;
    }

    public Object getIdentityIssueState() {
        return identityIssueState;
    }

    public void setIdentityIssueState(Object identityIssueState) {
        this.identityIssueState = identityIssueState;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public Object getSex() {
        return sex;
    }

    public void setSex(Object sex) {
        this.sex = sex;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Boolean getValidSMS() {
        return validSMS;
    }

    public void setValidSMS(Boolean validSMS) {
        this.validSMS = validSMS;
    }

    public Boolean getValidEmail() {
        return validEmail;
    }

    public void setValidEmail(Boolean validEmail) {
        this.validEmail = validEmail;
    }

    public Boolean getValidID() {
        return validID;
    }

    public void setValidID(Boolean validID) {
        this.validID = validID;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Boolean getAuthy() {
        return authy;
    }

    public void setAuthy(Boolean authy) {
        this.authy = authy;
    }

    public Object getAuthyId() {
        return authyId;
    }

    public void setAuthyId(Object authyId) {
        this.authyId = authyId;
    }

    public Boolean getOauth() {
        return oauth;
    }

    public void setOauth(Boolean oauth) {
        this.oauth = oauth;
    }

    public Boolean getPaymentCreated() {
        return paymentCreated;
    }

    public void setPaymentCreated(Boolean paymentCreated) {
        this.paymentCreated = paymentCreated;
    }

    public Object getOauthId() {
        return oauthId;
    }

    public void setOauthId(Object oauthId) {
        this.oauthId = oauthId;
    }

    public Object getPaymentRef() {
        return paymentRef;
    }

    public void setPaymentRef(Object paymentRef) {
        this.paymentRef = paymentRef;
    }

    public Object getBirthCountry() {
        return birthCountry;
    }

    public void setBirthCountry(Object birthCountry) {
        this.birthCountry = birthCountry;
    }

    public Object getBirthCity() {
        return birthCity;
    }

    public void setBirthCity(Object birthCity) {
        this.birthCity = birthCity;
    }

    public Object getBirthState() {
        return birthState;
    }

    public void setBirthState(Object birthState) {
        this.birthState = birthState;
    }

    public String getResidenceCountry() {
        return residenceCountry;
    }

    public void setResidenceCountry(String residenceCountry) {
        this.residenceCountry = residenceCountry;
    }

    public String getResidenceCity() {
        return residenceCity;
    }

    public void setResidenceCity(String residenceCity) {
        this.residenceCity = residenceCity;
    }

    public Object getResidenceState() {
        return residenceState;
    }

    public void setResidenceState(Object residenceState) {
        this.residenceState = residenceState;
    }

    public Object getCompanyName() {
        return companyName;
    }

    public void setCompanyName(Object companyName) {
        this.companyName = companyName;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public Boolean getSendRegisterCompleteEmail() {
        return sendRegisterCompleteEmail;
    }

    public void setSendRegisterCompleteEmail(Boolean sendRegisterCompleteEmail) {
        this.sendRegisterCompleteEmail = sendRegisterCompleteEmail;
    }

    public Boolean getBlock() {
        return block;
    }

    public void setBlock(Boolean block) {
        this.block = block;
    }

    public Object getBlockReason() {
        return blockReason;
    }

    public void setBlockReason(Object blockReason) {
        this.blockReason = blockReason;
    }

    public Boolean getAccessForScooter() {
        return accessForScooter;
    }

    public void setAccessForScooter(Boolean accessForScooter) {
        this.accessForScooter = accessForScooter;
    }

    public Boolean getAccessForBike() {
        return accessForBike;
    }

    public void setAccessForBike(Boolean accessForBike) {
        this.accessForBike = accessForBike;
    }

    public Boolean getAccessForCar() {
        return accessForCar;
    }

    public void setAccessForCar(Boolean accessForCar) {
        this.accessForCar = accessForCar;
    }

    public String getLastPaymentStatus() {
        return lastPaymentStatus;
    }

    public void setLastPaymentStatus(String lastPaymentStatus) {
        this.lastPaymentStatus = lastPaymentStatus;
    }

    public String getReferal() {
        return referal;
    }

    public void setReferal(String referal) {
        this.referal = referal;
    }

    public Integer getFreeMilliseconds() {
        return freeMilliseconds;
    }

    public void setFreeMilliseconds(Integer freeMilliseconds) {
        this.freeMilliseconds = freeMilliseconds;
    }

    public Integer getPrepaidMilliseconds() {
        return prepaidMilliseconds;
    }

    public void setPrepaidMilliseconds(Integer prepaidMilliseconds) {
        this.prepaidMilliseconds = prepaidMilliseconds;
    }

    public Integer getCashbackCount() {
        return cashbackCount;
    }

    public void setCashbackCount(Integer cashbackCount) {
        this.cashbackCount = cashbackCount;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Object getFbAccountKitId() {
        return fbAccountKitId;
    }

    public void setFbAccountKitId(Object fbAccountKitId) {
        this.fbAccountKitId = fbAccountKitId;
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

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public List<LicensePlate> getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(List<LicensePlate> licensePlate) {
        this.licensePlate = licensePlate;
    }

    public List<Object> getDocument() {
        return document;
    }

    public void setDocument(List<Object> document) {
        this.document = document;
    }

    public List<Object> getAvatar() {
        return avatar;
    }

    public void setAvatar(List<Object> avatar) {
        this.avatar = avatar;
    }

    public List<UserSite> getUserSites() {
        return userSites;
    }

    public void setUserSites(List<UserSite> userSites) {
        this.userSites = userSites;
    }

    public Integer getFreeCredit() {
        return freeCredit;
    }

    public void setFreeCredit(Integer freeCredit) {
        this.freeCredit = freeCredit;
    }

    }
