/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 */

package com.ihangjing.ZJYXForAndroid;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.ihangjing.util.HJAppConfig;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ģ���̻�Ӧ�õ���Ʒ�б����ײ��衣
 * 
 * 1. ���̻�ID���տ��ʺţ��ⲿ�����ţ���Ʒ���ƣ���Ʒ���ܣ��۸�֪ͨ��ַ��װ�ɶ�����Ϣ 2. �Զ�����Ϣ����ǩ�� 3.
 * ��������Ϣ��ǩ����ǩ����ʽ��װ��������� 4. ����pay����
 * 
 * @version v4_0413 2012-03-02
 */
public class AlixDemo extends Activity implements OnItemClickListener,
		OnItemLongClickListener {
	static String TAG = "AppDemo4";

	//
	// ģ���̻���Ʒ�б�
	ListView mproductListView = null;
	ProductListAdapter m_listViewAdapter = null;
	ArrayList<Products.ProductDetail> mproductlist;

	private ProgressDialog mProgress = null;

	//
	// Called when the activity is first created.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate");

		//
		// check to see if the MobileSecurePay is already installed.
		// ��ⰲȫ֧�������Ƿ񱻰�װ
		MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(this);
		mspHelper.detectMobile_sp();

		//
		setContentView(R.layout.remote_service_binding);

		//
		// set title
		// ���ý������
		TextView mTitleName = (TextView) findViewById(R.id.AlipayTitleItemName);
		mTitleName.setText(getString(R.string.app_name));

		//
		// retrieve and show the product list.
		// ��ʾ��Ʒ�б�
		initProductList();
	}

	/**
	 * retrieve the product list. ������Ʒ�б�
	 */
	void initProductList() {
		Products products = new Products();
		this.mproductlist = products.retrieveProductInfo();

		mproductListView = (ListView) findViewById(R.id.ProductListView);
		m_listViewAdapter = new ProductListAdapter(this, this.mproductlist);
		mproductListView.setAdapter(m_listViewAdapter);
		mproductListView.setOnItemClickListener(this);
		mproductListView.setOnItemLongClickListener(this);
	}

	/**
	 * get the selected order info for pay. ��ȡ��Ʒ������Ϣ
	 * 
	 * @param position
	 *            ��Ʒ���б��е�λ��
	 * @return
	 */
	String getOrderInfo(int position) {
		String strOrderInfo = "partner=" + "\"" + PartnerConfig.PARTNER + "\"";
		strOrderInfo += "&";
		strOrderInfo += "seller=" + "\"" + PartnerConfig.SELLER + "\"";
		strOrderInfo += "&";
		strOrderInfo += "out_trade_no=" + "\"" + getOutTradeNo() + "\"";
		strOrderInfo += "&";
		strOrderInfo += "subject=" + "\"" + mproductlist.get(position).subject
				+ "\"";
		strOrderInfo += "&";
		strOrderInfo += "body=" + "\"" + mproductlist.get(position).body + "\"";
		strOrderInfo += "&";
		strOrderInfo += "total_fee=" + "\""
				+ mproductlist.get(position).price.replace("һ�ڼ�:", "") + "\"";
		strOrderInfo += "&";
		strOrderInfo += "notify_url=" + "\""
				+ "http://www.renrenzx.com/ad.aspx" + "\"";
		
		return strOrderInfo;
	}

	/**
	 * get the out_trade_no for an order. ��ȡ�ⲿ������
	 * 
	 * @return
	 */
	String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
		Date date = new Date();
		String strKey = format.format(date);

		java.util.Random r = new java.util.Random();
		strKey = strKey + r.nextInt();
		strKey = strKey.substring(0, 15);
		return strKey;
	}

	//
	//
	/**
	 * sign the order info. �Զ�����Ϣ����ǩ��
	 * 
	 * @param signType
	 *            ǩ����ʽ
	 * @param content
	 *            ��ǩ��������Ϣ
	 * @return
	 */
	String sign(String signType, String content) {
		return Rsa.sign(content, PartnerConfig.RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. ��ȡǩ����ʽ
	 * 
	 * @return
	 */
	String getSignType() {
		String getSignType = "sign_type=" + "\"" + "RSA" + "\"";
		return getSignType;
	}

	/**
	 * get the char set we use. ��ȡ�ַ���
	 * 
	 * @return
	 */
	String getCharset() {
		String charset = "charset=" + "\"" + "utf-8" + "\"";
		return charset;
	}

	/**
	 * the onItemClick for the list view of the products. ��Ʒ�б���Ʒ������¼�
	 */
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//
		// check to see if the MobileSecurePay is already installed.
		// ��ⰲȫ֧�������Ƿ�װ
		MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(this);
		boolean isMobile_spExist = mspHelper.detectMobile_sp();
		if (!isMobile_spExist)
			return;

		// check some info.
		// ���������Ϣ
		if (!checkInfo()) {
			BaseHelper
					.showDialog(
							AlixDemo.this,
							"��ʾ",
							"ȱ��partner����seller������src/com/alipay/android/appDemo4/PartnerConfig.java�����ӡ�",
							R.drawable.infoicon);
			return;
		}

		// start pay for this order.
		// ���ݶ�����Ϣ��ʼ����֧��
		try {
			// prepare the order info.
			// ׼��������Ϣ
			String orderInfo = getOrderInfo(position);
			// �������ǩ����ʽ�Զ�����Ϣ����ǩ��
			String signType = getSignType();
			String strsign = sign(signType, orderInfo);
			Log.v("sign:", strsign);
			// ��ǩ�����б���
			strsign = URLEncoder.encode(strsign);
			// ��װ�ò���
			String info = orderInfo + "&sign=" + "\"" + strsign + "\"" + "&"
					+ getSignType();
			Log.v("orderInfo:", info);
			// start the pay.
			// ����pay��������֧��
			/*MobileSecurePayer msp = new MobileSecurePayer();
			boolean bRet = msp.pay(info, mHandler, AlixId.RQF_PAY, this);

			if (bRet) {
				// show the progress bar to indicate that we have started
				// paying.
				// ��ʾ������֧����������
				closeProgress();
				mProgress = BaseHelper.showProgress(this, null, "����֧��", false,
						true);
			} else*/
				;
		} catch (Exception ex) {
			Toast.makeText(AlixDemo.this, R.string.remote_call_failed,
					Toast.LENGTH_SHORT).show();
		}
	}

	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		return false;
	}

	/**
	 * check some info.the partner,seller etc. ���������Ϣ
	 * partnerid�̻�id��seller�տ��ʺŲ���Ϊ��
	 * 
	 * @return
	 */
	private boolean checkInfo() {
		String partner = PartnerConfig.PARTNER;
		String seller = PartnerConfig.SELLER;
		if (partner == null || partner.length() <= 0 || seller == null
				|| seller.length() <= 0)
			return false;

		return true;
	}

	//
	// the handler use to receive the pay result.
	// �������֧�������֧�����ֻ���ͬ��֪ͨ
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				String strRet = (String) msg.obj;

				Log.e(TAG, strRet);	// strRet������resultStatus={9000};memo={};result={partner="2088201564809153"&seller="2088201564809153"&out_trade_no="050917083121576"&subject="123456"&body="2010�¿�NIKE �Ϳ�902��������Ь �Ϳ���ŮЬ 386201 �׺�"&total_fee="0.01"&notify_url="http://notify.java.jpxx.org/index.jsp"&success="true"&sign_type="RSA"&sign="d9pdkfy75G997NiPS1yZoYNCmtRbdOP0usZIMmKCCMVqbSG1P44ohvqMYRztrB6ErgEecIiPj9UldV5nSy9CrBVjV54rBGoT6VSUF/ufjJeCSuL510JwaRpHtRPeURS1LXnSrbwtdkDOktXubQKnIMg2W0PreT1mRXDSaeEECzc="}
				switch (msg.what) {
				case AlixId.RQF_PAY: {
					//
					closeProgress();

					BaseHelper.log(TAG, strRet);

					// �����׽��
					try {
						// ��ȡ����״̬�룬����״̬������ο��ĵ�
						String tradeStatus = "resultStatus={";
						int imemoStart = strRet.indexOf("resultStatus=");
						imemoStart += tradeStatus.length();
						int imemoEnd = strRet.indexOf("};memo=");
						tradeStatus = strRet.substring(imemoStart, imemoEnd);
						
						//����ǩ֪ͨ
						ResultChecker resultChecker = new ResultChecker(strRet);
						int retVal = resultChecker.checkSign();
						// ��ǩʧ��
						if (retVal == ResultChecker.RESULT_CHECK_SIGN_FAILED) {
							BaseHelper.showDialog(
									AlixDemo.this,
									"��ʾ",
									getResources().getString(
											R.string.check_sign_failed),
									android.R.drawable.ic_dialog_alert);
						} else {// ��ǩ�ɹ�����ǩ�ɹ������жϽ���״̬��
							if(tradeStatus.equals("9000"))//�жϽ���״̬�룬ֻ��9000��ʾ���׳ɹ�
								BaseHelper.showDialog(AlixDemo.this, "��ʾ","֧���ɹ�������״̬�룺"+tradeStatus, R.drawable.infoicon);
							else
							BaseHelper.showDialog(AlixDemo.this, "��ʾ", "֧��ʧ�ܡ�����״̬��:"
									+ tradeStatus, R.drawable.infoicon);
						}

					} catch (Exception e) {
						e.printStackTrace();
						BaseHelper.showDialog(AlixDemo.this, "��ʾ", strRet,
								R.drawable.infoicon);
					}
				}
					break;
				}

				super.handleMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	//
	//
	/**
	 * the OnCancelListener for lephone platform. lephoneϵͳʹ�õ���ȡ��dialog����
	 */
	static class AlixOnCancelListener implements
			DialogInterface.OnCancelListener {
		Activity mcontext;

		AlixOnCancelListener(Activity context) {
			mcontext = context;
		}

		public void onCancel(DialogInterface dialog) {
			mcontext.onKeyDown(KeyEvent.KEYCODE_BACK, null);
		}
	}

	//
	// close the progress bar
	// �رս��ȿ�
	void closeProgress() {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���ؼ������¼�
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			BaseHelper.log(TAG, "onKeyDown back");

			this.finish();
			return true;
		}

		return false;
	}

	//
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v(TAG, "onDestroy");

		try {
			mProgress.dismiss();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}