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
import com.idt.ontomedia.geoconsum.loaders.DatabaseCursorLoader;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author Ruben Serrano
 * @author Joan Fuentes
 *
 */
public abstract class DetailsBaseFragment extends Fragment 
{
	protected static final int NO_LINKIFY = -1;
	
	public static final String EXTRA_ID_KEY  = "id";
	private DatabaseCursorLoader mLoader;
	private FragmentActivity mParentActivity;
	private View mHierarchyView;
	
	public DetailsBaseFragment()
	{
		super();
	}
	
	public DetailsBaseFragment(DatabaseCursorLoader _cursorLoader)
	{
		super();
		mLoader = _cursorLoader;
	}
	
	@Override
	public void onCreate(Bundle _savedInstanceState)
	{
		super.onCreate(_savedInstanceState);
		
		mParentActivity = getActivity();
	}
    
	public View onCreateView(LayoutInflater _inflater, ViewGroup _container, int _idLayout) 
	{
		mHierarchyView = _inflater.inflate(_idLayout, _container, false);
		getLoaderManager().initLoader(0, null, new DatabaseCursorLoaderCallback());
		return mHierarchyView;
	}
	
	@Override
	public void onDestroy()
	{
		getLoaderManager().destroyLoader(0);
		super.onDestroy();
	}
	
	protected void setURL(Cursor _cursor, int _idTextViewTextURL, int _idTextViewTitleURL, int _idImageViewURL)
	{
		final String url = _cursor.getString(_cursor.getColumnIndex(DatabaseAdapter.COLUMN_URL));
		TextView textViewText = (TextView) mParentActivity.findViewById(_idTextViewTextURL);
		TextView textviewTitle = (TextView) mParentActivity.findViewById(_idTextViewTitleURL);
		ImageView imageViewUrl = (ImageView) mParentActivity.findViewById(_idImageViewURL);
		
		if(url.length()!=0)
		{
			textViewText.setVisibility(View.VISIBLE);
			textviewTitle.setVisibility(View.VISIBLE);
			imageViewUrl.setVisibility(View.VISIBLE);
			imageViewUrl.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			
			imageViewUrl.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					String httpURL = url;

					if (!url.startsWith("http"))
					{
						httpURL ="http://"+url;
					}
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(httpURL));
					startActivity(intent);				
				}
			});
		}
	}
	
	protected void setAvailableText(Cursor _cursor, int _idTextView, String _column)
	{
		TextView textView = (TextView) mHierarchyView.findViewById(_idTextView);
		setAvailableText(textView, _cursor.getString(_cursor.getColumnIndex(_column)), NO_LINKIFY);
	}
	
	protected void setAvailableText(Cursor _cursor, int _idTextView, String _column, int _linkifyFlags)
	{
		TextView textView = (TextView) mHierarchyView.findViewById(_idTextView);
		setAvailableText(textView, _cursor.getString(_cursor.getColumnIndex(_column)), _linkifyFlags);
	}
	
	protected void setAvailableText(TextView _textView, String _content, int _linkifyFlags)
	{
		if (!(_content.equalsIgnoreCase("")))
		{
			_textView.setText(_content);
		}
		else
		{
			_textView.setText(R.string.not_available);
		}
		if (_linkifyFlags != -1)
		{
			Linkify.addLinks(_textView, _linkifyFlags);
		}
	}
	
	protected View getHierarchyView()
	{
		return mHierarchyView;
	}
	
	abstract protected void loadDetails(Cursor _cursor);
	
	abstract protected void onFinishedLoadDetails();
	
	protected void onFinishedLoadDetails(int _idLayout)
	{
		mHierarchyView.findViewById(_idLayout).setVisibility(View.VISIBLE);
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
				loadDetails(_cursor);
			}
			onFinishedLoadDetails();
		}

		@Override
		public void onLoaderReset(Loader<Cursor> _loader) 
		{
			
		}
	}
}