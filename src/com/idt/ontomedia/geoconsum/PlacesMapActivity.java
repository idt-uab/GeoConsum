/*
   Copyright 2012 IDT UAB

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.idt.ontomedia.geoconsum;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.idt.ontomedia.geoconsum.R;
import com.idt.ontomedia.geoconsum.entities.MediOverlay;
import com.idt.ontomedia.geoconsum.fragments.PlaceDetailFragment;

/**
 * 
 * @author Ruben Serrano
 *
 */
public class PlacesMapActivity extends MapActivity 
{	
	@Override
	protected void onCreate(Bundle _savedInstanceState) 
	{
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.placesmap);
		
		Intent intent = getIntent();
		
		Double latitude = intent.getDoubleExtra(PlaceDetailFragment.EXTRA_LATITUDE_KEY, 0);
		Double longitude = intent.getDoubleExtra(PlaceDetailFragment.EXTRA_LONGITUDE_KEY, 0);
		GeoPoint point = new GeoPoint(latitude.intValue(), longitude.intValue());
		
		MapView mapView = (MapView)findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		
		MapController mapController = mapView.getController();
		mapController.setCenter(point);
		mapController.setZoom(intent.getIntExtra(PlaceDetailFragment.EXTRA_ZOOM_KEY,15));
		
		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.map_icon);
		MediOverlay itemizedoverlay = new MediOverlay(drawable, this);
		
		OverlayItem overlayitem = new OverlayItem(point, intent.getStringExtra(PlaceDetailFragment.EXTRA_NAME_KEY), intent.getStringExtra(PlaceDetailFragment.EXTRA_PLACE_KEY));
		
		itemizedoverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedoverlay);
	}
	
	@Override
	protected boolean isRouteDisplayed() 
	{
		return false;
	}

}
