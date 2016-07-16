package com.taxiapp.model.business;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Amit S on 16/11/15.
 */
public enum OperatorType {
    D("Driver"), V("Vendor"), O("Driver/Owner");

    private String title;

    private OperatorType(String name) {
        this.title = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static OperatorType get(String type) {
        for (OperatorType t : values()) {
            if (StringUtils.equalsIgnoreCase(type, t.name())) {
                return t;
            }
        }

        return OperatorType.D;
    }
}
