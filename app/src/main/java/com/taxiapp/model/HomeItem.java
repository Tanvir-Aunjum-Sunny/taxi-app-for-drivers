package com.taxiapp.model;

/**
 * Created by Amit S on 02/12/15.
 */
public class HomeItem {

    public static final int NEW_TRIP = 1;
    public static final int START_A_TRIP = 2;


    private int context;
    private int count;

    private String ids;

    public HomeItem(){}
    public HomeItem(int c, int count){
        context = c;
        this.count = count;
    }

    public int getContext() {
        return context;
    }

    public void setContext(int context) {
        this.context = context;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
}
