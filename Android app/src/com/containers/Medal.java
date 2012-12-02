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

package com.containers;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.utils.DataBase;

public class Medal {

	private int mId;
	private String mDescription;
	private int mIdObj;
	private int mNbObj;
	private int mPoints;
	private int mCompletion; // number of points the player already has
	private String mName;
	private String mUrlImg;

	public Medal(JSONObject type, Context context) throws JSONException {
		mId = type.getInt("id");
		mDescription = type.getString("description");
		mUrlImg = type.getString("url_picto");
		mIdObj = type.getInt("id_obj");
		mNbObj = type.getInt("nb");
		mPoints = type.getInt("points");
		mName = type.getString("name");
	}

	public Medal(Cursor c) {
		mUrlImg = c.getString(c.getColumnIndex(DataBase.TABLE_MEDALS_IMG_URL));
		mId = c.getInt(c.getColumnIndex(DataBase.TABLE_MEDALS_ID));
		mDescription = c.getString(c
				.getColumnIndex(DataBase.TABLE_MEDALS_DESCRIPTION));
		mIdObj = c.getInt(c.getColumnIndex(DataBase.TABLE_MEDALS_ID_OBJ));
		mNbObj = c.getInt(c.getColumnIndex(DataBase.TABLE_MEDALS_NB_OBJ));
		mPoints = c.getInt(c.getColumnIndex(DataBase.TABLE_MEDALS_POINTS));
		mName = c.getString(c.getColumnIndex(DataBase.TABLE_MEDALS_NAME));
		mCompletion = 0;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder().append(" MEDAL {")
				.append("description=").append(this.mDescription)
				.append(" id=").append(this.mId).append(" name=")
				.append(this.mName).append(" url img=").append(this.mUrlImg)
				.append(" id obj=").append(this.mIdObj).append(" nb obj=")
				.append(this.mNbObj).append("}");

		return sb.toString();
	}

	public int getId() {
		return mId;
	}

	public String getDescription() {
		return mDescription;
	}

	public int getIdObj() {
		Log.i("id objet", String.valueOf(mIdObj));
		return mIdObj;
	}

	public int getNbObj() {
		return mNbObj;
	}

	public String getUrlImg() {
		return mUrlImg;
	}

	public int getPoints() {
		return mPoints;
	}

	public int getCompletion() {
		return mCompletion;
	}

	public String getName() {
		return mName;
	}

	public void setCompletion(int comp) {
		mCompletion = comp;
	}

}
