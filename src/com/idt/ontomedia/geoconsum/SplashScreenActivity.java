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

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


/**
 * 
 * @author Joan Fuentes
 *
 */
public class SplashScreenActivity extends Activity
{
	private int SPLASH_TIME = 900; // time to display the splash screen in ms
	private WaitAsyncTask mWaitAsyncTask;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    setContentView(R.layout.splash);
	    
	    if (mWaitAsyncTask == null)
	    {
	    	mWaitAsyncTask = new WaitAsyncTask();
	    	mWaitAsyncTask.execute();
	    }
	}

	private class WaitAsyncTask extends AsyncTask<Void, Void,Void>
	{	
		@Override
		protected Void doInBackground(Void... params) 
		{
			int runningTime = 0;
			//Check when finish the minimal splash time
			while(runningTime < SPLASH_TIME )
			{
				try
				{
					Thread.sleep(250);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
				runningTime+=250;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
            /* Create an intent that will start the main activity. */
            Intent mainIntent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(mainIntent);
           
            /* Finish splash activity so user can't go back to it. */
            finish();
		}
	}   
}
