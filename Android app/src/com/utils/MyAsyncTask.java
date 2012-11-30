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
 * Private class to handle the sending and receiving of datas from the web
 * 
 * @author Morgan
 * 
 */
public class MyAsyncTask extends AsyncTask<Pair, Integer, String> {

	public static final String SERVER_URL = "http://88.191.143.205";
	private String mUrl;

	public MyAsyncTask(String url) {
		this.mUrl = SERVER_URL + "/~tmd/" + url;
		Log.i("url = ", this.mUrl);
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
