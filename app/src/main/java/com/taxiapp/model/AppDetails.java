package com.taxiapp.model;

import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppDetails {

	private String packageName;
	private String versionNumber;
	private Date lastUpdated;
	private String licenseText;
	private String apkFilePath;
	private boolean markForUpdate;
	private boolean isSystemApp;
	private long downloadId = -1;
	private String downloadFilePath;
	private boolean isForceInstall = false;
	private int versionCode;
	private transient ApplicationInfo app;
	private String appName;
	private transient Drawable appIcon;

	public AppDetails(String packageName) {
		this.packageName = packageName;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getLicenseText() {
		return licenseText;
	}

	public void setLicenseText(String licenseText) {
		this.licenseText = licenseText;
	}

	public String getApkFilePath() {
		return apkFilePath;
	}

	public void setApkFilePath(String apkFilePath) {
		this.apkFilePath = apkFilePath;
	}

	@SuppressLint("SimpleDateFormat")
	public String getFormattedDate() {
		if (lastUpdated == null)
			return "";
		SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"EEE, dd MMM yyyy");
		return dateFormatter.format(lastUpdated);

	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((packageName == null) ? 0 : packageName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppDetails other = (AppDetails) obj;
		if (packageName == null) {
			if (other.packageName != null)
				return false;
		} else if (!packageName.equals(other.packageName))
			return false;
		return true;
	}

	public boolean isMarkForUpdate() {
		return markForUpdate;
	}

	public void setMarkForUpdate(boolean markForUpdate) {
		this.markForUpdate = markForUpdate;
	}

	public boolean isSystemApp() {
		return isSystemApp;
	}

	public void setSystemApp(boolean isSystemApp) {
		this.isSystemApp = isSystemApp;
	}

	public long getDownloadId() {
		return downloadId;
	}

	public void setDownloadId(long downloadId) {
		this.downloadId = downloadId;
	}

	public String getDownloadFilePath() {
		return downloadFilePath;
	}

	public void setDownloadFilePath(String downloadFilePath) {
		this.downloadFilePath = downloadFilePath;
	}

	public boolean isForceInstall() {
		return isForceInstall;
	}

	public void setForceInstall(boolean isForceInstall) {
		this.isForceInstall = isForceInstall;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public ApplicationInfo getApp() {
		return app;
	}

	public void setApp(ApplicationInfo app) {
		this.app = app;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Drawable getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}

}
