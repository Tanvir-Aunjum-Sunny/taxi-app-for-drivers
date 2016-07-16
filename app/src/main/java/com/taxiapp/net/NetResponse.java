package com.taxiapp.net;

import com.google.gson.annotations.SerializedName;

public class NetResponse<T> {

    @SerializedName("status_code")
    private int statusCode;

    @SerializedName("status_description")
    private String statusDescription;

    @SerializedName("status_details")
    private String statusDetails;

    //Used only for OTP activation.
    private String jwt;

    private T rows;

    //Billing purpose only
    //{"status_code":601,"status_description":"ERR_INTERNAL_ERROR","status_details":"",
    // "advance_amount_paid":0,"bill_total_amt":0,"cashtocollect":0,"billed_days":0,
    // "billed_kms":4500,"billed_hrs":0,"booking_id":"2006181"}

    private int bill_total_amt;
    private int advance_amount_paid;
    private int cashtocollect;
    private int billed_days;
    private int billed_kms;
    private int billed_hrs;


    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getStatusDetails() {
        return statusDetails;
    }

    public void setStatusDetails(String statusDetails) {
        this.statusDetails = statusDetails;
    }

    public T getRows() {
        return rows;
    }

    public void setRows(T rows) {
        this.rows = rows;
    }

    public int getBill_total_amt() {
        return bill_total_amt;
    }

    public void setBill_total_amt(int bill_total_amt) {
        this.bill_total_amt = bill_total_amt;
    }

    public int getAdvance_amount_paid() {
        return advance_amount_paid;
    }

    public void setAdvance_amount_paid(int advance_amount_paid) {
        this.advance_amount_paid = advance_amount_paid;
    }

    public int getCashtocollect() {
        return cashtocollect;
    }

    public void setCashtocollect(int cashtocollect) {
        this.cashtocollect = cashtocollect;
    }

    public int getBilled_days() {
        return billed_days;
    }

    public void setBilled_days(int billed_days) {
        this.billed_days = billed_days;
    }

    public int getBilled_kms() {
        return billed_kms;
    }

    public void setBilled_kms(int billed_kms) {
        this.billed_kms = billed_kms;
    }

    public int getBilled_hrs() {
        return billed_hrs;
    }

    public void setBilled_hrs(int billed_hrs) {
        this.billed_hrs = billed_hrs;
    }
}
