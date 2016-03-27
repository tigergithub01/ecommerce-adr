package com.ecommerce.utils;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.http.cookie.Cookie;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.ecommerce.R;

public class CommonUtils {
	private static final String TAG = "CommonUtils";

	public static String durationInSecondsToString(int sec) {
		int hours = sec / 3600;
		int minutes = (sec / 60) - (hours * 60);
		int seconds = sec - (hours * 3600) - (minutes * 60);
		String formatted = String.format("%d:%02d:%02d", hours, minutes,
				seconds);
		return formatted;
	}

	public static String getScreenSize(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		return display.getWidth() + "x" + display.getHeight();
	}
	
	public static String getDeviceId(Context context) {
		String imei = ((TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId();
		return imei;
	}

	public static void setBrightness(int level, Context context,
			Activity activity) {
		ContentResolver cr = context.getContentResolver();
		Settings.System.putInt(cr, "screen_brightness", level);
		Window window = activity.getWindow();
		LayoutParams attributes = window.getAttributes();
		float flevel = level;
		Log.d(TAG, "---before---" + attributes.screenBrightness);
		attributes.screenBrightness = flevel / 255;
		Log.d(TAG, "---after---" + attributes.screenBrightness);

		activity.getWindow().setAttributes(attributes);
	}

	public static String getSessionId() {
		return Constants.sessionId;
	}

	public static Cookie getSessionCookie() {
		return Constants.sessionCookie;
	}

	public static List<Cookie> getSessionCookieList() {
		return Constants.sessionCookieList;
	}

	public static String getFirstWords(String orignalString, int length,
			String chopedString) {
		if (orignalString == null || orignalString.length() == 0) {
			return orignalString;
		}
		orignalString = orignalString.replaceAll("   ", "   ");
		if (orignalString.length() < length) {
			return orignalString;
		}
		StringBuffer buffer = new StringBuffer(length);
		length = length * 2;
		int count = 0;
		int stringLength = orignalString.length();
		int i = 0;
		for (; count < length && i < stringLength; i++) {
			char c = orignalString.charAt(i);
			if (c < '\u00ff') {
				count++;
			} else {
				count += 2;
			}
			buffer.append(c);
		}
		if (i < stringLength) {
			buffer.append(chopedString);
		}
		return buffer.toString();
	}

	public static String formatUrl(String url, String parameters) {
		if (url.indexOf("?") == -1) {
			url = url + "?" + parameters;
		} else {
			url = url + "&" + parameters;
		}
		return url;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.  
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	public static void setBackgroundAlpha(Window window, float bgAlpha) {
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.alpha = bgAlpha; //0.0-1.0
		window.setAttributes(lp);
		//        view.getBackground().setAlpha(bgAlpha);
	}

	public static void showAlertDialog(Context context, String msg) {
		new AlertDialog.Builder(context)
				.setTitle(R.string.common_dialog_title)
				.setMessage(msg)
				.setPositiveButton(R.string.common_dialog_confirm,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						}).create().show();
	}

	public static String getAmount(String amount) {
		return "￥" + amount;
	}

	public static String formatAmount(Double value) {
		DecimalFormat df = new DecimalFormat(Constants.DECIMAL_FORMAT);
		return "￥" + df.format(value);
	}

	public static void addActivity(Activity activity) {
		Constants.activityList.add(activity);
	}

	public static void exitSystem(Context context) {
		//finish all activity
		for (int i = 0; i < Constants.activityList.size(); i++) {
			if (null != Constants.activityList.get(i)) {
				Constants.activityList.get(i).finish();
			}
		}
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		activityManager.killBackgroundProcesses("com.ecommerce");
		System.exit(0);
	}

	public static String getWebchatAppId(Context context) {
		String verFlag = ConfigReader.getInstance(context).getProperties(
				"ver_flag");
		if ("1".equals(verFlag)) {
			return Constants.WEBCHAT_APP_ID_FORMAL;
		} else {
			return Constants.WEBCHAT_APP_ID;
		}
	}

	public static String getPhoneModel() {
		return android.os.Build.MODEL;
	}

	public static String getOsSdk() {
		return android.os.Build.VERSION.SDK;
	}

	public static String getOsRelease() {
		return android.os.Build.VERSION.RELEASE;
	}

	public static String getPhoneBrand() {
		return android.os.Build.BRAND;
	}
	
	
	
	

}
