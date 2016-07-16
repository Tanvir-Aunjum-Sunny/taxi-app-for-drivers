package com.taxiapp.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class StorageUtils {

	public static File getFileInSDCard(String name) {
		File updaterDir = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/smargav/");
		if (!updaterDir.exists()) {
			updaterDir.mkdir();
		}
		return new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/smargav/" + name);
	}

	public static File getFileInSDCard(Context ctx, String name) {
		return new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/" + name);
	}
}
