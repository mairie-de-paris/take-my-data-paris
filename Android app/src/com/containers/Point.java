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

package com.containers;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;

import com.utils.DataBase;
import com.utils.SqliteRequestPoints;

public class Point {
	
	private ArrayList<Spec> mList;
	private String mStringList;
	private int mTimestamp;
	private int mId;
	private TypePoint mType;
	private int mLattitude;
	private int mLongitude;
	private String mImgUrl;

	/**
	 * Constructor used when adding a new point to the database
	 * 
	 * @param point
	 * @throws JSONException
	 */
	public Point(JSONObject point) throws JSONException {
		mStringList = point.getString("spec");
		mTimestamp = point.getInt("timestamp");
		mId = point.getInt("id");
		mType = new TypePoint(point.getInt("id_type"));
		mLattitude = (int) (point.getDouble("lat") * 1000000);
		mLongitude = (int) (point.getDouble("long") * 1000000);
		mImgUrl = point.getString("url_img");
	}

	/**
	 * Used to create a new point to send to the server
	 * 
	 * @param spec list of the specs
	 * @param id_type type
	 * @param lat lattitude
	 * @param lon longitude
	 * @return the points in a JSON format
	 */
	public static String getJsonPoint(JSONArray spec, int id_type, double lat,
			double lon) {
		JSONObject jo = new JSONObject();

		try {
			jo.put("spec", spec);
			jo.put("timestamp", (int) (Calendar.getInstance().getTime()
					.getTime() / 1000));
			jo.put("id", 0);
			jo.put("id_type", id_type);
			jo.put("lat", lat);
			jo.put("long", lon);
			return jo.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return (null);
	}

	/**
	 * Retrieve a point from the database
	 * 
	 * @param type
	 */
	public static ArrayList<Point> getPointsFromType(int type, Context context) {
		SqliteRequestPoints bdd = new SqliteRequestPoints(context);
		ArrayList<Point> points = bdd.getPointsFromType(type, context);
		bdd.close();
		return points;
	}

	public static Point getPointFromId(Integer id, Context context) {
		SqliteRequestPoints bdd = new SqliteRequestPoints(context);
		Point point = bdd.getPointFromId(id, context);
		bdd.close();
		return point;
	}

	public Point(Cursor c, Context context) throws JSONException {

		String specs = c.getString(c.getColumnIndex(DataBase.TABLE_POINT_SPECS));
		JSONArray JSONSpecList = new JSONArray(specs);
		int size = JSONSpecList.length();
		mList = new ArrayList<Spec>();
		for (int i = 0; i < size; i++) {
			mList.add(new Spec((JSONSpecList.getJSONObject(i)), context));
		}
		mTimestamp = c.getInt(c.getColumnIndex(DataBase.TABLE_POINT_TIMESTAMP));
		mId = c.getInt(c.getColumnIndex(DataBase.TABLE_POINT_ID));
		mType = TypePoint.getTypeFromId(
				c.getInt(c.getColumnIndex(DataBase.TABLE_POINT_TYPE)), context);
		mLattitude = c.getInt(c.getColumnIndex(DataBase.TABLE_POINT_LATTITUDE));
		mLongitude = c.getInt(c.getColumnIndex(DataBase.TABLE_POINT_LONGITUDE));
		mImgUrl = c.getString(c.getColumnIndex(DataBase.TABLE_POINT_URL_IMG));

	}

	public int getTimestamp() {
		return mTimestamp;
	}

	public int getId() {
		return mId;
	}

	public TypePoint getType() {
		return mType;
	}

	public int getLattitude() {
		return mLattitude;
	}

	public int getLongitude() {
		return mLongitude;
	}

	public ArrayList<Spec> getList() {
		return mList;
	}

	public String getStringList() {
		return mStringList;
	}

	public String getImgUrl() {
		return mImgUrl;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder().append(" POINT {").append("id=")
				.append(this.mId).append(" latt=").append(this.mLattitude)
				.append(" long=").append(this.mLongitude).append(" timestamp=")
				.append(this.mTimestamp).append(" type=")
				.append(this.getType()).append(" list=").append(this.getList())
				.append(" mImgUrl=").append(this.getImgUrl()).append("}");

		return sb.toString();
	}

	public static Point getStandardPointFromType(int type, Context context) {
		SqliteRequestPoints bdd = new SqliteRequestPoints(context);
		Point point = bdd.getStandardPointFromType(type, context);
		bdd.close();
		return point;
	}

}
