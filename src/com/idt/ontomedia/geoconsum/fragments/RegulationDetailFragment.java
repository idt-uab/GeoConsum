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

import com.idt.ontomedia.geoconsum.R;
import com.idt.ontomedia.geoconsum.adapters.DatabaseAdapter;
import com.idt.ontomedia.geoconsum.loaders.SingleRegulationCursorLoader;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 
 * @author Ruben Serrano
 * @author Joan Fuentes
 *
 */
public class RegulationDetailFragment extends DetailsBaseFragment 
{
	public RegulationDetailFragment()
	{
		super();
	}
	
	public RegulationDetailFragment(Context _context) 
	{
		super(new SingleRegulationCursorLoader(_context, ((Activity)_context).getIntent().getExtras()));
	}

	@Override
	public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) 
	{
		return super.onCreateView(_inflater, _container, R.layout.regulationdetail);
	}
	
	@Override
	protected void onFinishedLoadDetails()
	{
		super.onFinishedLoadDetails(R.id.regulationdetail_rl_main);
	}

	@Override
	protected void loadDetails(Cursor _cursor)
	{
		// Nom
		setAvailableText(_cursor, R.id.regulationdetail_text_name, DatabaseAdapter.COLUMN_NAME);
		
		// Àmbit
		setAvailableText(_cursor, R.id.regulationdetail_text_scope, DatabaseAdapter.COLUMN_VINCULATION);
		
		// Tipo
		TextView textView = (TextView) getActivity().findViewById(R.id.regulationdetail_text_type);
		setAvailableText(textView, 
						 _cursor.getString(_cursor.getColumnIndex(DatabaseAdapter.COLUMN_REGULATION_TYPE)) + 
								" - " + 
								_cursor.getString(_cursor.getColumnIndex(DatabaseAdapter.COLUMN_COMMUNITY)), 
						 NO_LINKIFY);
		
		// Subtipo
		setAvailableText(_cursor, R.id.regulationdetail_text_subtype, DatabaseAdapter.COLUMN_REGULATION_SUBTYPE);
		
		// Descripcion
		setAvailableText(_cursor, R.id.regulationdetail_text_descripcion, DatabaseAdapter.COLUMN_CONTENT);
		
		setURL(_cursor, R.id.regulationdetail_text_url, R.id.regulationdetail_title_url, R.id.regulationdetail_imagebutton_url);
	}
}