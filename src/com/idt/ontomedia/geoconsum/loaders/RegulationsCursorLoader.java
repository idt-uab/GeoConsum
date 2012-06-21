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

import com.idt.ontomedia.geoconsum.RegulationSearchActivity;
import com.idt.ontomedia.geoconsum.SearchDialogActivity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

/**
 * 
 * @author Ruben Serrano
 *
 */
public class RegulationsCursorLoader extends DatabaseCursorLoader
{	
	private Bundle mBundle;
	
	public RegulationsCursorLoader(Context _context, Bundle _bundle) 
	{
		super(_context, "");
		mBundle = _bundle;
	}

	@Override
	protected Cursor placeQuery() 
	{ 	
		// -1 means all the types
    	int type = mBundle.getInt(RegulationSearchActivity.EXTRA_TYPE, -1);
    	// -1 means all the subtypes
    	int subtype = mBundle.getInt(RegulationSearchActivity.EXTRA_SUBTYPE, -1); 
    	 // 0 means that the scope of the regulation is the whole state
    	int autonomousCommunity = mBundle.getInt(RegulationSearchActivity.EXTRA_AUTONOMOUS_COMMUNITY, 0);
    	String text = mBundle.getString(SearchDialogActivity.EXTRA_TEXT_TO_SEARCH);
		
    	mCursor = getDatabaseAdapter().getRegulationsList(type, subtype, autonomousCommunity, text);
		
		return mCursor;
	}
}
