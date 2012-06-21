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

import com.idt.ontomedia.geoconsum.fragments.ListRegulationsFragment;

import android.os.Bundle;

/**
 * 
 * @author Ruben Serrano
 *
 */
public class ListRegulationsActivity extends BaseActivity 
{
	private static final String FRAGMENT_TAG = "fragmentTag";
	
    @Override
    public void onCreate(Bundle _savedInstanceState) 
    {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.fragment_frame);
        
        getSupportFragmentManager().beginTransaction()
        				   			.add(R.id.fragment_frame_frameLayout, ListRegulationsFragment.getInstance(this), FRAGMENT_TAG)
        				   			.commit();
    }
}

