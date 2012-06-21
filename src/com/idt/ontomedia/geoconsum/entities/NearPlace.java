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

/**
 * 
 * @author Joan Fuentes
 *
 */
public class NearPlace
{
	private int mID;
    private String mName;
    private String mAddress;
    private String mCity;
    private int mDistance;
  
    public NearPlace(int _id, String _name, String _city, String _address, int _distance)
    {
    	mID = _id;
    	mName = _name;
    	mAddress = _address;
        mCity = _city;
        mDistance = _distance;
    }
  
    public String getAddress()
    {
        return "(" + mCity + ") " + mAddress;
    }
  
    public void setAddress(String _address)
    {
    	mAddress = _address;
    }
  
    public int getId()
    {
        return mID;
    }
  
    public void setId(int _id)
    {
    	mID = _id;
    }
  
    public String getName()
    {
        return mName;
    }
  
    public void setName(String _name)
    {
    	mName = _name;
    }
    
    public int getDistance()
    {
        return mDistance;
    }
  
    public void setName(int _distance)
    {
    	mDistance = _distance;
    }
	
}
