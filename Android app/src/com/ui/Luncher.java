package com.ui;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.TextView;

import com.containers.User;
import com.ui.takemydata.R;
import com.utils.AsyncGetLuncherInformation;
import com.utils.AsyncGetNewObjects;
import com.utils.DownloadImage;
import com.utils.FileAccess;
import com.utils.Pair;

public class Luncher extends Activity {

	public static final String IMG_URL = "http://pichon.emmanuel.perso.neuf.fr/revues/OI/grand/2002/oi_2002_12.jpg";
	public static final String IMG_NAME = "launcher_screen_background";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String lastUpdate = FileAccess.ReadSettings(this,
				FileAccess.LAST_UPDATE);
		if (lastUpdate.contains(";")) {
			lastUpdate = lastUpdate.split(";")[0];
		} else {
			lastUpdate = "0";
		}

		new AsyncGetNewObjects(this).execute(new Pair("timestamp", lastUpdate),
				new Pair("id", User.getId(this)));
		FileAccess.WriteSettings(
				String.valueOf((int) (Calendar.getInstance().getTime()
						.getTime() / 1000))
						+ ";", this, FileAccess.LAST_UPDATE);

		setContentView(R.layout.luncher);

		String info = FileAccess.ReadSettings(this, FileAccess.SPLASH_SCREEN);
		if (info.length() > 0) {
			((TextView) findViewById(R.id.description)).setText(info);
		}

		Bitmap b = DownloadImage.checkExistance(Luncher.IMG_NAME, this);
		/*
		 * ImageView background = (ImageView)findViewById(R.id.background); if
		 * (b != null) { background.setImageBitmap(b); }
		 */
		new AsyncGetLuncherInformation(this).execute();
	}

	public void lunch() {
		if (this.isFinishing()) {
			return;
		}
		Intent i = new Intent(this, Home.class);
		this.startActivity(i);
		this.finish();
	}

}
