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
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.containers.Point;
import com.containers.TypePoint;
import com.containers.User;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;
import com.ui.takemydata.R;
import com.utils.AsyncDeleteObject;
import com.utils.FileAccess;
import com.utils.GPSOverlay;
import com.utils.Pair;
import com.utils.SherlockMapActivity;

public class Map extends SherlockMapActivity implements AuthentInterface {

	public static final int ACTION_BARRE = 0; // Click on action barre
	public static final int POINT = 1; // Click on a point
	public static final int CHECK = 2; // Click on a point
	public static final int CHANGE = 0;
	public static final int DELETE = 1;
	private static final int SIGNAL = 2;

	public static int LATTITUDE = 0;
	public static int LONGITUDE = 0;
	private static int ZOOM = 18;
	private static int CHECK_ZOOM = 20;
	private static boolean mStopLocationListener = false;
	private GeoPoint mCheckGeoPoint;
	private Point mPointToChange;
	private int from;
	private MapController mMapController;
	private MapView mMapView;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportActionBar().setTitle(R.string.map_name);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().removeAllTabs();

		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		if (from == CHECK) {
			inflater.inflate(R.menu.map_check, menu);
		} else {
			if (from == POINT) {
				inflater.inflate(R.menu.map_point_add, menu);
			}
			inflater.inflate(R.menu.map_point_recenter, menu);
		}
		inflater.inflate(R.menu.map_general, menu);
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i;

		switch (item.getItemId()) {

		case android.R.id.home:
			this.finish();
			break;
		case R.id.map_add:
			User.connect(this, SIGNAL);
			break;
		case R.id.map_recenter:
			mStopLocationListener = false;
			initLocationListener();
			break;
		case R.id.validate:
			i = new Intent();
			i.putExtra("lattitude", mCheckGeoPoint.getLatitudeE6());
			i.putExtra("longitude", mCheckGeoPoint.getLongitudeE6());
			setResult(Reporting.OK, i);
			finish();
			break;
		case R.id.back:
			i = new Intent();
			setResult(-Reporting.OK, i);
			finish();
			break;
		case R.id.decorations:
			User.connect(this, TmdUi.DECORATION);
			break;
		case R.id.help:
			i = new Intent(this, Help.class);
			i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			this.startActivity(i);
			break;
		case R.id.about:
			i = new Intent(this, About.class);
			i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			this.startActivity(i);
			break;
		case R.id.grade:
			User.connect(this, TmdUi.CLASSEMENT);
			break;
		case R.id.challenges:
			User.connect(this, TmdUi.CHALLENGES);
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (from == CHECK) {
			mMapController.setZoom(CHECK_ZOOM);
			mStopLocationListener = true;
		} else {
			mMapController.setZoom(ZOOM);
		}
		this.mMapView.getController().setCenter(
				new GeoPoint(LATTITUDE, LONGITUDE));
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.map);
		mMapView = (MapView) findViewById(R.id.mapview);
		mMapView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				mStopLocationListener = true;
				return false;
			}
		});
		initLocationListener();
		this.displayPoints();
	}

	private void initLocationListener() {
		LocationManager locManager;
		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L,
				500.0f, locationListener);
		Location location = locManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		mMapView.setBuiltInZoomControls(true);
		mMapController = mMapView.getController();
		updateWithNewLocation(location);
	}

	private void displayPoints() {
		Intent i = this.getIntent();
		this.from = i.getIntExtra("from", ACTION_BARRE);
		mMapView.setOnTouchListener(null);
		if (from == ACTION_BARRE) {
			ArrayList<TypePoint> types = TypePoint.getAllPointTypes(this);
			for (TypePoint t : types) {
				displayOneType(t);
			}
		} else if (from == CHECK) {
			TypePoint t = TypePoint.getTypeFromId(i.getIntExtra("type", 0),
					this);
			mCheckGeoPoint = new GeoPoint(i.getIntExtra("lattitude", 0),
					i.getIntExtra("longitude", 0));
			mMapView.getOverlays().add(
					new MovableOverlay(new BitmapDrawable(getResources(), t
							.getPictoBitmap())));
		} else {
			TypePoint t = TypePoint.getTypeFromId(i.getIntExtra("type", 0),
					this);
			displayOneType(t);
		}
	}

	void displayOneType(TypePoint t) {
		ArrayList<Point> points = Point.getPointsFromType(t.getId(), this);
		List<Overlay> mapOverlays = mMapView.getOverlays();
		Drawable drawable = new BitmapDrawable(getResources(),
				t.getPictoBitmap());
		for (Point p : points) {
			GPSOverlay overlay = new GPSOverlay(drawable, this);
			GeoPoint point = new GeoPoint(p.getLattitude(), p.getLongitude());
			OverlayItem overlayitem = new OverlayItem(point, String.valueOf(p
					.getId()), null);
			overlay.addOverlay(overlayitem);
			mapOverlays.add(overlay);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		ZOOM = mMapView.getZoomLevel();
		saveLatLon();
	}

	private void saveLatLon() {
		Projection proj = mMapView.getProjection();
		mStopLocationListener = true;
		GeoPoint gp = proj.fromPixels(mMapView.getWidth() / 2,
				mMapView.getHeight() / 2);
		LATTITUDE = gp.getLatitudeE6();
		LONGITUDE = gp.getLongitudeE6();
	}

	@Override
	public void onStop() {
		super.onStop();
		saveLatLon();
		StringBuilder sb = new StringBuilder();
		sb.append(LATTITUDE).append(";").append(LONGITUDE).append(";");
		FileAccess.WriteSettings(sb.toString(), this, FileAccess.LATLONG);
	}

	protected boolean isRouteDisplayed() {
		return false;
	}

	private void updateWithNewLocation(Location location) {

		if (mStopLocationListener == false) {
			if (location != null) {
				LATTITUDE = (int) (location.getLatitude() * 1000000);
				LONGITUDE = (int) (location.getLongitude() * 1000000);
			} else {
				String data = FileAccess.ReadSettings(this, FileAccess.LATLONG);
				if (data.contains(";")) {
					String[] list = data.split(";");
					LATTITUDE = Integer.valueOf(list[0]);
					LONGITUDE = Integer.valueOf(list[1]);
				} else {
					LATTITUDE = 48850000;
					LONGITUDE = 2330000;
				}
			}
			GeoPoint point = new GeoPoint(LATTITUDE, LONGITUDE);
			mMapController.animateTo(point);
		}

	}

	private final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
		}

		public void onProviderDisabled(String provider) {
			updateWithNewLocation(null);
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	private class MovableOverlay extends ItemizedOverlay<OverlayItem> {
		private List<OverlayItem> items = new ArrayList<OverlayItem>();
		private Drawable marker = null;
		private OverlayItem inDrag = null;
		private ImageView dragImage = null;
		private int xDragImageOffset = 0;
		private int yDragImageOffset = 0;
		private int xDragTouchOffset = 0;
		private int yDragTouchOffset = 0;

		public MovableOverlay(Drawable marker) {
			super(marker);
			this.marker = marker;
			dragImage = (ImageView) findViewById(R.id.drag);
			xDragImageOffset = dragImage.getDrawable().getIntrinsicWidth() / 2;
			yDragImageOffset = dragImage.getDrawable().getIntrinsicHeight();
			items.add(new OverlayItem(mCheckGeoPoint, null, null));
			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return (items.get(i));
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			super.draw(canvas, mapView, shadow);
			boundCenterBottom(marker);
		}

		@Override
		public int size() {
			return items.size();
		}

		@Override
		public boolean onTouchEvent(MotionEvent event, MapView mapView) {
			final int action = event.getAction();
			final int x = (int) event.getX();
			final int y = (int) event.getY();

			if (action == MotionEvent.ACTION_DOWN) {
				OverlayItem item = items.get(0);
				android.graphics.Point p = new android.graphics.Point(0, 0);
				mMapView.getProjection().toPixels(item.getPoint(), p);
				if (hitTest(item, marker, x - p.x, y - p.y)) {
					inDrag = item;
					items.remove(inDrag);
					populate();
					xDragTouchOffset = 0;
					yDragTouchOffset = 0;
					setDragImagePosition(p.x, p.y);
					dragImage.setVisibility(View.VISIBLE);
					xDragTouchOffset = x - p.x;
					yDragTouchOffset = y - p.y;
				}
			} else if (action == MotionEvent.ACTION_MOVE && inDrag != null) {
				setDragImagePosition(x, y);
			} else if (action == MotionEvent.ACTION_UP && inDrag != null) {
				dragImage.setVisibility(View.GONE);
				mCheckGeoPoint = mMapView.getProjection().fromPixels(
						x - xDragTouchOffset, y - yDragTouchOffset);
				OverlayItem toDrop = new OverlayItem(mCheckGeoPoint,
						inDrag.getTitle(), inDrag.getSnippet());
				items.add(toDrop);
				populate();
				inDrag = null;
			}
			return true;
		}

		private void setDragImagePosition(int x, int y) {
			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) dragImage
					.getLayoutParams();
			lp.setMargins(x - xDragImageOffset - xDragTouchOffset, y
					- yDragImageOffset - yDragTouchOffset, 0, 0);
			dragImage.setLayoutParams(lp);
		}
	}

	private void lunchReporting(Point p) {
		Intent i = new Intent(this, Reporting.class);
		i.putExtra("type", p.getType().getId());
		i.putExtra("lattitude", p.getLattitude());
		i.putExtra("longitude", p.getLongitude());
		i.putExtra("edition", true);
		this.startActivity(i);
		this.mMapView.getController().setCenter(
				new GeoPoint(p.getLattitude(), p.getLongitude()));
	}

	private void signaler() {
		LocationManager locManager;
		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L,
				500.0f, locationListener);
		Location location = locManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location == null) {
			Toast.makeText(this, getText(R.string.no_gps),
					Toast.LENGTH_SHORT).show();
			return;
		}

		LATTITUDE = (int) (location.getLatitude() * 1000000);
		LONGITUDE = (int) (location.getLongitude() * 1000000);
		Intent i = new Intent(this, Reporting.class);
		i.putExtra("type", this.getIntent().getIntExtra("type", 0));
		i.putExtra("lattitude", LATTITUDE);
		i.putExtra("longitude", LONGITUDE);
		i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		this.startActivity(i);
		this.mMapView.getController().setCenter(
				new GeoPoint(LATTITUDE, LONGITUDE));
	}

	public void authentAction(boolean isAuthenticated, int keyCode) {
		if (!isAuthenticated) {
			return;
		}
		if (keyCode == SIGNAL) {
			signaler();
		} else if (keyCode == CHANGE) {
			this.lunchReporting(mPointToChange);
		} else if (keyCode == DELETE) {
			new AsyncDeleteObject().execute(new Pair("id", User.getId(this)),
					new Pair("id_obj", mPointToChange.getId()));
			Toast.makeText(this, getText(R.string.new_object_sent),
					Toast.LENGTH_SHORT).show();
		} else {
			Intent i = null;
			if (keyCode == TmdUi.CHALLENGES) {
				i = new Intent(this, Challenges.class);
			} else if (keyCode == TmdUi.CLASSEMENT) {
				i = new Intent(this, Grades.class);
			} else if (keyCode == TmdUi.DECORATION) {
				i = new Intent(this, Decorations.class);
			}
			if (i != null) {
				i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(i);
			}
		}
	}

	public void setPointToChange(Point p) {
		mPointToChange = p;
	}

}
