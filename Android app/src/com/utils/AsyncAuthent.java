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

/**
 * Authenticate the user
 * 
 * @params email
 * @params pwd
 * @author Morgan
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
					Log.i("JSON ERROR", s);
					Toast.makeText(
							(Context) mAct,
							"Une erreur est survenue, veuillez reessayer plus tard.",
							Toast.LENGTH_SHORT).show();
				}
				mAct.authentAction(true, mKeyCode);
				mAlert.cancel();
			} else if (s.contains("bad email")) {
				Toast.makeText((Context) mAct, "Cet email est deja utilise",
						Toast.LENGTH_SHORT).show();
			} else {
				Log.i("ERROR", s);
				Toast.makeText(
						(Context) mAct,
						"Une erreur est survenue, veuillez reessayer plus tard.",
						Toast.LENGTH_SHORT).show();
			}
			Log.i("dialog", "cancel");
			mProgress.dismiss();
		}
	}
}
