package com.taxiapp.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.apache.commons.lang.StringUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    // any time you make changes to your database objects, you may have to
    // increase the database version
    private static final int DATABASE_VERSION = 1;

    private Class[] dbClasses;
    private String dbName;

    public DatabaseHelper(Context context, Class[] dbClasses, String dbName) {
        super(context, dbName, null, DATABASE_VERSION);
        this.dbClasses = dbClasses;
        this.dbName = dbName;

    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            createDbTables(database, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void createDbTables(SQLiteDatabase database, ConnectionSource connectionSource)
            throws java.sql.SQLException {

        for (Class clazz : dbClasses) {
            TableUtils.createTable(connectionSource, clazz);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion,
                          int newVersion) {

        // AppLogger.get().i(getClass(), "Upgrading DB - " + dbName + " from " + oldVersion + " to "
        // + newVersion);
        //
        // switch (oldVersion) {
        // case 1:
        // updateFromVersion1(db, connectionSource, oldVersion, newVersion);
        // break;
        // case 2:
        // updateFromVersion2(db, connectionSource, oldVersion, newVersion);
        // break;
        // case 3:
        // updateFromVersion3(db, connectionSource, oldVersion, newVersion);
        // break;
        // case 5:
        //
        // break;
        // default:
        // break;
        // }

        updateFromVersion4(db, connectionSource, oldVersion, newVersion);

    }

    private void updateFromVersion4(SQLiteDatabase db, ConnectionSource connectionSource,
                                    int oldVersion, int newVersion) {
        if (StringUtils.equals(dbName, TransactionDBManager.dbName)) {
            TransactionDBManager.getInstance().runUpdate(connectionSource, oldVersion, newVersion);
        }
    }

    private void updateFromVersion3(SQLiteDatabase db, ConnectionSource connectionSource,
                                    int oldVersion, int newVersion) {
        onUpgrade(db, connectionSource, oldVersion + 1, newVersion);

    }

    private void updateFromVersion1(SQLiteDatabase database, ConnectionSource connectionSource,
                                    int oldVersion, int newVersion) {

        onUpgrade(database, connectionSource, oldVersion + 1, newVersion);
    }

    private void updateFromVersion2(SQLiteDatabase database, ConnectionSource connectionSource,
                                    int oldVersion, int newVersion) {
        // do some stuff here
        onUpgrade(database, connectionSource, oldVersion + 1, newVersion);
    }
}
