package com.ihangjing.net;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.util.Log;

import com.ihangjing.common.HjActivity;
import com.ihangjing.common.OtherManager;
import com.ihangjing.util.HJAppConfig;
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

public class HttpManager implements Runnable {
	/*
	private static final String ENCODING = "UTF-8";
	private static final String PROXY = "10.0.0.172";
	private static final String SEARCHHOST = "www.fanede.com";
	private static final String WEBHOST = "www.fanede.com";
	*/
	HttpURLConnection conn = null;
	private Context context;
	private Thread currentRequest = null;
	private String host = "";
	InputStream input = null;
	private String keyIp;
	private HttpRequestListener listener;
	private String page = "";
	private Map<String, String> params = null;
	private int requestFlag = 1;
	private int requestStatus = 1;
	private int Tag = 1;
	private int time = 60000;
	private byte[] image;
	private AsyncHttpClient client = new AsyncHttpClient();
	
	private AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
				@Override
			    public void onStart() {
			        // called before request is started
			    }

			    @Override
			    public void onRetry(int retryNo) {
			        // called when request is retried
				}

				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					// TODO Auto-generated method stub
					String value = "http数据转换数据错误";
					try {
						if(arg2 != null)
							value = new String(arg2, "UTF8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					listener.action(260, value, Tag);
				}

				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					// TODO Auto-generated method stub
					String value = "http数据转换数据错误";
					try {
						if(arg2 != null)
							value = new String(arg2, "UTF8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					listener.action(260, value, Tag);
				}
			}; 
	

	public HttpManager(Context paramContext,
			HttpRequestListener paramHttpRequestListener, String paramString,
			Map<String, String> paramMap, int paramInt, int tag) {

		this.context = paramContext;
		this.listener = paramHttpRequestListener;
		this.params = paramMap;
		this.page = paramString.replace("APP/", "");
		this.requestFlag = paramInt;
		Tag = tag;
		if (OtherManager.DEBUG) {
			Log.v("HttpManager", "HttpManager:" + paramString);
		}
	}
	
	public HttpManager(Context paramContext,
			HttpRequestListener paramHttpRequestListener, String paramString,
			Map<String, String> paramMap, byte[] img, int tag) {

		this.context = paramContext;
		this.listener = paramHttpRequestListener;
		this.params = paramMap;
		this.page = paramString.replace("APP/", "");
		image = img;
		Tag = tag;
		
		if (OtherManager.DEBUG) {
			Log.v("HttpManager", "HttpManager:" + paramString);
		}
	}

	public HttpManager(Context paramContext,
			HttpRequestListener paramHttpRequestListener, String paramString,
			Map<String, String> paramMap, int paramInt1, int paramInt2, int tag) {
	}

	public HttpManager(Context paramContext,
			HttpRequestListener paramHttpRequestListener, String paramString1,
			Map<String, String> paramMap, int paramInt, String paramString2, int tag) {
		this.context = paramContext;
		this.listener = paramHttpRequestListener;
		this.params = paramMap;
		this.page = paramString1;
		this.requestFlag = paramInt;
		this.keyIp = paramString2;
		Tag = tag;
	}

	private String readStream(InputStream paramInputStream) throws Exception {
		ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
		byte[] arrayOfByte1 = new byte[1024];
		while (true) {
			int i = paramInputStream.read(arrayOfByte1);
			if (i == -1) {
				localByteArrayOutputStream.close();
				paramInputStream.close();
				byte[] arrayOfByte2 = localByteArrayOutputStream.toByteArray();
				// return new String(arrayOfByte2, "GB2312");//中文解析
				return new String(arrayOfByte2, "UTF-8");
			}
			localByteArrayOutputStream.write(arrayOfByte1, 0, i);
		}
	}

	private String requestEncodeStr() throws UnsupportedEncodingException {
		StringBuilder localStringBuilder1 = new StringBuilder();
		Iterator localIterator = this.params.entrySet().iterator();

		while (true) {
			if (!localIterator.hasNext()) {
				int i = localStringBuilder1.length() + -1;
				localStringBuilder1.deleteCharAt(i);
				return localStringBuilder1.toString();
			}

			Map.Entry localEntry = (Map.Entry) localIterator.next();
			String str1 = (String) localEntry.getKey();
			Log.v("requestEncodeStr", str1);

			localStringBuilder1.append(str1).append('=');
			String str2 = URLEncoder.encode((String) localEntry.getValue(),
					"UTF-8");
			Log.v("requestEncodeStr", str2);
			localStringBuilder1.append(str2);
			localStringBuilder1.append('&');
		}
	}
	
	private RequestParams requestPamras(){
		return new RequestParams(this.params);
	}

	private void sendGetRequest(String paramString, boolean paramBoolean) {
		
		try {
			if (this.params != null) {
				paramString = String.valueOf(paramString) + requestEncodeStr();
			}
			client.setResponseTimeout(this.time);
			client.setUserAgent("ihangjing.com");
			client.get(paramString, responseHandler);
			
			return;
			/*int m = -1;
			URL localURL;
			HttpURLConnection localHttpURLConnection = null;
			for(int i = 0; i < 5; i++){
				localURL = new URL(paramString);
				

				if (OtherManager.DEBUG) {
					Log.v("HttpManager", "gurl = " + paramString);
				}
	
				localHttpURLConnection = (HttpURLConnection) localURL
						.openConnection();
				if (paramBoolean) {
					localHttpURLConnection.setRequestProperty("X-Online-Host",
							this.host);
				}
	
				localHttpURLConnection.setRequestMethod("GET");
	
				localHttpURLConnection.setConnectTimeout(this.time);
	
				localHttpURLConnection.setReadTimeout(this.time);
			
				localHttpURLConnection.setRequestProperty("User-Agent", "ihangjing.com");
				m = localHttpURLConnection.getResponseCode();
				if(m != -1){
					break;
				}
			}
			if (m == 200) {
				this.input = localHttpURLConnection.getInputStream();
				this.requestFlag = 0;
				if (this.input == null) {
					return;
				}

				// 调用处理返回信息函数
				this.listener.action(260, readStream(this.input), Tag);

				return;
			}else if ((m == 404) || (m == 500)) {
				this.input = localHttpURLConnection.getErrorStream();
				if (this.input != null) {
					this.listener.action(260, readStream(this.input), Tag);
					return;
				}
			}else if(m == -1){
				this.listener.action(258, null, Tag);
			}else{
				this.listener.action(258, null, Tag);
			}
			
			
		} catch (SocketTimeoutException localSocketTimeoutException) {
			localSocketTimeoutException.printStackTrace();
			this.listener.action(258, null, Tag);
			return;*/

		} catch (IOException localIOException) {
			localIOException.printStackTrace();
			this.listener.action(259, null, Tag);
			return;
		} catch (Exception localException) {
			localException.printStackTrace();
			this.listener.action(258, null, Tag);
		}
	}

	// paramString url地址（不含参数）
	private void sendPostRequest(String paramString, boolean paramBoolean) {
		try {
			String str1 = requestEncodeStr();// 组合参数
			if (OtherManager.DEBUG) {
				Log.v("sendPostRequest", "url = " + paramString + str1);
			}
			RequestParams params = requestPamras();//new RequestParams();
			//Set<String> set = new HashSet<String>(); 
			//set.add(str1);
			//params.put("", file);
			client.setResponseTimeout(this.time);
			client.setUserAgent("ihangjing.com");
			client.post(paramString, params, responseHandler);
			return;
			/*byte[] arrayOfByte = str1.getBytes();
			int m = -1;
			URL localURL;
			HttpURLConnection localHttpURLConnection1 = null;
			for(int i = 0; i < 5; i++){
				localURL = new URL(paramString);
	
				
	
				localHttpURLConnection1 = (HttpURLConnection) localURL
						.openConnection();
	
				this.conn = localHttpURLConnection1;
				this.conn.setConnectTimeout(this.time);
				this.conn.setReadTimeout(this.time);
				this.conn.setDoInput(true);
				this.conn.setDoOutput(true);
				this.conn.setUseCaches(false);
				this.conn.setRequestProperty("Charset", "UTF-8");
				this.conn.setRequestProperty("User-Agent", "ihangjing.com");
				if (paramBoolean) {
					this.conn.setRequestProperty("X-Online-Host",
							HJAppConfig.HOST);
				}
	
				this.conn.setRequestProperty("Content-Length",
						String.valueOf(arrayOfByte.length));
				this.conn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				this.conn.setRequestMethod("POST");
	
				OutputStream localOutputStream = this.conn.getOutputStream();
				DataOutputStream localDataOutputStream = new DataOutputStream(
						localOutputStream);
				localDataOutputStream.write(arrayOfByte);
				localDataOutputStream.flush();
				localDataOutputStream.close();
				m = this.conn.getResponseCode();
				if(m != -1){
					break;
				}
			}

			if (m == 200) {
				InputStream localInputStream1 = this.conn.getInputStream();
				this.input = localInputStream1;
				this.requestFlag = 0;
				if (this.input == null)
					return;
				HttpRequestListener localHttpRequestListener1 = this.listener;
				InputStream localInputStream2 = this.input;
				String str4 = readStream(localInputStream2);
				localHttpRequestListener1.action(260, str4, Tag);
				return;
			}else if ((m == 404) || (m == 500)) {
				InputStream localInputStream3 = this.conn.getErrorStream();
				this.input = localInputStream3;
				if (this.input != null) {
					HttpRequestListener localHttpRequestListener2 = this.listener;
					InputStream localInputStream4 = this.input;
					String str5 = readStream(localInputStream4);
					localHttpRequestListener2.action(260, str5, Tag);
					return;
				}
			}else if(m == -1){
				this.listener.action(258, null, Tag);
			}else{
				this.listener.action(258, null, Tag);
			}
		} catch (SocketTimeoutException localSocketTimeoutException) {
			localSocketTimeoutException.printStackTrace();
			this.listener.action(258, null, Tag);
			return;*/
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
			this.listener.action(259, null, Tag);
			return;
		} catch (Exception localException) {
			localException.printStackTrace();
			this.listener.action(258, null, Tag);
		}
	}

	public void cancelHttpRequest() {
		if (this.currentRequest == null) {
			return;
		}
		if (!this.currentRequest.isAlive()) {
			return;
		}

		try {
			this.input.close();
			this.input = null;
		} catch (Exception localException1) {
			try {
				this.conn.disconnect();
				this.conn = null;
				this.currentRequest.stop();
				this.currentRequest = null;
				System.gc();
			} catch (Exception localException2) {
				while (true)
					localException2.printStackTrace();
			}
		}
	}
	public void getApi(){
		this.requestFlag = 2;
		int i = OtherManager.isNetworkAvailable(this.context);

		if (i != 0) {
			if (!"".equals(this.keyIp) && this.keyIp != null) {
				this.params.put(this.keyIp,
						OtherManager.getLocalIpAddress(this.context, i));
			}

			boolean bool = false;
			this.host = HJAppConfig.HOST;
			StringBuilder url;
			if(this.page.indexOf("http://") > -1){
				url = new StringBuilder(this.page);
			}else{
				url = new StringBuilder("http://");
	
				if ((i == 1) || (i == 3) || i== 2) {// wifi 3G
					url.append(this.host).append("/Api/");
					url.append(this.page);
				}
			}

			
			sendGetRequest(url.toString(), bool);
				

			return;
		}
		this.listener.action(257, null, Tag);
	}
	public void getRequeest() {
		this.requestFlag = 2;
		int i = OtherManager.isNetworkAvailable(this.context);

		if (i != 0) {
			if (!"".equals(this.keyIp) && this.keyIp != null) {
				this.params.put(this.keyIp,
						OtherManager.getLocalIpAddress(this.context, i));
			}

			boolean bool = false;
			this.host = HJAppConfig.HOST;
			StringBuilder url;
			if(this.page.indexOf("http://") > -1){
				url = new StringBuilder(this.page);
			}else{
				url = new StringBuilder("http://");
	
				if ((i == 1) || (i == 3) || i== 2) {// wifi 3G
					url.append(this.host).append("/App/");
					url.append(this.page);
				}
			}

			
			sendGetRequest(url.toString(), bool);
				

			return;
		}
		this.listener.action(257, null, Tag);
	
		/*Thread localThread = new Thread(this);
		this.currentRequest = localThread;
		this.currentRequest.start();*/
	}

	public boolean isRunning() {
		if ((this.currentRequest != null) && (this.currentRequest.isAlive())) {
			return true;
		}
		return false;
	}

	public void postRequest() {
		this.requestFlag = 1;
		int i = OtherManager.isNetworkAvailable(this.context);

		if (i != 0) {
			if (!"".equals(this.keyIp) && this.keyIp != null) {
				this.params.put(this.keyIp,
						OtherManager.getLocalIpAddress(this.context, i));
			}

			boolean bool = false;
			this.host = HJAppConfig.HOST;
			StringBuilder url;
			if(this.page.indexOf("http://") > -1){
				url = new StringBuilder(this.page);
			}else{
				url = new StringBuilder("http://");
	
				if ((i == 1) || (i == 3) || i== 2) {// wifi 3G
					url.append(this.host).append("/App/");
					url.append(this.page);
				}
			}

			

			
			sendPostRequest(url.toString(), bool);
				

			return;
		}
		this.listener.action(257, null, Tag);
		/*this.currentRequest = new Thread(this);
		this.currentRequest.start();*/
	}
	
	public void sendImage() {
		this.requestFlag = 3;
		this.currentRequest = new Thread(this);
		this.currentRequest.start();
	}

	/**
	 * @param 只发送普通数据
	 *            ,调用此方法
	 * @param urlString
	 *            对应的Php 页面
	 * @param params
	 *            需要发送的相关数据 包括调用的方法
	 * @paramimage 图片字节数组或者文件字节数组
	 * @paramimg 图片名称
	 * @return Json
	 */
	public String communication01(String urlString) {
		String result = "";

		String end = "\r\n";
		if (!urlString.equals("")) {
			try {
				URL url = new URL(urlString);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setDoInput(true);// 允许输入
				conn.setDoOutput(true);// 允许输出
				conn.setUseCaches(false);// 不使用Cache
				conn.setConnectTimeout(6000);// 6秒钟连接超时
				conn.setReadTimeout(60000);// 6秒钟读数据超时
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("Charset", "UTF-8");
				//StringBuilder localStringBuilder1 = new StringBuilder();
				Iterator localIterator = this.params.entrySet().iterator();

				while (true) {
					if (!localIterator.hasNext()) {
						break;
					}

					Map.Entry localEntry = (Map.Entry) localIterator.next();
					conn.setRequestProperty((String) localEntry.getKey(), URLEncoder.encode((String) localEntry.getValue(),
							"UTF-8"));
				}
				/*
				 * conn.setRequestProperty("id", "1");
				 * conn.setRequestProperty("type", "1");
				 * conn.setRequestProperty("ext", "jpg");
				 */
				// / type=1 表示上传商家图片 id 表示商家编号
				// / type=2 表示上传菜品图片 id 表示菜品编号
				// / ext=jpg 表示后缀名

				DataOutputStream dos = new DataOutputStream(
						conn.getOutputStream());
				dos.write(image, 0, image.length);
				dos.writeBytes(end);
				dos.flush();
				InputStream is = conn.getInputStream();
				InputStreamReader isr = new InputStreamReader(is, "utf-8");
				BufferedReader br = new BufferedReader(isr);
				result = br.readLine();
				listener.action(260, result, Tag);
			} catch (Exception e) {
				result = "{\"ret\":\"898\"}";
				listener.action(258, result, Tag);
			}
		}
		return result;

	}

	/**
	 * @param 只发送普通数据
	 *            ,调用此方法
	 * @param urlString
	 *            对应的Php 页面
	 * @param params
	 *            需要发送的相关数据 包括调用的方法
	 * @param imageuri
	 *            图片或文件手机上的地址 如:sdcard/photo/123.jpg
	 * @param img
	 *            图片名称
	 * @return Json
	 */
	public String communication02(String urlString, Map<String, Object> params,
			String imageuri, String img) {
		String result = "";

		String end = "\r\n";
		String uploadUrl = HJAppConfig.URL + "/";// new BingoApp().URLIN
													// 是我定义的上传URL
		String MULTIPART_FORM_DATA = "multipart/form-data";
		String BOUNDARY = "---------7d4a6d158c9"; // 数据分隔线
		String imguri = "";
		if (!imageuri.equals("")) {
			imguri = imageuri.substring(imageuri.lastIndexOf("/") + 1);// 获得图片或文件名称
		}

		if (!urlString.equals("")) {
			uploadUrl = uploadUrl + urlString;

			try {
				URL url = new URL(uploadUrl);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setDoInput(true);// 允许输入
				conn.setDoOutput(true);// 允许输出
				conn.setUseCaches(false);// 不使用Cache
				conn.setConnectTimeout(6000);// 6秒钟连接超时
				conn.setReadTimeout(60000);// 6秒钟读数据超时
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("Charset", "UTF-8");
				conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA
						+ "; boundary=" + BOUNDARY);

				StringBuilder sb = new StringBuilder();

				// 上传的表单参数部分，格式请参考文章
				for (Map.Entry<String, Object> entry : params.entrySet()) {// 构建表单字段内容
					sb.append("--");
					sb.append(BOUNDARY);
					sb.append("\r\n");
					sb.append("Content-Disposition: form-data; name=\""
							+ entry.getKey() + "\"\r\n\r\n");
					sb.append(entry.getValue());
					sb.append("\r\n");
				}

				sb.append("--");
				sb.append(BOUNDARY);
				sb.append("\r\n");

				DataOutputStream dos = new DataOutputStream(
						conn.getOutputStream());
				dos.write(sb.toString().getBytes());

				if (!imageuri.equals("") && !imageuri.equals(null)) {
					dos.writeBytes("Content-Disposition: form-data; name=\""
							+ img + "\"; filename=\"" + imguri + "\"" + "\r\n"
							+ "Content-Type: image/jpeg\r\n\r\n");
					FileInputStream fis = new FileInputStream(imageuri);
					byte[] buffer = new byte[1024]; // 8k
					int count = 0;
					while ((count = fis.read(buffer)) != -1) {
						dos.write(buffer, 0, count);
					}
					dos.writeBytes(end);
					fis.close();
				}
				dos.writeBytes("--" + BOUNDARY + "--\r\n");
				dos.flush();

				InputStream is = conn.getInputStream();
				InputStreamReader isr = new InputStreamReader(is, "utf-8");
				BufferedReader br = new BufferedReader(isr);
				result = br.readLine();

			} catch (Exception e) {
				result = "{\"ret\":\"898\"}";
			}
		}
		return result;

	}

	@Override
	public void run() {

		int i = OtherManager.isNetworkAvailable(this.context);

		if (i != 0) {
			if (!"".equals(this.keyIp) && this.keyIp != null) {
				this.params.put(this.keyIp,
						OtherManager.getLocalIpAddress(this.context, i));
			}

			boolean bool = false;
			this.host = HJAppConfig.HOST;
			StringBuilder url = new StringBuilder("http://");

			if ((i == 1) || (i == 3) || i== 2) {// wifi 3G
				url.append(this.host).append("/App/");
				url.append(this.page);
			}

			if (i == 2)// cmwap 上网部分
			{
				// http://10.0.0.172/
			}

			switch (requestFlag) {
			case 1:
				sendPostRequest(url.toString(), bool);
				
				break;
			case 2:
				sendGetRequest(url.toString(), bool);
				break;
			case 3:
				communication01(url.toString());
				break;
			}

			return;
		}
		this.listener.action(257, null, Tag);
	}
}