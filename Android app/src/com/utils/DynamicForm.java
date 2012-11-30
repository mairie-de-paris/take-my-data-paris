package com.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.containers.Spec;
import com.containers.TypeSpec;

public class DynamicForm {

	private Context mContext;
	private ArrayList<Spec> mSpecs;

	public DynamicForm(Context context, ArrayList<Spec> specs) {
		mContext = context;
		mSpecs = specs;
	}

	/**
	 * Create a custom form from the spec list
	 * 
	 * @param specs
	 *            : the list of specifications
	 * @param isModifiable
	 *            : true if the form must be modifiable (for editing).
	 * @return
	 */
	public LinearLayout generateForm(boolean isModifiable) {
		LinearLayout principal = new LinearLayout(mContext);
		principal.setOrientation(LinearLayout.VERTICAL);

		LinearLayout.LayoutParams params_content = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params_content.setMargins(60, 10, 10, 10);

		int i = 0;
		for (Spec spec : mSpecs) {
			TypeSpec type = spec.getType();
			final int j = i;
			TextView tv = new TextView(mContext);
			tv.setText(spec.getType().getDescription());
			tv.setTextSize(21);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			params.setMargins(50, 15, 10, 10);
			tv.setLayoutParams(params);
			principal.addView(tv);

			if (type.getFormat().equals(Spec.TYPE_NOTE)) {
				RatingBar rb = new RatingBar(mContext);
				rb.setMax(spec.getNoteMax());
				rb.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT));
				rb.setNumStars(spec.getNoteMax() - spec.getNoteMin());
				rb.setProgress(spec.getNotMoy());
				rb.setEnabled(isModifiable);
				rb.setLayoutParams(params_content);
				principal.addView(rb);
				rb.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						mSpecs.get(j).setNotMoy(ratingBar.getProgress());
					}
				});
			} else if (type.getFormat().equals(Spec.TYPE_CHECK)) {
				ArrayList<String> list = spec.getCheckOK();

				for (final String s : list) {
					CheckBox cb = new CheckBox(mContext);
					cb.setText(s);
					cb.setChecked(true);
					cb.setTextSize(20);
					cb.setEnabled(isModifiable);
					cb.setLayoutParams(params_content);
					principal.addView(cb);
					cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							mSpecs.get(j).setCheck(s, isChecked);
						}
					});
				}
				list = spec.getCheckKO();
				for (final String s : list) {
					CheckBox cb = new CheckBox(mContext);
					cb.setText(s);
					cb.setTextSize(20);
					cb.setChecked(false);
					cb.setEnabled(isModifiable);
					cb.setLayoutParams(params_content);
					principal.addView(cb);
					cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							mSpecs.get(j).setCheck(s, isChecked);
						}
					});
				}
			} else if (type.getFormat().equals(Spec.TYPE_RADIO)) {
				String selected = spec.getRadioVal();
				if (isModifiable) {
					RadioGroup radG = new RadioGroup(mContext);
					ArrayList<String> list = spec.getRadioList();
					for (final String value : list) {
						RadioButton radB = new RadioButton(mContext);
						radB.setText(value);
						radB.setChecked(false);
						radB.setEnabled(true);
						radB.setTextSize(18);
						radG.addView(radB);
						radB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
								if (isChecked) {
									mSpecs.get(j).setRadioVal(value);
								}
							}
						});
					}
					radG.setLayoutParams(params_content);
					principal.addView(radG);
				} else {
					TextView tv_selected = new TextView(mContext);
					tv_selected.setText(selected);
					tv_selected.setLayoutParams(params_content);
					principal.addView(tv_selected);
				}

			} else if (type.getFormat().equals(Spec.TYPE_FREE)) {
				final String value = spec.getFreeValue();
				if (isModifiable) {
					EditText free = new EditText(mContext);
					free.setText("");
					free.setLayoutParams(params_content);
					free.setMaxLines(1);

					free.addTextChangedListener(new TextWatcher() {
						public void afterTextChanged(Editable s) {
							mSpecs.get(j).setFreeValue(s.toString());
						}
						public void beforeTextChanged(CharSequence s, int start, int count,
								int after) {
						}
						public void onTextChanged(CharSequence s, int start, int before, int count) {
						}
					});


					principal.addView(free);
				} else {
					TextView tv_free = new TextView(mContext);
					tv_free.setText(value);
					tv_free.setLayoutParams(params_content);
					principal.addView(tv_free);
				}
			}
			i++;
		}
		return principal;
	}

	public JSONArray generateSpecs() {
		JSONArray list = new JSONArray();
		for (Spec s : mSpecs) {
			JSONObject ob = new JSONObject();
			try {
				ob.put("id_spec", s.getType().getId());
				ob.put("metadata", s.getMetadata());
				list.put(ob);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		Log.i("generate Specs : ", list.toString());
		return list;
	}

}
