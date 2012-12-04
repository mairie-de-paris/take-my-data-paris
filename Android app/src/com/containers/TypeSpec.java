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

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;

import com.utils.DataBase;
import com.utils.SqliteRequestSpecTypes;

public class TypeSpec {
	private int mId;
	private String mName;
	private String mDescription;
	private String mFormat;
	private boolean mUserDefined;
	private String mMeta;
	private boolean mToDelete;

	/**
	 * Constructor used when adding a new spec type to the database
	 * 
	 * @throws JSONException
	 */
	public TypeSpec(JSONObject type, Context context) throws JSONException {
		mName = type.getString("name");
		mId = type.getInt("id");
		mDescription = type.getString("description");
		mFormat = type.getString("format");
		mUserDefined = type.getBoolean("user_defined");
		mMeta = type.getString("metadata");
		mToDelete = type.getBoolean("delete");
	}

	public TypeSpec(Cursor c) {
		mName = c.getString(c.getColumnIndex(DataBase.TABLE_SPEC_TYPE_NAME));
		mId = c.getInt(c.getColumnIndex(DataBase.TABLE_SPEC_TYPE_ID));
		mDescription = c.getString(c.getColumnIndex(DataBase.TABLE_SPEC_TYPE_DESCRIPTION));
		mFormat = c.getString(c.getColumnIndex(DataBase.TABLE_SPEC_TYPE_FORMAT));
		mUserDefined = c.getInt(c.getColumnIndex(DataBase.TABLE_SPEC_TYPE_USER_DEFINED)) == 1;
		mMeta = c.getString(c.getColumnIndex(DataBase.TABLE_SPEC_TYPE_META));
	}

	public boolean mustDelete() {
		return mToDelete;
	}

	public TypeSpec(int id) {
		mId = id;
	}

	/**
	 * retrieve a type from the database
	 * 
	 * @param type
	 */
	public static TypeSpec getTypeFromId(int id, Context context) {
		SqliteRequestSpecTypes bdd = new SqliteRequestSpecTypes(context);
		TypeSpec type = bdd.getTypeFromId(id);
		bdd.close();
		return type;
	}

	public int getId() {
		return mId;
	}

	public String getName() {
		return mName;
	}

	public String getDescription() {
		return mDescription;
	}

	public String getFormat() {
		return mFormat;
	}

	public boolean isUserDefined() {
		return mUserDefined;
	}

	public String getMeta() {
		return mMeta;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder().append(" TYPE SPEC{")
				.append("description=").append(this.mDescription)
				.append(" id=").append(this.mId).append(" name=")
				.append(this.mName).append("}");

		return sb.toString();
	}

}
