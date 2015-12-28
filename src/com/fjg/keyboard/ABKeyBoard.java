package com.fjg.keyboard;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;



import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;

public class ABKeyBoard extends PopupWindow {
	private KeyboardView keyboardView;
	private Keyboard keyAlp;// 字母键盘
	private Keyboard keyDig;// 数字键盘
	private Keyboard keyPun;// 标点键盘
	public boolean isnun = false;// 是否数字键盘
	public boolean isInterpunction = false;// 是否标点符号键盘
	public boolean isupper = false;// 是否大写
	private ABEditText ed;

	public ABKeyBoard(Context mContext, ABEditText edit) {
		this.ed = edit;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View conentView = inflater.inflate(R.layout.music_popwindow, null);
		this.setContentView(conentView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		this.setFocusable(false);
		this.setOutsideTouchable(true);
		// 刷新状态
		this.update();
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0000000000);
		// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
		this.setBackgroundDrawable(dw);
		this.setAnimationStyle(R.style.PopupAnimation);
		keyAlp = new Keyboard(mContext, R.xml.qwerty);
		keyDig = new Keyboard(mContext, R.xml.symbols);
		keyPun = new Keyboard(mContext, R.xml.interpunction);
		keyboardView = (KeyboardView) conentView.findViewById(R.id.keyboard_view);
		keyboardView.setKeyboard(keyDig);
		// keyboardView.setEnabled(true);
		keyboardView.setPreviewEnabled(false);// 必须设置为fasle，否则崩溃；
		keyboardView.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				ABKeyBoard.this.setFocusable(true);
				if (arg1 == KeyEvent.KEYCODE_BACK) {

					ABKeyBoard.this.dismiss();

				}
				return false;
			}
		});
		keyboardView.setOnKeyboardActionListener(listener);

	}

	private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
		@Override
		public void swipeUp() {
		}

		@Override
		public void swipeRight() {
		}

		@Override
		public void swipeLeft() {
		}

		@Override
		public void swipeDown() {
		}

		@Override
		public void onText(CharSequence text) {
		}

		@Override
		public void onRelease(int primaryCode) {
		}

		@Override
		public void onPress(int primaryCode) {
		}

		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			Editable editable = ed.getText();
			int start = ed.getSelectionStart();
			if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
				ABKeyBoard.this.dismiss();
			} else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
				if (!TextUtils.isEmpty(editable)) {
					if (start > 0) {
						editable.delete(start - 1, start);
					}
				}
			} else if (primaryCode == Keyboard.KEYCODE_SHIFT) {// 大小写切换
				changeKey();
				keyboardView.setKeyboard(keyAlp);

			} else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {// 数字键盘切换
				if (isnun) {
					isnun = false;
					isInterpunction = false;
					randomalpkey();
				} else {
					isnun = true;
					randomdigkey();
				}
			} else if (primaryCode == Keyboard.KEYCODE_DONE)// 标点符号键换切
			{
				if (isInterpunction) {
					isInterpunction = false;
					randomalpkey();
				} else {
					isInterpunction = true;
					randomInterpunctionkey();
				}
			} else {
				editable.insert(start, Character.toString((char) primaryCode));
			}
		}
	};

	/**
	 * 键盘大小写切换
	 */
	private void changeKey() {
		List<Key> keylist = keyAlp.getKeys();
		if (isupper) {// 大写切换小写
			isupper = false;
			for (Key key : keylist) {
				if (key.label != null && isword(key.label.toString())) {
					key.label = key.label.toString().toLowerCase();
					key.codes[0] = key.codes[0] + 32;
				}
			}
		} else {// 小写切换大写
			isupper = true;
			for (Key key : keylist) {
				if (key.label != null && isword(key.label.toString())) {
					key.label = key.label.toString().toUpperCase();
					key.codes[0] = key.codes[0] - 32;
				}
			}
		}
	}

	private boolean isNumber(String str) {
		String wordstr = "0123456789.,";
		if (wordstr.indexOf(str) > -1) {
			return true;
		}
		return false;
	}

	private boolean isInterpunction(String str) {
		String wordstr = "!\"#$%&()*+-\\/:;<=>?`'^_[]{|}~";
		if (wordstr.indexOf(str) > -1) {
			return true;
		}
		return false;
	}

	private boolean isword(String str) {
		String wordstr = "abcdefghijklmnopqrstuvwxyz@";
		if (wordstr.indexOf(str.toLowerCase()) > -1) {
			return true;
		}
		return false;
	}

	private void randomdigkey() {
		List<Key> keyList = keyDig.getKeys();
		// 查找出0-9的数字键
		List<Key> newkeyList = new ArrayList<Key>();
		for (int i = 0; i < keyList.size(); i++) {
			if (keyList.get(i).label != null && isNumber(keyList.get(i).label.toString())) {
				newkeyList.add(keyList.get(i));
			}
		}
		// 数组长度
		int count = newkeyList.size();
		// 结果集
		List<KeyModel> resultList = new ArrayList<KeyModel>();
		// 用一个LinkedList作为中介
		LinkedList<KeyModel> temp = new LinkedList<KeyModel>();
		// 初始化temp
		for (int i = 0; i < count - 2; i++) {
			temp.add(new KeyModel(48 + i, i + ""));
		}
		temp.add(new KeyModel(44, "" + (char) 44));
		temp.add(new KeyModel(46, "" + (char) 46));
		// 取数
		Random rand = new Random();
		for (int i = 0; i < count; i++) {
			int num = rand.nextInt(count - i);
			resultList.add(new KeyModel(temp.get(num).getCode(), temp.get(num).getLable()));
			temp.remove(num);
		}
		for (int i = 0; i < newkeyList.size(); i++) {
			newkeyList.get(i).label = resultList.get(i).getLable();
			newkeyList.get(i).codes[0] = resultList.get(i).getCode();
		}

		keyboardView.setKeyboard(keyDig);
	}

	private void randomalpkey() {
		List<Key> keyList = keyAlp.getKeys();
		// 查找出a-z的数字键
		List<Key> newkeyList = new ArrayList<Key>();
		for (int i = 0; i < keyList.size(); i++) {
			if (keyList.get(i).label != null && isword(keyList.get(i).label.toString())) {
				newkeyList.add(keyList.get(i));
			}
		}
		// 数组长度
		int count = newkeyList.size();
		// 结果集
		List<KeyModel> resultList = new ArrayList<KeyModel>();
		// 用一个LinkedList作为中介
		LinkedList<KeyModel> temp = new LinkedList<KeyModel>();
		// 初始化temp
		for (int i = 0; i < count - 1; i++) {
			temp.add(new KeyModel(97 + i, "" + (char) (97 + i)));
		}
		temp.add(new KeyModel(64, "" + (char) 64));// .
		// 取数
		Random rand = new Random();
		for (int i = 0; i < count; i++) {
			int num = rand.nextInt(count - i);
			resultList.add(new KeyModel(temp.get(num).getCode(), temp.get(num).getLable()));
			temp.remove(num);
		}
		for (int i = 0; i < newkeyList.size(); i++) {
			newkeyList.get(i).label = resultList.get(i).getLable();
			newkeyList.get(i).codes[0] = resultList.get(i).getCode();
		}

		keyboardView.setKeyboard(keyAlp);
	}

	/**
	 * 标点符号键盘-随机
	 */
	private void randomInterpunctionkey() {
		List<Key> keyList = keyPun.getKeys();

		// 查找出标点符号的数字键
		List<Key> newkeyList = new ArrayList<Key>();
		for (int i = 0; i < keyList.size(); i++) {
			if (keyList.get(i).label != null && isInterpunction(keyList.get(i).label.toString())) {
				newkeyList.add(keyList.get(i));
			}
		}
		// 数组长度
		int count = newkeyList.size();
		// 结果集
		List<KeyModel> resultList = new ArrayList<KeyModel>();
		// 用一个LinkedList作为中介
		LinkedList<KeyModel> temp = new LinkedList<KeyModel>();

		// 初始化temp
		temp.add(new KeyModel(33, "" + (char) 33));
		temp.add(new KeyModel(34, "" + (char) 34));
		temp.add(new KeyModel(35, "" + (char) 35));
		temp.add(new KeyModel(36, "" + (char) 36));
		temp.add(new KeyModel(37, "" + (char) 37));
		temp.add(new KeyModel(38, "" + (char) 38));
		temp.add(new KeyModel(39, "" + (char) 39));
		temp.add(new KeyModel(40, "" + (char) 40));
		temp.add(new KeyModel(41, "" + (char) 41));
		temp.add(new KeyModel(42, "" + (char) 42));
		temp.add(new KeyModel(43, "" + (char) 43));
		temp.add(new KeyModel(45, "" + (char) 45));
		temp.add(new KeyModel(47, "" + (char) 47));
		temp.add(new KeyModel(58, "" + (char) 58));
		temp.add(new KeyModel(59, "" + (char) 59));
		temp.add(new KeyModel(60, "" + (char) 60));
		temp.add(new KeyModel(61, "" + (char) 61));
		temp.add(new KeyModel(62, "" + (char) 62));
		temp.add(new KeyModel(63, "" + (char) 63));
		temp.add(new KeyModel(91, "" + (char) 91));
		temp.add(new KeyModel(92, "" + (char) 92));
		temp.add(new KeyModel(93, "" + (char) 93));
		temp.add(new KeyModel(94, "" + (char) 94));
		temp.add(new KeyModel(95, "" + (char) 95));
		temp.add(new KeyModel(96, "" + (char) 96));
		temp.add(new KeyModel(123, "" + (char) 123));
		temp.add(new KeyModel(124, "" + (char) 124));
		temp.add(new KeyModel(125, "" + (char) 125));
		temp.add(new KeyModel(126, "" + (char) 126));

		// 取数
		Random rand = new Random();
		for (int i = 0; i < count; i++) {
			int num = rand.nextInt(count - i);
			resultList.add(new KeyModel(temp.get(num).getCode(), temp.get(num).getLable()));
			temp.remove(num);
		}
		for (int i = 0; i < newkeyList.size(); i++) {
			newkeyList.get(i).label = resultList.get(i).getLable();
			newkeyList.get(i).codes[0] = resultList.get(i).getCode();
		}

		keyboardView.setKeyboard(keyPun);
	}

}
