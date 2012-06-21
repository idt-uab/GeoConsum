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

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.idt.ontomedia.geoconsum.R;
import com.idt.ontomedia.geoconsum.adapters.DatabaseAdapter;
import com.idt.ontomedia.geoconsum.loaders.AutonomousCommunityCursorLoader;
import com.idt.ontomedia.geoconsum.loaders.DatabaseCursorLoader;
import com.idt.ontomedia.geoconsum.loaders.LocationCursorLoader;
import com.idt.ontomedia.geoconsum.loaders.ProvinceCursorLoader;
import com.idt.ontomedia.geoconsum.loaders.TypeOfLocationCursorLoader;
import com.idt.ontomedia.geoconsum.loaders.TypeOfLocationDescriptionCursorLoader;
import com.idt.ontomedia.geoconsum.utils.Utils;

public class PlaceSearchActivity extends BaseActivity 
{
	public static final String EXTRA_TYPE = "type";
	public static final String EXTRA_AUTONOMOUS_COMUNITY = "ac";
	public static final String EXTRA_PROVINCE = "province";
	public static final String EXTRA_LOCALITY = "locality";
	
	private static final int[] VIEWS_TO = new int[] { android.R.id.text1 };
	private static final String[] COLUMNS_FROM = new String[] { DatabaseAdapter.COLUMN_NAME };
	private static final String[] TYPE_OF_LOCATION_COLUMNS_FROM;
	private static final String TYPE_OF_LOCATION_COLUMN_CONTENT;
	static
	{
		if (Utils.getCurrentLanguage().compareTo(DatabaseAdapter.ISO3_LANGUAGE_CATALAN)==0)
        {
			TYPE_OF_LOCATION_COLUMNS_FROM = new String[] { DatabaseAdapter.COLUMN_NAME };
			TYPE_OF_LOCATION_COLUMN_CONTENT = DatabaseAdapter.COLUMN_CONTENT;
        }
		else
        {
			TYPE_OF_LOCATION_COLUMNS_FROM = new String[] { DatabaseAdapter.COLUMN_NAME_ES };
			TYPE_OF_LOCATION_COLUMN_CONTENT = DatabaseAdapter.COLUMN_CONTENT_ES;
        }
	}
	
	private static final int TYPE_OF_LOCATION_LOADER_ID = 0;
	private static final int AUTONOMOUS_COMMUNITY_LOADER_ID = 1;
	private static final int TYPE_OF_LOCATION_DESCRIPTION_LOADER_ID = 2;
	private static final int PROVINCE_LOADER_ID = 3;
	private static final int LOCATION_LOADER_ID = 4;
	
	private Spinner mSpinnerTypeOfLocation;
	private Spinner mSpinnerAutonomousCommunity;
	private Spinner mSpinnerProvince;
	private Spinner mSpinnerLocation;
	private TextView mTextViewLocationDescription;
	
	private SimpleCursorAdapter mAdapterTypeOfLocation;
	private SimpleCursorAdapter mAdapterAutonomousCommunity;
	private SimpleCursorAdapter mAdapterProvince;
	private SimpleCursorAdapter mAdapterLocation;
	

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_by_location);

        //Link elements
    	final TextView textViewProvince = (TextView) findViewById(R.id.textView_Province);
    	final TextView textViewLocation = (TextView) findViewById(R.id.textView_Location);
    	mTextViewLocationDescription = (TextView) findViewById(R.id.textView_TypeOfLocationDescription);
    	mSpinnerTypeOfLocation = (Spinner) findViewById(R.id.spinner_TypeOfLocation);
    	mSpinnerAutonomousCommunity = (Spinner) findViewById(R.id.spinner_AutonomousRegion);
    	mSpinnerProvince = (Spinner) findViewById(R.id.spinner_Province);
    	mSpinnerLocation = (Spinner) findViewById(R.id.spinner_Location);
    	final Button buttonSearch = (Button) findViewById(R.id.buttonSearch);      
        
        //fill and select the elements of spinners.
        mAdapterTypeOfLocation = new SimpleCursorAdapter(this, 
        												 android.R.layout.simple_spinner_item, 
        												 null, 
        												 TYPE_OF_LOCATION_COLUMNS_FROM, 
        												 VIEWS_TO,
        												 0);
        mAdapterTypeOfLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerTypeOfLocation.setAdapter(mAdapterTypeOfLocation);   
        
        mAdapterAutonomousCommunity = new SimpleCursorAdapter(this, 
        												      android.R.layout.simple_spinner_item, 
        												      null, 
        												      COLUMNS_FROM, 
        												      VIEWS_TO,
        												      0);
        mAdapterAutonomousCommunity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerAutonomousCommunity.setAdapter(mAdapterAutonomousCommunity);  
        //lock the Autonomous Community Spinner with 'Catalunya'
        mSpinnerAutonomousCommunity.setEnabled(false);
        
        mAdapterProvince = new SimpleCursorAdapter(this, 
        										   android.R.layout.simple_spinner_item, 
        										   null, 
        										   COLUMNS_FROM, VIEWS_TO, 
        										   0);
        mAdapterProvince.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerProvince.setAdapter(mAdapterProvince);  
        
        mAdapterLocation = new SimpleCursorAdapter(this, 
        										   android.R.layout.simple_spinner_item, 
        										   null, 
        										   COLUMNS_FROM, 
        										   VIEWS_TO,
        										   0);
        mAdapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerLocation.setAdapter(mAdapterLocation); 
        
        
        //Create a listener to show the description 
        mSpinnerTypeOfLocation.setOnItemSelectedListener(new OnItemSelectedListener() 
        {
			@Override
			public void onItemSelected(AdapterView<?> _containerView, View _itemView, int _postion, long _id)
			{
				getSupportLoaderManager().getLoader(TYPE_OF_LOCATION_DESCRIPTION_LOADER_ID).onContentChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> _containerView)
			{
				mTextViewLocationDescription.setText("");
			}

        });
        
        //Create a listener to show/hide the province and location
        mSpinnerAutonomousCommunity.setOnItemSelectedListener(new OnItemSelectedListener()
        {
			@Override
			public void onItemSelected(AdapterView<?> _containerView, View _itemView, int _postion, long _id) 
			{
				if (mSpinnerAutonomousCommunity.getSelectedItemId() <= 0)
				{
					mSpinnerProvince.setVisibility(View.GONE);
					mSpinnerLocation.setVisibility(View.GONE);
					textViewProvince.setVisibility(View.GONE);
					textViewLocation.setVisibility(View.GONE);
            	}
				else
				{
					mSpinnerProvince.setVisibility(View.VISIBLE);
            		textViewProvince.setVisibility(View.VISIBLE);
            		
            		/*
            		Loader<Cursor> loader = getSupportLoaderManager().getLoader(PROVINCE_LOADER_ID);
            		if ((loader != null) && (loader.isStarted()) && (!loader.isAbandoned()))
            		{
            			loader.onContentChanged();      		
            		}
            		*/
            	}
    		}

			@Override
			public void onNothingSelected(AdapterView<?> _containerView) 
			{
				
			}
        });
        
        //Create a listener to show/hide the Location
        mSpinnerProvince.setOnItemSelectedListener(new OnItemSelectedListener() 
        {
			@Override
			public void onItemSelected(AdapterView<?> _containerView, View _itemView, int _postion, long _id) 
			{
				if (mSpinnerProvince.getSelectedItemId() <= 0)
				{		
					textViewLocation.setVisibility(View.GONE);
					mSpinnerLocation.setVisibility(View.GONE);
            	}
				else
				{
	                mSpinnerLocation.setVisibility(View.VISIBLE);
	                textViewLocation.setVisibility(View.VISIBLE);
	                
	                Loader<Cursor> loader = getSupportLoaderManager().getLoader(LOCATION_LOADER_ID);
	                DatabaseCursorLoader cursorLoader = (DatabaseCursorLoader) loader;
            		if ((cursorLoader != null) && (cursorLoader.isStarted()) && (!cursorLoader.isAbandoned()))
            		{
            			cursorLoader.forceLoad();      		
            		}
            	}
    		}

			@Override
			public void onNothingSelected(AdapterView<?> _containerView) 
			{
				
			}
        }); 
        
        //Create a listener to capture data and load ListPlaceActivity
        buttonSearch.setOnClickListener(new OnClickListener () 
        {
			@Override
			public void onClick(View v) 
			{		
				int autonomousCommunity = -1;
				int province = -1;
				int locality = -1;
				
				if ((autonomousCommunity = (int)mSpinnerAutonomousCommunity.getSelectedItemId()) > 0)
				{
					if ((province = (int) mSpinnerProvince.getSelectedItemId()) > -1)
					{
						locality = (int) mSpinnerLocation.getSelectedItemId();
					}
				}
				
				Intent intent = new Intent(getBaseContext(), ListPlacesActivity.class);
				intent.putExtra(EXTRA_TYPE, (int) mSpinnerTypeOfLocation.getSelectedItemId());
				intent.putExtra(EXTRA_AUTONOMOUS_COMUNITY,autonomousCommunity);
				intent.putExtra(EXTRA_PROVINCE,province);
				intent.putExtra(EXTRA_LOCALITY,locality);
				startActivity(intent);
			}
        });
        
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(LOCATION_LOADER_ID, null, new LocationCursorLoaderCallback());
        loaderManager.initLoader(PROVINCE_LOADER_ID, null, new ProvinceCursorLoaderCallback());
        loaderManager.initLoader(TYPE_OF_LOCATION_DESCRIPTION_LOADER_ID, null, new TypeOfLocationDescriptionCursorLoaderCallback());
        loaderManager.initLoader(AUTONOMOUS_COMMUNITY_LOADER_ID, null, new AutonomousCommunityCursorLoaderCallback());
        loaderManager.initLoader(TYPE_OF_LOCATION_LOADER_ID, null, new TypeOfLocationCursorLoaderCallback());      
    }
    
    @Override
	public void onDestroy()
	{
    	LoaderManager loaderManager = getSupportLoaderManager();
    	loaderManager.destroyLoader(TYPE_OF_LOCATION_LOADER_ID);
    	loaderManager.destroyLoader(AUTONOMOUS_COMMUNITY_LOADER_ID);
    	loaderManager.destroyLoader(TYPE_OF_LOCATION_DESCRIPTION_LOADER_ID);
    	loaderManager.destroyLoader(PROVINCE_LOADER_ID);
    	loaderManager.destroyLoader(LOCATION_LOADER_ID);
		
    	super.onDestroy();
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
	
	private class AutonomousCommunityCursorLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor>
	{
		// In the constructor, obtain a new Loader
		@Override
		public Loader<Cursor> onCreateLoader(int _id, Bundle _args) 
		{
			return new AutonomousCommunityCursorLoader(getBaseContext(), Boolean.TRUE);
		}

		@Override
		public void onLoadFinished(Loader<Cursor> _loader, Cursor _cursor) 
		{
			if ((_cursor != null) && (!_cursor.isClosed()))
			{
				mAdapterAutonomousCommunity.swapCursor(_cursor);
				// This line shouldn't be here, but needed to enforce the application to show only catalonian provinces
				// as it was decided to only show the catalonian info in a first phase
				mSpinnerAutonomousCommunity.setSelection(9);
			}
		}

		@Override
		public void onLoaderReset(Loader<Cursor> _loader) 
		{
			mAdapterAutonomousCommunity.swapCursor(null);
		}
	}
	
	private class TypeOfLocationDescriptionCursorLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor>
	{
		// In the constructor, obtain a new Loader
		@Override
		public Loader<Cursor> onCreateLoader(int _id, Bundle _args) 
		{
			return new TypeOfLocationDescriptionCursorLoader(getBaseContext(), mSpinnerTypeOfLocation);
		}

		@Override
		public void onLoadFinished(Loader<Cursor> _loader, Cursor _cursor) 
		{
			if ((_cursor != null) && (!_cursor.isClosed()))
			{
				_cursor.moveToFirst();

				try
				{
					mTextViewLocationDescription.setText(_cursor.getString(_cursor.getColumnIndex(TYPE_OF_LOCATION_COLUMN_CONTENT)));
				}
				catch(Exception e)
				{
					// Sometimes an exception is thrown, but if captured and ignored, the application works fine
					// RandomCrappyException that make your life very unhappy
					//Log.w("GeoConsum", "Exception controlled");
				}
			}
		}

		@Override
		public void onLoaderReset(Loader<Cursor> _loader) 
		{
			
		}
	}
	
	private class ProvinceCursorLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor>
	{
		// In the constructor, obtain a new Loader
		@Override
		public Loader<Cursor> onCreateLoader(int _id, Bundle _args) 
		{
			return new ProvinceCursorLoader(getBaseContext(), mSpinnerAutonomousCommunity);
		}

		@Override
		public void onLoadFinished(Loader<Cursor> _loader, Cursor _cursor) 
		{
			if ((_cursor != null) && (!_cursor.isClosed()))
			{
				mAdapterProvince.swapCursor(_cursor);
			}
		}

		@Override
		public void onLoaderReset(Loader<Cursor> _loader) 
		{
			mAdapterProvince.swapCursor(null);
		}
	}
	
	private class LocationCursorLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor>
	{
		// In the constructor, obtain a new Loader
		@Override
		public Loader<Cursor> onCreateLoader(int _id, Bundle _args) 
		{
			return new LocationCursorLoader(getBaseContext(), mSpinnerProvince);
		}

		@Override
		public void onLoadFinished(Loader<Cursor> _loader, Cursor _cursor) 
		{
			if ((_cursor != null) && (!_cursor.isClosed()))
			{
				mAdapterLocation.swapCursor(_cursor);
			}
		}

		@Override
		public void onLoaderReset(Loader<Cursor> _loader) 
		{
			mAdapterLocation.swapCursor(null);
		}
	}
}


