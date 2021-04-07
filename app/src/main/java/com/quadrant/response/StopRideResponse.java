package com.quadrant.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StopRideResponse {
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

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("type")
        @Expose
        private Integer type;
        @SerializedName("distance")
        @Expose
        private Integer distance;
        @SerializedName("duration")
        @Expose
        private Integer duration;
        @SerializedName("started")
        @Expose
        private Integer started;
        @SerializedName("current_timer")
        @Expose
        private Integer currentTimer;
        @SerializedName("expired")
        @Expose
        private Integer expired;
        @SerializedName("setupEnd")
        @Expose
        private Integer setupEnd;
        @SerializedName("price")
        @Expose
        private Integer price;
        @SerializedName("free")
        @Expose
        private Boolean free;
        @SerializedName("detailedPrice")
        @Expose
        private DetailedPrice detailedPrice;
        @SerializedName("createdBy")
        @Expose
        private Integer createdBy;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("deleted_at")
        @Expose
        private String deletedAt;
        @SerializedName("vehicle")
        @Expose
        private Vehicle vehicle;
        @SerializedName("transactions")
        @Expose
        private List<Transaction> transactions = null;
        @SerializedName("timers")
        @Expose
        private Timers timers;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public Integer getDistance() {
            return distance;
        }

        public void setDistance(Integer distance) {
            this.distance = distance;
        }

        public Integer getDuration() {
            return duration;
        }

        public void setDuration(Integer duration) {
            this.duration = duration;
        }

        public Integer getStarted() {
            return started;
        }

        public void setStarted(Integer started) {
            this.started = started;
        }

        public Integer getCurrentTimer() {
            return currentTimer;
        }

        public void setCurrentTimer(Integer currentTimer) {
            this.currentTimer = currentTimer;
        }

        public Integer getExpired() {
            return expired;
        }

        public void setExpired(Integer expired) {
            this.expired = expired;
        }

        public Integer getSetupEnd() {
            return setupEnd;
        }

        public void setSetupEnd(Integer setupEnd) {
            this.setupEnd = setupEnd;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public Boolean getFree() {
            return free;
        }

        public void setFree(Boolean free) {
            this.free = free;
        }

        public DetailedPrice getDetailedPrice() {
            return detailedPrice;
        }

        public void setDetailedPrice(DetailedPrice detailedPrice) {
            this.detailedPrice = detailedPrice;
        }

        public Integer getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(Integer createdBy) {
            this.createdBy = createdBy;
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

        public Vehicle getVehicle() {
            return vehicle;
        }

        public void setVehicle(Vehicle vehicle) {
            this.vehicle = vehicle;
        }

        public List<Transaction> getTransactions() {
            return transactions;
        }

        public void setTransactions(List<Transaction> transactions) {
            this.transactions = transactions;
        }

        public Timers getTimers() {
            return timers;
        }

        public void setTimers(Timers timers) {
            this.timers = timers;
        }

    }
    public class DetailedPrice {

        @SerializedName("running")
        @Expose
        private Integer running;
        @SerializedName("parked")
        @Expose
        private Integer parked;
        @SerializedName("free")
        @Expose
        private Integer free;
        @SerializedName("prepaid")
        @Expose
        private Integer prepaid;
        @SerializedName("outOfGeoFence")
        @Expose
        private Integer outOfGeoFence;
        @SerializedName("adjustment")
        @Expose
        private Integer adjustment;
        @SerializedName("vat")
        @Expose
        private Integer vat;

        public Integer getRunning() {
            return running;
        }

        public void setRunning(Integer running) {
            this.running = running;
        }

        public Integer getParked() {
            return parked;
        }

        public void setParked(Integer parked) {
            this.parked = parked;
        }

        public Integer getFree() {
            return free;
        }

        public void setFree(Integer free) {
            this.free = free;
        }

        public Integer getPrepaid() {
            return prepaid;
        }

        public void setPrepaid(Integer prepaid) {
            this.prepaid = prepaid;
        }

        public Integer getOutOfGeoFence() {
            return outOfGeoFence;
        }

        public void setOutOfGeoFence(Integer outOfGeoFence) {
            this.outOfGeoFence = outOfGeoFence;
        }

        public Integer getAdjustment() {
            return adjustment;
        }

        public void setAdjustment(Integer adjustment) {
            this.adjustment = adjustment;
        }

        public Integer getVat() {
            return vat;
        }

        public void setVat(Integer vat) {
            this.vat = vat;
        }

    }
    public class Vehicle {

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
        private Integer fixtaken;
        @SerializedName("satellites")
        @Expose
        private Integer satellites;
        @SerializedName("dilution")
        @Expose
        private Integer dilution;
        @SerializedName("altitude")
        @Expose
        private Integer altitude;
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
        @SerializedName("inMaintenance")
        @Expose
        private Boolean inMaintenance;
        @SerializedName("action_needed")
        @Expose
        private Object actionNeeded;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("total_percentage")
        @Expose
        private Integer totalPercentage;
        @SerializedName("type_engine")
        @Expose
        private Integer typeEngine;
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
        private Object deviceId;
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

        public Integer getFixtaken() {
            return fixtaken;
        }

        public void setFixtaken(Integer fixtaken) {
            this.fixtaken = fixtaken;
        }

        public Integer getSatellites() {
            return satellites;
        }

        public void setSatellites(Integer satellites) {
            this.satellites = satellites;
        }

        public Integer getDilution() {
            return dilution;
        }

        public void setDilution(Integer dilution) {
            this.dilution = dilution;
        }

        public Integer getAltitude() {
            return altitude;
        }

        public void setAltitude(Integer altitude) {
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
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

        public Object getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(Object deviceId) {
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

    }
    public class Transaction {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("price")
        @Expose
        private Integer price;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("code")
        @Expose
        private String code;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("data")
        @Expose
        private Data_ data;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("deleted_at")
        @Expose
        private String deletedAt;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Data_ getData() {
            return data;
        }

        public void setData(Data_ data) {
            this.data = data;
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

    }
    public class Timers {

        @SerializedName("running")
        @Expose
        private Integer running;
        @SerializedName("parked")
        @Expose
        private Integer parked;

        public Integer getRunning() {
            return running;
        }

        public void setRunning(Integer running) {
            this.running = running;
        }

        public Integer getParked() {
            return parked;
        }

        public void setParked(Integer parked) {
            this.parked = parked;
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

    public class Data_ {

        @SerializedName("captured")
        @Expose
        private Boolean captured;
        @SerializedName("failure_code")
        @Expose
        private String failureCode;
        @SerializedName("failure_message")
        @Expose
        private String failureMessage;
        @SerializedName("balance_transaction")
        @Expose
        private String balanceTransaction;
        @SerializedName("invoice")
        @Expose
        private String invoice;
        @SerializedName("paid")
        @Expose
        private Boolean paid;

        public Boolean getCaptured() {
            return captured;
        }

        public void setCaptured(Boolean captured) {
            this.captured = captured;
        }

        public String getFailureCode() {
            return failureCode;
        }

        public void setFailureCode(String failureCode) {
            this.failureCode = failureCode;
        }

        public String getFailureMessage() {
            return failureMessage;
        }

        public void setFailureMessage(String failureMessage) {
            this.failureMessage = failureMessage;
        }

        public String getBalanceTransaction() {
            return balanceTransaction;
        }

        public void setBalanceTransaction(String balanceTransaction) {
            this.balanceTransaction = balanceTransaction;
        }

        public String getInvoice() {
            return invoice;
        }

        public void setInvoice(String invoice) {
            this.invoice = invoice;
        }

        public Boolean getPaid() {
            return paid;
        }

        public void setPaid(Boolean paid) {
            this.paid = paid;
        }

    }

}

