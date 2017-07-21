package com.ihangjing.alipay;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

public class Result extends Activity {
	private static final Map<String, String> sError;

	public static String sResult;

	static {
		sError = new HashMap<String, String>();
		sError.put("9000", "�����ɹ�");
		sError.put("4000", "ϵͳ�쳣");
		sError.put("4001", "���ݸ�ʽ����ȷ");
		sError.put("4003", "���󶨵�֧�����˻������������֧��");
		sError.put("4004", "���ѽ����");
		sError.put("4005", "��ʧ�ܻ�û�а�");
		sError.put("4006", "����֧��ʧ��");
		sError.put("4010", "���°��˻�");
		sError.put("6000", "֧���������ڽ�����������");
		sError.put("6001", "����;ȡ��֧������");
		sError.put("7001", "��ҳ֧��ʧ��");
	}

	public static String getResult() {
		String src = sResult.replace("{", "");
		src = src.replace("}", "");
		return getContent(src, "memo=", ";result");
	}

	public static void parseResult() {
		String resultStatus = null;
		String memo = null;
		String result = null;
		boolean isSignOk = false;
		try {
			String src = sResult.replace("{", "");
			src = src.replace("}", "");
			String rs = getContent(src, "resultStatus=", ";memo");
			if (sError.containsKey(rs)) {
				resultStatus = sError.get(rs);
			} else {
				resultStatus = "��������";
			}
			resultStatus += "(" + rs + ")";

			memo = getContent(src, "memo=", ";result");
			result = getContent(src, "result=", null);
			isSignOk = checkSign(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean checkSign(String result) {
		boolean retVal = false;
		try {
			JSONObject json = string2JSON(result, "&");

			int pos = result.indexOf("&sign_type=");
			String signContent = result.substring(0, pos);

			String signType = json.getString("sign_type");
			signType = signType.replace("\"", "");

			String sign = json.getString("sign");
			sign = sign.replace("\"", "");

			if (signType.equalsIgnoreCase("RSA")) {
				retVal = SignUtils.doCheck(signContent, sign, Keys.PUBLIC);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("Result", "Exception =" + e);
		}
		Log.i("Result", "checkSign =" + retVal);
		return retVal;
	}

	public static JSONObject string2JSON(String src, String split) {
		JSONObject json = new JSONObject();

		try {
			String[] arr = src.split(split);
			for (int i = 0; i < arr.length; i++) {
				String[] arrKey = arr[i].split("=");
				json.put(arrKey[0], arr[i].substring(arrKey[0].length() + 1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	private static String getContent(String src, String startTag, String endTag) {
		String content = src;
		int start = src.indexOf(startTag);
		start += startTag.length();

		try {
			if (endTag != null) {
				int end = src.indexOf(endTag);
				content = src.substring(start, end);
			} else {
				content = src.substring(start);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return content;
	}
}
