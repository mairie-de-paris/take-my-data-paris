package com.utils;

import org.apache.http.message.BasicNameValuePair;

import android.graphics.Bitmap;

public class Pair extends BasicNameValuePair {
	private int mIntValue = -1;
	private Bitmap mBitmapValue;
	private int mId;

	public Pair(String name, String value) {
		super(name, value);
	}

	public Pair(String name, Bitmap b) {
		super(name, "");
		mBitmapValue = b;
	}

	public Pair(String name, int value) {
		super(name, "");
		this.mIntValue = value;
	}

	@Override
	public String getValue() {
		String val = super.getValue();
		if (val.equals("") && mIntValue != -1) {
			return (String.valueOf(mIntValue));
		}
		return val;
	}

	public Pair(int id, int value) {
		super(String.valueOf(id), "");
		this.mIntValue = value;
		this.mId = id;
	}

	public int getIntValue() {
		return (mIntValue);
	}

	public int getId() {
		return (mId);
	}

	public Bitmap getBitmapValue() {
		return (mBitmapValue);
	}
}
