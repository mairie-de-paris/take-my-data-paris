/**
 * Copyright 2012-2013 Mairie de Paris
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class DownloadImage {

	private static String PATH = null;
	private static final String INTERNAL_PATH = "/tmd/";

	/**
	 * Download a bitmap from internet
	 * 
	 * @param url
	 *            : bitmap's url
	 * @param facultative_name
	 *            : the name used to save the bitmap on the phone, if no
	 *            facultative name is given, the bitmap name is defined from is
	 *            url
	 * @param mustStoreOnPhone
	 *            : true if the bitmap must be stored on phone.
	 * @param renew
	 *            : true if an existing bitmap with the same name must be
	 *            replaced on the phone
	 * @param scale
	 *            : true if the image must be scaled or not.
	 * @return : the bitmap if success or null if failed.
	 */
	public static Bitmap getBitmap(String url, String facultative_name,
			boolean mustStoreOnPhone, Context context, boolean renew,
			boolean scale) {
		String name = getName(url, facultative_name);
		if (name.equals("")) {
			return null;
		}
		if (!mustStoreOnPhone) {
			return (downloadBitmap(url, true, scale));
		}

		checkDirectoryExistance();
		Bitmap bitmap;
		if (!renew) {
			bitmap = checkExistance(name, context);
			if (bitmap != null) {
				return (bitmap);
			}
		}
		bitmap = downloadBitmap(url, true, scale);
		if (bitmap != null) {
			DownloadImage.stockBitmap(name, bitmap);
		} else {
			Log.e("download faillure", name);
		}
		return (bitmap);
	}

	public static Bitmap checkExistance(String name, Context context) {
		Bitmap bitmap = BitmapFactory.decodeFile(DownloadImage.PATH + name);
		if (bitmap == null) {
			try {
				Bitmap b = BitmapFactory.decodeStream(context
						.openFileInput(name));
				return (b);
			} catch (FileNotFoundException f) {
				File child = new File(DownloadImage.PATH + name);
				child.delete();
			}
		}
		return bitmap;
	}

	/**
	 * Generate the bitmap name for storing it from its url or his facultative
	 * name if not null
	 * 
	 * @param url : bitmap's url
	 * @param facultative_name : faculative name, null if not wanted
	 * @return the bitmap name or "" if an error occured
	 */
	private static String getName(String url, String facultative_name) {
		String name = "";
		if (facultative_name != null && !facultative_name.equals("")) {
			name = facultative_name;
		} else {
			if (url == null || !url.contains("/")) {
				return "";
			}
			name = url.substring(url.lastIndexOf("/") + 1);
			if (name.contains("?")) {
				name = name.split("\\?")[0];
			}
			name = name.split("\\.")[0];
		}
		return (name);
	}

	/**
	 * Check if the directory to store the bitmap exists or not
	 */
	private static void checkDirectoryExistance() {
		if (DownloadImage.PATH == null) {
			DownloadImage.PATH = Environment.getExternalStorageDirectory()
					.toString() + INTERNAL_PATH;
			File f0 = new File(DownloadImage.PATH);
			if (!(f0.exists() && f0.isDirectory())) {
				File directory = new File(DownloadImage.PATH);
				directory.mkdirs();
			}
		}
	}

	/**
	 * Download the picture
	 * 
	 * @param url : picture's url
	 * @param first : used for double trying download
	 * @return the bitmap downloaded
	 */
	private static Bitmap downloadBitmap(String url, boolean first,
			boolean scale) {
		try {
			URL urlImage = new URL(url);

			HttpURLConnection connection = (HttpURLConnection) urlImage
					.openConnection();
			InputStream inputStream = connection.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			if (scale)
				bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
			return (bitmap);
		} catch (Exception e) {
			e.printStackTrace();
			if (first) {
				return (DownloadImage.downloadBitmap(url, false, scale));
			}
		}
		return (null);
	}

	/**
	 * Store a bitmap on the sd
	 * 
	 * @param name : bitmap's name
	 * @param bitmap : bitmap to store
	 */
	private static void stockBitmap(String name, Bitmap bitmap) {
		try {
			FileOutputStream os;

			File file = new File(PATH, name);
			os = new FileOutputStream(file);
			bitmap.compress(CompressFormat.PNG, 100, os);
			os.flush();
			os.close();
		} catch (Exception e) {
			Log.e("DownloadImage error", "Sd card not available for storing : "
					+ name);
			e.printStackTrace();
		}
	}

}
