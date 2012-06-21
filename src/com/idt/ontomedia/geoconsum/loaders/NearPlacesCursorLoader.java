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
package com.idt.ontomedia.geoconsum.loaders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.idt.ontomedia.geoconsum.NearPlacesActivity;
import com.idt.ontomedia.geoconsum.R;
import com.idt.ontomedia.geoconsum.adapters.DatabaseAdapter;
import com.idt.ontomedia.geoconsum.entities.NearPlace;
import com.idt.ontomedia.geoconsum.utils.Utils;

import android.content.Context;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

/**
 * 
 * @author Ruben Serrano
 * @author Joan Fuentes
 *
 */
public class NearPlacesCursorLoader extends DatabaseCursorLoader
{
	private NearPlacesActivity mActivity;
	private ArrayList<NearPlace> mNearPlaceList;

	public NearPlacesCursorLoader(Context _context, ArrayList<NearPlace> _nearPlacesArray) 
	{
		super(_context, "");
		mActivity = (NearPlacesActivity) _context;
		mNearPlaceList = _nearPlacesArray;
	}

	@Override
	protected Cursor placeQuery() 
	{
		//Get the type of place selected with the TypeOfPlace spinner.
		int typeOfPlace = (int) mActivity.getSpinnerTypeOfLocationItemID();
		
		//Get the distance selected with the Distance spinner.
		String distanceInKmString = mActivity.getResources().getStringArray(R.array.distances_to_places)[(int) mActivity.getSpinnerDistancesItemID()];
		distanceInKmString = distanceInKmString.replaceAll(" kms", "");
		distanceInKmString = distanceInKmString.replaceAll(" km", "");
		int distanceInKm = Integer.parseInt(distanceInKmString);
		
		//Get current position
		GeoPoint currentGeoPosition = getCurrentGeoPosition();
		//Get values of area necessaries to recover inner places 
		int[] areaValues = Utils.getArea(distanceInKm, currentGeoPosition);
		
		
    	mCursor = getDatabaseAdapter().getNearPlacesList(areaValues,currentGeoPosition, typeOfPlace);
		
    	if ((mCursor != null) && (!mCursor.isClosed()))
		{
    		mNearPlaceList.clear();
		
			//Create the ArrayList with NearPlaces
			while (mCursor.moveToNext())
	    	{
				int latitud = (int)mCursor.getDouble(mCursor.getColumnIndex("latitud"));
				int longitud = (int)mCursor.getDouble(mCursor.getColumnIndex("longitud"));
				GeoPoint placeGeoPosition = new GeoPoint(latitud, longitud);
				
				Location locationA = new Location("current Point");  
				Location locationB = new Location("Place Point");
				locationA.setLatitude(currentGeoPosition.getLatitudeE6() / 1E6);  
				locationA.setLongitude(currentGeoPosition.getLongitudeE6() / 1E6);  
				  
				locationB.setLatitude(placeGeoPosition.getLatitudeE6() / 1E6);  
				locationB.setLongitude(placeGeoPosition.getLongitudeE6() / 1E6);  
				int distance = Double.valueOf(locationA.distanceTo(locationB)).intValue();
				
				int distanceInM = Integer.parseInt(distanceInKmString)*1000;
				//double check to avoid by distance
				if(distance <= distanceInM)
				{
					NearPlace nearPlace = new NearPlace(mCursor.getInt(mCursor.getColumnIndex(DatabaseAdapter.COLUMN_ROWID)),
														mCursor.getString(mCursor.getColumnIndex(DatabaseAdapter.COLUMN_NAME)),
														mCursor.getString(mCursor.getColumnIndex(DatabaseAdapter.COLUMN_CITY)), 
														mCursor.getString(mCursor.getColumnIndex(DatabaseAdapter.COLUMN_ADDRESS)),
														distance);
					mNearPlaceList.add(nearPlace);
				}
	    	}
			
			//Order the ArrayList by distance
			Collections.sort(mNearPlaceList, new Comparator<NearPlace>() 
			{  	  
	            public int compare(NearPlace _nearPlace1, NearPlace _nearPlace2) 
	            {  
	                int distance1 = _nearPlace1.getDistance();  
	                int distance2 = _nearPlace2.getDistance();  
	  
	                if (distance1 > distance2)
	                {  
	                    return 1;  
	                } 
	                else if (distance1 < distance2)
	                {  
	                    return -1;  
	                } 
	                else 
	                {  
	                    return 0;  
	                }  
	            }  
	        });
		}
			
		return mCursor;
	}
	
	public GeoPoint getCurrentGeoPosition()
	{
		int latitude=0;
		int longitude=0;

		LocationManager lm = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_HIGH);
		// to get the list of providers and select the best considering time, accuracy,...
		List<String> providers = lm.getProviders(criteria,false);
		for (String provider: providers)
		{
			Location location = lm.getLastKnownLocation(provider);
		    // Is the best previously known location?
		    if(location!=null)
		    {
	        		latitude = (int)(location.getLatitude()*1E6);
	        		longitude = (int)(location.getLongitude()*1E6);
		    }
		}
		
		return new GeoPoint(latitude,longitude); 
	}
}
