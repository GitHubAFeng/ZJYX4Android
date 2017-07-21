package com.ihangjing.common;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Bitmap;
/****
 *�ڴ��еĻ��� 
 ****/
public class ImageMemoryCache {
	private static final int HARD_CACHE_CAPACITY = 30;
    private HashMap<String, Bitmap> mHardBitmapCache ;
    private final static ConcurrentHashMap<String, SoftReference<Bitmap>> mSoftBitmapCache =
          new ConcurrentHashMap<String, SoftReference<Bitmap>>(HARD_CACHE_CAPACITY / 2);
            
    public ImageMemoryCache()      
    {
        mHardBitmapCache =
              new LinkedHashMap<String, Bitmap>(HARD_CACHE_CAPACITY / 2, 0.75f, true) {
              /**
                   * 
                   */
                  private static final long serialVersionUID = 1L;

              @Override
              protected boolean removeEldestEntry(LinkedHashMap.Entry<String, Bitmap> eldest) {
                  if (size() > HARD_CACHE_CAPACITY) {
                      // Entries push-out of hard reference cache are transferred to soft reference cache
                      mSoftBitmapCache.put(eldest.getKey(), new SoftReference<Bitmap>(eldest.getValue()));
                      return true;
                  } else
                      return false;
              }
          };

    }
    
    /**
    * �ӻ����л�ȡͼƬ
    */
    public Bitmap getBitmapFromCache(String url) {
    // �ȴ�mHardBitmapCache�����л�ȡ
    synchronized (mHardBitmapCache) {
    final Bitmap bitmap =mHardBitmapCache.get(url);
    if (bitmap != null) {
    //����ҵ��Ļ�����Ԫ���Ƶ�linkedhashmap����ǰ�棬�Ӷ���֤��LRU�㷨�������ɾ��
    mHardBitmapCache.remove(url);
    mHardBitmapCache.put(url,bitmap);
    return bitmap;
    }
    }
    //���mHardBitmapCache���Ҳ�������mSoftBitmapCache����
    SoftReference<Bitmap>bitmapReference = mSoftBitmapCache.get(url);
    if (bitmapReference != null) {
    final Bitmap bitmap =bitmapReference.get();
    if (bitmap != null) {
        //��ͼƬ�ƻ�Ӳ����
        mHardBitmapCache.put(url, bitmap);
        mSoftBitmapCache.remove(url);
    return bitmap;
    } else {
    mSoftBitmapCache.remove(url);
    }
    }
    return null;
    } 
    /***���ͼƬ������***/
    public void addBitmapToCache(String url, Bitmap bitmap) {
          if (bitmap != null) {
              synchronized (mHardBitmapCache) {
                  mHardBitmapCache.put(url, bitmap);
              }
          }
      }
}
