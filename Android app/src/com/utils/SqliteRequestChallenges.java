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

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.containers.Challenge;

public class SqliteRequestChallenges extends SqliteRequest {

	public SqliteRequestChallenges(Context context) {
		super(context, DataBase.TABLE_CHALLENGES);
	}

	public void insertRow(Challenge c) {
		ContentValues values = new ContentValues();
		values.put(DataBase.TABLE_CHALLENGES_ID, c.getId());
		values.put(DataBase.TABLE_CHALLENGES_DESCRIPTION, c.getDescription());
		values.put(DataBase.TABLE_CHALLENGES_ID_OBJ, c.getIdObj());
		values.put(DataBase.TABLE_CHALLENGES_NB_OBJ, c.getNbObj());
		values.put(DataBase.TABLE_CHALLENGES_POINTS, c.getPoints());
		values.put(DataBase.TABLE_CHALLENGES_NAME, c.getName());
		values.put(DataBase.TABLE_CHALLENGES_TIME_END, c.getTimeEnd());

		this.insert(values);
	}

	public Challenge getChallengeFromId(int id) {
		Challenge challenge = null;
		String[] cols = new String[] { DataBase.TABLE_CHALLENGES_ID,
				DataBase.TABLE_CHALLENGES_DESCRIPTION,
				DataBase.TABLE_CHALLENGES_ID_OBJ,
				DataBase.TABLE_CHALLENGES_NB_OBJ,
				DataBase.TABLE_CHALLENGES_POINTS,
				DataBase.TABLE_CHALLENGES_NAME,
				DataBase.TABLE_CHALLENGES_TIME_END };

		Cursor c = this.query(cols, DataBase.TABLE_CHALLENGES_ID + " = \"" + id
				+ "\"");
		c.moveToFirst();
		if (c.getCount() == 1) {
			challenge = new Challenge(c);
		}

		c.close();
		return (challenge);
	}

	public ArrayList<Challenge> getAllTypes() {
		ArrayList<Challenge> types = new ArrayList<Challenge>();
		String[] cols = new String[] { DataBase.TABLE_CHALLENGES_ID,
				DataBase.TABLE_CHALLENGES_DESCRIPTION,
				DataBase.TABLE_CHALLENGES_ID_OBJ,
				DataBase.TABLE_CHALLENGES_NB_OBJ,
				DataBase.TABLE_CHALLENGES_NAME,
				DataBase.TABLE_CHALLENGES_POINTS,
				DataBase.TABLE_CHALLENGES_TIME_END };

		Cursor c = this.query(cols, "1");
		c.moveToFirst();
		int len = c.getCount();
		int i = 0;
		while (i < len) {
			types.add(new Challenge(c));
			c.moveToNext();
			i++;
		}

		c.close();
		return (types);
	}

}