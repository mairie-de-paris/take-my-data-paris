package com.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;

public class AsyncDownloadPointImg extends AsyncTask<Void, Integer, Boolean> {

	private String mUrl;
	private AlertDialog mDialog;
	private Context mContext;
	private Bitmap mImage;

	public AsyncDownloadPointImg(String imgUrl, AlertDialog alertDialog,
			Context context) {
		mUrl = imgUrl;
		mDialog = alertDialog;
		mContext = context;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		mImage = DownloadImage
				.getBitmap(mUrl, "", false, mContext, false, true);
		return (mImage != null);
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
			mDialog.setIcon(new BitmapDrawable(mContext.getResources(), mImage));
		} else {

		}
	}

}
