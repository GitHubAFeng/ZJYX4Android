/*
 * Copyright (C) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ihangjing.app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Manages retrieval and storage of icon images. Use the put method to download
 * and store images. Use the get method to retrieve images from the manager.
 */
public class ImageManager implements ImageCache {
	private static final String TAG = "ImageManager";

	// Ŀǰ�����֧��496px, ������ͬ����С
	// �����Ϊ992px, �������н�ȡ
	public static final int DEFAULT_COMPRESS_QUALITY = 90;
	public static final int MAX_WIDTH = 596;
	public static final int MAX_HEIGHT = 1192;

	private Context mContext;
	// In memory cache.
	private Map<String, SoftReference<Bitmap>> mCache;
	private HttpClient mClient;
	// MD5 hasher.
	private MessageDigest mDigest;

	// We want the requests to timeout quickly.
	// Tweets are processed in a batch and we don't want to stay on one too
	// long.
	private static final int CONNECTION_TIMEOUT_MS = 10 * 1000;
	private static final int SOCKET_TIMEOUT_MS = 10 * 1000;

	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public ImageManager(Context context) {
		mContext = context;
		mCache = new HashMap<String, SoftReference<Bitmap>>();
		mClient = new DefaultHttpClient();

		try {
			mDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// This shouldn't happen.
			throw new RuntimeException("No MD5 algorithm.");
		}
	}

	public void setContext(Context context) {
		mContext = context;
	}

	private String getHashString(MessageDigest digest) {
		StringBuilder builder = new StringBuilder();

		for (byte b : digest.digest()) {
			builder.append(Integer.toHexString((b >> 4) & 0xf));
			builder.append(Integer.toHexString(b & 0xf));
		}

		return builder.toString();
	}

	// MD5 hases are used to generate filenames based off a URL.
	private String getMd5(String url) {
		mDigest.update(url.getBytes());

		return getHashString(mDigest);
	}

	// Looks to see if an image is in the file system.
	private Bitmap lookupFile(String url) {
		String hashedUrl = getMd5(url);
		FileInputStream fis = null;

		try {
			fis = mContext.openFileInput(hashedUrl);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			// Not there.
			return null;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// Ignore.
				}
			}
		}
	}

	/**
	 * Downloads a file
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public Bitmap fetchImage(String url) throws IOException {
		Log.d(TAG, "Fetching image: " + url);
		//urlΪ��ʱ
		if(url.length() < 5)
		{
			return null;
		}
		
		HttpGet get = new HttpGet(url);
		HttpConnectionParams.setConnectionTimeout(get.getParams(),
				CONNECTION_TIMEOUT_MS);
		HttpConnectionParams.setSoTimeout(get.getParams(), SOCKET_TIMEOUT_MS);

		HttpResponse response;

		try {
			response = mClient.execute(get);
		} catch (ClientProtocolException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new IOException("Invalid client protocol.");
		}

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new IOException("Non OK response: "
					+ response.getStatusLine().getStatusCode());
		}

		HttpEntity entity = response.getEntity();
		BufferedInputStream bis = new BufferedInputStream(entity.getContent());
		Bitmap bitmap = BitmapFactory.decodeStream(bis);
		bis.close();

		return bitmap;
	}

	/**
	 * ����Զ��ͼƬ -> ת��ΪBitmap -> д�뻺����.
	 * 
	 * @param url
	 * @param quality
	 *            image quality 1��100
	 * @throws IOException
	 */
	public void put(String url, int quality, boolean forceOverride)
			throws IOException {
		if (!forceOverride && contains(url)) {
			// Image already exists.
			return;

			// TODO: write to file if not present.
		}

		Bitmap bitmap = fetchImage(url);

		if (bitmap == null) {
			Log.w(TAG, "Retrieved bitmap is null.");
		} else {
			put(url, bitmap, quality);
		}
	}

	/**
	 * ���� put(String url, int quality)
	 * 
	 * @param url
	 * @throws IOException
	 */
	public void put(String url) throws IOException {
		put(url, DEFAULT_COMPRESS_QUALITY, false);
	}

	/**
	 * ������File -> ת��ΪBitmap -> д�뻺����. ���ͼƬ��С����MAX_WIDTH/MAX_HEIGHT, �򽫻��ͼƬ����.
	 * 
	 * @param file
	 * @param quality
	 *            ͼƬ����(0~100)
	 * @param forceOverride
	 * @throws IOException
	 */
	public void put(File file, int quality, boolean forceOverride)
			throws IOException {
		if (!file.exists()) {
			Log.w(TAG, file.getName() + " is not exists.");
			return;
		}
		if (!forceOverride && contains(file.getPath())) {
			// Image already exists.
			Log.d(TAG, file.getName() + " is exists");
			return;
			// TODO: write to file if not present.
		}

		Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
		bitmap = resizeBitmap(bitmap, MAX_WIDTH, MAX_HEIGHT);

		if (bitmap == null) {
			Log.w(TAG, "Retrieved bitmap is null.");
		} else {
			put(file.getPath(), bitmap, quality);
		}
	}

	/**
	 * ��Bitmapд�뻺����.
	 * 
	 * @param filePath
	 *            file path
	 * @param bitmap
	 * @param quality
	 *            1~100
	 */
	public void put(String file, Bitmap bitmap, int quality) {
		synchronized (this) {
			mCache.put(file, new SoftReference<Bitmap>(bitmap));
		}

		writeFile(file, bitmap, quality);
	}

	/**
	 * ���� put(String file, Bitmap bitmap, int quality)
	 * 
	 * @param filePath
	 *            file path
	 * @param bitmap
	 * @param quality
	 *            1~100
	 */
	@Override
	public void put(String file, Bitmap bitmap) {
		put(file, bitmap, DEFAULT_COMPRESS_QUALITY);
	}

	/**
	 * ��Bitmapд�뱾�ػ����ļ�.
	 * 
	 * @param file
	 *            URL/PATH
	 * @param bitmap
	 * @param quality
	 */
	private void writeFile(String file, Bitmap bitmap, int quality) {
		if (bitmap == null) {
			Log.w(TAG, file + "Can't write file. Bitmap is null.");
			return;
		}

		BufferedOutputStream bos = null;
		try {
			String hashedUrl = getMd5(file);
			bos = new BufferedOutputStream(mContext.openFileOutput(hashedUrl,
					Context.MODE_PRIVATE));
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
			Log.d(TAG, "Writing file: " + file);
		} catch (IOException ioe) {
			Log.e(TAG, ioe.getMessage());
		} finally {
			try {
				if (bos != null) {
					bos.flush();
					bos.close();
				}
				// bitmap.recycle();
			} catch (IOException e) {
				Log.e(TAG, "Could not close file.");
			}
		}

	}

	public Bitmap get(File file) {
		return get(file.getPath());
	}

	/**
	 * �жϻ��������Ƿ���ڸ��ļ���Ӧ��bitmap
	 */
	public boolean isContains(String file) {
		return mCache.containsKey(file);
	}

	/**
	 * ���ָ��file/URL��Ӧ��Bitmap�������ұ����ļ��������ֱ��ʹ�ã�����ȥ���ϻ�ȡ
	 * 
	 * @param file
	 *            file URL/file PATH
	 * @param bitmap
	 * @param quality
	 */
	public Bitmap safeGet(String file) throws IOException {
		SoftReference<Bitmap> ref;
		Bitmap bitmap;

		// first try file.
		bitmap = lookupFile(file);

		if (bitmap != null) {
			synchronized (this) {
				mCache.put(file, new SoftReference<Bitmap>(bitmap));
			}

			return bitmap;
		} else {
			// get from web
			String url = file;
			bitmap = fetchImage(url);
		}

		// д��Cache
		put(file, bitmap);

		return bitmap;

	}

	/**
	 * �ӻ������ж�ȡ�ļ�
	 * 
	 * @param file
	 *            file URL/file PATH
	 * @param bitmap
	 * @param quality
	 */
	@Override
	public Bitmap get(String file) {
		SoftReference<Bitmap> ref;
		Bitmap bitmap;

		// Look in memory first.
		synchronized (this) {
			ref = mCache.get(file);
		}

		if (ref != null) {
			bitmap = ref.get();

			if (bitmap != null) {
				return bitmap;
			}
		}

		// Now try file.
		bitmap = lookupFile(file);

		if (bitmap != null) {
			synchronized (this) {
				mCache.put(file, new SoftReference<Bitmap>(bitmap));
			}

			return bitmap;
		}

		// TODO: why?
		// upload: see profileImageCacheManager line 96
		Log.w(TAG, "Image is missing: " + file);
		// return the default photo
		//return mDefaultBitmap;
		return null;
	}

	public boolean contains(String url) {
		return get(url) != mDefaultBitmap;
	}

	public void clear() {
		String[] files = mContext.fileList();

		for (String file : files) {
			mContext.deleteFile(file);
		}

		synchronized (this) {
			mCache.clear();
		}
	}

	public void cleanup(HashSet<String> keepers) {
		String[] files = mContext.fileList();
		HashSet<String> hashedUrls = new HashSet<String>();

		for (String imageUrl : keepers) {
			hashedUrls.add(getMd5(imageUrl));
		}

		for (String file : files) {
			if (!hashedUrls.contains(file)) {
				Log.d(TAG, "Deleting unused file: " + file);
				mContext.deleteFile(file);
			}
		}
	}

	/**
	 * Compress and resize the Image
	 * 
	 * @param targetFile
	 * @param quality
	 * @return
	 * @throws IOException
	 */
	public File compressImage(File targetFile, int quality) throws IOException {

		put(targetFile, quality, true); // compress, resize, store

		String filePath = getMd5(targetFile.getPath());
		File compressedImage = mContext.getFileStreamPath(filePath);

		return compressedImage;
	}

	/**
	 * ���ֳ������СBitmap
	 * 
	 * @param bitmap
	 * @param maxWidth
	 * @param maxHeight
	 * @param quality
	 *            1~100
	 * @return
	 */
	public Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {

		int originWidth = bitmap.getWidth();
		int originHeight = bitmap.getHeight();

		// no need to resize
		if (originWidth < maxWidth && originHeight < maxHeight) {
			return bitmap;
		}

		int width = originWidth;
		int height = originHeight;

		// ��ͼƬ����, �򱣳ֳ��������ͼƬ
		if (originWidth > maxWidth) {
			width = maxWidth;

			double i = originWidth * 1.0 / maxWidth;
			height = (int) Math.floor(originHeight / i);

			bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
		}

		// ��ͼƬ����, ����в���ȡ
		if (height > maxHeight) {
			height = maxHeight;

			int half_diff = (int) ((originHeight - maxHeight) / 2.0);
			bitmap = Bitmap.createBitmap(bitmap, 0, half_diff, width, height);
		}

		// Log.d(TAG, width + " width");
		// Log.d(TAG, height + " height");

		return bitmap;
	}

}
