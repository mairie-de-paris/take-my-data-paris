package com.utils;

import com.ui.Luncher;

import android.content.Context;

/**
 * Get the informations to display on the splash screen
 * 
 * @author Morgan
 * 
 */
public class AsyncGetLuncherInformation extends MyAsyncTask {

	private Context mContext;

	public AsyncGetLuncherInformation(Context c) {
		super("ws/splash_screen.php");
		mContext = c;
	}

	@Override
	protected String doInBackground(Pair... params) {
		DownloadImage.getBitmap(Luncher.IMG_URL, Luncher.IMG_NAME, true,
				mContext, true, false);
		return super.doInBackground(params);
	}

	@Override
	public void onPostExecute(String s) {
		FileAccess.WriteSettings(s, mContext, FileAccess.SPLASH_SCREEN);
	}
}
