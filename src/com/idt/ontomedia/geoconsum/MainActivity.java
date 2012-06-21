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

import com.idt.ontomedia.geoconsum.adapters.DatabaseAdapter;
import com.idt.ontomedia.geoconsum.utils.Utils;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * 
 * @author Joan Fuentes
 *
 */
public class MainActivity extends BaseActivity  
{
	private static final String PREFERENCE_FIRST_TIME = "firstTime";
	private static final String PREFERENCE_DATABASE_VERSION = "DBVersion";
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) 
	{
		super.onCreate(_savedInstanceState);
        setContentView(R.layout.main);
        
        // ********** Link buttons ********************
        ImageView button = (ImageView) findViewById(R.id.buttonRegulationsByTopic);
        button.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View view) 
            {
                Intent intent = new Intent(view.getContext(), SearchDialogActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        
        button = (ImageView) findViewById(R.id.buttonRegulationsAdvancedSearch);
        button.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View view) 
            {
            	Intent intent = new Intent(view.getContext(), RegulationSearchActivity.class);
                startActivityForResult(intent, 0);
            }

        });

        button = (ImageView) findViewById(R.id.buttonPlacesNear);
        button.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View view) 
            {
                Intent intent = new Intent(view.getContext(), NearPlacesActivity.class);
                startActivityForResult(intent, 0);            	
            }

        });
        
        button = (ImageView) findViewById(R.id.buttonPlacesAdvancedSearch);
        button.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View view) 
            {
                Intent intent = new Intent(view.getContext(), PlaceSearchActivity.class);
                startActivityForResult(intent, 0);
            }

        });
        // *********************************************
        
        SharedPreferences sharedPreferences = getSharedPreferences(INIT_PREFS, Activity.MODE_PRIVATE);
        boolean firstTime = sharedPreferences.getBoolean(PREFERENCE_FIRST_TIME, true);
        int storedDatabaseVersion = sharedPreferences.getInt(PREFERENCE_DATABASE_VERSION, 1);
        
        if ((firstTime)||(storedDatabaseVersion != DatabaseAdapter.DB_VERSION))
        {
        	startService(new Intent(this, ImportDatabaseService.class));
        }
	}

	/**
	 * 
	 * @author Ruben Serrano
	 *
	 */
	public static class ImportDatabaseService extends IntentService
	{
		public static final String SERVICE_NAME = "ImportDatabaseService";
		
		public ImportDatabaseService()
		{
			super(SERVICE_NAME);
		}
		
		public ImportDatabaseService(String _name) 
		{
			super(_name);
		}

		@Override
		protected void onHandleIntent(Intent intent) 
		{
			DatabaseAdapter databaseAdapter = new DatabaseAdapter(getApplicationContext());
	    	databaseAdapter.create();
	    	
	    	databaseAdapter.open();
	    	
	    	if (Utils.getCurrentLanguage().equalsIgnoreCase(DatabaseAdapter.ISO3_LANGUAGE_CATALAN))
	    	{
	    		databaseAdapter.insertProvince(-1, DatabaseAdapter.OPTION_ALL_CATALAN);
	    		databaseAdapter.insertLocation(-1, DatabaseAdapter.OPTION_ALL_CATALAN);
	    	}
	    	else
	    	{
	    		databaseAdapter.insertProvince(-1, DatabaseAdapter.OPTION_ALL_SPANISH);
	    		databaseAdapter.insertLocation(-1, DatabaseAdapter.OPTION_ALL_SPANISH);
	    	}
	    	
	    	databaseAdapter.close();
	    	
	    	SharedPreferences sharedPreferences = getSharedPreferences(INIT_PREFS, Activity.MODE_PRIVATE);
	    	SharedPreferences.Editor editor = sharedPreferences.edit();
        	editor.putBoolean(PREFERENCE_FIRST_TIME, false);
        	editor.putInt(PREFERENCE_DATABASE_VERSION, DatabaseAdapter.DB_VERSION);
        	editor.commit();
		}
	}
}
