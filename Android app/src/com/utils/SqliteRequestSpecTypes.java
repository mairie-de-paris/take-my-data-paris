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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.containers.TypeSpec;

public class SqliteRequestSpecTypes extends SqliteRequest {

	public SqliteRequestSpecTypes(Context context) {
		super(context, DataBase.TABLE_SPEC_TYPES);
	}

	public void insertRow(TypeSpec t) {
		ContentValues values = new ContentValues();
		values.put(DataBase.TABLE_SPEC_TYPE_ID, t.getId());
		values.put(DataBase.TABLE_SPEC_TYPE_NAME, t.getName());
		values.put(DataBase.TABLE_SPEC_TYPE_DESCRIPTION, t.getDescription());
		values.put(DataBase.TABLE_SPEC_TYPE_FORMAT, t.getFormat());
		values.put(DataBase.TABLE_SPEC_TYPE_META, t.getMeta());
		values.put(DataBase.TABLE_SPEC_TYPE_USER_DEFINED, t.isUserDefined());

		this.insert(values);
	}

	public TypeSpec getTypeFromId(int id) {
		TypeSpec type = null;
		String[] cols = new String[] { DataBase.TABLE_SPEC_TYPE_ID,
				DataBase.TABLE_SPEC_TYPE_NAME,
				DataBase.TABLE_SPEC_TYPE_DESCRIPTION,
				DataBase.TABLE_SPEC_TYPE_FORMAT, DataBase.TABLE_SPEC_TYPE_META,
				DataBase.TABLE_SPEC_TYPE_USER_DEFINED };

		Cursor c = this.query(cols, DataBase.TABLE_SPEC_TYPE_ID + " = \"" + id
				+ "\"");
		c.moveToFirst();
		if (c.getCount() == 1) {
			type = new TypeSpec(c);
		}

		c.close();
		return (type);
	}

	public ArrayList<TypeSpec> getAllTypes() {
		ArrayList<TypeSpec> types = new ArrayList<TypeSpec>();
		String[] cols = new String[] { DataBase.TABLE_SPEC_TYPE_ID,
				DataBase.TABLE_SPEC_TYPE_NAME,
				DataBase.TABLE_SPEC_TYPE_DESCRIPTION,
				DataBase.TABLE_SPEC_TYPE_FORMAT, DataBase.TABLE_SPEC_TYPE_META,
				DataBase.TABLE_SPEC_TYPE_USER_DEFINED };

		Cursor c = this.query(cols, null);
		c.moveToFirst();
		int len = c.getCount();
		int i = 0;
		while (i < len) {
			types.add(new TypeSpec(c));
			c.moveToNext();
			i++;
		}

		c.close();
		return (types);
	}

}