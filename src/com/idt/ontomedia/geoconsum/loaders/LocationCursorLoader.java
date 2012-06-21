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

import android.content.Context;
import android.database.Cursor;
import android.widget.Spinner;

/**
 * 
 * @author Ruben Serrano
 *
 */
public class LocationCursorLoader extends DatabaseCursorLoader
{	
	private Spinner mSpinnerLocation;
	
	public LocationCursorLoader(Context _context, Spinner _spinnerLocation) 
	{
		super(_context, "");
		mSpinnerLocation = _spinnerLocation;
	}

	@Override
	protected Cursor placeQuery() 
	{ 		
    	mCursor = getDatabaseAdapter().getLocationsByProvinces((int) mSpinnerLocation.getSelectedItemId());
		
		return mCursor;
	}
}
