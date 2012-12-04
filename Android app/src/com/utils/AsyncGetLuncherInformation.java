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

import com.ui.Luncher;

import android.content.Context;

/**
 * Get the information to display on the splash screen
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
