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

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.containers.Challenge;
import com.containers.Grade;
import com.containers.Medal;
import com.containers.Point;
import com.containers.TypePoint;
import com.containers.TypeSpec;
import com.containers.User;
import com.ui.Luncher;
import com.ui.takemydata.R;

/**
 * Retrieve every points created after the timestamp sent
 * 
 * @params id : the id of the user
 * @params timestamp : date of the last update
 * 
 */
public class AsyncGetNewObjects extends MyAsyncTask {

	public class AsyncStoreData extends AsyncTask<String, Integer, Integer> {

		private static final int CONNECTION_OK = 0;
		private static final int NO_DATA = 2;

		public AsyncStoreData() {
		}

		@Override
		protected Integer doInBackground(String... params) {
			String flx = params[0];

			SqliteRequestPoints bdd_p = new SqliteRequestPoints(mActivity);
			SqliteRequestPointTypes bdd_t = new SqliteRequestPointTypes(mActivity);
			SqliteRequestSpecTypes bdd_s = new SqliteRequestSpecTypes(mActivity);
			SqliteRequestChallenges bdd_c = new SqliteRequestChallenges(mActivity);
			SqliteRequestMedals bdd_m = new SqliteRequestMedals(mActivity);

			JSONObject data = null;
			try {
				data = new JSONObject(flx);
				JSONArray typePointList = data.getJSONArray("types_points");
				int size = typePointList.length();
				for (int i = 0; i < size; i++) {
					addPointType(typePointList.getJSONObject(i), bdd_t);
				}

				JSONArray typeSpecList = data.getJSONArray("types_spec");
				size = typeSpecList.length();
				for (int i = 0; i < size; i++) {
					addSpecType(typeSpecList.getJSONObject(i), bdd_s);
				}

				JSONArray pointList = data.getJSONArray("points");
				size = pointList.length();
				for (int i = 0; i < size; i++) {
					addPoint(pointList.getJSONObject(i), bdd_p);
				}

				JSONArray challengeList = data.getJSONArray("challenges");
				size = challengeList.length();
				for (int i = 0; i < size; i++) {
					addChallenge(challengeList.getJSONObject(i), bdd_c);
				}

				JSONArray medalList = data.getJSONArray("medals");
				size = medalList.length();
				for (int i = 0; i < size; i++) {
					addMedal(medalList.getJSONObject(i), bdd_m);
				}

			} catch (JSONException e) {
				e.printStackTrace();
				ArrayList<TypePoint> t = TypePoint.getAllPointTypes(mActivity);
				if (t.size() == 0) {
					return NO_DATA;
				}
				e.printStackTrace();
			} finally {
				bdd_p.close();
				bdd_t.close();
				bdd_s.close();
				bdd_c.close();
			}

			try {
				if (data != null) {
					try { User.initUser(data.getJSONObject("user"), mActivity); } catch (Exception nouser) { }
					// Grades must be initiated after the user
					Grade.initGrades(data.getJSONObject("grades"), mActivity);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			// Check the data content and parsing
			// checkDb();
			if (data != null) {
				return CONNECTION_OK;
			} else {
				return NO_DATA;
			}

		}

		private void addMedal(JSONObject medal, SqliteRequestMedals bdd) {
			try {
				Medal m = new Medal(medal, mActivity);
				bdd.delete(DataBase.TABLE_CHALLENGES_ID + " = \"" + m.getId()
						+ "\"");
				bdd.insertRow(m);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		private void addChallenge(JSONObject challenge,
				SqliteRequestChallenges bdd) {
			try {
				Challenge c = new Challenge(challenge, mActivity);
				bdd.delete(DataBase.TABLE_CHALLENGES_ID + " = \"" + c.getId()
						+ "\"");
				bdd.insertRow(c);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		private void addPointType(JSONObject type, SqliteRequestPointTypes bdd) {
			try {
				TypePoint t = new TypePoint(type, mActivity);
				bdd.delete(DataBase.TABLE_POINT_TYPE_ID + " = \"" + t.getId()
						+ "\"");
				if (!t.mustDelete())
					bdd.insertRow(t);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		private void addSpecType(JSONObject type, SqliteRequestSpecTypes bdd) {
			try {
				TypeSpec t = new TypeSpec(type, mActivity);
				bdd.delete(DataBase.TABLE_SPEC_TYPE_ID + " = \"" + t.getId()
						+ "\"");
				if (!t.mustDelete())
					bdd.insertRow(t);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		private void addPoint(JSONObject point, SqliteRequestPoints bdd) {
			try {
				Point p = new Point(point);
				bdd.delete(DataBase.TABLE_POINT_ID + " = \"" + p.getId() + "\"");
				if (!p.getType().mustDelete())
					bdd.insertRow(p);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@SuppressWarnings("unused")
		private void checkDb() {
			Log.i("Getting points", "checking db");
			SqliteRequestPoints bdd_p = new SqliteRequestPoints(mActivity);
			SqliteRequestPointTypes bdd_t = new SqliteRequestPointTypes(
					mActivity);
			SqliteRequestChallenges bdd_c = new SqliteRequestChallenges(
					mActivity);

			bdd_p.open();
			bdd_t.open();
			bdd_c.open();

			StringBuilder check = new StringBuilder();
			for (int i = 0; i < 10; i++) {
				check.append("point number = ").append(String.valueOf(i));
				ArrayList<Point> list = bdd_p.getPointsFromType(i, mActivity);
				for (Point p : list) {
					check.append(p.toString());
				}
			}

			bdd_p.close();
			bdd_t.close();
			bdd_c.close();

			if (User.getChallenges() != null) {
				for (Challenge c : User.getChallenges()) {
					check.append(c.toString());
				}
			}
			if (User.getMedals() != null) {
				for (Medal m : User.getMedals()) {
					check.append(m.toString());
				}
			}
			Log.i("check ", check.toString());
			BugSenseHandler.addCrashExtraData("check_db", check.toString());
			Log.i("Getting points", "end checking db");

		}

		@Override
		public void onPostExecute(Integer result) {
			if (result == CONNECTION_OK) {
				mActivity.lunch();
			} else if (result == NO_DATA) {
				Toast.makeText(mActivity,
						mActivity.getText(R.string.bad_internet_connexion),
						Toast.LENGTH_LONG).show();
				mActivity.finish();
			}
		}

	}

	private Luncher mActivity;
	private static final String URL = "ws/global.php";

	public AsyncGetNewObjects(Luncher ac) {
		super(URL);
		this.mActivity = ac;
	}

	@Override
	public void onPostExecute(String s) {
		BugSenseHandler.addCrashExtraData("server data received", s);
		new AsyncStoreData().execute(s);
	}

}
