package com.taxiapp.utils;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;

public class FileWriter {
	public static void writeBitmap(File file, Bitmap bitmap) {
		if (file.exists())
			file.delete();
		try {
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
