package com.taxiapp.sync;

public class AppSettings {

    public static final String KEY = "appSettings";

    private boolean isInitSuccessFirstTime;
    private boolean isAppInitSuccessfully;

    private long lastOnlineLogin;
    private long lastLoginTime;
    private boolean syncRunning;
    private long lastSyncRunTime;


    public boolean isInitSuccessFirstTime() {
        return isInitSuccessFirstTime;
    }

    public void setInitSuccessFirstTime(boolean isInitSuccessFirstTime) {
        this.isInitSuccessFirstTime = isInitSuccessFirstTime;
    }

    public boolean isAppInitSuccessfully() {
        return isAppInitSuccessfully;
    }

    public void setAppInitSuccessfully(boolean isAppInitSuccessfully) {
        this.isAppInitSuccessfully = isAppInitSuccessfully;
    }

    public long getLastOnlineLogin() {
        return lastOnlineLogin;
    }

    public void setLastOnlineLogin(long lastOnlineLogin) {
        this.lastOnlineLogin = lastOnlineLogin;
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }


    public boolean isSyncRunning() {
        return syncRunning;
    }

    public void setSyncRunning(boolean syncRunning) {
        this.syncRunning = syncRunning;
    }

    public long getLastSyncRunTime() {
        return lastSyncRunTime;
    }

    public void setLastSyncRunTime(long lastSyncRunTime) {
        this.lastSyncRunTime = lastSyncRunTime;
    }
}
