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

package com.idt.ontomedia.geoconsum.utils;

import java.util.Locale;
import com.google.android.maps.GeoPoint;
import com.idt.ontomedia.geoconsum.adapters.DatabaseAdapter;

/**
 * 
 * @author Joan Fuentes
 *
 */
public class Utils 
{
	/**
	 * Function to calculate an area of X kilometers centered at a GeoPoint.
	 * @param distanceinKm Area in kilometers
	 * @param geoPosition Geopoint with the center of Area. It will be usually the current position.
	 * @return int[] with 4 values (2 positions of latitude and 2 positions of longitude -> Area.
	 */
	public static int[] getArea(int _distanceInKm, GeoPoint _geoPosition) 
	{
		int[] areaValues;
		areaValues = new int[4];

		//1 meter is one ten-millionth of the quadrant of the terrestrial meridian.
		//The terrestrial meridian is 40.000.000 meters = 40.000 Km 
		//The terrestrial meridian is 360º (a circle)
		//1 degree of latitude = meridian longitude [km]/360º
		//1 degree of latitude= 40.000 km/360º = 111.11... km
		float oneGradeInKm = Float.valueOf("40000")/Float.valueOf("360"); 
		int radioDistance =  (int)((((float)_distanceInKm)/oneGradeInKm)*1E6);
		areaValues[0] = _geoPosition.getLatitudeE6() - radioDistance;
		areaValues[1] = _geoPosition.getLatitudeE6() + radioDistance;
		areaValues[2] = _geoPosition.getLongitudeE6() - radioDistance;
		areaValues[3] = _geoPosition.getLongitudeE6() + radioDistance;		

		return areaValues;
	}
	
	/**
	 * Function to recover the language tu use.
	 * @return If the System language is spanish, the language retrieved from the database will be in spanish (spa), otherwise Catalan (cat).
	 */	
	public static String getCurrentLanguage()
	{
		return (Locale.getDefault()
						.getISO3Language()
						.equalsIgnoreCase(DatabaseAdapter.ISO3_LANGUAGE_SPANISH) ? DatabaseAdapter.ISO3_LANGUAGE_SPANISH : 
																				   DatabaseAdapter.ISO3_LANGUAGE_CATALAN);
	}	
	
}
