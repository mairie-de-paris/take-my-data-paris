/**
 * Copyright 2012-2013 Mairie de Paris
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
**/

package com.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;

/**
 * Download the image of a point from its url
 *
 */
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
		mImage = DownloadImage.getBitmap(mUrl, "", false, mContext, false, true);
		return (mImage != null);
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
			mDialog.setIcon(new BitmapDrawable(mContext.getResources(), mImage));
		}
	}

}
