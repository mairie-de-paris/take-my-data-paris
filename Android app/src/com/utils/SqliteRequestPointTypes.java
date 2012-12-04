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

import com.containers.TypePoint;

public class SqliteRequestPointTypes extends SqliteRequest {

	public SqliteRequestPointTypes(Context context) {
		super(context, DataBase.TABLE_POINT_TYPES);
	}

	public void insertRow(TypePoint t) {
		ContentValues values = new ContentValues();
		values.put(DataBase.TABLE_POINT_TYPE_ID, t.getId());
		values.put(DataBase.TABLE_POINT_TYPE_NAME, t.getName());
		values.put(DataBase.TABLE_POINT_TYPE_DESCRIPTION, t.getDescription());
		values.put(DataBase.TABLE_POINT_TYPE_IMG, t.getUrlImg());
		values.put(DataBase.TABLE_POINT_TYPE_IMG_PICTO, t.getUrlPicto());

		this.insert(values);
	}

	public TypePoint getTypeFromId(int id) {
		TypePoint type = null;
		String[] cols = new String[] { DataBase.TABLE_POINT_TYPE_ID,
				DataBase.TABLE_POINT_TYPE_DESCRIPTION,
				DataBase.TABLE_POINT_TYPE_IMG,
				DataBase.TABLE_POINT_TYPE_IMG_PICTO,
				DataBase.TABLE_POINT_TYPE_NAME };

		Cursor c = this.query(cols, DataBase.TABLE_POINT_TYPE_ID + " = \"" + id
				+ "\"");
		c.moveToFirst();
		if (c.getCount() == 1) {
			type = new TypePoint(c);
		}

		c.close();
		return (type);
	}

	public ArrayList<TypePoint> getAllTypes() {
		ArrayList<TypePoint> types = new ArrayList<TypePoint>();
		String[] cols = new String[] { DataBase.TABLE_POINT_TYPE_ID,
				DataBase.TABLE_POINT_TYPE_DESCRIPTION,
				DataBase.TABLE_POINT_TYPE_IMG,
				DataBase.TABLE_POINT_TYPE_IMG_PICTO,
				DataBase.TABLE_POINT_TYPE_NAME };

		Cursor c = this
				.query(cols, DataBase.TABLE_POINT_TYPE_IMG + " != \"\" ");
		c.moveToFirst();
		int len = c.getCount();
		int i = 0;
		while (i < len) {
			types.add(new TypePoint(c));
			c.moveToNext();
			i++;
		}

		c.close();
		return (types);
	}

}