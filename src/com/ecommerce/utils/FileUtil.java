package com.ecommerce.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class FileUtil {
	private static final String TAG = "FileUtil";
	private static final int BUFFER_SIZE = 16 * 1024;

	/**
	 * get extention of files
	 * 
	 * @param fileName
	 * @return
	 */
	private static String getExtention(String fileName) {
		int pos = fileName.lastIndexOf(".");
		if (pos == -1)
			return "";
		return fileName.substring(pos + 1);
	}

	/**
	 * 拷贝文件
	 * 
	 * @param src
	 * @param dst
	 */
	public static void copy(File src, File dst) {
		try {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = new BufferedInputStream(new FileInputStream(src),
						BUFFER_SIZE);
				out = new BufferedOutputStream(new FileOutputStream(dst),
						BUFFER_SIZE);
				byte[] buffer = new byte[BUFFER_SIZE];
				int num = 0;
				while ((num = in.read(buffer)) != -1) {
					out.write(buffer, 0, num);
				}
			} finally {
				if (null != in) {
					in.close();
				}
				if (null != out) {
					out.close();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void copy(InputStream inputStream, String dst) {
		try {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = new BufferedInputStream(inputStream, BUFFER_SIZE);
				out = new BufferedOutputStream(new FileOutputStream(dst),
						BUFFER_SIZE);
				byte[] buffer = new byte[BUFFER_SIZE];
				int num = 0;
				while ((num = in.read(buffer)) != -1) {
					out.write(buffer, 0, num);
				}
			} finally {
				if (null != in) {
					in.close();
				}
				if (null != out) {
					out.close();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void copy(Bitmap bitmap, String dst) {
		try {
//			InputStream in = null;
			OutputStream out = null;
			try {
//				in = new BufferedInputStream(inputStream, BUFFER_SIZE);
				out = new BufferedOutputStream(new FileOutputStream(dst),
						BUFFER_SIZE);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
//				byte[] buffer = new byte[BUFFER_SIZE];
				out.flush();
			} finally {
				if (null != out) {
					out.close();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void copy(byte[] buffer, String dst) {
		try {
			OutputStream out = null;
			try {
				out = new BufferedOutputStream(new FileOutputStream(dst),
						BUFFER_SIZE);
				out.write(buffer);
			} finally {
				if (null != out) {
					out.close();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

	public static void copy(String src, String dst){
		try {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = new BufferedInputStream(new FileInputStream(src),
						BUFFER_SIZE);
				out = new BufferedOutputStream(new FileOutputStream(dst),
						BUFFER_SIZE);
				byte[] buffer = new byte[BUFFER_SIZE];
				int num = 0;
				while ((num = in.read(buffer)) != -1) {
					out.write(buffer, 0, num);
				}
			} finally {
				if (null != in) {
					in.close();
				}
				if (null != out) {
					out.close();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean deleteFile(String filename) {
		File file = new File(filename);
		if (file.exists() && file.isFile()) {
			file.delete();
			return true;
		} else {
			return false;
		}
	}

	public static File[] getSubDirectory(File file) {
		File[] files = file.listFiles();
		/*for (File subFile : files) {
			System.out.println(subFile.getAbsolutePath());
		}*/
		return files;
	}

	public static List<String> getSubDirectoryVals(File file) {
		List<String> values = null;
		File[] files = file.listFiles();
		if (files != null && files.length > 0) {
			values = new ArrayList<String>();
			for (File subFile : files) {
				values.add(subFile.getAbsolutePath());
				System.out.println(subFile.getAbsolutePath());
			}
		}
		return values;
	}

	public static void getFiles(File file, List<String> extentions) {
		File[] files = file.listFiles();
		if (files != null) {
			for (File subFile : files) {
				if (subFile.isDirectory()) {
					getFiles(subFile, extentions);
				} else {
					if (extentions != null
							&& extentions.contains(getExtention(subFile
									.getName()))) {
						Log.d(TAG, subFile.getAbsolutePath());
					}
				}
			}
		}
	}

	public static void getFiles(File file, List<String> extentions,
			List<File> subFiles) {
		File[] files = file.listFiles();
		if (files != null) {
			for (File subFile : files) {
				if (subFile.isDirectory()) {
					getFiles(subFile, extentions,subFiles);
				} else {
					if (extentions != null
							&& extentions.contains(getExtention(subFile
									.getName().toLowerCase()))) {
						subFiles.add(subFile);
						Log.d(TAG, subFile.getAbsolutePath());
					}
				}
			}
		}
	}

	public static List<File> getSubFiles(File file, List<String> extentions) {
		List<File> files = new ArrayList<File>();
		getFiles(file, extentions, files);
		return files;
	}

	public static List<File> getSubFiles(List<File> fileList,
			List<String> extentions) {
		List<File> files = new ArrayList<File>();
		for (File file : fileList) {
			getFiles(file, extentions, files);
		}
		return files;
	}

	public static boolean isSdCardExist() {
		return Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	public static File getSDCardRootPath() {
		return Environment.getExternalStorageDirectory();
	}

	public static String getSDPath() {
		File sdCardDir = null;
		if (isSdCardExist()) {
			sdCardDir = Environment.getExternalStorageDirectory();
			return sdCardDir.getAbsolutePath();
		} else {
			return null;
		}
	}

	public static String readIsAsString(InputStream inputStream) {
		StringBuilder sb = new StringBuilder();
		try {
			InputStream in = null;
			try {
				in = new BufferedInputStream(inputStream, BUFFER_SIZE);
				byte[] buffer = new byte[BUFFER_SIZE];
				int num = 0;
				while ((num = in.read(buffer)) != -1) {
					sb.append(new String(buffer, "UTF-8"), 0, num);
				}
			} finally {
				if (null != in) {
					in.close();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return sb.toString();
	}
	
	public static String byteToMi(long contentLength) {
		BigDecimal result = new BigDecimal(Long.toString(contentLength));
		DecimalFormat df = new DecimalFormat("0.00");
		result = result.divide(new BigDecimal("1024.0")).divide(
				new BigDecimal("1024.0"));
		return df.format(result.doubleValue());
	}
	
	public static Bitmap getLocalBitmap(String url) {
	     try {
//	          FileInputStream fis = new FileInputStream(url);
	    	 BitmapFactory.Options opts = new BitmapFactory.Options();  
	    	 opts.inJustDecodeBounds = true;  
	    	 BitmapFactory.decodeFile(url, opts);
	    	 opts.inSampleSize = CommonUtils.computeSampleSize(opts, -1, 128*128);   
	    	 
	    	 opts.inJustDecodeBounds = false;   
	    	 Bitmap bitmap = BitmapFactory.decodeFile(url, opts);
	         return bitmap;
	     } catch (Exception e) {
	          e.printStackTrace();
	          return null;
	     }
	}
	
	

	public static void main(String[] args) {
		getFiles(new java.io.File("D:\\"),
				java.util.Arrays.asList(new String[] { "xls" }));
	}

}
