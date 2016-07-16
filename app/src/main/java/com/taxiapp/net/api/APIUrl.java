package com.taxiapp.net.api;

import org.apache.commons.lang.StringUtils;

import java.net.URI;
import java.util.Map;

public enum APIUrl {

//    BASE_URL("http://betataxiapp.com"),
//
//    //Parameters: ?mobile_no=9900489498
//    OTP_REQUEST_URL("/api/customer_app/v3/activations.php"),
//
//    //Parameters: ?token=00078d65507f40a151136ac9ca032f1e
//    DRIVER_AUTHENTICATION("/api/customer_app/v3/authentication.php"),
//
//    //?action_no=7&user_type=V&token=1ac5f6c69e7884af93f519fa7282f7bc&count_context_id=3&cellphone=9562337008&imei_no=76542332
//    DRIVER_API("/admin/driver_api.php"),
//
//    DEVICE_DATA_COLLECTION("/admin/app_data_collector.php"),
//

    BASE_URL("http://api.betataxiapp.com/driver_app"),

    //Parameters: ?mobile_no=9900489498
    OTP_REQUEST_URL("/activations.php"),

    //Parameters: ?token=00078d65507f40a151136ac9ca032f1e
    DRIVER_AUTHENTICATION("/authentication.php"),

    //?action_no=7&user_type=V&token=1ac5f6c69e7884af93f519fa7282f7bc&count_context_id=3&cellphone=9562337008&imei_no=76542332
    DRIVER_API("/driver_api.php"),

    DEVICE_DATA_COLLECTION("/app_data_collector.php"),

    ;
    private String path;

    private APIUrl(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFullPath() {
        return BASE_URL.getPath() + path;
    }

    public URI getURI() {
        return URI.create(getFullPath());
    }

    public URI getURI(Map<String, String> params) {
        String url = getFullPath();
        for (String key : params.keySet()) {
            url = StringUtils.replaceOnce(url, "{" + key + "}", params.get(key));
        }
        return URI.create(url);
    }
}
