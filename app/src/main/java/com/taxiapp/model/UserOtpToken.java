package com.taxiapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Amit S on 16/11/15.
 */
public class UserOtpToken {

    private String token;

    @SerializedName("enc_otp")
    private String encodedToken;

    private String expiry;

    @SerializedName("operator_type")
    private String operatorType;

    private String phone;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEncodedToken() {
        return encodedToken;
    }

    public void setEncodedToken(String encodedToken) {
        this.encodedToken = encodedToken;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
