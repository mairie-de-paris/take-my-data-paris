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

package com.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.containers.Point;
import com.containers.TypePoint;
import com.containers.User;
import com.ui.takemydata.R;
import com.utils.AsyncSendObject;
import com.utils.DynamicForm;
import com.utils.Pair;

public class Reporting extends SherlockActivity {

	private TypePoint mType;
	private int mLongitude;
	private int mLattitude;
	private boolean mEdition;

	private DynamicForm mForm;
	private Uri mFilePicture;
	private String mImg64Encoded = "";
	protected boolean sTaken = true;

	protected static final String PHOTO_TAKEN = "photo_taken";
	private static final int CHECK = 1;
	private static final int PICTURE = 2;
	public static final int OK = 1;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportActionBar().setTitle(R.string.reporting_name);
		getSupportActionBar().setHomeButtonEnabled(true);

		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.reporting, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i;

		switch (item.getItemId()) {

		case android.R.id.home:
			i = new Intent(this, Map.class);
			i.putExtra("from", Map.POINT);
			i.putExtra("value", this.mType.getId());
			startActivity(i);
			this.finish();
			break;

		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reporting);

		Intent i = getIntent();
		this.mType = TypePoint.getTypeFromId(i.getIntExtra("type", 0), this);
		this.mLattitude = i.getIntExtra("lattitude", 0);
		this.mLongitude = i.getIntExtra("longitude", 0);
		this.mEdition = i.getBooleanExtra("edition", false);
		this.displayViews();
	}

	private void displayViews() {

		TextView title = (TextView) findViewById(R.id.title);

		if (mEdition) {
			title.setText(getText(R.string.change) + this.mType.getName());
		} else {
			title.setText(getText(R.string.found) + this.mType.getName());
		}

		Button CheckPosition = (Button) findViewById(R.id.check_position);
		CheckPosition.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(Reporting.this, Map.class);
				i.putExtra("lattitude", mLattitude);
				i.putExtra("longitude", mLongitude);
				i.putExtra("type", mType.getId());
				i.putExtra("from", Map.CHECK);
				startActivityForResult(i, CHECK);
			}
		});

		Button TakePicture = (Button) findViewById(R.id.take_picture);
		TakePicture.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				takePicture();
			}
		});

		Button Valider = (Button) findViewById(R.id.valider);
		com.containers.Point p = com.containers.Point.getStandardPointFromType(
				mType.getId(), this);
		mForm = new DynamicForm(this, p.getList());
		Valider.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Toast.makeText(Reporting.this, R.string.thanks_contrib,
						Toast.LENGTH_SHORT).show();
				String pt = Point.getJsonPoint(mForm.generateSpecs(),
						mType.getId(), (double) mLattitude / 1000000,
						(double) mLongitude / 1000000);
				new AsyncSendObject().execute(
						new Pair("img_64", mImg64Encoded), new Pair("obj", pt),
						new Pair("id", User.getId(Reporting.this)));
				finish();
			}
		});

		LinearLayout principal = (LinearLayout) findViewById(R.id.principal);
		principal.addView(mForm.generateForm(true));
	}

	public static Uri getOutputMediaFile() {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
		.format(new Date());
		File mediaFile = new File(Environment.getExternalStorageDirectory()
				.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
		return (Uri.fromFile(mediaFile));
	}

	private void takePicture() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		mFilePicture = getOutputMediaFile();
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mFilePicture);
		startActivityForResult(intent, PICTURE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == PICTURE) {
			if (resultCode == RESULT_OK) {
				Bitmap picture = BitmapFactory.decodeFile(mFilePicture
						.getPath());
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				picture.compress(Bitmap.CompressFormat.JPEG, 10, baos);
				byte[] b = baos.toByteArray();
				mImg64Encoded = Base64.encodeToString(b, Base64.DEFAULT);
			} else {
				Toast t = Toast.makeText(this, getText(R.string.error_picture),
						Toast.LENGTH_LONG);
				t.setGravity(Gravity.CENTER, 0, 0);
				t.show();
			}
		} else if (requestCode == CHECK) {
			if (resultCode == OK) {
				Toast.makeText(this, getText(R.string.change_position),
						Toast.LENGTH_SHORT).show();
				mLattitude = data.getIntExtra("lattitude", 0);
				mLongitude = data.getIntExtra("longitude", 0);
			} else {
				Toast.makeText(this, getText(R.string.change_position_back),
						Toast.LENGTH_SHORT).show();
			}
		}
	}



}
