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

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author Joan Fuentes
 *
 */
public class BaseActivity extends FragmentActivity 
{
	private static final int REPORT_BUG_ITEM_ID = 0;
	private static final int PREFERENCES_ITEM_ID = 1;
	private static final int ABOUT_ITEM_ID = 2;
    protected static final String INIT_PREFS = "INIT_PREFS";
	protected static final int DIALOG_ABOUT_ID = 0;
	public static final int DIALOG_WARNING_ID = 1;
	protected String mErrorInfo="";
    
	@Override
	public boolean onCreateOptionsMenu(Menu _menu) 
	{
		if (this instanceof PlaceDetailActivity)
		{
			_menu.add(0, REPORT_BUG_ITEM_ID, 0, R.string.menu_report_bug_title);
		}
		_menu.add(0, PREFERENCES_ITEM_ID, 1, R.string.menu_preferences_title);
		_menu.add(0, ABOUT_ITEM_ID, 2, R.string.menu_about_title);
		return super.onCreateOptionsMenu(_menu);
	}

	@Override
	public boolean onMenuItemSelected(int _featureId, MenuItem _item) 
	{
		switch(_item.getItemId())
		{
			case REPORT_BUG_ITEM_ID:
			{
				final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				emailIntent .setType("plain/text");
				emailIntent .putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{this.getResources().getString(R.string.report_bug_emailto)});
				emailIntent .putExtra(android.content.Intent.EXTRA_SUBJECT, this.getResources().getString(R.string.report_bug_subject));
				emailIntent .putExtra(android.content.Intent.EXTRA_TEXT, mErrorInfo + this.getResources().getString(R.string.report_bug_body));
				this.startActivity(Intent.createChooser(emailIntent, this.getResources().getString(R.string.report_bug_selector)));
				
				break;
			}
			case ABOUT_ITEM_ID:
			{
				showDialog(DIALOG_ABOUT_ID);
				break;
			}
			case PREFERENCES_ITEM_ID:
			{
				startActivity(new Intent(this, DialogPreferenceActivity.class));
				break;
			}
		}
		return super.onMenuItemSelected(_featureId, _item);
	} 

    @Override
	protected Dialog onCreateDialog(int _id) 
    {
        Dialog dialog = null;
        
        switch(_id) 
        {
        	case DIALOG_ABOUT_ID:
        	case DIALOG_WARNING_ID:
        	{
        		dialog = setDialogWithCheck(_id);
	            break;
        	}
        	default:
        		return null;
        }
        return dialog;
    }
	
    private Dialog setDialogWithCheck(int _id)
    {
    	final Dialog dialog = new Dialog(this);
    	
    	dialog.setContentView(R.layout.dialog_with_check);
    	
    	TextView textViewInfo = (TextView)dialog.findViewById(R.id.text_layout);
    	CheckBox checkBox = (CheckBox)dialog.findViewById(R.id.checkBox1);
    	
    	switch(_id)
    	{
    		case DIALOG_ABOUT_ID:
    		{
    			dialog.setTitle(getResources().getString(R.string.dialog_title_about)+" "+getResources().getString(R.string.app_name));
	            dialog.setCancelable(true);
	            
	            checkBox.setVisibility(View.GONE);
	            
	            ImageView imageViewBanner = (ImageView)dialog.findViewById(R.id.imageViewBanner);
	            imageViewBanner.setImageDrawable(getResources().getDrawable(R.drawable.logo_idt));
	            imageViewBanner.setVisibility(View.VISIBLE);
	            
	            textViewInfo.setText(R.string.dialog_text_about);	
    		}
    		case DIALOG_WARNING_ID:
    		{
    			dialog.setTitle(getResources().getString(R.string.dialog_title_warning));
	            dialog.setCancelable(false);
	            
	            textViewInfo.setText(R.string.dialog_text_warning);
	            
	            checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
	            {
					@Override
					public void onCheckedChanged(CompoundButton _buttonView, boolean _isChecked) 
					{
						SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
						SharedPreferences.Editor editor = sharedPreferences.edit();
						editor.putBoolean(DialogPreferenceActivity.PREF_DIALOG_CHECK, _isChecked);
						editor.commit();
					}
	            });
    		}
    	}
    	
    	Button acceptButton = (Button) dialog.findViewById(R.id.button1);
        acceptButton.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View v) 
			{
				dialog.cancel();
			}
        
        });
        
        return dialog;
    }
}
