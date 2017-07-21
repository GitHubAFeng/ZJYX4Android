package com.ihangjing.ZJYXForAndroid;

import com.ihangjing.common.Configuration;
import com.ihangjing.http.HttpClient;

class EastEatSupport {
	protected HttpClient http = null;
	protected String source = Configuration.getSource();
	protected final boolean USE_SSL;

	/* package */
	EastEatSupport() {
		USE_SSL = Configuration.useSSL();
		http = new HttpClient(); // In case that the user is not logged in
	}

	/* package */
	EastEatSupport(String userId, String password) {
		USE_SSL = Configuration.useSSL();
		http = new HttpClient(userId, password);
	}

	/**
	 * Returns authenticating userid
	 * 
	 * @return userid
	 */
	public String getUserId() {
		return http.getUserId();
	}

	/**
	 * Returns authenticating password
	 * 
	 * @return password
	 */
	public String getPassword() {
		return http.getPassword();
	}

	// Low-level interface
	public HttpClient getHttpClient() {
		return http;
	}
}
