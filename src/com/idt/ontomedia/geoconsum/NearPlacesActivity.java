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

import java.util.ArrayList;

import com.idt.ontomedia.geoconsum.adapters.DatabaseAdapter;
import com.idt.ontomedia.geoconsum.adapters.NearPlacesAdapter;
import com.idt.ontomedia.geoconsum.entities.NearPlace;
import com.idt.ontomedia.geoconsum.fragments.ListPlacesFragment;
import com.idt.ontomedia.geoconsum.loaders.NearPlacesCursorLoader;
import com.idt.ontomedia.geoconsum.loaders.TypeOfLocationCursorLoader;
import com.idt.ontomedia.geoconsum.utils.Utils;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

/**
 * 
 * @author Joan Fuentes
 * @author Ruben Serrano
 *
 */
public class NearPlacesActivity extends BaseActivity 
{
	//Define the name of columns to show (mColumns) and the destination (mTo)  
	private static final int[] VIEWS_TO = new int[] { android.R.id.text1 };
	private static final String[] COLUMNS_FROM;
	static
	{
		if (Utils.getCurrentLanguage().compareTo(DatabaseAdapter.ISO3_LANGUAGE_CATALAN)==0)
        {
			COLUMNS_FROM = new String[] { DatabaseAdapter.COLUMN_NAME };
        }
		else
        {
        	COLUMNS_FROM = new String[] { DatabaseAdapter.COLUMN_NAME_ES };
        }
	}
	
	private static final int TYPE_OF_LOCATION_LOADER_ID = 0;
	private static final int NEAR_PLACES_LOADER_ID = 1;
	
	private String[] mDistances;
	private Spinner mSpinnerTypeOfLocation;
	private Spinner mSpinnerDistances;
	private ListView mListViewNearPlaces;
	private SimpleCursorAdapter mAdapterTypeOfLocation;
	private NearPlacesAdapter mNearPlacesAdapter;

	private ArrayList<NearPlace> mNearPlaceList;
	private int mTypeOfLocationPosition = 0;
	private int mDistancePosition = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.places_near);

        //Initialize some values
        mNearPlaceList = new ArrayList<NearPlace>();
        mDistances = getResources().getStringArray(R.array.distances_to_places);
        
        mSpinnerTypeOfLocation = (Spinner) findViewById(R.id.spinner1);
        mSpinnerTypeOfLocation.setOnItemSelectedListener(new OnItemSelectedListener() 
        {
			@Override
			public void onItemSelected(AdapterView<?> _containerView, View _itemView, int _position, long _id)
			{
				getSupportLoaderManager().getLoader(NEAR_PLACES_LOADER_ID).onContentChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> _containerView) 
			{
				
			}
		});

        mSpinnerDistances = (Spinner) findViewById(R.id.spinner2);
        mSpinnerDistances.setOnItemSelectedListener(new OnItemSelectedListener() 
        {
			@Override
			public void onItemSelected(AdapterView<?> _containerView, View _itemView, int _position, long _id)
			{
				getSupportLoaderManager().getLoader(NEAR_PLACES_LOADER_ID).onContentChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> _containerView) 
			{
				
			}
		});
        
        ImageButton buttonUpdate = (ImageButton) findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(new OnClickListener() 
        {	
			@Override
			public void onClick(View _view) 
			{
				getSupportLoaderManager().getLoader(NEAR_PLACES_LOADER_ID).onContentChanged();
			}
		});
        
        // Configure the adapter and set the spinner of distances
        // This is a programatically operation because we can't modify the text color 
        // with the use of "entries" in the xml file. We define an ArrayAdapter and specify the layout of this to avoid
        // the default layout with the normal text style (black and normal)
        ArrayAdapter<String> arrayAdapterDistances = new ArrayAdapter<String>(this, R.layout.my_spinner_layout, mDistances);
        arrayAdapterDistances.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerDistances.setAdapter(arrayAdapterDistances);
        
        // Configure the adapter and set the spinner of types of Places
        // This is a programatically operation because we can't modify the text color 
        // with the use of "entries" in the xml file. We define an ArrayAdapter and specify the layout of this to avoid
        // the default layout with the normal text style (black and normal)
        mAdapterTypeOfLocation = new SimpleCursorAdapter(this, 
				    									 R.layout.my_spinner_layout, 
				    									 null, 
				    									 COLUMNS_FROM, 
				    									 VIEWS_TO,
				    									 0);
        mAdapterTypeOfLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerTypeOfLocation.setAdapter(mAdapterTypeOfLocation);
        mSpinnerTypeOfLocation.setSelection(mTypeOfLocationPosition, true);
        
        //Create the adapter and fill the place list with the real distances
        mNearPlacesAdapter = new NearPlacesAdapter(this, mNearPlaceList );
        
        mListViewNearPlaces = (ListView) findViewById(R.id.listViewNearPlaces);            
        mListViewNearPlaces.setAdapter(mNearPlacesAdapter) ;
        mListViewNearPlaces.setOnItemClickListener(new OnItemClickListener() 
        {
			@Override
			public void onItemClick(AdapterView<?> _containerView, View _itemView, int _position, long _id)
			{
				Intent intent = new Intent();
				intent.setClass(getBaseContext(), PlaceDetailActivity.class);
				intent.putExtra(ListPlacesFragment.EXTRA_ID_KEY, mNearPlaceList.get(_position).getId());
				startActivity(intent);
			}
		});
        
        getSupportLoaderManager().initLoader(TYPE_OF_LOCATION_LOADER_ID, null, new TypeOfLocationCursorLoaderCallback());
        getSupportLoaderManager().initLoader(NEAR_PLACES_LOADER_ID, null, new NearPlacesCursorLoaderCallback());
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
	
        mSpinnerDistances.setSelection(mDistancePosition, true);
        mSpinnerTypeOfLocation.setSelection(mTypeOfLocationPosition, true);   
	}
	
	@Override
	protected void onPause()
	{
		//Save positions of Spinners
		mTypeOfLocationPosition = (int) mSpinnerTypeOfLocation.getSelectedItemPosition();
		mDistancePosition = (int) mSpinnerDistances.getSelectedItemPosition();
		
		super.onPause();
	}
	
	@Override
	public void onDestroy()
	{
		getSupportLoaderManager().destroyLoader(NEAR_PLACES_LOADER_ID);
		getSupportLoaderManager().destroyLoader(TYPE_OF_LOCATION_LOADER_ID);
		super.onDestroy();
	}
	
	public long getSpinnerTypeOfLocationItemID()
	{
		return mSpinnerTypeOfLocation.getSelectedItemId();
	}
	
	public long getSpinnerDistancesItemID()
	{
		return mSpinnerDistances.getSelectedItemId();
	}

	private class TypeOfLocationCursorLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor>
	{
		// In the constructor, obtain a new Loader
		@Override
		public Loader<Cursor> onCreateLoader(int _id, Bundle _args) 
		{
			return new TypeOfLocationCursorLoader(getBaseContext());
		}

		@Override
		public void onLoadFinished(Loader<Cursor> _loader, Cursor _cursor) 
		{
			if ((_cursor != null) && (!_cursor.isClosed()))
			{
				mAdapterTypeOfLocation.swapCursor(_cursor);
			}
		}

		@Override
		public void onLoaderReset(Loader<Cursor> _loader) 
		{
			mAdapterTypeOfLocation.swapCursor(null);
		}
	}
	
	private class NearPlacesCursorLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor>
	{
		// In the constructor, obtain a new Loader
		@Override
		public Loader<Cursor> onCreateLoader(int _id, Bundle _args) 
		{
			return new NearPlacesCursorLoader(NearPlacesActivity.this, mNearPlaceList);
		}

		@Override
		public void onLoadFinished(Loader<Cursor> _loader, Cursor _cursor) 
		{
			mNearPlacesAdapter.notifyDataSetChanged();
		}

		@Override
		public void onLoaderReset(Loader<Cursor> _loader) 
		{
			
		}
	}
	
}
