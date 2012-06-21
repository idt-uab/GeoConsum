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

package com.idt.ontomedia.geoconsum.fragments;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.idt.ontomedia.geoconsum.DialogPreferenceActivity;
import com.idt.ontomedia.geoconsum.PlaceDetailActivity;
import com.idt.ontomedia.geoconsum.PlacesMapActivity;
import com.idt.ontomedia.geoconsum.R;
import com.idt.ontomedia.geoconsum.adapters.DatabaseAdapter;
import com.idt.ontomedia.geoconsum.fragments.GeoconsumListFragment;
import com.idt.ontomedia.geoconsum.loaders.SinglePlaceCursorLoader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author Ruben Serrano
 * @author Joan Fuentes
 *
 */
public class PlaceDetailFragment extends DetailsBaseFragment 
{
	public static final String EXTRA_NAME_KEY = "name";
	public static final String EXTRA_ZOOM_KEY = "zoom";
	public static final String EXTRA_PLACE_KEY = "place";
	public static final String EXTRA_LATITUDE_KEY = "latitude";
	public static final String EXTRA_LONGITUDE_KEY = "longitude";
	
	public PlaceDetailFragment()
	{
		super();
	}
	
	public PlaceDetailFragment(Context _context) 
	{
		super(new SinglePlaceCursorLoader(_context, ((Activity)_context).getIntent().getExtras()));
	}
	
	@Override
	public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) 
	{
		return super.onCreateView(_inflater, _container, R.layout.placedetail);
	}
	
	@Override
	protected void onFinishedLoadDetails()
	{
		super.onFinishedLoadDetails(R.id.placedetail_rl_main);
	}

	@Override
	protected void loadDetails(Cursor _cursor)
	{	
		final PlaceDetailActivity parentActivity = (PlaceDetailActivity) getActivity();
		final View hierarchyView = getHierarchyView();
		
		// Nombre
		setAvailableText(_cursor, R.id.placedetail_text_name, DatabaseAdapter.COLUMN_NAME);
		
		// Direccion
		setAvailableText(_cursor, R.id.placedetail_text_direccion, DatabaseAdapter.COLUMN_ADDRESS);
		
		// Localidad (Provincia)
		final String provincia = _cursor.getString(_cursor.getColumnIndex(DatabaseAdapter.COLUMN_PROVINCE));
		final String localidad = _cursor.getString(_cursor.getColumnIndex(DatabaseAdapter.COLUMN_LOCATION));
		
		TextView textView = (TextView) hierarchyView.findViewById(R.id.placedetail_text_localidad_and_provincia);
		setAvailableText(textView, localidad + " (" + provincia + ")", NO_LINKIFY);
		
		// Descripcion
		final String descripcion = _cursor.getString(_cursor.getColumnIndex(DatabaseAdapter.COLUMN_LOCATION_TYPE));
		
		textView = (TextView) hierarchyView.findViewById(R.id.placedetail_text_descripcion);
		setAvailableText(textView, descripcion, NO_LINKIFY);
		
		// Telefono
		setAvailableText(_cursor, R.id.placedetail_text_telefono, DatabaseAdapter.COLUMN_PHONE, Linkify.PHONE_NUMBERS);
		
		// Fax (no linkified!!)
		setAvailableText(_cursor, R.id.placedetail_text_fax, DatabaseAdapter.COLUMN_FAX);
		
		// Email
		setAvailableText(_cursor, R.id.placedetail_text_email, DatabaseAdapter.COLUMN_EMAIL, Linkify.EMAIL_ADDRESSES);

		// Latitude & Longitude
		final double latitud = _cursor.getInt(_cursor.getColumnIndex(DatabaseAdapter.COLUMN_LATITUDE));
		final double longitud = _cursor.getInt(_cursor.getColumnIndex(DatabaseAdapter.COLUMN_LONGITUDE));
		
		// message check images
		textView = (TextView) hierarchyView.findViewById(R.id.placedetail_text_coordenadas);
		textView.setText(R.string.textview_placedetail_checkMap);		
		
		setURL(_cursor, R.id.placedetail_text_url, R.id.placedetail_title_url, R.id.placedetail_imagebutton_url);
		
		//NAVIGATION IMAGE
		ImageView imageViewNavigation = (ImageView) hierarchyView.findViewById(R.id.placedetail_imagebutton_navigation);
		imageViewNavigation.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View _view) 
			{
				Intent intent;
				
				if ((latitud != 0) && (longitud != 0)) 
				{
					intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q="+latitud/1E6+","+longitud/1E6));
				}
				else
				{
					String streetAddress = ((TextView) hierarchyView.findViewById(R.id.placedetail_text_direccion)).getText().toString() + ",+" + localidad + ",+" + provincia;
					intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q="+streetAddress));	
				}
				startActivity(intent);
			}
		});		
		
		//MAP IMAGE
		ImageView imageViewMap = (ImageView) hierarchyView.findViewById(R.id.placedetail_imagebutton_map);
		imageViewMap.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View _view) 
			{
				Intent intent = new Intent();
				intent.setClass(parentActivity, PlacesMapActivity.class);
				
				String streetAddress = ((TextView) hierarchyView.findViewById(R.id.placedetail_text_direccion)).getText().toString() + ", " + localidad + ", " + provincia;

				intent.putExtra(EXTRA_NAME_KEY, ((TextView) hierarchyView.findViewById(R.id.placedetail_text_name)).getText().toString());
				intent.putExtra(EXTRA_PLACE_KEY, streetAddress);
				intent.putExtra(EXTRA_ZOOM_KEY, 17);
				
				if ((latitud == 0) && (longitud == 0))
				{				
					Geocoder fwdGeocoder = new Geocoder(parentActivity, Locale.getDefault());
					List<Address> locations = null;
					
					try 
					{
						if ((latitud != 0) && (longitud != 0))
						{
							locations = fwdGeocoder.getFromLocationName(streetAddress, 
																		5, 
																		latitud-0.009*5, 
																		longitud-0.009*5, 
																		latitud+0.009*5, 
																		longitud+0.009*5);
						}
						else
						{
							locations = fwdGeocoder.getFromLocationName(streetAddress,5);
						}
						
						if (locations.size() > 0)
						{
							intent.putExtra(EXTRA_LONGITUDE_KEY, locations.get(0).getLongitude()*1E6);
							intent.putExtra(EXTRA_LATITUDE_KEY, locations.get(0).getLatitude()*1E6);
						}
						else
						{
							intent.putExtra(EXTRA_LONGITUDE_KEY, longitud*1E6);
							intent.putExtra(EXTRA_LATITUDE_KEY, latitud*1E6);
						}
					} 
					catch (IOException e) 
					{
						intent.putExtra(EXTRA_LONGITUDE_KEY, longitud*1E6);
						intent.putExtra(EXTRA_LATITUDE_KEY, latitud*1E6);
					}
				}else
				{
					intent.putExtra(EXTRA_LONGITUDE_KEY, longitud);
					intent.putExtra(EXTRA_LATITUDE_KEY, latitud);
				}
				startActivity(intent);
			}
		});
		
		//Guardo los datos en la variable de error por si el usuario desea informar de datos erroneos
		parentActivity.setErrorInfo(getResources().getString(R.string.report_bug_location) + " " + parentActivity.getIntent().getIntExtra(GeoconsumListFragment.EXTRA_ID_KEY, 1) + ".");
			
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        
        if (sharedPreferences.getBoolean(DialogPreferenceActivity.PREF_DIALOG_CHECK, true))
        {
        	parentActivity.showDialog(PlaceDetailActivity.DIALOG_WARNING_ID);
        }
	}
}