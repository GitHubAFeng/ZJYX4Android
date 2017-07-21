package com.ihangjing.ZJYXForAndroid;

import android.R.integer;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CouponInputDialogActivity extends Dialog {
	private Button btnReturn;

	private TextView tvSave;

	private EditText etRemark;
	private EditText etInput;
	public Context context;
	int nMaxLength = 0;
	int nInputType = 4;// 0：数字，1：小数，2：英文 3：英文数字 ， 4，无限制
	// int rnInputType = 4;//保存传过来的输入类型
	char[] input = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	char[] input1 = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.' };
	char[] input2 = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
			'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
			'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A',
			'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
			'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
	char[] input3 = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
			'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
			'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A',
			'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
			'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '.',
			',', '<', '>', '/', '?', '\\', '\'', '"', ';', ':', '`', '~', '!',
			'@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '-', '_', '+',
			'=' };

	char[] input4 = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
			'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
			'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A',
			'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
			'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '-' };
	public OnClickOKListener ClickOKListener;

	protected CouponInputDialogActivity(Context context, String title,
			String notice, String content, int maxLength, final int inputType,
			boolean singleLine, OnClickOKListener clickOkListener) {
		super(context, R.style.no_title_dialog);
		this.context = context;
		setContentView(R.layout.coupon_input_dialog);
		nInputType = inputType;
		// rnInputType = inputType;
		ClickOKListener = clickOkListener;
		View.OnClickListener btnClick = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.title_bar_back_btn:
					dismiss();
					break;
				case R.id.title_bar_right_btn:
					if (etRemark.getText().toString().length() < 19) {
						Toast.makeText(CouponInputDialogActivity.this.context,
								"请输入正确的券号", 6).show();
						return;
					}
					if (ClickOKListener != null) {
						ClickOKListener.getInput(etInput.getText().toString(),
								etRemark.getText().toString());
						dismiss();
					}
					break;
				}
			}
		};
		nMaxLength = maxLength;

		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.width = LayoutParams.FILL_PARENT;
		params.height = LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);
		btnReturn = (Button) findViewById(R.id.title_bar_back_btn);
		btnReturn.setOnClickListener(btnClick);
		tvSave = (TextView) findViewById(R.id.title_bar_right_btn);
		tvSave.setOnClickListener(btnClick);
		tvSave.setText("确认");
		tvSave.setVisibility(View.VISIBLE);

		etInput = (EditText) findViewById(R.id.et_first);
		etRemark = (EditText) findViewById(R.id.et_second);
		InputFilter[] filters = { new LengthFilter(maxLength) };
		etInput.setFilters(filters);
		etInput.addTextChangedListener(new TextWatcher() {
			Boolean isDot = false;

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				// s.length();
				// tvInputNumber.setText(String.valueOf(nMaxLength -
				// s.length()));

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				/*
				 * s. if(s.equals('.') && nInputType == 1){ isDot = true; }
				 */

			}

		});
		if (content != null && !content.equals("")) {
			etInput.setText(content);
		}
		if (nInputType < 4) {// 0：数字，1：小数，2：英文 3：英文数字 ， 4，无限制
			etInput.setKeyListener(new NumberKeyListener() {

				@Override
				public int getInputType() {
					// TODO Auto-generated method stub
					// 0无键盘 1英文键盘 2模拟键盘 3数字键盘
					switch (nInputType) {
					case 0:
					case 1:
						return 3;
					case 2:
					case 3:
					default:
						return 1;
					}

				}

				@Override
				protected char[] getAcceptedChars() {
					// TODO Auto-generated method stub
					// char[] input;
					switch (nInputType) {
					case 0:

						return input;
					case 1:

						return input1;
					case 2:

						return input2;
					case 3:

					default:

						return input3;
					}
					// return input;
				}

			});
		}

		etRemark.addTextChangedListener(new TextWatcher() {
			Boolean isEdit = false;

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				// s.length();
				// tvInputNumber.setText(String.valueOf(nMaxLength -
				// s.length()));

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				//start;
				if (!isEdit) {
					int len = start + 1;
					
					if (len > 19 && before == 0) {//已经是最大值
						isEdit = true;
						etRemark.setText(String.format("%s", s.subSequence(0, start)));
						return;
					}
					
					if (before > 0) {//删除
						return;
					}
					if ((len == 5 && start == 4) || (len == 10 && start == 9) || (len == 15 && start == 14)) {
						String value = String.valueOf(s.charAt(start));
						isEdit = true;
						etRemark.setText(String.format("%s-%s", s.subSequence(0, start), value));
						return;
					}
					if (start == 4 || start == 9 || start == 14) {
						isEdit = true;
						etRemark.setText(String.format("%s-", s));
						return;
					}
				}else{
					//etRemark.set
					//Spannable spanText = (Spannable)s;
					etRemark.setSelection(s.length(), s.length());//设置光标位置
					isEdit = false;
				}
				

			}

		});

		etRemark.setKeyListener(new NumberKeyListener() {

			@Override
			public int getInputType() {
				// TODO Auto-generated method stub
				// 0无键盘 1英文键盘 2模拟键盘 3数字键盘

				return 1;

			}

			@Override
			protected char[] getAcceptedChars() {
				// TODO Auto-generated method stub
				return input4;
			}

		});

		if (singleLine) {
			etInput.setSingleLine(true);
		} else {
			// etInput.setLayoutParams(new
			// LayoutParams(LayoutParams.FILL_PARENT,
			// LayoutParams.FILL_PARENT));

		}
		// TODO Auto-generated constructor stub
	}

	public interface OnClickOKListener {
		void getInput(String value1, String value2);
	}

	public class HJInputConnection extends InputConnectionWrapper {

		public HJInputConnection(InputConnection target, boolean mutable) {
			super(target, mutable);
		}

		public boolean commitText(CharSequence text, int newCursorPosition) {
			// 此处拦截
			return super.commitText(text, newCursorPosition);
		}

		public boolean sendKeyEvent(KeyEvent event) {
			// 此处拦截
			return super.sendKeyEvent(event);
		}
	}

	public class HJEditText extends EditText {

		public HJEditText(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
			return new HJInputConnection(
					super.onCreateInputConnection(outAttrs), false);
		}

	}

}
