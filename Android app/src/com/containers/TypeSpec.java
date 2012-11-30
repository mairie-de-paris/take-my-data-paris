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
		mDescription = c.getString(c
				.getColumnIndex(DataBase.TABLE_SPEC_TYPE_DESCRIPTION));
		mFormat = c
				.getString(c.getColumnIndex(DataBase.TABLE_SPEC_TYPE_FORMAT));
		mUserDefined = c.getInt(c
				.getColumnIndex(DataBase.TABLE_SPEC_TYPE_USER_DEFINED)) == 1;
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
