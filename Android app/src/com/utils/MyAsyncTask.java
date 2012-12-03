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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Private class to handle sending and receiving of datas from the web
 * 
 */
public class MyAsyncTask extends AsyncTask<Pair, Integer, String> {

	public static final String SERVER_URL = "xxx";
	private String mUrl;

	public MyAsyncTask(String url) {
		this.mUrl = SERVER_URL + url;
	}

	@Override
	protected String doInBackground(Pair... params) {

		String s = this.downloadContent(params);
		return (s);
	}

	private String downloadContent(Pair... params) {
		int i = 0;
		int len = params.length;

		HttpPost httppost = new HttpPost(this.mUrl);
		List<NameValuePair> variables = new ArrayList<NameValuePair>();
		Log.d("NAME", "VALUE");
		while (i < len) {
			Log.d(params[i].getName(), params[i].getValue());
			variables.add(params[i]);
			++i;
		}
		try {
			httppost.setEntity(new UrlEncodedFormEntity(variables,
					"iso-8859-15"));
			HttpClient httpclient = new DefaultHttpClient();
			StringBuilder stringBuffer = new StringBuilder();
			HttpResponse response = httpclient.execute(httppost);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()), 8 * 1024);
			String ligne;
			while ((ligne = reader.readLine()) != null) {
				stringBuffer.append(ligne);
			}
			return stringBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ("");
	}

	@Override
	protected void onPostExecute(String s) {

	}

}
