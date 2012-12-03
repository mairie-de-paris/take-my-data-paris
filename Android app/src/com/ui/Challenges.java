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
import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.containers.Challenge;
import com.containers.TypePoint;
import com.containers.User;
import com.ui.takemydata.R;
import com.utils.DownloadImage;

public class Challenges extends TmdUi {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportActionBar().setTitle(R.string.challenges_name);
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
		setContentView(R.layout.challenges);
		initViews();
	}

	private void initViews() {

		final ArrayList<Challenge> challenges = User.getChallenges();
		if (challenges == null) {
			setContentView(R.layout.no_connection);
			return;
		}

		ListView list = (ListView) findViewById(R.id.list);
		list.setAdapter(new ChallengesAdapter(this, 0, challenges));
		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				AlertDialog alertDialog = new AlertDialog.Builder(
						Challenges.this).create();
				alertDialog.setTitle(challenges.get(position).getName());
				TypePoint t = TypePoint.getTypeFromId(challenges.get(position)
						.getIdObj(), Challenges.this);
				alertDialog.setIcon(new BitmapDrawable(Challenges.this
						.getResources(), t.getImgBitmap()));
				alertDialog.setMessage(challenges.get(position)
						.getDescription());

				alertDialog.show();
			}
		});
	}

	public class ChallengesAdapter extends ArrayAdapter<Challenge> {

		private ArrayList<Challenge> list;

		public ChallengesAdapter(Context context, int textViewResourceId,
				ArrayList<Challenge> challenges) {
			super(context, textViewResourceId, challenges);
			list = challenges;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = li.inflate(R.layout.challenges_include, null);

				holder = new ViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.pts = (TextView) convertView.findViewById(R.id.pts);
				holder.tps = (TextView) convertView.findViewById(R.id.tps);
				holder.completion = (TextView) convertView.findViewById(R.id.completion);
				holder.icon = (ImageView) convertView.findViewById(R.id.icon);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Challenge c = list.get(position);
			holder.name.setText(c.getName());
			holder.completion.setText(String.valueOf(c.getCompletion()) + "/"
					+ String.valueOf(c.getNbObj()));

			holder.pts.setText(String.valueOf(c.getPoints()) + "pts");
			holder.position = position;
			TypePoint type = TypePoint.getTypeFromId(c.getIdObj(),
					Challenges.this);
			holder.img_url = type.getUrlImg();
			holder.img_name = type.getName();

			StringBuilder tps = new StringBuilder();
			int diff = c.getTimeEnd()
					- (int) (Calendar.getInstance().getTime().getTime() / 1000);
			if (diff > 0) {
				int h = diff / 3600;
				int m = (diff % 3600) / 60;
				if (h > 0) {
					tps.append(h).append("h");
				}
				tps.append(m).append("m");
			} else {
				if (c.getCompletion() >= c.getNbObj()) {
					tps.append(getText(R.string.success));
				} else {
					tps.append(getText(R.string.faillure));
				}
			}
			holder.tps.setText(tps.toString());
			new ThumbnailTask(position, holder).execute();

			return convertView;
		}

	}

	private class ViewHolder {
		public TextView name;
		public TextView pts;
		public TextView tps;
		public TextView completion;
		public ImageView icon;
		public String img_name;
		public int position;
		public String img_url;
	}

	private class ThumbnailTask extends AsyncTask<Integer, Void, Bitmap> {
		private int mPosition;
		private ViewHolder mHolder;

		public ThumbnailTask(int position, ViewHolder holder) {
			mPosition = position;
			mHolder = holder;
		}

		@Override
		protected Bitmap doInBackground(Integer... params) {
			Bitmap b = DownloadImage.getBitmap(mHolder.img_url,
					mHolder.img_name, true, Challenges.this, false, true);
			return b;
		}

		@Override
		public void onPostExecute(Bitmap bitmap) {
			if (mHolder.position == mPosition) {
				mHolder.icon.setImageBitmap(bitmap);
			}
		}
	}
}
