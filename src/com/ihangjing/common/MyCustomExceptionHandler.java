package com.ihangjing.common;

import android.content.Context;
import android.os.Process;
import android.widget.Toast;

public class MyCustomExceptionHandler implements
		Thread.UncaughtExceptionHandler {
	private Context ctx;
	private Thread.UncaughtExceptionHandler defaultUEH;

	public MyCustomExceptionHandler(Context paramContext) {
		Thread.UncaughtExceptionHandler localUncaughtExceptionHandler = Thread
				.getDefaultUncaughtExceptionHandler();
		this.defaultUEH = localUncaughtExceptionHandler;
		this.ctx = paramContext;
	}

	@Override
	public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
		Toast.makeText(this.ctx, "应用程序异常,请重新进入", 0).show();
		Process.killProcess(Process.myPid());
		this.defaultUEH.uncaughtException(paramThread, paramThrowable);
	}
}