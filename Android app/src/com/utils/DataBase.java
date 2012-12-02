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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Definition of the database
 * 
 * @author Morgan
 * 
 */
public class DataBase extends SQLiteOpenHelper {

	public static final String TABLE_POINT = "POINTS";
	public static final String TABLE_POINT_ID = "id";
	public static final String TABLE_POINT_TIMESTAMP = "timestamp";
	public static final String TABLE_POINT_SPECS = "specs";
	public static final String TABLE_POINT_LATTITUDE = "lat";
	public static final String TABLE_POINT_TYPE = "type";
	public static final String TABLE_POINT_LONGITUDE = "long";
	public static final String TABLE_POINT_URL_IMG = "url_img";

	protected static final String CREATE_TABLE_POINT = "CREATE TABLE "
			+ TABLE_POINT + " ( " + TABLE_POINT_ID + " INTEGER PRIMARY KEY, "
			+ TABLE_POINT_SPECS + " TEXT, " + TABLE_POINT_URL_IMG + " TEXT, "
			+ TABLE_POINT_TYPE + " INTEGER, " + TABLE_POINT_LONGITUDE
			+ " INTEGER, " + TABLE_POINT_LATTITUDE + " INTEGER, "
			+ TABLE_POINT_TIMESTAMP + " INTEGER );";

	public static final String TABLE_POINT_TYPES = "POINT_TYPES";
	public static final String TABLE_POINT_TYPE_ID = "id";
	public static final String TABLE_POINT_TYPE_NAME = "name";
	public static final String TABLE_POINT_TYPE_IMG = "img_url";
	public static final String TABLE_POINT_TYPE_IMG_PICTO = "img_picto";
	public static final String TABLE_POINT_TYPE_DESCRIPTION = "description";

	protected static final String CREATE_TABLE_POINT_TYPES = "CREATE TABLE "
			+ TABLE_POINT_TYPES + " ( " + TABLE_POINT_TYPE_ID
			+ " INTEGER PRIMARY KEY, " + TABLE_POINT_TYPE_NAME + " TEXT, "
			+ TABLE_POINT_TYPE_DESCRIPTION + " TEXT, "
			+ TABLE_POINT_TYPE_IMG_PICTO + " TEXT, " + TABLE_POINT_TYPE_IMG
			+ " TEXT );";

	public static final String TABLE_SPEC_TYPES = "SPEC_TYPES";
	public static final String TABLE_SPEC_TYPE_ID = "id";
	public static final String TABLE_SPEC_TYPE_NAME = "name";
	public static final String TABLE_SPEC_TYPE_FORMAT = "format";
	public static final String TABLE_SPEC_TYPE_USER_DEFINED = "user_def";
	public static final String TABLE_SPEC_TYPE_DESCRIPTION = "description";
	public static final String TABLE_SPEC_TYPE_META = "meta";

	protected static final String CREATE_TABLE_SPEC_TYPES = "CREATE TABLE "
			+ TABLE_SPEC_TYPES + " ( " + TABLE_SPEC_TYPE_ID
			+ " INTEGER PRIMARY KEY, " + TABLE_SPEC_TYPE_NAME + " TEXT, "
			+ TABLE_SPEC_TYPE_DESCRIPTION + " TEXT, "
			+ TABLE_SPEC_TYPE_USER_DEFINED + " TEXT, " + TABLE_SPEC_TYPE_META
			+ " TEXT, " + TABLE_SPEC_TYPE_FORMAT + " TEXT );";

	public static final String TABLE_CHALLENGES = "CHALLENGES";
	public static final String TABLE_CHALLENGES_ID = "id";
	public static final String TABLE_CHALLENGES_DESCRIPTION = "description";
	public static final String TABLE_CHALLENGES_TIME_END = "time_end";
	public static final String TABLE_CHALLENGES_ID_OBJ = "id_obj";
	public static final String TABLE_CHALLENGES_NB_OBJ = "nb_obj";
	public static final String TABLE_CHALLENGES_POINTS = "points";
	public static final String TABLE_CHALLENGES_NAME = "name";

	protected static final String CREATE_TABLE_CHALLENGES = "CREATE TABLE "
			+ TABLE_CHALLENGES + " ( " + TABLE_CHALLENGES_ID
			+ " INTEGER PRIMARY KEY, " + TABLE_CHALLENGES_DESCRIPTION
			+ " TEXT, " + TABLE_CHALLENGES_ID_OBJ + " INTEGER, "
			+ TABLE_CHALLENGES_POINTS + " INTEGER, " + TABLE_CHALLENGES_NAME
			+ " TEXT, " + TABLE_CHALLENGES_TIME_END + " INTEGER, "
			+ TABLE_CHALLENGES_NB_OBJ + " INTEGER);";

	public static final String TABLE_MEDALS = "MEDALS";
	public static final String TABLE_MEDALS_ID = "id";
	public static final String TABLE_MEDALS_DESCRIPTION = "description";
	public static final String TABLE_MEDALS_IMG_URL = "img_url";
	public static final String TABLE_MEDALS_ID_OBJ = "id_obj";
	public static final String TABLE_MEDALS_NB_OBJ = "nb_obj";
	public static final String TABLE_MEDALS_POINTS = "points";
	public static final String TABLE_MEDALS_NAME = "name";

	protected static final String CREATE_TABLE_MEDALS = "CREATE TABLE "
			+ TABLE_MEDALS + " ( " + TABLE_MEDALS_ID + " INTEGER PRIMARY KEY, "
			+ TABLE_MEDALS_DESCRIPTION + " TEXT, " + TABLE_MEDALS_ID_OBJ
			+ " INTEGER, " + TABLE_MEDALS_POINTS + " INTEGER, "
			+ TABLE_MEDALS_NAME + " TEXT, " + TABLE_MEDALS_IMG_URL + " TEXT, "
			+ TABLE_MEDALS_NB_OBJ + " INTEGER);";

	public DataBase(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_POINT);
		db.execSQL(CREATE_TABLE_SPEC_TYPES);
		db.execSQL(CREATE_TABLE_POINT_TYPES);
		db.execSQL(CREATE_TABLE_CHALLENGES);
		db.execSQL(CREATE_TABLE_MEDALS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

}