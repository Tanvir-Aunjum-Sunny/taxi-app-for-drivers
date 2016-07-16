package com.taxiapp.net.api;

/**
 * Created by Amit S on 16/11/15.
 */
public enum APIStatusCodes {

//    101 = SUCCESS;
//
//    # 2xx - General server errors
//    201 = ERR_SERVICE_UNAVAILABLE; # API system temporarly out of service
//
//    # 3xx - Authentication errors
//    301 = ERR_INVALID_KEY;
//    302 = ERR_INACTIVE_ACCOUNT;    # Account code is 0 -> Inactive
//    303 = ERR_INVALID_KEYFORMAT;   # KEY format is not correct.
//    304 = ERR_ACCESS_PERMISSION;
//    305 = ERR_AUTHENTICATION_FAILED;#  Authentication Failure, By login or API keys
//
//    # 4xx - Function call errors
//    401 = ERR_INTERNAL_ERROR;
//    402 = ERR_BAD_PARAMETER_VALUE;
//    403 = ERR_UNKNOWN_PARAMETER;  # Unexpected parameter name in params list
//    404 = ERR_MISSING_PARAMETER;  # Required parameter is not present in input
//
//    # 5xx - Data errors
//    501 = ERR_UPDATE_FAILED; # System Error in updating a record. Has to be used with try_catch
//    502 = ERR_NO_RESULT;     # Request has no data to output, or internal DB query return no result.
//            503 = ERR_DATA_UNAVAILABLE

    SUCCESS(101),

    ERR_SERVICE_UNAVAILABLE(201),


    ERR_INVALID_KEY(301),
    ERR_INACTIVE_ACCOUNT(302),
    ERR_INVALID_KEYFORMAT(303),
    ERR_ACCESS_PERMISSION(304),
    ERR_AUTHENTICATION_FAILED(305),

    ERR_INTERNAL_ERROR(401),
    ERR_BAD_PARAMETER_VALUE(402),
    ERR_UNKNOWN_PARAMETER(403),
    ERR_MISSING_PARAMETER(404),

    ERR_UPDATE_FAILED(501),
    ERR_NO_RESULT(502),
    ERR_DATA_UNAVAILABLE(503);


    private int code;

    private APIStatusCodes(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
