package com.taxiapp.model;

import java.io.Serializable;

/**
 * The persistent class for the device_hardware_info database table.
 */

public class DeviceHardwareInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private double cpuSpeed;
    private int dpi;
    private String manufacturer;
    private String model;
    private String operatingSystem;
    private String osVersion;
    private int screenHeight;
    private int screenWidth;
    private double totalMemory;
    private double totalStorage;
    private String uid;
    private String imeiNumber;
    private String gcmId;
    private String simCardNumber;
    private boolean nfsAvailable;

    private String carrier;
    private boolean dataConnAvailable;
    private boolean roaming;
    private String dataMode;
    private boolean telephoneAvailable;

    private boolean btEnabled;
    private String btVersion;

    private String taxiappAppVersion;

    private String email;
    private String phoneNumber;

    // bi-directional many-to-one association to DeviceDetail
    public DeviceHardwareInfo() {
    }

    public double getCpuSpeed() {
        return this.cpuSpeed;
    }

    public void setCpuSpeed(double cpuSpeed) {
        this.cpuSpeed = cpuSpeed;
    }

    public int getDpi() {
        return this.dpi;
    }

    public void setDpi(int dpi) {
        this.dpi = dpi;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getOperatingSystem() {
        return this.operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getOsVersion() {
        return this.osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public int getScreenHeight() {
        return this.screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public int getScreenWidth() {
        return this.screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public double getTotalMemory() {
        return this.totalMemory;
    }

    public void setTotalMemory(double totalMemory) {
        this.totalMemory = totalMemory;
    }

    public double getTotalStorage() {
        return this.totalStorage;
    }

    public void setTotalStorage(double totalStorage) {
        this.totalStorage = totalStorage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImeiNumber() {
        return imeiNumber;
    }

    public void setImeiNumber(String imeiNumber) {
        this.imeiNumber = imeiNumber;
    }

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }

    public String getSimCardNumber() {
        return simCardNumber;
    }

    public void setSimCardNumber(String simCardNumber) {
        this.simCardNumber = simCardNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isNfsAvailable() {
        return nfsAvailable;
    }

    public void setNfsAvailable(boolean nfsAvailable) {
        this.nfsAvailable = nfsAvailable;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public boolean isDataConnAvailable() {
        return dataConnAvailable;
    }

    public void setDataConnAvailable(boolean dataConnAvailable) {
        this.dataConnAvailable = dataConnAvailable;
    }

    public String getDataMode() {
        return dataMode;
    }

    public void setDataMode(String dataMode) {
        this.dataMode = dataMode;
    }

    public boolean isTelephoneAvailable() {
        return telephoneAvailable;
    }

    public void setTelephoneAvailable(boolean telephoneAvailable) {
        this.telephoneAvailable = telephoneAvailable;
    }

    public boolean isBtEnabled() {
        return btEnabled;
    }

    public void setBtEnabled(boolean btEnabled) {
        this.btEnabled = btEnabled;
    }

    public String getBtVersion() {
        return btVersion;
    }

    public void setBtVersion(String btVersion) {
        this.btVersion = btVersion;
    }

    public String getTaxiAppAppVersion() {
        return taxiappAppVersion;
    }

    public void setTaxiAppAppVersion(String taxiappAppVersion) {
        this.taxiappAppVersion = taxiappAppVersion;
    }

    public boolean isRoaming() {
        return roaming;
    }

    public void setRoaming(boolean roaming) {
        this.roaming = roaming;
    }
}