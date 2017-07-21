package com.ihangjing.common;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.util.Log;

public class ImageGetForHttp {
	private static final String LOG_TAG="ImageGetForHttp";
    public static Bitmap downloadBitmap(String url) {
           //final int IO_BUFFER_SIZE = 4 * 1024;

           // AndroidHttpClient is not allowed to be used from the main thread
           final HttpClient client = AndroidHttpClient.newInstance("Android");
           final HttpGet getRequest = new HttpGet(url);

           try {
               HttpResponse response = client.execute(getRequest);
               final int statusCode = response.getStatusLine().getStatusCode();
               if (statusCode != HttpStatus.SC_OK) {
                   Log.w("ImageDownloader", "Error " + statusCode +
                           " while retrieving bitmap from " + url);
                   return null;
               }

               final HttpEntity entity = response.getEntity();
               if (entity != null) {
                   InputStream inputStream = null;
                   try {
                       inputStream = entity.getContent();
                       // return BitmapFactory.decodeStream(inputStream);
                       // Bug on slow connections, fixed in future release.
                       FilterInputStream fit= new FlushedInputStream(inputStream);
                       return BitmapFactory.decodeStream(fit);
                       /*BitmapFactory.Options options = new BitmapFactory.Options();
               		// ��ָ��ԭʼ��С
               		options.inSampleSize = 1;
               		// ֻ���д�С�ж�
               		options.inJustDecodeBounds = true;
               		// ���ô˷����õ�options�õ�ͼƬ�Ĵ�С
               		BitmapFactory.decodeStream(fit, null, options);
               		// ���ǵ�Ŀ������800pixel�Ļ�������ʾ��
               		// ������Ҫ����computeSampleSize�õ�ͼƬ���ŵı���
               		options.inSampleSize = computeSampleSize(options, 400);
               		// OK,���ǵõ������ŵı��������ڿ�ʼ��ʽ����BitMap����
               		options.inJustDecodeBounds = false;
               		options.inDither = false;
               		options.inPreferredConfig = Bitmap.Config.RGB_565;
                       return BitmapFactory.decodeStream(fit, null, options);*/
                   } finally {
                       if (inputStream != null) {
                           inputStream.close();
                       }
                       entity.consumeContent();
                   }
               }
           } catch (IOException e) {
               getRequest.abort();
               Log.w(LOG_TAG, "I/O error while retrieving bitmap from " + url, e);
           } catch (IllegalStateException e) {
               getRequest.abort();
               Log.w(LOG_TAG, "Incorrect URL: " + url);
           } catch (Exception e) {
               getRequest.abort();
               Log.w(LOG_TAG, "Error while retrieving bitmap from " + url, e);
           } finally {
               if ((client instanceof AndroidHttpClient)) {
                   ((AndroidHttpClient) client).close();
               }
           }
           return null;
       }
  // ����������ͼƬ�Ĵ�С�����жϣ����õ����ʵ����ű���������2��1/2,3��1/3
    static int computeSampleSize(BitmapFactory.Options options, int target) {
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
    /*
        * An InputStream that skips the exact number of bytes provided, unless it reaches EOF.
        */
       static class FlushedInputStream extends FilterInputStream {
           public FlushedInputStream(InputStream inputStream) {
               super(inputStream);
           }

           @Override
           public long skip(long n) throws IOException {
               long totalBytesSkipped = 0L;
               while (totalBytesSkipped < n) {
                   long bytesSkipped = in.skip(n - totalBytesSkipped);
                   if (bytesSkipped == 0L) {
                       int b = read();
                       if (b < 0) {
                           break;  // we reached EOF
                       } else {
                           bytesSkipped = 1; // we read one byte
                       }
                   }
                   totalBytesSkipped += bytesSkipped;
               }
               return totalBytesSkipped;
           }
       }
}