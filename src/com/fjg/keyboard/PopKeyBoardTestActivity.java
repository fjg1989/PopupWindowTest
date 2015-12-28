package com.fjg.keyboard;



import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class PopKeyBoardTestActivity extends Activity implements OnFocusChangeListener {
	private ABEditText ed1, ed2, ed3, ed4, ed5, ed6, ed7, ed8;
	private EditText ed;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ed = (EditText) findViewById(R.id.ed);
		ed1 = (ABEditText) findViewById(R.id.ed1);
		ed2 = (ABEditText) findViewById(R.id.ed2);
		ed3 = (ABEditText) findViewById(R.id.ed3);
		ed4 = (ABEditText) findViewById(R.id.ed4);
		ed5 = (ABEditText) findViewById(R.id.ed5);
		ed6 = (ABEditText) findViewById(R.id.ed6);
		ed7 = (ABEditText) findViewById(R.id.ed7);
		ed8 = (ABEditText) findViewById(R.id.ed8);
		// hiddenKeyBoard();
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {

		// if (v.getId() != R.id.ed) {
		// if (hasFocus) {
		// hiddenKeyBoard();// 隐藏键盘
		// }
		// }
	}

	/**
	 * 掩藏键盘
	 */
	public void hiddenKeyBoard() {
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this
				.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

}