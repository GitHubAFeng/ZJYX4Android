package com.ihangjing.app;

import java.io.File;
import java.io.IOException;

import android.os.Environment;

/**
 * ��SD���ļ��Ĺ���
 * 
 * @author ch.linghu
 * 
 */
public class FileHelper {
	private static final String TAG = "FileHelper";
	
	//����SD���е��ļ�Ŀ¼����
	private static final String BASE_PATH = "EasyEatAndroid";

	public static File getBasePath() throws IOException {
		File basePath = new File(Environment.getExternalStorageDirectory(),
				BASE_PATH);

		if (!basePath.exists()) {
			if (!basePath.mkdirs()) {
				throw new IOException(String.format("%s cannot be created!",
						basePath.toString()));
			}
		}

		if (!basePath.isDirectory()) {
			throw new IOException(String.format("%s is not a directory!",
					basePath.toString()));
		}

		return basePath;
	}
}
