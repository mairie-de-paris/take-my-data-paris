package com.ui;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.containers.Medal;
import com.containers.User;
import com.ui.takemydata.R;
import com.utils.DownloadImage;

public class Decorations extends TmdUi {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportActionBar().setTitle(R.string.decorations_name);
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
		setContentView(R.layout.stats);

		initViews();
	}

	private void initViews() {

		final ArrayList<Medal> medals = User.getMedals();
		if (medals == null) {
			setContentView(R.layout.no_connection);
			return;
		}
		final ListView list = (ListView) findViewById(R.id.list);
		list.setAdapter(new StatsAdapter(this, 0, medals));
		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				Medal medal = medals.get(position);
				AlertDialog alertDialog = new AlertDialog.Builder(
						Decorations.this).create();
				StringBuilder sb = new StringBuilder();
				sb.append(medal.getName()).append(" (")
				.append(medal.getCompletion()).append("/")
				.append(medal.getNbObj()).append(")");
				alertDialog.setTitle(sb.toString());
				alertDialog.setMessage(medal.getDescription());
				alertDialog.show();
			}
		});
	}

	private class StatsAdapter extends ArrayAdapter<Medal> {

		private ArrayList<Medal> items;

		public StatsAdapter(Context context, int textViewResourceId,
				ArrayList<Medal> medals) {
			super(context, textViewResourceId, medals);
			this.items = medals;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = li.inflate(R.layout.stats_points, null);

				holder = new ViewHolder();
				holder.tname = (TextView) convertView.findViewById(R.id.name);
				holder.completion = (ProgressBar) convertView
						.findViewById(R.id.completion);
				holder.points = (TextView) convertView
						.findViewById(R.id.points);
				holder.icon = (ImageView) convertView.findViewById(R.id.icon);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Medal m = items.get(position);
			holder.tname.setText(m.getName());
			holder.name = m.getName();
			holder.points.setText(String.valueOf(m.getPoints()));
			holder.completion.setMax(m.getNbObj());
			holder.completion.setProgress(m.getCompletion());
			holder.position = position;
			holder.img_url = m.getUrlImg();
			new ThumbnailTask(position, holder).execute();

			return convertView;
		}

	}

	private class ViewHolder {
		public TextView tname;
		public TextView points;
		public ProgressBar completion;
		public ImageView icon;
		public int position;
		public String img_url;
		public String name;
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
			Bitmap b = DownloadImage.getBitmap(mHolder.img_url, mHolder.name,
					true, Decorations.this, false, true);
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
