package com.ecommerce.utils;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;

public class VersionUtils {
	private static final String TAG = "VersionUtils";

	public static int getVerCode(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(
					"com.ecommerce", 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
		}
		return verCode;
	}

	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					"com.ecommerce", 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
		}
		return verName;
	}

	public static void updateVer(Context context, File file) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	

}
