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

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * 
 * @author Joan Fuentes
 *
 */
public class DialogPreferenceActivity extends PreferenceActivity 
{
	public static final String PREF_DIALOG_CHECK = "PREF_DIALOG_CHECK";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.userpreferences);
	}

}