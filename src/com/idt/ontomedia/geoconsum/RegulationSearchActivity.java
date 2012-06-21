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

import com.idt.ontomedia.geoconsum.R;
import com.idt.ontomedia.geoconsum.adapters.DatabaseAdapter;
import com.idt.ontomedia.geoconsum.loaders.AutonomousCommunityCursorLoader;
import com.idt.ontomedia.geoconsum.loaders.SubtypeOfRegulationCursorLoader;
import com.idt.ontomedia.geoconsum.loaders.TypeOfRegulationCursorLoader;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class RegulationSearchActivity extends BaseActivity 
{
	public static final String EXTRA_TYPE = "type";
	public static final String EXTRA_SUBTYPE = "subtype";
	public static final String EXTRA_AUTONOMOUS_COMMUNITY = "ac";
	
	private static final int[] VIEWS_TO = new int[] { android.R.id.text1 };
	private static final String[] COLUMNS_FROM = new String[] { DatabaseAdapter.COLUMN_NAME };
	private static final String[] TYPE_OF_LOCATION_COLUMNS_FROM;
	static
	{
		if (Utils.getCurrentLanguage().compareTo(DatabaseAdapter.ISO3_LANGUAGE_CATALAN)==0)
        {
			TYPE_OF_LOCATION_COLUMNS_FROM = new String[] { DatabaseAdapter.COLUMN_NAME };
        }
		else
        {
			TYPE_OF_LOCATION_COLUMNS_FROM = new String[] { DatabaseAdapter.COLUMN_NAME_ES };
        }
	}

	private Spinner mSpinnerTypeOfRegulation = null;
	private Spinner mSpinnerSubtypeOfRegulation = null;
	private Spinner mSpinnerAutonomousCommunity = null;
	private TextView mTextViewAutonomousCommunity = null;
	
	private int mAutonomousCommunityPosition = 8;
	
	private SimpleCursorAdapter mAdapterTypeOfRegulation;
	private SimpleCursorAdapter mAdapterSubtypeOfRegulation;
	private SimpleCursorAdapter mAdapterAutonomousCommunity;
	
	private static final int TYPE_OF_REGULATION_LOADER_ID = 0;
	private static final int SUBTYPE_OF_REGULATION_LOADER_ID = 1;
	private static final int AUTONOMOUS_COMMUNITY_LOADER_ID = 2;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_regulation);

        //Link elements
        mTextViewAutonomousCommunity = (TextView) findViewById(R.id.textView_AutonomousRegion_regulation);
        mSpinnerAutonomousCommunity = (Spinner) findViewById(R.id.spinner_AutonomousRegion_regulation);
        mSpinnerSubtypeOfRegulation = (Spinner) findViewById(R.id.spinner_subtype_regulation);
        mSpinnerTypeOfRegulation = (Spinner) findViewById(R.id.spinner_type_regulation);
        
        //Lock Autonomous Community to Catalunya
        mSpinnerAutonomousCommunity.setEnabled(false);
        
        mSpinnerTypeOfRegulation.setOnItemSelectedListener(new OnItemSelectedListener() 
        {
			@Override
			public void onItemSelected(AdapterView<?> _containerView, View _itemView, int _position, long _id) 
			{
				mSpinnerAutonomousCommunity.setSelection(mAutonomousCommunityPosition);
				if (mSpinnerTypeOfRegulation.getSelectedItemId()==1)
				{
					mSpinnerAutonomousCommunity.setSelection(mAutonomousCommunityPosition);				
					mSpinnerAutonomousCommunity.setVisibility(View.VISIBLE);
					mTextViewAutonomousCommunity.setVisibility(View.VISIBLE);
				}
				else
				{
					mSpinnerAutonomousCommunity.setVisibility(View.GONE);
					mTextViewAutonomousCommunity.setVisibility(View.GONE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) 
			{
			}
		});
           
        Button buttonSearch = (Button) findViewById(R.id.buttonSearch_regulation);      
        buttonSearch.setOnClickListener(new OnClickListener () 
        {
			@Override
			public void onClick(View _view) 
			{
				Intent intent = new Intent(getBaseContext(), ListRegulationsActivity.class);
				intent.putExtra(EXTRA_TYPE, (int)mSpinnerTypeOfRegulation.getSelectedItemId());
				intent.putExtra(EXTRA_SUBTYPE, (int)mSpinnerSubtypeOfRegulation.getSelectedItemId());
				if (mSpinnerAutonomousCommunity.getVisibility()==View.VISIBLE)
				{
					intent.putExtra(EXTRA_AUTONOMOUS_COMMUNITY, (int) mSpinnerAutonomousCommunity.getSelectedItemId());
				}
				startActivity(intent);
			}
        });
        
		mAdapterTypeOfRegulation = new SimpleCursorAdapter(this, 
														   android.R.layout.simple_spinner_item, 
														   null, 
														   TYPE_OF_LOCATION_COLUMNS_FROM, 
														   VIEWS_TO,
														   0);
		mAdapterTypeOfRegulation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerTypeOfRegulation.setAdapter(mAdapterTypeOfRegulation);

        mAdapterSubtypeOfRegulation = new SimpleCursorAdapter(this, 
        													  android.R.layout.simple_spinner_item, 
        													  null, 
        													  TYPE_OF_LOCATION_COLUMNS_FROM, 
        													  VIEWS_TO,
        													  0);
        mAdapterSubtypeOfRegulation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerSubtypeOfRegulation.setAdapter(mAdapterSubtypeOfRegulation);
        
        mAdapterAutonomousCommunity = new SimpleCursorAdapter(this, 
        												   android.R.layout.simple_spinner_item, 
        												   null, 
        												   COLUMNS_FROM, 
        												   VIEWS_TO,
        												   0);
        mAdapterAutonomousCommunity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerAutonomousCommunity.setAdapter(mAdapterAutonomousCommunity);
        
        getSupportLoaderManager().initLoader(TYPE_OF_REGULATION_LOADER_ID, null, new TypeOfRegulationCursorLoaderCallback());
        getSupportLoaderManager().initLoader(SUBTYPE_OF_REGULATION_LOADER_ID, null, new SubtypeOfRegulationCursorLoaderCallback());
        getSupportLoaderManager().initLoader(AUTONOMOUS_COMMUNITY_LOADER_ID, null, new AutonomousCommunityCursorLoaderCallback());
    }
    
    @Override
	public void onDestroy()
	{
    	getSupportLoaderManager().destroyLoader(AUTONOMOUS_COMMUNITY_LOADER_ID);
    	getSupportLoaderManager().destroyLoader(SUBTYPE_OF_REGULATION_LOADER_ID);
    	getSupportLoaderManager().destroyLoader(TYPE_OF_REGULATION_LOADER_ID);
    	
    	super.onDestroy();
	}

	@Override
	protected void onStop() 
	{
	    if (mSpinnerAutonomousCommunity.getVisibility()==View.GONE)
	    {
	    	mAutonomousCommunityPosition = 8;
	    }
	    else
	    {
	    	mAutonomousCommunityPosition = (int) mSpinnerAutonomousCommunity.getSelectedItemPosition();	
	    }
		
		super.onStop();
	}
	
	private class AutonomousCommunityCursorLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor>
	{
		// In the constructor, obtain a new Loader
		@Override
		public Loader<Cursor> onCreateLoader(int _id, Bundle _args) 
		{
			return new AutonomousCommunityCursorLoader(getBaseContext(), Boolean.FALSE);
		}

		@Override
		public void onLoadFinished(Loader<Cursor> _loader, Cursor _cursor) 
		{
			if ((_cursor != null) && (!_cursor.isClosed()))
			{
				mAdapterAutonomousCommunity.swapCursor(_cursor);
			}
		}

		@Override
		public void onLoaderReset(Loader<Cursor> _loader) 
		{
			mAdapterAutonomousCommunity.swapCursor(null);
		}
	}

	private class TypeOfRegulationCursorLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor>
	{
		// In the constructor, obtain a new Loader
		@Override
		public Loader<Cursor> onCreateLoader(int _id, Bundle _args) 
		{
			return new TypeOfRegulationCursorLoader(getBaseContext());
		}

		@Override
		public void onLoadFinished(Loader<Cursor> _loader, Cursor _cursor) 
		{
			if ((_cursor != null) && (!_cursor.isClosed()))
			{
				mAdapterTypeOfRegulation.swapCursor(_cursor);
			}
		}

		@Override
		public void onLoaderReset(Loader<Cursor> _loader) 
		{
			mAdapterTypeOfRegulation.swapCursor(null);
		}
	}
	
	private class SubtypeOfRegulationCursorLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor>
	{
		// In the constructor, obtain a new Loader
		@Override
		public Loader<Cursor> onCreateLoader(int _id, Bundle _args) 
		{
			return new SubtypeOfRegulationCursorLoader(getBaseContext());
		}

		@Override
		public void onLoadFinished(Loader<Cursor> _loader, Cursor _cursor) 
		{
			if ((_cursor != null) && (!_cursor.isClosed()))
			{
				mAdapterSubtypeOfRegulation.swapCursor(_cursor);
			}
		}

		@Override
		public void onLoaderReset(Loader<Cursor> _loader) 
		{
			mAdapterSubtypeOfRegulation.swapCursor(null);
		}
	}
	
}


