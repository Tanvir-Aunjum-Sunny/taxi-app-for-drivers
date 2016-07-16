package com.taxiapp.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;

public class FileCache {

	private File cacheDir;

	public FileCache(Context context, String name) {

		// Find the dir at SDCARD to save cached images

		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory(), name);
		} else {
			// if checking on simulator the create cache dir in your application
			// context
			cacheDir = context.getCacheDir();
		}

		if (!cacheDir.exists()) {
			// create cache dir in your application context
			cacheDir.mkdirs();
		}
	}

	public void clear() {
		// list all files inside cache directory
		File[] files = cacheDir.listFiles();
		if (files == null)
			return;
		// delete all cache directory files
		for (File f : files)
			f.delete();
	}

	public String getFilePath(String fileName) throws FileNotFoundException {
		File file = new File(cacheDir, fileName);
		if (!file.exists()) {
			throw new FileNotFoundException(fileName + "not Found");
		}
		Log.i("FilePath", file.getAbsolutePath());
		return file.getAbsolutePath();
	}

	public File getFile(String url) {
		// Identify images by hashcode or encode by URLEncoder.encode.
		// String filename=String.valueOf(url);
		File f = new File(cacheDir, url);
		f.getParentFile().mkdirs();
		return f;

	}

}