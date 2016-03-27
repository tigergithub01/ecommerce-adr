package com.ecommerce.application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.ecommerce.R;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.CommonUtils;
import com.ecommerce.utils.ConfigReader;

public class CrashHandler implements UncaughtExceptionHandler {

	public static final String TAG = "CrashHandler";

	private Thread.UncaughtExceptionHandler defaultHandler;
	private static CrashHandler handler;
	private Context context;
	private Map<String, String> infos = new HashMap<String, String>();
	private SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyyMMddHHmmssSSS", Locale.getDefault());

	private CrashHandler() {
	}

	public static CrashHandler getInstance() {
		if (handler == null) {
			handler = new CrashHandler();
		}
		return handler;
	}

	public void init(Context context) {
		this.context = context;
		defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && defaultHandler != null) {
			defaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Log.e(TAG, "error : ", e);
			}
			/*android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);*/
			CommonUtils.exitSystem(context);
			
			/*new Thread() {  
            @Override  
            public void run() {  
                Looper.prepare();  
                new AlertDialog.Builder(context).setTitle("提示").setCancelable(false)  
                        .setMessage("程序崩溃了...").setNeutralButton("我知道了", new OnClickListener() {  
                            @Override  
                            public void onClick(DialogInterface dialog, int which) {  
                                System.exit(0);  
                            }  
                        })  
                        .create().show();  
                Looper.loop();  
            }  
        }.start();  */
		}
		
	}

	private boolean handleException(final Throwable ex) {
		if (ex == null) {
			return false;
		}
		Log.e(TAG, ex.getMessage(), ex);
//		final String crashMsg = ex.getMessage();
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				String msg = "程序崩溃了:" + ExceptionHandler.getMessage(ex).toString();
				/*new Builder(context).setMessage(msg)
				.setTitle("系统异常")
				.setNegativeButton(context.getString(R.string.common_dialog_confirm),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
				}).create().show();*/
				Toast.makeText(context, msg,
						Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();
		collectDeviceInfo(context);
		saveCrashInfo2File(ex);
		return true;
	}

	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}
	}

	private String getCrashMsg(Throwable ex) {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}

		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		printWriter.close();
		return sb.toString() + writer.toString();
	}

	private String saveCrashInfo2File(Throwable ex) {
		String crashMsg = getCrashMsg(ex);
		try {
			String time = formatter.format(new Date());
			String fileName = "crash_" + time + ".log";
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				String path = Environment.getExternalStorageDirectory()
						.getAbsolutePath()+ConfigReader.getInstance(context).getProperties(
								"crash_path");
				//			String path = context.getFilesDir().getPath() + "/log/";
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(path + fileName);
				fos.write(crashMsg.getBytes());
				fos.close();
			}
			return fileName;
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing file...", e);
		}
		return null;
	}
}
