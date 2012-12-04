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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SqliteRequest {

	protected static final String BDD_NAME = "database.db";

	private SQLiteDatabase mBdd = null;
	private DataBase mSQLiteBase = null;
	protected Context mContext;
	private String mTableName;

	public SqliteRequest(Context context, String table_name) {
		this.mContext = context;
		this.mSQLiteBase = new DataBase(this.mContext, BDD_NAME, null, 1);
		if (this.mSQLiteBase == null) {
			Log.e("error init sqlite request", table_name+" is null");
		}
		this.mTableName = table_name;
		this.open();
	}

	protected void finalize() {
		this.close();
	}

	public void open() {
		if (this.mSQLiteBase == null) {
			Log.e("error init sqlite request", "base is null");
		} else {
			this.mBdd = this.mSQLiteBase.getWritableDatabase();
		}
	}

	public void close() {
		if (this.mBdd != null) {
			this.mBdd.close();
			this.mBdd = null;
		}
	}

	public void del() {
		this.mBdd.delete(this.mTableName, null, null);
	}

	protected void insert(ContentValues values) {
		this.mBdd.insert(this.mTableName, null, values);
	}

	protected Cursor query(String[] nomObjet, String where) {
		if (this.mBdd == null) {
			this.open();
		}
		Cursor c = this.mBdd.query(this.mTableName, nomObjet, where, null,
				null, null, null);
		return (c);
	}

	protected void delete(String where) {
		this.mBdd.delete(this.mTableName, where, null);
	}
}