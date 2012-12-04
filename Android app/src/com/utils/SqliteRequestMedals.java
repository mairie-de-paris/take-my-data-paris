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

import com.containers.Medal;

public class SqliteRequestMedals extends SqliteRequest {

	public SqliteRequestMedals(Context context) {
		super(context, DataBase.TABLE_MEDALS);
	}

	public void insertRow(Medal m) {
		ContentValues values = new ContentValues();
		values.put(DataBase.TABLE_MEDALS_ID, m.getId());
		values.put(DataBase.TABLE_MEDALS_DESCRIPTION, m.getDescription());
		values.put(DataBase.TABLE_MEDALS_ID_OBJ, m.getIdObj());
		values.put(DataBase.TABLE_MEDALS_NB_OBJ, m.getNbObj());
		values.put(DataBase.TABLE_MEDALS_POINTS, m.getPoints());
		values.put(DataBase.TABLE_MEDALS_NAME, m.getName());
		values.put(DataBase.TABLE_MEDALS_IMG_URL, m.getUrlImg());

		this.insert(values);
	}

	public Medal getMedalFromId(int id) {
		Medal Medal = null;
		String[] cols = new String[] { DataBase.TABLE_MEDALS_ID,
				DataBase.TABLE_MEDALS_DESCRIPTION,
				DataBase.TABLE_MEDALS_ID_OBJ, DataBase.TABLE_MEDALS_NB_OBJ,
				DataBase.TABLE_MEDALS_POINTS, DataBase.TABLE_MEDALS_NAME,
				DataBase.TABLE_MEDALS_IMG_URL };

		Cursor c = this.query(cols, DataBase.TABLE_MEDALS_ID + " = \"" + id
				+ "\"");
		c.moveToFirst();
		if (c.getCount() == 1) {
			Medal = new Medal(c);
		}

		c.close();
		return (Medal);
	}

	public ArrayList<Medal> getAllTypes() {
		ArrayList<Medal> types = new ArrayList<Medal>();
		String[] cols = new String[] { DataBase.TABLE_MEDALS_ID,
				DataBase.TABLE_MEDALS_DESCRIPTION,
				DataBase.TABLE_MEDALS_ID_OBJ, DataBase.TABLE_MEDALS_NB_OBJ,
				DataBase.TABLE_MEDALS_NAME, DataBase.TABLE_MEDALS_POINTS,
				DataBase.TABLE_MEDALS_IMG_URL };

		Cursor c = this.query(cols, "1");
		c.moveToFirst();
		int len = c.getCount();
		int i = 0;
		while (i < len) {
			types.add(new Medal(c));
			c.moveToNext();
			i++;
		}

		c.close();
		return (types);
	}

}