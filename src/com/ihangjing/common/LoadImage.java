package com.ihangjing.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ihangjing.common.OtherManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class LoadImage {
	private ExecutorService executorService; // 固定五个线程来
	public ImageMemoryCache memoryCache;// 内存缓存
	private HJImageFileCache fileCache;// 文件缓存
	private Map<String, RelativeLayout> taskMap;// 存放任务
	private Map<String, String> taskMap1;// 存放任务
	private boolean run = true;
	private Context context;
	private LoadImage(){
		
	}
	public LoadImage(Context c) {
		executorService = Executors.newFixedThreadPool(2);
		context = c;
		memoryCache = new ImageMemoryCache();
		fileCache = new HJImageFileCache();
		taskMap = new HashMap<String, RelativeLayout>();
		taskMap1 = new HashMap<String, String>();

	}

	public void addTask(String url, RelativeLayout rl, int defultID) {
		Bitmap bitmap = getBitmapFromCache(url);
		if (bitmap != null) {
			if (rl != null) {
				rl.setBackgroundDrawable(new BitmapDrawable(bitmap));// (bitmap);
			}

		} else {
			if (rl != null) {
				synchronized (taskMap) {
					taskMap.put(Integer.toString(rl.hashCode()), rl);
				}
				rl.setBackgroundResource(defultID);
			} else {
				synchronized (taskMap1) {
					taskMap1.put(Integer.toString(url.hashCode()), url);
				}
			}

		}
	}
	
	public Bitmap getBitmapFromCache(String url) {
		// TODO Auto-generated method stub
		Bitmap result;
		result = memoryCache.getBitmapFromCache(url);// 文件缓存中获取
		if (result == null) {
			
			result = fileCache.getImage(context, url);
			if (result != null) {
				memoryCache.addBitmapToCache(url, result);
			}
			/*if (result == null) {
				// 从网络获取
				result = ImageGetForHttp.downloadBitmap(url);
				if (result != null) {
					memoryCache.addBitmapToCache(url, result);
					fileCache.saveBmpToSd(result, url);
				}
			} else {
				// 添加到内存缓存
				
			}*/
		}
		return result;
	}

	public void doTask() {
		if (run && taskMap.size() != 0) {
			synchronized (taskMap) {
				Collection<RelativeLayout> con = taskMap.values();
				for (RelativeLayout i : con) {
					if (i != null) {
						if (i.getTag() != null) {
							loadImage((String) i.getTag(), i);
						}
					}
				}
				taskMap.clear();
			}
		} else {
			synchronized (taskMap1) {
				Collection<String> con = taskMap1.values();
				for (String i : con) {
					if (i != null) {
						loadImage(i, null);
					}
				}
				taskMap1.clear();
			}
		}

	}
	
	public void stopTask(){
		run   = false;
	}

	private void loadImage(String url, RelativeLayout img) {
		/*** 加入新的任务 ***/
		executorService.submit(new TaskWithResult(new TaskHandler(url, img),
				url));
	}

	/*** 获得一个图片,从三个地方获取,首先是内存缓存,然后是文件缓存,最后从网络获取 ***/
	public Bitmap getBitmap(String url) {
		// 从内存缓存中获取图片
		Bitmap result;
		result = memoryCache.getBitmapFromCache(url);
		if (result == null) {
			// 文件缓存中获取
			result = fileCache.getImage(context, url);
			if (result == null) {
				// 从网络获取
				result = ImageGetForHttp.downloadBitmap(url);
				if (result != null) {
					memoryCache.addBitmapToCache(url, result);
					fileCache.saveBmpToSd(result, url);
				}
			} else {
				// 添加到内存缓存
				memoryCache.addBitmapToCache(url, result);
			}
		}
		return result;
	}

	/*** 完成消息 ***/
	private class TaskHandler extends Handler {
		String url;
		RelativeLayout img;

		public TaskHandler(String url, RelativeLayout img) {
			this.url = url;
			this.img = img;
		}

		@Override
		public void handleMessage(Message msg) {
			/*** 查看imageview需要显示的图片是否被改变 ***/
			if (img != null && img.getTag().equals(url)) {
				if (msg.obj != null) {
					Bitmap bitmap = (Bitmap) msg.obj;
					img.setBackgroundDrawable(new BitmapDrawable(bitmap));// (bitmap);
				}
			}
		}
	}

	/*** 子线程任务 ***/
	private class TaskWithResult implements Callable<String> {
		private String url;
		private Handler handler;

		public TaskWithResult(Handler handler, String url) {
			this.url = url;
			this.handler = handler;
		}

		@Override
		public String call() throws Exception {
			Message msg = new Message();
			msg.obj = getBitmap(url);
			if (msg.obj != null) {
				handler.sendMessage(msg);
			}
			return url;
		}

	}
}
