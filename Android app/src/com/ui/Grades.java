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

package com.ui;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.containers.Grade;
import com.containers.User;
import com.ui.takemydata.R;

public class Grades extends TmdUi {

	private int mColorOthers;
	private int mColorMe;
	private String mUserName;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportActionBar().setTitle(R.string.grade_name);
		getSupportActionBar().setHomeButtonEnabled(true);
		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.general, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			this.finish();
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.general_stats);

		mColorOthers = getResources().getColor(R.color.white);
		mColorMe = getResources().getColor(R.color.moi);
		mUserName = User.getName(Grades.this);
		initViews();
	}

	private void initViews() {

		if (User.getGrade() == null) {
			setContentView(R.layout.no_connection);
			return;
		}

		ListView list = (ListView) findViewById(R.id.list);
		list.setAdapter(new GradeAdapter(this, 0, Grade.getGrades()));

		ListView listBest = (ListView) findViewById(R.id.listBest);
		listBest.setAdapter(new GradeAdapter(this, 0, Grade.getBestGrades()));
	}

	public class GradeAdapter extends ArrayAdapter<Grade> {

		private ArrayList<Grade> items;

		public GradeAdapter(Context context, int textViewResourceId,
				ArrayList<Grade> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = li.inflate(R.layout.general_stats_include, null);

				holder = new ViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.position = (TextView) convertView
						.findViewById(R.id.position);
				holder.points = (TextView) convertView
						.findViewById(R.id.points);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Grade g = items.get(position);
			holder.name.setText(g.getName());
			holder.position.setText("#" + String.valueOf(g.getPosition()));
			holder.points.setText(String.valueOf(g.getPoints()) + " pts");

			int color = mColorOthers;
			if (g.getName().equals(mUserName)) {
				color = mColorMe;
			}
			holder.name.setTextColor(color);
			holder.position.setTextColor(color);
			holder.points.setTextColor(color);

			return convertView;
		}

	}

	private class ViewHolder {
		public TextView name;
		public TextView points;
		public TextView position;
	}

}
