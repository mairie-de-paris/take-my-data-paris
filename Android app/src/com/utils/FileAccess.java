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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.util.Log;

/**
 * Store and retrive any kind of data from a file
 * 
 */
public class FileAccess {

	public static final String SPLASH_SCREEN = "SplashScreen.data";
	public static final String LATLONG = "LatLong.data";
	public static final String AUTHENT = "Authent.data";
	public static final String LAST_UPDATE = "LastUpdate.data";

	public static void WriteSettings(String data, Context context, String name) {
		FileOutputStream fOut = null;
		OutputStreamWriter osw = null;
		try {
			fOut = context.openFileOutput(name, Context.MODE_WORLD_WRITEABLE);
			osw = new OutputStreamWriter(fOut);
			osw.write(data.trim());
			osw.flush();
			osw.close();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public static String ReadSettings(Context context, String name) {
		FileInputStream fIn = null;
		InputStreamReader isr = null;
		try {
			fIn = context.openFileInput(name);
			isr = new InputStreamReader(fIn);
			char[] inputBuffer = new char[1000];

			int len = isr.read(inputBuffer);
			StringBuilder res = new StringBuilder();
			while (len != -1) {
				res.append(inputBuffer);
				len = isr.read(inputBuffer);
			}

			return (res.toString());
		} catch (Exception e) {
			return ("");
		}
	}

}
