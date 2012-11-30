package com.ui;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.bugsense.trace.BugSenseHandler;
import com.containers.TypePoint;
import com.ui.takemydata.R;
import com.utils.DownloadImage;

public class Home extends TmdUi {


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.general, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BugSenseHandler.initAndStartSession(this, "de24e98f");
		Log.i("oncreate", "pouet");
		setContentView(R.layout.home);

		this.initViews();
	}

	private void initViews() {

		final ArrayList<TypePoint> types = TypePoint.getAllPointTypes(this);

		ListView list = (ListView) findViewById(R.id.list);
		list.setAdapter(new HomeAdapter(this, 0, types));
		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				Intent intent = new Intent(Home.this, Map.class);
				intent.putExtra("from", Map.POINT);
				intent.putExtra("type", types.get(position).getId());
				startActivity(intent);
			}
		});

	}

	public class HomeAdapter extends ArrayAdapter<TypePoint> {

		private ArrayList<TypePoint> items;

		public HomeAdapter(Context context, int textViewResourceId,
				ArrayList<TypePoint> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = li.inflate(R.layout.home_points, null);

				holder = new ViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.description = (TextView) convertView
						.findViewById(R.id.description);
				holder.icon = (ImageView) convertView.findViewById(R.id.icon);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			TypePoint t = items.get(position);
			holder.name.setText(t.getName());
			holder.description.setText(t.getDescription());
			holder.position = position;
			holder.img_url = t.getUrlImg();
			new ThumbnailTask(position, holder).execute();

			return convertView;
		}

	}

	private class ViewHolder {
		public TextView name;
		public TextView description;
		public ImageView icon;
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
			Bitmap b = DownloadImage.getBitmap(mHolder.img_url, mHolder.name
					.getText().toString(), true, Home.this, false, true);
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
