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

package com.idt.ontomedia.geoconsum.entities;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * 
 * @author Ruben Serrano
 *
 */
public class MediOverlay extends ItemizedOverlay<OverlayItem> 
{
	private ArrayList<OverlayItem> mOverlays;
	private Context mContext;
	
	public MediOverlay(Drawable _defaultMarker, Context _context) 
	{
		super(boundCenterBottom(_defaultMarker));
		mContext = _context;
		mOverlays = new ArrayList<OverlayItem>();
	}
	
	public void addOverlay(OverlayItem _overlay) 
	{
	    mOverlays.add(_overlay);
	    populate();
	}

	@Override
	protected OverlayItem createItem(int _index) 
	{
		return mOverlays.get(_index);
	}

	@Override
	public int size() 
	{
		return mOverlays.size();
	}
	
	@Override
	protected boolean onTap(int _index) 
	{
		OverlayItem item = mOverlays.get(_index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();
		return true;
	}

}
