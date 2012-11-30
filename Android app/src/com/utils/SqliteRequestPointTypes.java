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