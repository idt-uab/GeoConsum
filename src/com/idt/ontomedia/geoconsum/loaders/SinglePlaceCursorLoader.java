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

import com.idt.ontomedia.geoconsum.adapters.DatabaseAdapter;
import com.idt.ontomedia.geoconsum.fragments.GeoconsumListFragment;
import com.idt.ontomedia.geoconsum.utils.Utils;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

/**
 * 
 * @author Ruben Serrano
 *
 */
public class SinglePlaceCursorLoader extends DatabaseCursorLoader
{	
	private Bundle mBundle;
	
	public SinglePlaceCursorLoader(Context _context, Bundle _bundle) 
	{
		super(_context, DatabaseAdapter.UPDATE_PLACES);
		mBundle = _bundle;
	}

	@Override
	protected Cursor placeQuery() 
	{
		mCursor = getDatabaseAdapter().getPlace(mBundle.getInt(GeoconsumListFragment.EXTRA_ID_KEY, 1), Utils.getCurrentLanguage());
		mCursor.moveToFirst();
		
		return mCursor;
	}
}
