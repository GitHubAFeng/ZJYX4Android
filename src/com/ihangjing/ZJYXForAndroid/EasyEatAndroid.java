package com.ihangjing.ZJYXForAndroid;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.http.message.BasicNameValuePair;

import com.ihangjing.common.Configuration;
import com.ihangjing.http.HttpClient;
import com.ihangjing.http.HttpException;
import com.ihangjing.http.Response;

public class EasyEatAndroid extends EastEatSupport implements
		java.io.Serializable {

	public static final String TAG = "EasyEatAndroid_API";

	public static final String CONSUMER_KEY = Configuration.getSource();
	public static final String CONSUMER_SECRET = "";

	//private String baseURL = Configuration.getScheme()
	//		+ "www.dianyifen.com/AndroidApi/";
	private static final long serialVersionUID = -1486360080128882436L;

	
	public EasyEatAndroid() {
		super(); // In case that the user is not logged in
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	private SimpleDateFormat format = new SimpleDateFormat(
			"EEE, d MMM yyyy HH:mm:ss z", Locale.US);

	//Get 用法:Response res = get(searchBaseURL + "trends.json", false)
	//得到Response JSONObject json = res.asJSONObject();
	//json.getString("as_of")
	//JSONArray array = json.getJSONArray("trends");
	/*
	 *  json 转换为 midel列表
	 * 	model[] models = new model[array.length()];
		for (int i = 0; i < array.length(); i++) {
			JSONObject model = array.getJSONObject(i);
			models[i] = new model(trend);
		}
	 */
	/**
	 * Issues an HTTP GET request.
	 * 
	 * @param url
	 *            the request url
	 * @param authenticate
	 *            if true, the request will be sent with BASIC authentication
	 *            header
	 * @return the response
	 * @throws HttpException
	 */

	protected Response get(String url, boolean authenticate)
			throws HttpException {
		return get(url, null, authenticate);
	}

	/**
	 * Issues an HTTP GET request.
	 * 
	 * @param url
	 *            the request url
	 * @param authenticate
	 *            if true, the request will be sent with BASIC authentication
	 *            header
	 * @param name1
	 *            the name of the first parameter
	 * @param value1
	 *            the value of the first parameter
	 * @return the response
	 * @throws HttpException
	 */

	protected Response get(String url, String name1, String value1,
			boolean authenticate) throws HttpException {
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair(name1, HttpClient.encode(value1)));
		return get(url, params, authenticate);
	}

	/**
	 * Issues an HTTP GET request.
	 * 
	 * @param url
	 *            the request url
	 * @param name1
	 *            the name of the first parameter
	 * @param value1
	 *            the value of the first parameter
	 * @param name2
	 *            the name of the second parameter
	 * @param value2
	 *            the value of the second parameter
	 * @param authenticate
	 *            if true, the request will be sent with BASIC authentication
	 *            header
	 * @return the response
	 * @throws HttpException
	 */

	protected Response get(String url, String name1, String value1,
			String name2, String value2, boolean authenticate)
			throws HttpException {
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair(name1, HttpClient.encode(value1)));
		params.add(new BasicNameValuePair(name2, HttpClient.encode(value2)));
		return get(url, params, authenticate);
	}

	/**
	 * Issues an HTTP GET request.
	 *  带参数列表
	 *  使用概述:
	 *  ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>()
	 *  params.add(new BasicNameValuePair("max_id", "9999");
	 *  
	 * @param url 
	 *            the request url
	 * @param params
	 *            the request parameters
	 * @param authenticate
	 *            if true, the request will be sent with BASIC authentication
	 *            header
	 * @return the response
	 * @throws HttpException
	 */
	protected Response get(String url, ArrayList<BasicNameValuePair> params,
			boolean authenticated) throws HttpException {
		
		if (url.indexOf("?") == -1) {
			url += "?source=" + CONSUMER_KEY;
		} else if (url.indexOf("source") == -1) {
			url += "&source=" + CONSUMER_KEY;
		}
		
		if (null != params && params.size() > 0) {
			url += "&" + HttpClient.encodeParameters(params);
		}

		return http.get(url, authenticated);
	}

	/** 带参数列表和分页信息
	 * Issues an HTTP GET request.
	 * 
	 * @param url
	 *            the request url
	 * @param params
	 *            the request parameters
	 * @param paging
	 *            controls pagination
	 * @param authenticate
	 *            if true, the request will be sent with BASIC authentication
	 *            header
	 * @return the response
	 * @throws HttpException
	 */
	protected Response get(String url, ArrayList<BasicNameValuePair> params,
			Paging paging, boolean authenticate) throws HttpException {
		if (null == params) {
			params = new ArrayList<BasicNameValuePair>();
		}

		if (null != paging) {
			if ("" != paging.getMaxId()) {
				params.add(new BasicNameValuePair("max_id", String
						.valueOf(paging.getMaxId())));
			}
			if ("" != paging.getSinceId()) {
				params.add(new BasicNameValuePair("since_id", String
						.valueOf(paging.getSinceId())));
			}
			if (-1 != paging.getPage()) {
				params.add(new BasicNameValuePair("page", String.valueOf(paging
						.getPage())));
			}
			if (-1 != paging.getCount()) {
				params.add(new BasicNameValuePair("count", String
						.valueOf(paging.getCount())));
			}

			return get(url, params, authenticate);
		} else {
			return get(url, params, authenticate);
		}
	}

	/**
	 * 生成POST Parameters助手
	 * 
	 * @param nameValuePair
	 *            参数(一个或多个)
	 * @return post parameters
	 */
	public ArrayList<BasicNameValuePair> createParams(
			BasicNameValuePair... nameValuePair) {
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		for (BasicNameValuePair param : nameValuePair) {
			params.add(param);
		}
		return params;
	}
}
