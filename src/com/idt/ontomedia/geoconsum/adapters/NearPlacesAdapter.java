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

package com.idt.ontomedia.geoconsum.adapters;

import java.util.ArrayList;

import com.idt.ontomedia.geoconsum.R;
import com.idt.ontomedia.geoconsum.entities.NearPlace;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * 
 * @author Joan Fuentes
 *
 */
public class NearPlacesAdapter extends ArrayAdapter<NearPlace>
{
	private final ArrayList<NearPlace> mNearPlacesList;

	public NearPlacesAdapter(Activity _parentActivity, ArrayList<NearPlace> _nearPlacesList)
	{
		super(_parentActivity, R.layout.near_places_list_item, _nearPlacesList);
		mNearPlacesList = _nearPlacesList;
	}

	@Override
	public View getView(int _position, View _convertView, ViewGroup _parent) 
	{
		NearPlacesView nearPlacesView = null;
		Float floatNumber;
 
        if(_convertView == null)
        {
            // Get a new instance of the row layout view
            LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();
            _convertView = inflater.inflate(R.layout.near_places_list_item, null);
 
            // Hold the view objects in an object,
            // so they don't need to be re-fetched
            nearPlacesView = new NearPlacesView();
            nearPlacesView.setName((TextView) _convertView.findViewById(R.id.textView_NearPlaces_Title));
            nearPlacesView.setAddress((TextView) _convertView.findViewById(R.id.textView_NearPlaces_Description));
            nearPlacesView.setDistance((TextView) _convertView.findViewById(R.id.textView_NearPlaces_Distance));
            nearPlacesView.setDistanceUnit((TextView) _convertView.findViewById(R.id.textView_NearPlaces_Distance_Unit));
 
            // Cache the view objects in the tag,
            // so they can be re-accessed later
            _convertView.setTag(nearPlacesView);
        }
        else
        {
        	nearPlacesView = (NearPlacesView) _convertView.getTag();
        }
 
        // Transfer the NearPlace data from the data object
        // to the view objects
        NearPlace nearPlace = mNearPlacesList.get(_position);
        nearPlacesView.getName().setText(nearPlace.getName());
        nearPlacesView.getAddress().setText(nearPlace.getAddress());
        
        if(nearPlace.getDistance()>=1000)
        {
        	floatNumber = Integer.valueOf(nearPlace.getDistance()/100).floatValue();
        	floatNumber = floatNumber/10;
        	nearPlacesView.getDistance().setText(floatNumber.toString());
        	nearPlacesView.getDistanceUnit().setText("km");
        }
        else
        {
        	nearPlacesView.getDistance().setText(Integer.valueOf(nearPlace.getDistance()).toString());
        	nearPlacesView.getDistanceUnit().setText(getContext().getResources().getString(R.string.label_meters));
        }

        return _convertView;
	}

	private static class NearPlacesView 
    {
        private TextView mName;
        private TextView mAddress;
        private TextView mDistance;
        private TextView mDistanceUnit;
        
        public TextView getName() 
        {
			return mName;
		}
        
		public void setName(TextView _name) 
		{
			mName = _name;
		}
		
		public TextView getAddress() 
		{
			return mAddress;
		}
		
		public void setAddress(TextView _address) 
		{
			mAddress = _address;
		}
		
		public TextView getDistance() 
		{
			return mDistance;
		}
		
		public void setDistance(TextView _distance) 
		{
			mDistance = _distance;
		}
		
		public TextView getDistanceUnit() 
		{
			return mDistanceUnit;
		}
		
		public void setDistanceUnit(TextView _distanceUnit) 
		{
			mDistanceUnit = _distanceUnit;
		}
    }
}



