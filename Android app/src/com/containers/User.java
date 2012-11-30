package com.containers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.R;
import com.ui.AuthentInterface;
import com.utils.AsyncAuthent;
import com.utils.FileAccess;
import com.utils.Pair;
import com.utils.SqliteRequestChallenges;
import com.utils.SqliteRequestMedals;

public class User {

	private static ArrayList<Challenge> mChallenges = null;
	private static ArrayList<Medal> mMedals = null;
	private static String email = null;
	private static String pwd = null;
	private static String name = null;
	private static Grade grade = null;
	private static int id = 0;

	public static void initUser(JSONObject user, Context context) {
		JSONArray challenges = null;
		JSONArray medals = null;
		try {
			challenges = user.getJSONArray("challenges");
			medals = user.getJSONArray("medals");
			grade = new Grade(user.getJSONObject("grade"));
		} catch (Exception aie) {
			Log.e("User", "missing root node : " + aie.getMessage());
			return;
		}
		try {
			id = user.getInt("id");
		} catch (JSONException e) {
			Log.e("init user", "pas d'id");
			id = 1;
		}
		SqliteRequestChallenges bdd_c = new SqliteRequestChallenges(context);
		mChallenges = bdd_c.getAllTypes();
		try {
			int size = challenges.length();
			for (int i = 0; i < size; i++) {
				JSONObject ob = challenges.getJSONObject(i);
				int id_ob = ob.getInt("id");
				int completion = ob.getInt("completion");
				for (int j = id_ob - 1; j < mChallenges.size(); j++) {
					if (mChallenges.get(j).getId() == id_ob) {
						mChallenges.get(j).setCompletion(completion);
						break;
					}
				}
			}
		} catch (Exception tdc) {
			Log.e("User", "Error in challenges parsor");
		}

		SqliteRequestMedals bdd_m = new SqliteRequestMedals(context);
		mMedals = bdd_m.getAllTypes();

		try {
			int size = medals.length();
			for (int i = 0; i < size; i++) {
				JSONObject ob = medals.getJSONObject(i);
				int id_ob = ob.getInt("id");
				int completion = ob.getInt("completion");
				for (int j = id_ob - 1; j < mMedals.size(); j++) {
					if (mMedals.get(j).getId() == id_ob) {
						mMedals.get(j).setCompletion(completion);
						break;
					}
				}
			}
		} catch (Exception tdc) {
			Log.e("User", "Error in medals parsor");
		}

		bdd_c.close();
	}

	public static ArrayList<Challenge> getChallenges() {
		return mChallenges;
	}

	public static void connect(final AuthentInterface act, final int keyCode) {
		final Context context = (Context) act;
		String info = FileAccess.ReadSettings(context, FileAccess.AUTHENT);
		if (info.length() > 0) {
			act.authentAction(true, keyCode);
			return;
		}
		final AlertDialog alertDialog = new AlertDialog.Builder(context)
		.create();
		alertDialog.setTitle("Authentification");

		LayoutInflater li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View form = li.inflate(R.layout.authent, null);

		Button validate = (Button) form.findViewById(R.id.validate);
		Button back = (Button) form.findViewById(R.id.back);
		final EditText ePseudo = (EditText) form.findViewById(R.id.pseudo);
		final EditText eEmail = (EditText) form.findViewById(R.id.email);
		final EditText ePassword = (EditText) form.findViewById(R.id.passwd);

		validate.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String password = ePassword.getText().toString();
				String email = eEmail.getText().toString();
				String pseudo = ePseudo.getText().toString();
				if (!checkEmail(email)) {
					Toast.makeText(context, "Email invalide",
							Toast.LENGTH_SHORT).show();
				} else if (password.length() < 3) {
					Toast.makeText(context, "Mot de passe trop court",
							Toast.LENGTH_SHORT).show();
				} else if (pseudo.length() < 3) {
					Toast.makeText(context, "Mot de passe trop court",
							Toast.LENGTH_SHORT).show();
				} else {
					ProgressDialog progress = ProgressDialog.show(context,
							"Connexion", "Verification en cours ...");
					new AsyncAuthent(keyCode, progress, alertDialog, email,
							md5(password), pseudo, act, context).execute(
									new Pair("mail", email), new Pair("name", pseudo),
									new Pair("pwd", md5(password)));
				}
			}
		});

		back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				alertDialog.cancel();
			}
		});

		alertDialog.setOnCancelListener(new OnCancelListener() {

			public void onCancel(DialogInterface dialog) {
				act.authentAction(false, keyCode);
			}
		});

		alertDialog.setView(form);
		alertDialog.show();

	}

	public static String md5(String s) {
		try {
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++)
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return s;
	}

	public final static Pattern EMAIL_ADDRESS_PATTERN = Pattern
			.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
					+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
					+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

	private static boolean checkEmail(String email) {
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}

	public static String getEmail(Context context) {
		if (email == null) {
			initDataUser(context);
		}
		return email;
	}

	public static String getPwd(Context context) {
		if (pwd == null) {
			initDataUser(context);
		}
		return pwd;
	}

	public static String getName(Context context) {
		if (name == null) {
			initDataUser(context);
		}
		return name;
	}

	private static void initDataUser(Context context) {
		String info = FileAccess.ReadSettings(context, FileAccess.AUTHENT);
		if (!info.contains(";")) {
			Log.e("USER GET NAME", "No name available");
			email = "";
			pwd = "";
			name = "";
			return;
		}
		email = info.split(";")[0];
		pwd = info.split(";")[1];
		name = info.split(";")[2];
		id = Integer.valueOf(info.split(";")[3]);
	}

	public static int getId(Context context) {
		if (id == 0) {
			initDataUser(context);
		}
		return id;
	}

	public static Grade getGrade() {
		return grade;
	}

	public static ArrayList<Medal> getMedals() {
		return mMedals;
	}

}
