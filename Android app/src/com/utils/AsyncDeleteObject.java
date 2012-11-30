package com.utils;

import android.util.Log;

/**
 * Delete an object from the server Params : obj_id -> id of the object : name
 * -> username of the user
 * 
 * @author Morgan
 * 
 */
public class AsyncDeleteObject extends MyAsyncTask {

	public AsyncDeleteObject() {
		super("delete.php");
	}

	@Override
	public void onPostExecute(String s) {
		Log.i("Deleting object Done", "rtr = " + s);
	}
}
