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

import org.json.JSONException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.containers.Point;

public class SqliteRequestPoints extends SqliteRequest {

	public SqliteRequestPoints(Context context) {
		super(context, DataBase.TABLE_POINT);
	}

	public void insertRow(Point point) {
		this.delPoint(point.getId());
		ContentValues values = new ContentValues();
		values.put(DataBase.TABLE_POINT_ID, point.getId());
		values.put(DataBase.TABLE_POINT_TIMESTAMP, point.getTimestamp());
		values.put(DataBase.TABLE_POINT_LATTITUDE, point.getLattitude());
		values.put(DataBase.TABLE_POINT_LONGITUDE, point.getLongitude());		
		values.put(DataBase.TABLE_POINT_TYPE, point.getType().getId());
		values.put(DataBase.TABLE_POINT_SPECS, point.getStringList());
		values.put(DataBase.TABLE_POINT_URL_IMG, point.getImgUrl());
		this.insert(values);
	}

	private void delPoint(int id) {

	}

	public ArrayList<Point> getPointsFromType(int type, Context context) {

		ArrayList<Point> points = new ArrayList<Point>();
		String[] cols = new String[] { DataBase.TABLE_POINT_ID,
				DataBase.TABLE_POINT_TIMESTAMP, DataBase.TABLE_POINT_LATTITUDE,
				DataBase.TABLE_POINT_LONGITUDE, DataBase.TABLE_POINT_TYPE,
				DataBase.TABLE_POINT_URL_IMG, DataBase.TABLE_POINT_SPECS };

		Cursor c = this.query(cols, DataBase.TABLE_POINT_TYPE + " = \"" + type
				+ "\"");
		c.moveToFirst();
		int len = c.getCount();
		int i = 0;
		while (i < len) {
			try {
				points.add(new Point(c, context));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			++i;
			c.moveToNext();
		}

		c.close();
		return (points);
	}

	public Point getPointFromId(Integer id, Context context) {
		Point point = null;
		String[] cols = new String[] { DataBase.TABLE_POINT_ID,
				DataBase.TABLE_POINT_TIMESTAMP, DataBase.TABLE_POINT_LATTITUDE,
				DataBase.TABLE_POINT_LONGITUDE, DataBase.TABLE_POINT_TYPE,
				DataBase.TABLE_POINT_URL_IMG, DataBase.TABLE_POINT_SPECS };

		Cursor c = this.query(cols, DataBase.TABLE_POINT_ID + " = \"" + id
				+ "\"");
		c.moveToFirst();
		if (c.getCount() == 1) {
			try {
				point = new Point(c, context);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		c.close();
		return (point);
	}

	public Point getStandardPointFromType(int type, Context context) {
		
		Point point = null;
		String[] cols = new String[] { DataBase.TABLE_POINT_ID,
				DataBase.TABLE_POINT_TIMESTAMP, DataBase.TABLE_POINT_LATTITUDE,
				DataBase.TABLE_POINT_LONGITUDE, DataBase.TABLE_POINT_TYPE,
				DataBase.TABLE_POINT_URL_IMG, DataBase.TABLE_POINT_SPECS };

		Cursor c = this.query(cols, DataBase.TABLE_POINT_TYPE + " = \"" + type
				+ "\"");
		c.moveToFirst();
		if (c.getCount() >= 1) {
			try {
				point = new Point(c, context);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		c.close();
		return (point);
	}

}