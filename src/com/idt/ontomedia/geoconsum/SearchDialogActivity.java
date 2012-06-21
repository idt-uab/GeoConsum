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

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 
 * @author Joan Fuentes
 *
 */
public class SearchDialogActivity extends BaseActivity 
{    
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
	public static final String EXTRA_TEXT_TO_SEARCH = "text2Search";
	
	private TextView mTextViewSearchData;
	
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) 
	{
		super.onCreate(_savedInstanceState);
        setContentView(R.layout.main_dialog);
        
        mTextViewSearchData = (TextView) findViewById(R.id.txtSearchData);
        
        Button button = (Button) findViewById(R.id.ButtonPositive);
        button.setOnClickListener(new OnClickListener() 
    	{
			@Override
			public void onClick(View _view) 
			{
				// TODO Auto-generated method stub
		    	Intent myIntent = new Intent(getBaseContext(), ListRegulationsActivity.class);
		    	myIntent.putExtra(EXTRA_TEXT_TO_SEARCH, mTextViewSearchData.getText().toString().trim().replaceAll(" ", "+"));		    	
		        startActivityForResult(myIntent, 0);
			}
		});
    	
        button = (Button) findViewById(R.id.ButtonNegative);
        button.setOnClickListener(new OnClickListener() 
    	{
			@Override
			public void onClick(View _view) 
			{
				finish();
			}
		});    	
    	
        ImageButton imageButtonVoiceRecognition = (ImageButton) findViewById(R.id.buttonVoiceRecognition);
        // Check to see if a recognition activity is present
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        
        if (activities.size() != 0) 
        {
        	imageButtonVoiceRecognition.setOnClickListener(new OnClickListener() 
        	{
    			@Override
	            public void onClick(View view) 
    			{
	                  Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	                  intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	                  intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getResources().getString(R.string.textRecognizer));
	                  startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
	            }
	        });
        } 
        else 
        {
        	imageButtonVoiceRecognition.setEnabled(false);
        }
	}
	
	
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
        if ((requestCode == VOICE_RECOGNITION_REQUEST_CODE) && (resultCode == RESULT_OK)) 
        {
            // Fill the list view with the strings the recognizer thought it could have heard
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mTextViewSearchData.setText(matches.get(0));
        }
 
        super.onActivityResult(requestCode, resultCode, data);
    }
	
}
