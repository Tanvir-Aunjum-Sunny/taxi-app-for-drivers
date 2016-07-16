package com.taxiapp.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.taxiapp.model.DriverLocation;
import com.taxiapp.model.business.Booking;
import com.taxiapp.utils.AppLogger;

import java.sql.SQLException;

public class TransactionDBManager {
    static private TransactionDBManager instance;
    public static String dbName = "/sdcard/taxiappdriver/app.db";

    static public void init(Context ctx) {
        if (null == instance) {
            instance = new TransactionDBManager(ctx);
        }
    }

    static public TransactionDBManager getInstance() {
        return instance;
    }

    private DatabaseHelper helper;
    private Class[] dbClasses = {Booking.class, DriverLocation.class};

    private TransactionDBManager(Context ctx) {
        helper = new DatabaseHelper(ctx, dbClasses, dbName);
    }

    public BookingDao getBookingDao() throws Exception {
        return (BookingDao) createDao(Booking.class);
    }

    public DriverLocationDAO getDriverLocationDAO() throws Exception {
        return (DriverLocationDAO) createDao(DriverLocation.class);
    }

    private <D> Dao<D, ?> createDao(Class<D> clazz) throws SQLException {
        return DaoManager.createDao(helper.getConnectionSource(), clazz);
    }

    private <D> Dao<D, ?> createDao(ConnectionSource connSrc, Class<D> clazz) throws SQLException {
        return DaoManager.createDao(connSrc, clazz);
    }

    public void runUpdate(ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            int result = 0;
        } catch (Exception e) {
            AppLogger.get().e(getClass(), "Error updating DB from - " + oldVersion + " to " + newVersion, e);
        }

    }

    public void clearTables() {
        for (Class tableClass : dbClasses) {
            try {
                TableUtils.clearTable(helper.getConnectionSource(), tableClass);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}