package com.utils;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.containers.Point;
import com.containers.User;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;
import com.ui.Map;

public class GPSOverlay extends ItemizedOverlay {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Map mMap;
	private AlertDialog alertDialog = null;

	public GPSOverlay(Drawable defaultMarker, Map context) {
		super(boundCenterBottom(defaultMarker));
		mMap = context;
	}

	@Override
	protected boolean onTap(int index) {
		OverlayItem item = mOverlays.get(index);

		final Point p = Point.getPointFromId(Integer.valueOf(item.getTitle()),
				mMap);

		alertDialog = new AlertDialog.Builder(mMap).create();
		alertDialog.setTitle(p.getType().getName());

		new AsyncDownloadPointImg(p.getImgUrl(), alertDialog, mMap).execute();
		alertDialog.setIcon(new BitmapDrawable(mMap.getResources(), p.getType()
				.getImgBitmap()));

		ScrollView scroll = new ScrollView(mMap);
		final DynamicForm form = new DynamicForm(mMap, p.getList());
		LinearLayout principal = form.generateForm(false);
		scroll.addView(principal);

		LinearLayout action = new LinearLayout(mMap);
		action.setOrientation(LinearLayout.VERTICAL);

		Button remove = new Button(mMap);
		Button change = new Button(mMap);

		remove.setText("Cet objet n'existe plus");
		change.setText("Modifier les caracteristiques");

		remove.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				alertDialog.cancel();
				mMap.setPointToChange(p);
				User.connect(mMap, Map.DELETE);
			}
		});

		change.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				alertDialog.cancel();
				mMap.setPointToChange(p);
				User.connect(mMap, Map.CHANGE);
			}
		});

		action.addView(remove);
		action.addView(change);

		principal.addView(action);

		alertDialog.setView(scroll);
		alertDialog.show();

		return true;
	}

	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
}