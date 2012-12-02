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
