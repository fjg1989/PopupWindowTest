package com.fjg.keyboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.EditText;

public class ABEditText extends EditText {
	private Context context;
	private ABKeyBoard mPopupWindow;

	public ABEditText(Context context) {
		super(context);
		init(context);

	}

	public ABEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public ABEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		this.context = context;

	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		requestFocus();
		showKeyBoard();
		return false;
	};

	public void showKeyBoard() {
		if (mPopupWindow != null) {
		} else {
			mPopupWindow = new ABKeyBoard(context, this);
		}
		if (!mPopupWindow.isShowing()) {
			mPopupWindow.showAtLocation(this, Gravity.BOTTOM, 0, 0);
		} else {
			mPopupWindow.dismiss();
		}
	}

}
