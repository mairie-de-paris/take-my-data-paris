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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class Grade {

	private String mName;
	private int mPoints;
	private int mPosition;

	private static ArrayList<Grade> list = null;
	private static ArrayList<Grade> listBest = null;

	public Grade(JSONObject grade) throws JSONException {
		this.mName = grade.getString("name");
		this.mPoints = grade.getInt("nb_points");
		this.mPosition = grade.getInt("grade");
	}

	public static void initGrades(JSONObject grade, Context context)
			throws JSONException {
		list = new ArrayList<Grade>();
		listBest = new ArrayList<Grade>();

		JSONArray jsonBest = grade.getJSONArray("best");
		int size = jsonBest.length();
		for (int i = 0; i < size; i++) {
			listBest.add(new Grade(jsonBest.getJSONObject(i)));
		}

		JSONArray jsonCurrents = grade.getJSONArray("current");
		size = jsonCurrents.length();

		for (int i = 0; i < size; i++) {
			Grade g = new Grade(jsonCurrents.getJSONObject(i));
			list.add(g);
		}

	}

	public static ArrayList<Grade> getGrades() {
		return (list);
	}

	public static ArrayList<Grade> getBestGrades() {
		return (listBest);
	}

	public String getName() {
		return mName;
	}

	public int getPoints() {
		return mPoints;
	}

	public int getPosition() {
		return mPosition;
	}

}
