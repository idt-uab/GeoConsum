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

import com.idt.ontomedia.geoconsum.PlaceDetailActivity;
import com.idt.ontomedia.geoconsum.R;

import com.idt.ontomedia.geoconsum.adapters.DatabaseAdapter;
import com.idt.ontomedia.geoconsum.loaders.PlacesCursorLoader;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SimpleCursorAdapter;

/**
 * 
 * @author Ruben Serrano
 *
 */
public class ListPlacesFragment extends GeoconsumListFragment 
{
	public ListPlacesFragment()
	{
		super();
	}
	
	private ListPlacesFragment(PlacesCursorLoader _loader, SimpleCursorAdapter _adapter, Class<?> _class)
	{
		super(_loader, _adapter, _class);
	}
	
	public static GeoconsumListFragment getInstance(Context _context)
	{
        String[] columnsFrom = {DatabaseAdapter.COLUMN_NAME, DatabaseAdapter.COLUMN_LOCATION};
    	int[] viewsTo = {R.id.textTitle, R.id.textDescription};
    	
    	SimpleCursorAdapter cursorAdapter = 
    			new SimpleCursorAdapter(_context, 
			    						R.layout.places_list_item,  
										null, 
										columnsFrom, 
										viewsTo,
										0);
    	
    	PlacesCursorLoader cursorLoader = new PlacesCursorLoader(_context, ((Activity)_context).getIntent().getExtras());
    	
    	return new ListPlacesFragment(cursorLoader, cursorAdapter, PlaceDetailActivity.class);
	}
}