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

import com.idt.ontomedia.geoconsum.adapters.DatabaseAdapter;
import com.idt.ontomedia.geoconsum.loaders.DatabaseCursorLoader;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;

/**
 * 
 * @author Ruben Serrano
 *
 */
public abstract class GeoconsumListFragment extends ListFragment 
{  
	public static final String EXTRA_ID_KEY  = "id";
	private FragmentActivity mParentActivity;
	private SimpleCursorAdapter mCursorAdapter;
	private DatabaseCursorLoader mLoader;
	private Class<?> mDestinationActivityClass;
	
	public GeoconsumListFragment()
	{
		super();
	}
	
	public GeoconsumListFragment(DatabaseCursorLoader _cursorLoader, SimpleCursorAdapter _cursorAdapter, Class<?> _class)
	{
		super();
		mLoader = _cursorLoader;
		mCursorAdapter = _cursorAdapter;
		mDestinationActivityClass = _class;
	}
	
    @Override
    public void onCreate(Bundle _savedInstanceState) 
    {
        super.onCreate(_savedInstanceState);
        
        mParentActivity = getActivity();
    }
    
	@Override
    public void onActivityCreated(Bundle _savedInstanceState) 
    {
    	super.onActivityCreated(_savedInstanceState);
    	
    	setListAdapter(mCursorAdapter);
    	
    	getLoaderManager().initLoader(0, null, new DatabaseCursorLoaderCallback());
    }
	
    @Override
	public void onListItemClick(ListView _listView, View _view, int _position, long _id) 
	{
    	super.onListItemClick(_listView, _view, _position, _id);
		
		Cursor cursor = mCursorAdapter.getCursor();
		
		if ((cursor != null)&&(!cursor.isClosed()))
		{
			cursor.moveToPosition(_position);
			Intent intent = new Intent();
			intent.setClass(mParentActivity, mDestinationActivityClass);
			intent.putExtra(EXTRA_ID_KEY, cursor.getInt(cursor.getColumnIndex(DatabaseAdapter.COLUMN_ROWID)));
			startActivity(intent);
		}
	}
	
	@Override
	public void onDestroy()
	{
		setListAdapter(null);
		getLoaderManager().destroyLoader(0);
		super.onDestroy();
	}

	private class DatabaseCursorLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor>
	{
		// In the constructor, obtain a new Loader
		@Override
		public Loader<Cursor> onCreateLoader(int _id, Bundle _args) 
		{
			return mLoader;
		}

		@Override
		public void onLoadFinished(Loader<Cursor> _loader, Cursor _cursor) 
		{
			if ((_cursor != null) && (!_cursor.isClosed()))
			{
				mCursorAdapter.swapCursor(_cursor);
			}
		}

		@Override
		public void onLoaderReset(Loader<Cursor> _loader) 
		{
			mCursorAdapter.swapCursor(null);
		}
	}
}