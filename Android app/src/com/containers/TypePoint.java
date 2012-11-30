package com.containers;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;

import com.utils.DataBase;
import com.utils.DownloadImage;
import com.utils.Pair;
import com.utils.SqliteRequestPointTypes;

public class TypePoint {
	private int mId;
	private String mName;
	private String mDescription;
	private String mUrlImg;
	private String mUrlPicto;
	private static ArrayList<Pair> listPicto = new ArrayList<Pair>();
	private static ArrayList<Pair> listImg = new ArrayList<Pair>();
	private boolean mToDelete;

	/**
	 * Constructor used when adding a new type to the database
	 * 
	 * @param point
	 * @throws JSONException
	 */
	public TypePoint(JSONObject type, Context context) throws JSONException {
		mName = type.getString("name");
		mId = type.getInt("id");
		mDescription = type.getString("description");
		mUrlImg = type.getString("url_img");
		mUrlPicto = type.getString("url_picto");
		listPicto.add(new Pair(mName, DownloadImage.getBitmap(mUrlPicto, mName
				+ "_picto", true, context, false, true)));
		listImg.add(new Pair(mName, DownloadImage.getBitmap(mUrlImg, mName
				+ "_img", true, context, false, true)));
		mToDelete = type.getBoolean("delete");
	}

	public Bitmap getPictoBitmap() {
		for (Pair p : listPicto) {
			if (p.getName().equals(mName)) {
				return p.getBitmapValue();
			}
		}
		return null;
	}

	public boolean mustDelete() {
		return mToDelete;
	}

	public Bitmap getImgBitmap() {
		for (Pair p : listImg) {
			if (p.getName().equals(mName)) {
				return p.getBitmapValue();
			}
		}
		return null;
	}

	public TypePoint(Cursor c) {
		mName = c.getString(c.getColumnIndex(DataBase.TABLE_POINT_TYPE_NAME));
		mId = c.getInt(c.getColumnIndex(DataBase.TABLE_POINT_TYPE_ID));
		mDescription = c.getString(c
				.getColumnIndex(DataBase.TABLE_POINT_TYPE_DESCRIPTION));
		mUrlImg = c.getString(c.getColumnIndex(DataBase.TABLE_POINT_TYPE_IMG));
		mUrlPicto = c.getString(c
				.getColumnIndex(DataBase.TABLE_POINT_TYPE_IMG_PICTO));
	}

	public TypePoint(int id) {
		mId = id;
	}

	/**
	 * retrieve a type from the database
	 * 
	 * @param type
	 */
	public static TypePoint getTypeFromId(int id, Context context) {
		SqliteRequestPointTypes bdd = new SqliteRequestPointTypes(context);
		TypePoint type = bdd.getTypeFromId(id);
		bdd.close();
		return type;
	}

	public static ArrayList<TypePoint> getAllPointTypes(Context context) {
		SqliteRequestPointTypes bdd = new SqliteRequestPointTypes(context);
		ArrayList<TypePoint> types = bdd.getAllTypes();
		
		// deleting the empty types
		for (int i = 0 ; i < types.size() ; i++) {
			TypePoint type = types.get(i);
			com.containers.Point p = com.containers.Point.getStandardPointFromType(type.getId(), context);
			if (p == null) {
				types.remove(i);
				if (i > 0)
					i--;
			}
		}
		bdd.close();
		return types;
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

	public String getUrlImg() {
		return mUrlImg;
	}

	public String getUrlPicto() {
		return mUrlPicto;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder().append(" TYPE {")
				.append("description=").append(this.mDescription)
				.append(" id=").append(this.mId).append(" name=")
				.append(this.mName).append(" urlImg=").append(this.mUrlImg)
				.append(" urlPicto=").append(this.mUrlPicto).append("}");

		return sb.toString();
	}

}
