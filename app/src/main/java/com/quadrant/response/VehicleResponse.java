package com.quadrant.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehicleResponse {
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
    public static class Service {

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

    }

    public class ExtraInfo {


    }

    public class ExternalStatus {

        @SerializedName("trip")
        @Expose
        private Boolean trip;
        @SerializedName("paused")
        @Expose
        private Boolean paused;
        @SerializedName("parked")
        @Expose
        private Boolean parked;
        @SerializedName("outside_operating_area")
        @Expose
        private Boolean outsideOperatingArea;
        @SerializedName("low_battery")
        @Expose
        private Boolean lowBattery;
        @SerializedName("repair_shop")
        @Expose
        private Boolean repairShop;
        @SerializedName("connection_issues")
        @Expose
        private Boolean connectionIssues;
        @SerializedName("minor_action_needed")
        @Expose
        private Boolean minorActionNeeded;
        @SerializedName("major_action_needed")
        @Expose
        private Boolean majorActionNeeded;
        @SerializedName("available")
        @Expose
        private Boolean available;

        public Boolean getTrip() {
            return trip;
        }

        public void setTrip(Boolean trip) {
            this.trip = trip;
        }

        public Boolean getPaused() {
            return paused;
        }

        public void setPaused(Boolean paused) {
            this.paused = paused;
        }

        public Boolean getParked() {
            return parked;
        }

        public void setParked(Boolean parked) {
            this.parked = parked;
        }

        public Boolean getOutsideOperatingArea() {
            return outsideOperatingArea;
        }

        public void setOutsideOperatingArea(Boolean outsideOperatingArea) {
            this.outsideOperatingArea = outsideOperatingArea;
        }

        public Boolean getLowBattery() {
            return lowBattery;
        }

        public void setLowBattery(Boolean lowBattery) {
            this.lowBattery = lowBattery;
        }

        public Boolean getRepairShop() {
            return repairShop;
        }

        public void setRepairShop(Boolean repairShop) {
            this.repairShop = repairShop;
        }

        public Boolean getConnectionIssues() {
            return connectionIssues;
        }

        public void setConnectionIssues(Boolean connectionIssues) {
            this.connectionIssues = connectionIssues;
        }

        public Boolean getMinorActionNeeded() {
            return minorActionNeeded;
        }

        public void setMinorActionNeeded(Boolean minorActionNeeded) {
            this.minorActionNeeded = minorActionNeeded;
        }

        public Boolean getMajorActionNeeded() {
            return majorActionNeeded;
        }

        public void setMajorActionNeeded(Boolean majorActionNeeded) {
            this.majorActionNeeded = majorActionNeeded;
        }

        public Boolean getAvailable() {
            return available;
        }

        public void setAvailable(Boolean available) {
            this.available = available;
        }

    }
    public class ExternalRole {

        @SerializedName("active")
        @Expose
        private Boolean active;
        @SerializedName("top_case")
        @Expose
        private Boolean topCase;
        @SerializedName("inactive")
        @Expose
        private Boolean inactive;

        public Boolean getActive() {
            return active;
        }

        public void setActive(Boolean active) {
            this.active = active;
        }

        public Boolean getTopCase() {
            return topCase;
        }

        public void setTopCase(Boolean topCase) {
            this.topCase = topCase;
        }

        public Boolean getInactive() {
            return inactive;
        }

        public void setInactive(Boolean inactive) {
            this.inactive = inactive;
        }

    }

    public static class Data {
        @SerializedName("id")
        @Expose
        private Integer id;

        @SerializedName("external_status")
        @Expose
        private ExternalStatus externalStatus;

        @SerializedName("longitude")
        @Expose
        private Double longitude;
        @SerializedName("latitude")
        @Expose
        private Double latitude;

        @SerializedName("type")
        @Expose
        private String type;

        @SerializedName("SiteId")
        @Expose
        private Integer siteId;

        @SerializedName("license_plate")
        @Expose
        private String licensePlate;

        @SerializedName("km")
        @Expose
        private Double km;

        @SerializedName("service")
        @Expose
        private Service service;

        @SerializedName("battery")
        @Expose
        private Battery battery;

        @SerializedName("address")
        @Expose
        private String address;

        @SerializedName("status")
        @Expose
        private String status;


        @SerializedName("total_percentage")
        @Expose
        private Integer totalPercentage;
        @SerializedName("type_engine")



        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getLicensePlate() {
            return licensePlate;
        }

        public void setLicensePlate(String licensePlate) {
            this.licensePlate = licensePlate;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public ExternalStatus getExternalStatus() {
            return externalStatus;
        }

        public void setExternalStatus(ExternalStatus externalStatus) {
            this.externalStatus = externalStatus;
        }
        public Battery getBattery() {
            return battery;
        }

        public void setBattery(Battery battery) {
            this.battery = battery;
        }

        public Service getService() {
            return service;
        }

        public void setService(Service service) {
            this.service = service;
        }

        public Double getKm() {
            return km;
        }

        public void setKm(Double km) {
            this.km = km;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }


        public Integer getTotalPercentage() {
            return totalPercentage;
        }

        public void setTotalPercentage(Integer totalPercentage) {
            this.totalPercentage = totalPercentage;
        }

/*
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("license_plate")
        @Expose
        private String licensePlate;
        @SerializedName("longitude")
        @Expose
        private Double longitude;
        @SerializedName("latitude")
        @Expose
        private Double latitude;
        @SerializedName("fixtaken")
        @Expose
        private Long fixtaken;
        @SerializedName("satellites")
        @Expose
        private Integer satellites;
        @SerializedName("dilution")
        @Expose
        private Double dilution;
        @SerializedName("altitude")
        @Expose
        private Double altitude;
        @SerializedName("sealevel")
        @Expose
        private Integer sealevel;
        @SerializedName("speed")
        @Expose
        private Integer speed;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("sender")
        @Expose
        private String sender;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("operative_field")
        @Expose
        private Integer operativeField;
        @SerializedName("registration_date")
        @Expose
        private String registrationDate;
        @SerializedName("km")
        @Expose
        private Integer km;
        @SerializedName("isTesting")
        @Expose
        private Boolean isTesting;
        @SerializedName("brand")
        @Expose
        private String brand;
        @SerializedName("version")
        @Expose
        private String version;
        @SerializedName("model_name")
        @Expose
        private String modelName;
        @SerializedName("cylinder_capacity")
        @Expose
        private Integer cylinderCapacity;
        @SerializedName("online")
        @Expose
        private Boolean online;
        @SerializedName("HistoryVehicleId")
        @Expose
        private Object historyVehicleId;
        @SerializedName("inMaintenance")
        @Expose
        private Boolean inMaintenance;
        @SerializedName("action_needed")
        @Expose
        private Object actionNeeded;
        @SerializedName("address")
        @Expose
        private Object address;
        @SerializedName("total_percentage")
        @Expose
        private Integer totalPercentage;
        @SerializedName("type_engine")
        @Expose
        private Integer typeEngine;
        @SerializedName("HistoryBatteryId")
        @Expose
        private Object historyBatteryId;
        @SerializedName("gpsEnable")
        @Expose
        private Boolean gpsEnable;
        @SerializedName("accelerometer")
        @Expose
        private List<Object> accelerometer = null;
        @SerializedName("parser")
        @Expose
        private Object parser;
        @SerializedName("qrcode")
        @Expose
        private String qrcode;
        @SerializedName("external_status")
        @Expose
        private ExternalStatus externalStatus;
        @SerializedName("external_role")
        @Expose
        private ExternalRole externalRole;
        @SerializedName("trip_status")
        @Expose
        private Integer tripStatus;
        @SerializedName("reference_code")
        @Expose
        private String referenceCode;
        @SerializedName("deviceType")
        @Expose
        private Integer deviceType;
        @SerializedName("deviceId")
        @Expose
        private String deviceId;
        @SerializedName("extraInfo")
        @Expose
        private ExtraInfo extraInfo;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("deleted_at")
        @Expose
        private Object deletedAt;
        @SerializedName("service")
        @Expose
        private Service service;
        @SerializedName("battery")
        @Expose
        private Battery battery;
        @SerializedName("fuels")
        @Expose
        private List<Object> fuels = null;
        @SerializedName("category_rent")
        @Expose
        private Object categoryRent;
        @SerializedName("vehicle_model")
        @Expose
        private Object vehicleModel;
        @SerializedName("images")
        @Expose
        private List<Object> images = null;
        @SerializedName("SiteId")
        @Expose
        private Integer siteId;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getLicensePlate() {
            return licensePlate;
        }

        public void setLicensePlate(String licensePlate) {
            this.licensePlate = licensePlate;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Long getFixtaken() {
            return fixtaken;
        }

        public void setFixtaken(Long fixtaken) {
            this.fixtaken = fixtaken;
        }

        public Integer getSatellites() {
            return satellites;
        }

        public void setSatellites(Integer satellites) {
            this.satellites = satellites;
        }

        public Double getDilution() {
            return dilution;
        }

        public void setDilution(Double dilution) {
            this.dilution = dilution;
        }

        public Double getAltitude() {
            return altitude;
        }

        public void setAltitude(Double altitude) {
            this.altitude = altitude;
        }

        public Integer getSealevel() {
            return sealevel;
        }

        public void setSealevel(Integer sealevel) {
            this.sealevel = sealevel;
        }

        public Integer getSpeed() {
            return speed;
        }

        public void setSpeed(Integer speed) {
            this.speed = speed;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getOperativeField() {
            return operativeField;
        }

        public void setOperativeField(Integer operativeField) {
            this.operativeField = operativeField;
        }

        public String getRegistrationDate() {
            return registrationDate;
        }

        public void setRegistrationDate(String registrationDate) {
            this.registrationDate = registrationDate;
        }

        public Integer getKm() {
            return km;
        }

        public void setKm(Integer km) {
            this.km = km;
        }

        public Boolean getIsTesting() {
            return isTesting;
        }

        public void setIsTesting(Boolean isTesting) {
            this.isTesting = isTesting;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getModelName() {
            return modelName;
        }

        public void setModelName(String modelName) {
            this.modelName = modelName;
        }

        public Integer getCylinderCapacity() {
            return cylinderCapacity;
        }

        public void setCylinderCapacity(Integer cylinderCapacity) {
            this.cylinderCapacity = cylinderCapacity;
        }

        public Boolean getOnline() {
            return online;
        }

        public void setOnline(Boolean online) {
            this.online = online;
        }

        public Object getHistoryVehicleId() {
            return historyVehicleId;
        }

        public void setHistoryVehicleId(Object historyVehicleId) {
            this.historyVehicleId = historyVehicleId;
        }

        public Boolean getInMaintenance() {
            return inMaintenance;
        }

        public void setInMaintenance(Boolean inMaintenance) {
            this.inMaintenance = inMaintenance;
        }

        public Object getActionNeeded() {
            return actionNeeded;
        }

        public void setActionNeeded(Object actionNeeded) {
            this.actionNeeded = actionNeeded;
        }

        public Object getAddress() {
            return address;
        }

        public void setAddress(Object address) {
            this.address = address;
        }

        public Integer getTotalPercentage() {
            return totalPercentage;
        }

        public void setTotalPercentage(Integer totalPercentage) {
            this.totalPercentage = totalPercentage;
        }

        public Integer getTypeEngine() {
            return typeEngine;
        }

        public void setTypeEngine(Integer typeEngine) {
            this.typeEngine = typeEngine;
        }

        public Object getHistoryBatteryId() {
            return historyBatteryId;
        }

        public void setHistoryBatteryId(Object historyBatteryId) {
            this.historyBatteryId = historyBatteryId;
        }

        public Boolean getGpsEnable() {
            return gpsEnable;
        }

        public void setGpsEnable(Boolean gpsEnable) {
            this.gpsEnable = gpsEnable;
        }

        public List<Object> getAccelerometer() {
            return accelerometer;
        }

        public void setAccelerometer(List<Object> accelerometer) {
            this.accelerometer = accelerometer;
        }

        public Object getParser() {
            return parser;
        }

        public void setParser(Object parser) {
            this.parser = parser;
        }

        public String getQrcode() {
            return qrcode;
        }

        public void setQrcode(String qrcode) {
            this.qrcode = qrcode;
        }

        public ExternalStatus getExternalStatus() {
            return externalStatus;
        }

        public void setExternalStatus(ExternalStatus externalStatus) {
            this.externalStatus = externalStatus;
        }

        public ExternalRole getExternalRole() {
            return externalRole;
        }

        public void setExternalRole(ExternalRole externalRole) {
            this.externalRole = externalRole;
        }

        public Integer getTripStatus() {
            return tripStatus;
        }

        public void setTripStatus(Integer tripStatus) {
            this.tripStatus = tripStatus;
        }

        public String getReferenceCode() {
            return referenceCode;
        }

        public void setReferenceCode(String referenceCode) {
            this.referenceCode = referenceCode;
        }

        public Integer getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(Integer deviceType) {
            this.deviceType = deviceType;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public ExtraInfo getExtraInfo() {
            return extraInfo;
        }

        public void setExtraInfo(ExtraInfo extraInfo) {
            this.extraInfo = extraInfo;
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

        public Service getService() {
            return service;
        }

        public void setService(Service service) {
            this.service = service;
        }

        public Battery getBattery() {
            return battery;
        }

        public void setBattery(Battery battery) {
            this.battery = battery;
        }

        public List<Object> getFuels() {
            return fuels;
        }

        public void setFuels(List<Object> fuels) {
            this.fuels = fuels;
        }

        public Object getCategoryRent() {
            return categoryRent;
        }

        public void setCategoryRent(Object categoryRent) {
            this.categoryRent = categoryRent;
        }

        public Object getVehicleModel() {
            return vehicleModel;
        }

        public void setVehicleModel(Object vehicleModel) {
            this.vehicleModel = vehicleModel;
        }

        public List<Object> getImages() {
            return images;
        }

        public void setImages(List<Object> images) {
            this.images = images;
        }

        public Integer getSiteId() {
            return siteId;
        }

        public void setSiteId(Integer siteId) {
            this.siteId = siteId;
        }*/

    }
    public class Battery {

        @SerializedName("id")
        @Expose
        private double id;
        @SerializedName("percentage")
        @Expose
        private double percentage;
        @SerializedName("value_I")
        @Expose
        private Integer valueI;
        @SerializedName("value_V")
        @Expose
        private Integer valueV;
        @SerializedName("value_T")
        @Expose
        private Integer valueT;
        @SerializedName("ah_tot")
        @Expose
        private Integer ahTot;
        @SerializedName("recharge")
        @Expose
        private Integer recharge;
        @SerializedName("code")
        @Expose
        private String code;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("deleted_at")
        @Expose
        private Object deletedAt;

        public double getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Double getPercentage() {
            return percentage;
        }

        public void setPercentage(Integer percentage) {
            this.percentage = percentage;
        }

        public Integer getValueI() {
            return valueI;
        }

        public void setValueI(Integer valueI) {
            this.valueI = valueI;
        }

        public Integer getValueV() {
            return valueV;
        }

        public void setValueV(Integer valueV) {
            this.valueV = valueV;
        }

        public Integer getValueT() {
            return valueT;
        }

        public void setValueT(Integer valueT) {
            this.valueT = valueT;
        }

        public Integer getAhTot() {
            return ahTot;
        }

        public void setAhTot(Integer ahTot) {
            this.ahTot = ahTot;
        }

        public Integer getRecharge() {
            return recharge;
        }

        public void setRecharge(Integer recharge) {
            this.recharge = recharge;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
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
}
