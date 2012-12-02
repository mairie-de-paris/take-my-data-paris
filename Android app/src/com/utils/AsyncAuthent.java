/**
 * Copyright 2012-2013 The Apache Software Foundation
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

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.containers.User;
import com.ui.AuthentInterface;
import com.ui.takemydata.R;

/**
 * Authenticate the user
 * 
 * @params email the email of the user
 * @params pwd the password of the user
 * 
 */
public class AsyncAuthent extends MyAsyncTask {

	private ProgressDialog mProgress;
	private AlertDialog mAlert;
	private String mEmail;
	private String mPwd;
	private String mPseudo;
	private AuthentInterface mAct;
	private int mKeyCode;
	private Context mContext;

	public AsyncAuthent(int keyCode, ProgressDialog progress,
			AlertDialog alertDialog, String email, String pwd, String pseudo,
			AuthentInterface act, Context context) {
		super("ws/signup.php");
		mProgress = progress;
		mAlert = alertDialog;
		mEmail = email;
		mPseudo = pseudo;
		mPwd = pwd;
		mAct = act;
		mKeyCode = keyCode;
		mContext = context;
	}

	@Override
	public void onPostExecute(String s) {
		if (mProgress.isShowing()) {
			if (s.length() > 10) {
				JSONObject user;
				try {
					user = new JSONObject(s);
					User.initUser(user, mContext);
					FileAccess.WriteSettings(mEmail + ";" + mPwd + ";"
							+ mPseudo + ";" + String.valueOf(user.getInt("id"))
							+ ";", (Context) mAct, FileAccess.AUTHENT);
				} catch (JSONException e) {
					Log.e("authentication json error", s);
					e.printStackTrace();
					Toast.makeText(mContext,
							mContext.getText(R.string.authent_error),
							Toast.LENGTH_SHORT).show();
				}
				mAct.authentAction(true, mKeyCode);
				mAlert.cancel();
			} else if (s.contains("bad email")) {
				Toast.makeText(mContext, mContext.getText(R.string.authent_used_email),
						Toast.LENGTH_SHORT).show();
			} else {
				Log.e("authentication unknown tag", s);
				Toast.makeText(mContext,
						mContext.getText(R.string.authent_error),
						Toast.LENGTH_SHORT).show();
			}
			mProgress.dismiss();
		}
	}
}
