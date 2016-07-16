package com.taxiapp.utils;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;

public class ActivityHelper {

	public static void initialize(AppCompatActivity activity) {
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

}
