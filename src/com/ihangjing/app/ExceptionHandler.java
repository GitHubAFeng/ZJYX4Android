package com.ihangjing.app;

import android.app.Activity;
import android.widget.Toast;

import com.ihangjing.http.HttpAuthException;
import com.ihangjing.http.HttpException;
import com.ihangjing.http.HttpRefusedException;
import com.ihangjing.http.HttpServerException;
import com.ihangjing.http.RefuseError;

public class ExceptionHandler {

	private Activity mActivity;

	public ExceptionHandler(Activity activity) {
		mActivity = activity;
	}

	public void handle(HttpException e) {

		Throwable cause = e.getCause();
		if (null == cause)
			return;

		// Handle Different Exception
		
		if (cause instanceof HttpAuthException) {
			// �û���/�������
			//Toast.makeText(mActivity, R.string.error_not_authorized,
			//		Toast.LENGTH_LONG).show();
			//Intent intent = new Intent(mActivity, LoginActivity.class);
			//mActivity.startActivity(intent); // redirect to the login activity
		} else if (cause instanceof HttpServerException) {
			// ��������ʱ�޷���Ӧ
			//Toast.makeText(mActivity, R.string.error_not_authorized,
			//		Toast.LENGTH_LONG).show();
		} else if (cause instanceof HttpAuthException) {
			// FIXME: ���д����û���/������֤���󣬷��ص���¼����
		} else if (cause instanceof HttpRefusedException) {
			// �������ܾ�������û��Ȩ�޲鿴ĳ�û���Ϣ
			RefuseError error = ((HttpRefusedException) cause).getError();
			switch (error.getErrorCode()) {
			// TODO: finish it
			case -1:
			default:
				Toast.makeText(mActivity, error.getMessage(), Toast.LENGTH_LONG)
						.show();
				break;
			}
		}

	}

	private void handleCause() {

	}

}
