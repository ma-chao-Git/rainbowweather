package com.rainbowweather.app.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

public class HttpUtil {
	
	public static boolean sendHttpRequestByHttpClient(final String address, int viewId) {
		boolean isSuccess = false;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);
			HttpGet httpGet = new HttpGet(
					"http://op.juhe.cn/onebox/weather/query?cityname=+"
							+ address + "+&key=a8743779df03518197c4919f0591a750");
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 请求响应成功
				HttpEntity entity = httpResponse.getEntity();
				String response = EntityUtils.toString(entity, "gb2312");
				Utility.parseJSONToMapWithGSON(response, viewId);
			}
			isSuccess = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return isSuccess;
	}
	
	public static void sendHttpRequestByHttpURLConnection (final String cityName, final HttpCallbackListener listener) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					HttpClient httpClient = new DefaultHttpClient();
					httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
					httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);
					HttpClientParams.setCookiePolicy(httpClient.getParams(), CookiePolicy.BROWSER_COMPATIBILITY); 
					HttpGet httpGet = new HttpGet("http://apistore.baidu.com/microservice/weather?cityname=" + cityName);
					HttpResponse httpResponse = httpClient.execute(httpGet);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						// 请求响应成功
						HttpEntity entity = httpResponse.getEntity();
						String response = EntityUtils.toString(entity, "utf-8");
						if (listener != null) {
							// 回调onFinish()方法
							listener.onFinish(response.toString());
						}
					}
				} catch (Exception e) {
					if (listener != null) {
						// 回调onError()方法
						listener.onError(e);
					}
				}
			}
		}).start();
	}
}
