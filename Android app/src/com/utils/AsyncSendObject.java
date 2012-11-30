package com.utils;

import android.util.Log;

/**
 * Send an object to the server Params : obj -> JSONObject serialiazed
 * containing the object : id -> id of the user
 * 
 * @author Morgan
 * 
 */
public class AsyncSendObject extends MyAsyncTask {

	public						AsyncSendObject() {
		super("ws/add_point.php");
	}

	@Override
	public void onPostExecute(String s) {
		Log.i("Sending object Done", "rtr = " + s);
	}
}
