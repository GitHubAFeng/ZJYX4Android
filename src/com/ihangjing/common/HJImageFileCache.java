package com.ihangjing.common;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.ihangjing.util.HJAppConfig;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;

public class HJImageFileCache {
	public static final String SDCARD_ROOT_PATH = android.os.Environment
			.getExternalStorageDirectory().getAbsolutePath();

	public void saveBitmapDrawable(Bitmap bitmap, String path, String name) {
		File dirFile = new File(path);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		File myCaptureFile = new File(path + name);
		BufferedOutputStream bos;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
			bitmap.compress(Bitmap.CompressFormat.PNG, 80, bos);
			bos.flush();
			bos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String saveBitmapWithType(Bitmap bitmap, String filed, int type,
			int id) {
		String path = SDCARD_ROOT_PATH + "/" + HJAppConfig.CookieName + "/";
		File dirFile = new File(path);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		path += filed + "/";
		dirFile = new File(path);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		path += String.valueOf(type) + "/";
		/*
		 * if (!dirFile.exists()) { dirFile.mkdir(); }
		 */
		String name = String.valueOf(id);
		saveBitmapDrawable(bitmap, path, name);
		return path + name;
	}

	public String saveBitmapWithType(Bitmap bitmap, String filed, int type,
			String id) {
		String path = SDCARD_ROOT_PATH + "/" + HJAppConfig.CookieName + "/";
		File dirFile = new File(path);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		path += filed + "/";
		dirFile = new File(path);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		path += String.valueOf(type) + "/";
		/*
		 * if (!dirFile.exists()) { dirFile.mkdir(); }
		 */
		// String name = String.valueOf(id);
		saveBitmapDrawable(bitmap, path, id);
		return path + id;
	}

	public String saveShopBitmapWithType(Bitmap bitmap, String shopID) {
		// TODO Auto-generated method stub
		shopID = shopID.replace("/", "$");
		return saveBitmapWithType(bitmap, "Shop", 0, shopID);
	}

	public String getShopImageLocalPath(int i, String id) {
		// TODO Auto-generated method stub
		id = id.replace("/", "$");
		return SDCARD_ROOT_PATH + "/" + HJAppConfig.CookieName + "/Shop/0/"
				+ id;
	}

	// ����������ͼƬ�Ĵ�С�����жϣ����õ����ʵ����ű���������2��1/2,3��1/3
	int computeSampleSize(BitmapFactory.Options options, int target) {
		int w = options.outWidth;
		int h = options.outHeight;
		int candidateW = w / target;
		int candidateH = h / target;
		int candidate = Math.max(candidateW, candidateH);
		if (candidate == 0)
			return 1;
		if (candidate > 1) {
			if ((w > target) && (w / candidate) < target)
				candidate -= 1;
		}
		if (candidate > 1) {
			if ((h > target) && (h / candidate) < target)
				candidate -= 1;
		}
		// if (VERBOSE)
		// Log.v(TAG, "for w/h " + w + "/" + h + " returning " + candidate + "("
		// + (w/candidate) + " / " + (h/candidate));
		return candidate;
	}

	public ParcelFileDescriptor openFile(Uri uri, String mode)
			throws FileNotFoundException {
		// TODO Auto-generated method stub
		File root = Environment.getExternalStorageDirectory();
		root.mkdirs();
		File path = new File(root, uri.getEncodedPath());

		int imode = 0;
		if (mode.contains("w")) {
			imode |= ParcelFileDescriptor.MODE_WRITE_ONLY;
			if (!path.exists()) {
				try {
					path.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (mode.contains("r"))
			imode |= ParcelFileDescriptor.MODE_READ_ONLY;
		if (mode.contains("+"))
			imode |= ParcelFileDescriptor.MODE_APPEND;

		return ParcelFileDescriptor.open(path, imode);
	}

	public Bitmap getBitmap(Context content, String path) {
		// TODO Auto-generated method stub
		File file = new File(path);
		if (!file.exists()) {
			return null;
		}	

		// �õ��ļ���������
		InputStream in;
		try {
			in = new FileInputStream(file); // ���ļ��������������
		} catch (FileNotFoundException e) { // TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		// ת����byte �� �ٸ�ʽ����λͼ
		//byte[] data = outStream.toByteArray();
		//Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// ����λͼ

		/*ParcelFileDescriptor pfd;
		try {
			ContextWrapper mCon = new ContextWrapper(content);
			Uri uri = Uri.parse("file:/" + path);
			//file.
			pfd = mCon.getContentResolver().openFileDescriptor(uri, "r");
		} catch (IOException ex) {
			return null;
		}
		java.io.FileDescriptor fd = pfd.getFileDescriptor();*/
		BitmapFactory.Options options = new BitmapFactory.Options();
		// ��ָ��ԭʼ��С
		options.inSampleSize = 1;
		// ֻ���д�С�ж�
		options.inJustDecodeBounds = true;
		// ���ô˷����õ�options�õ�ͼƬ�Ĵ�С
		BitmapFactory.decodeFile(path, options);//.decodeStream(in, null, options);
		// ���ǵ�Ŀ������800pixel�Ļ�������ʾ��
		// ������Ҫ����computeSampleSize�õ�ͼƬ���ŵı���
		options.inSampleSize = computeSampleSize(options, 400);
		// OK,���ǵõ������ŵı��������ڿ�ʼ��ʽ����BitMap����
		options.inJustDecodeBounds = false;
		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.RGB_565;

		// ����options��������������Ҫ���ڴ�
		/*Bitmap sourceBitmap = BitmapFactory.decodeFileDescriptor(fd, null,
				options);*/
		Bitmap sourceBitmap = BitmapFactory.decodeStream(in, null,
				options);
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sourceBitmap;

		// return bitmap;
	}

	public Bitmap getImage(Context content, String url) {
		// TODO Auto-generated method stub
		url = url.replace("http://", "");
		url = url.replace("/", "^");
		url = url.replace(":", "^");
		return getBitmap(content, SDCARD_ROOT_PATH + "/"
				+ HJAppConfig.CookieName + "/ImageCache/" + url + ".hj");
	}

	public void saveBmpToSd(Bitmap result, String url) {
		// TODO Auto-generated method stub
		url = url.replace("http://", "");
		url = url.replace("/", "^");
		url = url.replace(":", "^");
		String path = SDCARD_ROOT_PATH + "/" + HJAppConfig.CookieName + "/";
		File dirFile = new File(path);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		saveBitmapDrawable(result, path + "ImageCache/", url + ".hj");
	}
}
