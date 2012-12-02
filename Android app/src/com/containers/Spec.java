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

package com.containers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

/**
 * Defines one specificity of an object. For instance, the size of a bench or
 * the accessibility of a toilet
 * 
 * @author Morgan
 * 
 */
public class Spec {

	public static final String TYPE_NOTE = "note";
	/*
	 * permet de donner une note : metadata (SpecType) : [valeur min, valeur
	 * max]
	 * 
	 * metadata (Spec) : val : valeur moyenne
	 */

	public static final String TYPE_RADIO = "radio";
	/*
	 * permet de choisir une possibilite parmis d'autre metadata (SpecType) :
	 * ["proposition 1", "proposition 2", "proposition n"]
	 * 
	 * metadata (Spec) : val : proposition qui revient le plus souvent
	 */

	public static final String TYPE_CHECK = "checkbox";
	/*
	 * permet de choisir entre metadata (SpecType) : values["proposition 1",
	 * "proposition 2", "proposition n"]
	 * 
	 * metadata (Spec) : ok : tableau coutenant les propositions validee en
	 * regle generale ko : tableau coutenant les propositions invalidee en regle
	 * generale
	 */

	public static final String TYPE_FREE = "free";
	/*
	 * permet de definir une entree de text metadata (SpecType) : val : la
	 * description de l'information
	 * 
	 * metadata (Spec) : val : le contenu de l'information
	 */

	private TypeSpec mType;
	private JSONObject mMetadata;
	private ArrayList<String> mListOK = null;
	private ArrayList<String> mListKO = null;

	/**
	 * Constructor used when getting a spec from the database
	 * 
	 * @param spec
	 * @throws JSONException
	 */
	public Spec(JSONObject spec, Context context) throws JSONException {
		mType = TypeSpec.getTypeFromId(spec.getInt("id_spec"), context);
		mMetadata = spec.getJSONObject("metadata");
	}

	public TypeSpec getType() {
		return mType;
	}

	public JSONObject getMetadata() {
		if (mListKO != null) {
			try {
				mMetadata.put("ok", new JSONArray(mListOK));
				mMetadata.put("ko", new JSONArray(mListKO));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return mMetadata;
	}

	public int getNoteMax() {
		try {
			JSONArray list = new JSONArray(mType.getMeta());
			return (list.getInt(1));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 10;
	}

	public int getNoteMin() {
		try {
			JSONArray list = new JSONArray(mType.getMeta());
			return (list.getInt(0));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public String getRadioVal() {
		try {
			return mMetadata.getString("val");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}	

	public void setRadioVal(String value) {
		try {
			mMetadata.put("val", value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public String getFreeValue() {
		try {
			return mMetadata.getString("val");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public void setFreeValue(String value) {
		try {
			mMetadata.put("val", value);
		} catch (JSONException e) {
			e.printStackTrace();
		}		
	}
	
	public ArrayList<String> getRadioList() {
		ArrayList<String> radios = new ArrayList<String>();
		try {
			JSONArray list = new JSONArray(mType.getMeta());
			int size = list.length();
			for (int i = 0; i < size; i++) {
				radios.add(list.getString(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return radios;
	}

	public ArrayList<String> getCheckOK() {
		if (mListOK != null) {
			return mListOK;
		}
		mListOK = new ArrayList<String>();
		try {
			JSONArray ok = mMetadata.getJSONArray("ok");
			int size = ok.length();
			for (int i = 0; i < size; i++) {
				mListOK.add(ok.getString(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mListOK;
	}

	public ArrayList<String> getCheckKO() {
		if (mListKO != null) {
			return mListKO;
		}
		mListKO = new ArrayList<String>();
		try {
			JSONArray ok = mMetadata.getJSONArray("ko");
			int size = ok.length();
			for (int i = 0; i < size; i++) {
				mListKO.add(ok.getString(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mListKO;
	}

	public void setCheck(String toStore, boolean isChecked) {
		if (isChecked && mListKO.contains(toStore)) {
			mListOK.add(toStore);
			mListKO.remove(toStore);
		} else if (!isChecked && mListOK.contains(toStore)) {
			mListKO.add(toStore);
			mListOK.remove(toStore);
		}
	}

	public int getNotMoy() {
		try {
			return (mMetadata.getInt("val"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 10;
	}

	public void setNotMoy(int progress) {
		try {
			mMetadata.put("val", progress);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder().append(" SPEC {")
				.append(" metadata=").append(this.mMetadata.toString())
				.append(" type=").append(this.mType).append("}");
		return sb.toString();
	}




}
