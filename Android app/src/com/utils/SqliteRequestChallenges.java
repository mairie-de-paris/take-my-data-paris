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