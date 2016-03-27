package com.ecommerce.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import android.content.Context;

public class ConfigReader {
	private Context context;
	private Properties pro;
	private static ConfigReader configReader = null;

	public static ConfigReader getInstance(Context context) {
		if (configReader == null) {
			configReader = new ConfigReader(context);
		}
		return configReader;
	}

	public ConfigReader(Context context) {
		this.context = context;
		loadProperties();
	}

	public void loadProperties() {
		try {
			pro = new Properties();
			InputStream is = context.getAssets().open("config.properties");
			pro.load(is);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String getProperties(String name) {
		return pro.getProperty(name);
	}

	public String getProperties(String name, String[] parameters,
			Object[] values) {
		String propertyVal = pro.getProperty(name);
		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				String key = parameters[i];
				String value = (String) values[i];
				if (value != null) {
					propertyVal = propertyVal.replace(key, value);
				}
			}
		}
		return propertyVal;
	}

	public String getProperties(String name, Map<String, Object> parameters) {
		String propertyVal = pro.getProperty(name);
		if (parameters != null) {
			for (java.util.Iterator<String> iter = parameters.keySet()
					.iterator(); iter.hasNext();) {
				String key = iter.next();
				String value = (String) parameters.get(key);
				if (value != null) {
					propertyVal = propertyVal.replace(key, value);
				}
			}
		}
		return propertyVal;
	}

	/*public String getRemoteUrl(String name) {
		return "http://" + pro.getProperty("remote_server") + ":"
				+ pro.getProperty("remote_port") + pro.getProperty(name);
	}*/

	public String getRemoteUrl(String url) {
		if(getProperties("ver_flag").equals("1")){
			return Constants.HOST_URL_FORMAL+url;
		}else{
			return Constants.HOST_URL+url;
		}
		/*return getContextPath()+pro.getProperty(url);*/
	}
	
	public String getContextPath(){
		int verFlag = Integer.valueOf(pro.getProperty("ver_flag"));

		if (verFlag == 1) {
			return pro.getProperty("context_path");
		} else {
			return pro.getProperty("context_path_test");
		}
	}
	
	/**
	 * 获取下载地址
	 * @return
	 */
	public String getProductDocUrl(){
		int verFlag = Integer.valueOf(pro.getProperty("ver_flag"));
		if (verFlag == 1) {
			return pro.getProperty("product_doc_url");
		} else {
			return pro.getProperty("product_doc_url_test");
		}
	}
	
	/**
	 * 获取下载版本号
	 * @return
	 */
	public String getProductVerUrl(){
		int verFlag = Integer.valueOf(pro.getProperty("ver_flag"));
		if (verFlag == 1) {
			return pro.getProperty("product_ver_url");
		} else {
			return pro.getProperty("product_ver_url_test");
		}
	}

	public String getRemoteUrl(String name, Map<String, Object> parameters) {
		String remoteUrl = getRemoteUrl(name);
		if (parameters != null) {
			for (java.util.Iterator<String> iter = parameters.keySet()
					.iterator(); iter.hasNext();) {
				String key = iter.next();
				String value = null;
				Object originalVal = parameters.get(key);
				if (originalVal instanceof java.lang.Integer) {
					value = String.valueOf(((Integer) originalVal));
				} else if (originalVal instanceof java.lang.Long) {
					value = String.valueOf(((Long) originalVal));
				} else {
					value = (String) originalVal;
				}
				if (value != null) {
					remoteUrl = remoteUrl.replace(key, value);
				}
			}
		}
		return remoteUrl;
	}

	public String getRemoteUrl(String name, String[] parameters, Object[] values) {
		String remoteUrl = getRemoteUrl(name);
		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				String key = parameters[i];
				String value = null;
				Object originalVal = values[i];
				if (originalVal instanceof java.lang.Integer) {
					value = String.valueOf(((Integer) originalVal));
				} else if (originalVal instanceof java.lang.Long) {
					value = String.valueOf(((Long) originalVal));
				} else {
					value = (String) originalVal;
				}

				if (value != null) {
					remoteUrl = remoteUrl.replace(key, value);
				}
			}
		}
		return remoteUrl;
	}
	
	

}
