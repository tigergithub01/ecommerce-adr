package com.ecommerce.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class HttpUtils {
	public static final String TAG = HttpUtils.class.getSimpleName();
	//	private static final String JSESSIONID = "JSESSIONID";
	private static final String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";

	public static DefaultHttpClient getHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
					HttpVersion.HTTP_1_1);

			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
			HttpProtocolParams.setUseExpectContinue(params, true);

			HttpConnectionParams.setConnectionTimeout(params, 30 * 1000);
			HttpConnectionParams.setSoTimeout(params, 30 * 1000);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	public static String getParam(Map<String, Object> map) {
		StringBuilder builder = new StringBuilder();
		if (map != null) {
			for (java.util.Iterator<String> iter = map.keySet().iterator(); iter
					.hasNext();) {
				String key = iter.next();
				Object value = map.get(key);
				if (value != null) {
					builder.append(key + "=" + String.valueOf(value) + "&");
				}
			}
		}
		return builder.toString();
	}

	/**
	 * from map to StringEntity
	 * @param map
	 * @return
	 */
	public static StringEntity getStringEntity(Map<String, Object> map) {
		StringEntity se = null;
		try {
			se = new StringEntity(getParam(map), "UTF-8");
			se.setContentType(DEFAULT_CONTENT_TYPE);
		} catch (Throwable ex) {
			throw new RuntimeException(ex);
		}
		return se;
	}

	/**
	 * from map to UrlEncodedFormEntity
	 * @param map
	 * @return
	 */
	public static UrlEncodedFormEntity getUrlEncodedFormEntity(
			Map<String, Object> map) {
		if(map==null){
			map = new HashMap<String,Object>();
		}
		if(Constants.DEBUG_MODE==1){
			map.put("XDEBUG_SESSION_START", Constants.XDEBUG_SESSION_START);
			map.put("KEY", Constants.XDEBUG_KEY);
		}
		
		if(Constants.phoneInfo!=null){
			map.put("phone_model", Constants.phoneInfo.getPhoneModel());
			map.put("os_type", "MODEL:"+Constants.phoneInfo.getPhoneModel()+" SDK:"+Constants.phoneInfo.getOsRelease()+" Brand:"+Constants.phoneInfo.getPhoneBrand());
			map.put("app_ver", Constants.phoneInfo.getAppVerName());
			map.put("app_type_id", Constants.ANDROID_IDENTITY);
		}
		UrlEncodedFormEntity urlEncodedFormEntity = null;
		List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		try {
			if (map != null) {
				for (java.util.Iterator<String> iter = map.keySet().iterator(); iter
						.hasNext();) {
					String key = iter.next();
					Object value = map.get(key);
					//TODO:may have errors,sometimes value can be null
					if (key != null) {
						nvps.add(new BasicNameValuePair(key, (value==null)?null:String
								.valueOf(value)));
					}
					/*if (value != null) {
						nvps.add(new BasicNameValuePair(key, String
								.valueOf(value)));
					}*/
				}
			}
			urlEncodedFormEntity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
			urlEncodedFormEntity.setContentType(DEFAULT_CONTENT_TYPE);
		} catch (Throwable ex) {
			throw new RuntimeException(ex);
		}
		return urlEncodedFormEntity;
	}

	public static HttpResponse getHttpResponse(String url,
			Map<String, Object> map, String sessionId) {
		return getHttpResponse(url, map, sessionId, false);
	}

	/**
	 * getHttpResponse with sessionId
	 * @param url
	 * @param map
	 * @param sessionId
	 * @return
	 */
	public static HttpResponse getHttpResponse(String url,
			Map<String, Object> map, String sessionId, boolean multipart) {
		HttpResponse httpResponse = null;
		try {
			HttpPost request = new HttpPost(url);
			//			HttpPost request = new HttpPost(url);
			if (sessionId != null) {
				request.setHeader("Cookie", Constants.JSESSIONID + "="
						+ sessionId);
			}
			/*BasicHttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, 30 * 1000);
			HttpConnectionParams.setSoTimeout(params, 30 * 1000);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
			HttpProtocolParams.setUseExpectContinue(params, true);
			params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
					HttpVersion.HTTP_1_1);*/
			//			DefaultHttpClient httpclient = new DefaultHttpClient(params);

			DefaultHttpClient httpclient = getHttpClient();

			if (multipart) {
				/*MultipartEntity entity = getMultipartEntity(map);
				if (entity != null) {
					request.setEntity(entity);
				}*/
			} else {
				UrlEncodedFormEntity entity = getUrlEncodedFormEntity(map);
				if (entity != null) {
					request.setEntity(entity);
				}
			}
			httpResponse = httpclient.execute(request);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
//			Log.d(TAG, "statusCode:" + statusCode);
			if (statusCode != HttpStatus.SC_OK) {
				request.abort();
			}
			//httpclient.getConnectionManager().shutdown();
		} catch (Throwable ex) {
			throw new RuntimeException(ex);
		}
		return httpResponse;
	}

	public static Object[] getHttpResponseAndHttpClient(String url,
			Map<String, Object> map, String sessionId, boolean multipart) {
		HttpResponse httpResponse = null;
		DefaultHttpClient httpclient = getHttpClient();
		try {
			HttpPost request = new HttpPost(url);
			//			HttpPost request = new HttpPost(url);
			if (sessionId != null) {
				request.setHeader("Cookie", Constants.JSESSIONID + "="
						+ sessionId);
			}
			/*BasicHttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, 30 * 1000);
			HttpConnectionParams.setSoTimeout(params, 30 * 1000);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
			HttpProtocolParams.setUseExpectContinue(params, true);
			params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
					HttpVersion.HTTP_1_1);*/
			//			DefaultHttpClient httpclient = new DefaultHttpClient(params);

			if (multipart) {
				/*MultipartEntity entity = getMultipartEntity(map);
				if (entity != null) {
					request.setEntity(entity);
				}*/
			} else {
				UrlEncodedFormEntity entity = getUrlEncodedFormEntity(map);
				if (entity != null) {
					request.setEntity(entity);
				}
			}
			httpResponse = httpclient.execute(request);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
//			Log.d(TAG, "statusCode:" + statusCode);
			if (statusCode != HttpStatus.SC_OK) {
				request.abort();
			}

			//httpclient.getConnectionManager().shutdown();
		} catch (Throwable ex) {
			throw new RuntimeException(ex);
		}
		return new Object[] { httpResponse, httpclient };
	}

	/*public static HttpResponse getHttpGetResponse(String url,
			Map<String, Object> map, String sessionId, boolean multipart,
			Long startPos, Long endPos) {
		HttpResponse httpResponse = null;
		try {
			if (multipart) {
				MultipartEntity entity = getMultipartEntity(map);
				if (entity != null) {
					request.setEntity(entity);
				}
			} else {
	//				StringEntity entity = getStringEntity(map);
				url = url+"?doc.id=9";
			}
			HttpGet request = new HttpGet(url);
	//			HttpPost request = new HttpPost(url);
			if (sessionId != null) {
				request.setHeader("Cookie", JSESSIONID + "=" + sessionId);
			}
			BasicHttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, 30 * 1000);
			HttpConnectionParams.setSoTimeout(params, 30 * 1000);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
			HttpProtocolParams.setUseExpectContinue(params, true);
			if (startPos != null && endPos!=null) {
				Header headerSize = new BasicHeader("Range", "bytes="
						+ startPos + "-" + endPos);
				request.addHeader(headerSize);
			}
			params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
					HttpVersion.HTTP_1_1);
			DefaultHttpClient httpclient = new DefaultHttpClient(params);
			
			httpResponse = httpclient.execute(request);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			Log.d(TAG, "statusCode:" + statusCode);
			if (statusCode != HttpStatus.SC_OK) {
				request.abort();
			}
			//			httpclient.getConnectionManager().shutdown();
		} catch (Throwable ex) {
			throw new RuntimeException(ex);
		}
		return httpResponse;
	}*/

	/*public static MultipartEntity getMultipartEntity(Map<String, Object> map) {
		MultipartEntity multipartEntity = null;
		try {
			if (map != null) {
				multipartEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE,null,Charset.forName(HTTP.UTF_8));
				for (java.util.Iterator<String> iter = map.keySet().iterator(); iter
						.hasNext();) {
					String key = iter.next();
					Object value = map.get(key);
					if (value != null) {
						if (value instanceof java.io.File) {
							multipartEntity.addPart(key, new FileBody(
									(java.io.File) value));
						} else {
							multipartEntity.addPart(key,
									new StringBody(String.valueOf(value),Charset.forName(HTTP.UTF_8)));
						}
					}
				}
			}
		} catch (Throwable ex) {
			throw new RuntimeException(ex);
		}
		return multipartEntity;
	}*/

	public static HttpResponse getHttpResponse(String url) {
		return getHttpResponse(url, null, null);
	}

	/**
	 * getHttpResponse with null session
	 * @param url
	 * @param map
	 * @param sessionId
	 * @return
	 */
	public static HttpResponse getHttpResponse(String url,
			Map<String, Object> map) {
		return getHttpResponse(url, map, null);
	}

	/**
	 * get all headers
	 * @param httpResponse
	 */
	public static void getAllHeaders(HttpResponse httpResponse) {
		Header[] headers = httpResponse.getAllHeaders();
		if (headers != null) {
			for (Header header : headers) {
				Log.d(TAG,
						"header:" + header.getName() + ":" + header.getValue());
				HeaderElement[] elements = header.getElements();
				if (elements != null) {
					for (HeaderElement element : elements) {
						Log.d(TAG, "element:" + element.getName() + ":"
								+ element.getValue());
					}
				}

			}
		}
	}

	public static Cookie getSessionCookie(DefaultHttpClient httpclient) {
		Cookie sessionCookie = null;
		List<Cookie> cookies = httpclient.getCookieStore().getCookies();
		if (!cookies.isEmpty()) {
			for (int i = 0; i < cookies.size(); i++) {
				Cookie cookie = cookies.get(i);
				/*Log.d(TAG, "cookieName:" + cookie.getName()
						+ ";cookieValue:" + cookie.getValue());*/
				if (cookie.getName().equalsIgnoreCase(Constants.JSESSIONID)) {
					sessionCookie = cookie;
					break;
				}
			}
		}
		return sessionCookie;
	}
	
	public static List<Cookie> getCookieList(DefaultHttpClient httpclient) {
		List<Cookie> cookies = httpclient.getCookieStore().getCookies();
		return cookies;
	}

	/**
	 * get sessionId from HttpResponse
	 * @param httpResponse
	 * @return
	 */
	public static String getSessionId(HttpResponse httpResponse) {
		String sessionId = null;
		Header header = httpResponse.getFirstHeader("Set-Cookie");
		HeaderElement[] elements = header.getElements();
		if (elements != null && elements.length > 0) {
			for (HeaderElement element : elements) {
				if (element.getName().equalsIgnoreCase(Constants.JSESSIONID)) {
					sessionId = element.getValue();
					break;
				}
			}
		}
		//		Log.d(TAG, "sessionId:" + sessionId);
		return sessionId;
	}

	/**
	 * get text from HttpResponse
	 * @param httpResponse
	 * @return
	 */
	public static String getResult(HttpResponse httpResponse) {
		String result = null;
		try {
			result = EntityUtils.toString(httpResponse.getEntity());
			//			Log.d(TAG, result);
		} catch (Throwable ex) {
			throw new RuntimeException(ex);
		}
		return result;
	}

	public static byte[] getResultBytes(HttpResponse httpResponse) {
		byte[] result = null;
		try {
			result = EntityUtils.toByteArray(httpResponse.getEntity());
			//			Log.d(TAG, result);
		} catch (Throwable ex) {
			throw new RuntimeException(ex);
		}
		return result;
	}

	public static byte[] getResultBytes(String url, Map<String, Object> map) {
		return getResultBytes(getHttpResponse(url, map));
	}

	public static InputStream getResultInputStream(HttpResponse httpResponse) {
		InputStream result = null;
		try {
			result = httpResponse.getEntity().getContent();
		} catch (Throwable ex) {
			throw new RuntimeException(ex);
		}
		return result;
	}

	public static InputStream getResultInputStream(String url,
			Map<String, Object> map) {
		return getResultInputStream(getHttpResponse(url, map));
	}
	
	
	public static Bitmap getBitmap(String url,
			Map<String, Object> map) throws IOException{
		return getBitmap(url, map, null);
	}
	
	public static Bitmap getBitmap(String url,
			Map<String, Object> map,String sessionId) throws IOException{
		InputStream is = getResultInputStream(getHttpResponse(url, map, sessionId));
		Bitmap bitmap = BitmapFactory.decodeStream(is); 
		is.close();
		return bitmap;
	}

	/**
	 * get text from HttpResponse
	 * @param httpResponse
	 * @return
	 */
	public static String getResult(String url, Map<String, Object> map) {
		return getResult(url, map, null);
	}

	public static String getResult(String url, Map<String, Object> map,
			String sessionId) {
		String result = null;
		try {
			HttpResponse httpResponse = getHttpResponse(url, map, sessionId);
			result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
//			result = EntityUtils.toString(httpResponse.getEntity());
			//			Log.d(TAG, result);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		return result;
	}

	public static String getResult(String url, String sessionId) {
		String result = null;
		try {
			HttpResponse httpResponse = getHttpResponse(url, null, sessionId);
			result = EntityUtils.toString(httpResponse.getEntity());
			//			Log.d(TAG, result);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		return result;
	}
	

}
