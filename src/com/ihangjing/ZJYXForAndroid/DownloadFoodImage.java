package com.ihangjing.ZJYXForAndroid;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

public class DownloadFoodImage {

	private Context mContext;

	// ���صİ�װ��url
	//private String apkUrl = "http://www.fanshigang.com/App/Android/Fsg4Android.apk";
	private Dialog downloadDialog;

	/* ���ذ���װ·�� */
	private static final String savePath = "/sdcard/fanshigang/";

	private String saveFileName = savePath
			+ "Fsg4AndroidRelease.apk";

	/* ��������֪ͨuiˢ�µ�handler��msg���� */
	private ProgressBar mProgress;

	private static final int DOWN_UPDATE = 1;

	private static final int DOWN_OVER = 2;

	private int progress;

	private Thread downLoadThread;

	private boolean interceptFlag = false;

	
	private String imageUrlString = "";
	private String shopidString = "";
	private String pidString = "";
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				break;
			case DOWN_OVER:
				installApk();
				break;
			default:
				break;
			}
		};
	};

	public DownloadFoodImage(Context context, String imageUrl, String shopid, String pid) {

		this.mContext = context;
		
		imageUrlString = imageUrl;
		shopidString = shopid;
		pidString = pid;

		//http://www.fanshigang.com/upload/201204/201204121710510625.jpg
		String temp = imageUrlString.replace("http://www.fanshigang.com/upload/", "s");
		//Log.v("DownloadFoodImage temp:", temp);
		
		String[] ss = new String[2];
		ss = temp.split("\\.");//. ��������ʽ��������ĺ��壬�������ʹ�õ�ʱ��������ת��

		String fileExtensionString = ss[1];
		
		Log.v("DownloadFoodImage fileExtensionString:", fileExtensionString);
		
		saveFileName = savePath + shopidString+ pidString+fileExtensionString;
		
		Log.v("DownloadFoodImage saveFileName:", saveFileName);
	}

	// �ⲿ�ӿ�����Activity����
	public void checkUpdateInfo() {
		showDownloadDialog();
	}

	private void showDownloadDialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("����ͼƬ");

		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.process, null);
		mProgress = (ProgressBar) v.findViewById(R.id.progress);

		builder.setView(v);
		builder.setNegativeButton("ȡ��", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		downloadDialog = builder.create();
		downloadDialog.show();

		downloadApk();
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(imageUrlString);

				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdir();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// ���½���
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// �������֪ͨ��װ
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// ���ȡ����ֹͣ����.

				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	/**
	 * ����apk
	 * 
	 * @param url
	 */
	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * ��װapk
	 * 
	 * @param url
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		/*
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
		*/
		
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(apkfile), "image/*");
		mContext.startActivity(intent);
		
		downloadDialog.hide();
	}
}