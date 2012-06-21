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

package com.idt.ontomedia.geoconsum.adapters;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.android.maps.GeoPoint;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;

/**
 * Simple session database access helper class. Defines the basic CRUD operations
 * for the application, and gives the ability to list locations, regulations,
 * Autonomous regions, places, types of location, types of regulation and
 * subtypes of location.
 * 
 * @author Joan Fuentes
 * @author Ruben Serrano
 * 
 */
public class DatabaseAdapter
{
	public static final String ISO3_LANGUAGE_CATALAN = "cat";
	public static final String ISO3_LANGUAGE_SPANISH = "spa";
	public static final String OPTION_ALL_CATALAN = "-- TOTES --";
	public static final String OPTION_ALL_SPANISH = "-- TODAS --";
	
	//The database version
    public static int DB_VERSION = 2; 
    
    //The Android's default system path of your application database.
    private static final String DB_PATH = "/data/data/com.idt.ontomedia.geoconsum/databases/";
    private static final String DB_NAME = "mediacion.db";
 
    private static final String DB_TABLE_TYPE_OF_LOCATION = "tipos_lugares";
    private static final String DB_TABLE_TYPE_OF_REGULATION = "tipos_normativas";
    private static final String DB_TABLE_SUBTYPE_OF_REGULATION = "subtipos_normativas";
    private static final String DB_TABLE_AUTONOMOUS_REGION = "comunidades";
    private static final String DB_TABLE_PROVINCE = "provincias";
    private static final String DB_TABLE_LOCATION = "localidades";
    private static final String DB_TABLE_REGULATIONS = "normativas";
    private static final String DB_TABLE_PLACES = "lugares";
    private static final String DB_TABLE_VINCULATION_TYPE = "tipo_vinculacion";
    
    public static final String COLUMN_ROWID = "_id";
    public static final String COLUMN_COMMUNITY_ID = "id_comunidad";
    public static final String COLUMN_REGULATION_TYPE_ID = "id_tipo_normativa";
    public static final String COLUMN_REGULATION_SUBTYPE_ID = "id_subtipo_normativa";
    public static final String COLUMN_PROVINCE_ID = "id_provincia";
    public static final String COLUMN_VINCULATION_ID = "id_vinculacion";
    public static final String COLUMN_LOCATION_TYPE_ID = "id_tipo_lugar";
    public static final String COLUMN_LOCATION_ID = "id_localidad";
    
    public static final String COLUMN_NAME = "nombre";
    public static final String COLUMN_NAME_ES = "nombre_ES";
    public static final String COLUMN_CONTENT = "descripcion";
    public static final String COLUMN_CONTENT_ES = "descripcion_ES";
    public static final String COLUMN_LATITUDE = "latitud";
    public static final String COLUMN_LONGITUDE = "longitud";
    public static final String COLUMN_REGULATION_TYPE = "tipo_normativa";
    public static final String COLUMN_REGULATION_SUBTYPE = "subtipo_normativa";
    public static final String COLUMN_VINCULATION = "tipo_vinculacion";
    public static final String COLUMN_COMMUNITY = "comunidad";
    public static final String COLUMN_LOCATION = "localidad";
    public static final String COLUMN_ADDRESS = "direccion";
    public static final String COLUMN_CITY = "ciudad";
    public static final String COLUMN_URL = "URL";
    public static final String COLUMN_EMAIL = "e_mail";
    public static final String COLUMN_FAX = "fax";
    public static final String COLUMN_PHONE = "telefono";
    public static final String COLUMN_PROVINCE = "provincia";
    public static final String COLUMN_LOCATION_TYPE = "tipo_lugar";
    
	// Intents for broadcasting the update database events
	public static final String UPDATE_PLACES = "com.idt.ontomedia.geoconsum.UPDATE_PLACES";
	private final Intent mUpdatePlacesIntent = new Intent(UPDATE_PLACES);
    
    private DatabaseHelper mDatabaseHelper; 
    private SQLiteDatabase mDatabase;
    private Context mContext;
 
    public DatabaseAdapter(Context _context) 
    {
    	mDatabaseHelper = new DatabaseHelper(_context);
    	mContext = _context;
    }
    
    public void create()
    {
    	mDatabaseHelper.createDatabase();
    }
 
    public void open()
    {
    	mDatabase = mDatabaseHelper.getWritableDatabase();
    }
    
    public void openReadOnly() throws SQLException 
    {
    	mDatabase = mDatabaseHelper.getReadableDatabase();
    }
    
	public void close() 
    {
	    if(mDatabaseHelper != null)
	    {
	    	mDatabaseHelper.close();
	    	mDatabase = null;
	    }
	}
	
	/**
     * Whenever that we update the database, we should inform of this fact
     * for the loader sake
     */
 	private void sendUpdatedPlacesEvent()
 	{
 		mContext.sendBroadcast(mUpdatePlacesIntent);
 	}
    
    public void insertType(int _id, String _nombre)
    {
    	ContentValues contentValues = new ContentValues();
    	contentValues.put(COLUMN_ROWID, _id);
    	contentValues.put(COLUMN_NAME, _nombre);
    	
    	mDatabase.insert(DB_TABLE_TYPE_OF_REGULATION, null, contentValues);
    }
    
    public void insertSubtype(int _id, String _nombre)
    {
    	ContentValues contentValues = new ContentValues();
    	contentValues.put(COLUMN_ROWID, _id);
    	contentValues.put(COLUMN_NAME, _nombre);
    	
    	mDatabase.insert(DB_TABLE_SUBTYPE_OF_REGULATION, null, contentValues);
    }
    
    public void insertProvince(int _id, String _nombre)
    {
    	ContentValues contentValues = new ContentValues();
    	contentValues.put(COLUMN_ROWID, _id);
    	contentValues.put(COLUMN_NAME, _nombre);
    	
    	mDatabase.insert(DB_TABLE_PROVINCE, null, contentValues);
    }
    
    public void insertAutonomousRegion(int _id, String _nombre)
    {
    	ContentValues contentValues = new ContentValues();
    	contentValues.put(COLUMN_ROWID, _id);
    	contentValues.put(COLUMN_NAME, _nombre);
    	
    	mDatabase.insert(DB_TABLE_AUTONOMOUS_REGION, null, contentValues);
    }    
    
    public void insertLocation(int _id, String _nombre)
    {
    	ContentValues contentValues = new ContentValues();
    	contentValues.put(COLUMN_ROWID, _id);
    	contentValues.put(COLUMN_NAME, _nombre);
    	
    	mDatabase.insert(DB_TABLE_LOCATION, null, contentValues);
    }
    
    public void insertPlaceType(int _id, String _nombre)
    {
    	ContentValues contentValues = new ContentValues();
    	contentValues.put(COLUMN_ROWID, _id);
    	contentValues.put(COLUMN_NAME, _nombre);
    	
    	mDatabase.insert(DB_TABLE_TYPE_OF_LOCATION, null, contentValues);
    }
 
    public void updateCoordenateOfPlace(int _id, int _latitud, int _longitud)
    {
    	ContentValues contentValues = new ContentValues();
    	contentValues.put(COLUMN_LATITUDE, _latitud);
    	contentValues.put(COLUMN_LONGITUDE, _longitud);
    	
    	String where = COLUMN_ROWID + "=" + _id;
    	
    	int recordsUpdated = mDatabase.update(DB_TABLE_PLACES, contentValues, where, null);
    	
    	if (recordsUpdated > 0)
    	{
    		sendUpdatedPlacesEvent();
    	}
    }
    
    public Cursor getTypeOfLocation(String _ISO3Language)
    {
    	String select[];
    	
        if (_ISO3Language.equalsIgnoreCase(ISO3_LANGUAGE_CATALAN))
        {
            select = new String[] { COLUMN_ROWID, COLUMN_NAME };
        }
        else
        {
            select = new String[] { COLUMN_ROWID , COLUMN_NAME_ES };
        }
        
        String orderBy = COLUMN_ROWID;
        
        return mDatabase.query(DB_TABLE_TYPE_OF_LOCATION, select, null, null, null, null, orderBy);
    }
    
    public Cursor getTypeOfLocationDescription(long _id, String _ISO3Language)
    {
    	String select[];
    	
        if (_ISO3Language.equalsIgnoreCase(ISO3_LANGUAGE_CATALAN))
        {
            select = new String[] { COLUMN_CONTENT };
        }
        else
        {
            select = new String[] { COLUMN_CONTENT_ES };
        }
        
        String where = "[" + COLUMN_ROWID + "] = " + _id;
        
        String orderBy = COLUMN_ROWID;
        
        Cursor cursorData = mDatabase.query(DB_TABLE_TYPE_OF_LOCATION, select, where, null, null, null, orderBy);
    	
        return cursorData;
    }
    
    public Cursor getAutonomousRegion(boolean _optionAllRegionsToo)
    {
    	String[] select = {COLUMN_ROWID, COLUMN_NAME};
    	
    	String where = "";
    	
    	if(!_optionAllRegionsToo)
    	{
    		where = COLUMN_ROWID + ">0";
    	}
    	
    	String orderBy = COLUMN_ROWID;
    	
    	return mDatabase.query(DB_TABLE_AUTONOMOUS_REGION, select, where, null, null, null, orderBy);
    }    
    
    public Cursor getProvincesByAutonomousRegion(int _id)
    {
    	String[] select = {COLUMN_ROWID, COLUMN_NAME};
    	
    	String where = COLUMN_COMMUNITY_ID + "=" + _id + " " +
    					"OR " + COLUMN_ROWID + "=-1";
    	
    	String orderBy = COLUMN_NAME;
    	
        return mDatabase.query(DB_TABLE_PROVINCE, select, where, null, null, null, orderBy);
    }
    
    public Cursor getLocationsByProvinces(int _id)
    {
    	String[] select = {COLUMN_ROWID, COLUMN_NAME};
    	
    	String where = COLUMN_PROVINCE_ID + "=" + _id + " " +
    					"OR " + COLUMN_ROWID + "=-1";
    	
    	String orderBy = COLUMN_NAME;
    	
        return mDatabase.query(DB_TABLE_LOCATION, select, where, null, null, null, orderBy);
    }
    
    public Cursor getTypeOfRegulation(String _ISO3Language)
    {
    	String select[];
        
    	if (_ISO3Language.equalsIgnoreCase(ISO3_LANGUAGE_CATALAN))
        {
    		select = new String[] { COLUMN_ROWID, COLUMN_NAME };
        }
        else
        {
        	select = new String[] { COLUMN_ROWID , COLUMN_NAME_ES };
        }
    	
    	String orderBy = COLUMN_NAME;
        
        return mDatabase.query(DB_TABLE_TYPE_OF_REGULATION, select, null, null, null, null, orderBy);
    }
    
    public Cursor getSubtypeOfRegulation(String _ISO3Language)
    {
    	String select[];
    	
        if (_ISO3Language.equalsIgnoreCase(ISO3_LANGUAGE_CATALAN))
        {
        	select = new String[] { COLUMN_ROWID, COLUMN_NAME };
        }
        else
        {
        	select = new String[] { COLUMN_ROWID , COLUMN_NAME_ES };
        }
        
        String orderBy = COLUMN_NAME;
    	
    	return mDatabase.query(DB_TABLE_SUBTYPE_OF_REGULATION, select, null, null, null, null, orderBy);
    }
    
    public Cursor getRegulationsList(int _type, int _subtype, int _autonomousCommunity, String _text)
    {
    	String[] select = {COLUMN_ROWID, COLUMN_NAME, COLUMN_CONTENT};
    	
    	String where = "1=1 ";

		//Debemos limitar las normativas autonómicas a sólo las Catalanas
    	//Cuando se seleccione normativa autónomica, ya vendrá limitado (pues se pasa la comunidad)
    	//pero no cuando la normativa sea "TODAS". Por ello lo tenemos en cuenta y si se ha seleccionado
    	//todas (_type=-1) seleccionamos todas aquellas con id_comunidad a 9(cataluña) y a 0 (cualquiera que no
    	//sea autónomica)
    	if (_type > -1)
    	{
    		where += "AND " + COLUMN_REGULATION_TYPE_ID + "=" + _type + " ";
    	}else
    	{
    		where += "AND (" + COLUMN_COMMUNITY_ID + "=9 or " + COLUMN_COMMUNITY_ID + "=0) ";
    	}
    	if (_subtype > -1)
    	{
    		where += "AND " + COLUMN_REGULATION_SUBTYPE_ID + "=" + _subtype + " ";
    	}
    	if (_autonomousCommunity > 0)
    	{
    		where += "AND " + COLUMN_COMMUNITY_ID + "=" + _autonomousCommunity + " ";
    	}
    	
    	if ((_text != null)&&(!_text.equals("")))
    	{
			
			String[] arrayWords = _text.split("\\+");
			
			String subquery = "";
			
			for (int i = 0; i < arrayWords.length; i++) 
			{
				subquery = subquery + COLUMN_CONTENT + " LIKE \"%" + arrayWords[i] + "%\" OR ";
			}
			subquery = subquery.substring(0, subquery.length()-4);// We substract the last " OR " substring
			where += "AND (" + subquery + ")";
    	}
    	
    	return mDatabase.query(DB_TABLE_REGULATIONS, select, where, null, null, null, null);
    }
    
    public Cursor getRegulation(int _id, String _language)
    {	
    	String dataByLanguage;
    	
    	if (_language.equalsIgnoreCase(ISO3_LANGUAGE_CATALAN))
    	{
    		dataByLanguage = COLUMN_NAME;
    	}
    	else
    	{
    		dataByLanguage = COLUMN_NAME_ES;
    	}
    			
    	String query = "SELECT n.[" + COLUMN_ROWID + "], " +
    						  "n.[" + COLUMN_NAME + "], " +
    						  "tn.[" + dataByLanguage + "] AS " + COLUMN_REGULATION_TYPE + ", " +
    						  "sn.[" + dataByLanguage + "] AS [" + COLUMN_REGULATION_SUBTYPE + "], " +
		    				  "tv.[" + dataByLanguage + "] AS [" + COLUMN_VINCULATION + "], " +
		    				  "n.[" + COLUMN_CONTENT + "], " +
		    				  "n.[URL], " +
		    				  "c.[" + dataByLanguage + "] AS [" + COLUMN_COMMUNITY + "] " +
		    			"FROM ((([" + DB_TABLE_REGULATIONS + "] AS n INNER JOIN [" + DB_TABLE_TYPE_OF_REGULATION + "] AS tn ON tn.[" + COLUMN_ROWID + "] = n.[" + COLUMN_REGULATION_TYPE_ID + "]) " +
		    									  "INNER JOIN [" + DB_TABLE_SUBTYPE_OF_REGULATION + "] AS sn on sn.[" + COLUMN_ROWID + "] = n.[" + COLUMN_REGULATION_SUBTYPE_ID + "]) " +
		    									  "INNER JOIN [" + DB_TABLE_VINCULATION_TYPE + "] AS tv on tv.[" + COLUMN_ROWID + "] = n.[" + COLUMN_VINCULATION_ID + "]) " +
		    									  "INNER JOIN [" + DB_TABLE_AUTONOMOUS_REGION + "] AS c on c.[" + COLUMN_ROWID + "] = n.[" + COLUMN_COMMUNITY_ID + "] " +
		    			"WHERE n.[" + COLUMN_ROWID + "]=" + _id;
    	
    	return mDatabase.rawQuery(query,null);
    }
    
    public Cursor getPlacesList(int _type, int _autonomousCommunity, int _province, int _location)
    {
    	String where = "WHERE 1=1 ";
    	
    	if (_type > -1)
    	{
    		where += "AND a." + COLUMN_LOCATION_TYPE_ID + "=" + _type + " ";
    	}
    	
    	if (_autonomousCommunity > 0)
    	{
    		where += "AND a." + COLUMN_COMMUNITY_ID + "=" + _autonomousCommunity + " ";
    	}
    	
    	if (_province > -1)
    	{
    		where += "AND a." + COLUMN_PROVINCE_ID + "=" + _province + " ";
    	}
    	
    	if (_location > -1)
    	{
    		where += "AND a.id_localidad=" + _location + " ";
    	}
    	
    	where += "AND a.id_localidad=b." + COLUMN_ROWID + " ";
    	
    	String query = "SELECT  a." + COLUMN_ROWID + " AS " + COLUMN_ROWID + ", " +
    							"a." + COLUMN_NAME + " AS " + COLUMN_NAME + ", " +
    							"b." + COLUMN_NAME + " AS " + COLUMN_LOCATION + " " + 
    				   "FROM " + DB_TABLE_PLACES + " a, " +
    				   		     DB_TABLE_LOCATION + " b " +
    				   where +
    				   "ORDER BY b." + COLUMN_NAME;
    	
    	return mDatabase.rawQuery(query, null);
    }
    
    public Cursor getNearPlacesList(int[] _areaValues, GeoPoint _currentGeoPosition, int _typeOfPlace)
    {	
    	String subQueryTypeOfPlace = "";
    	//typeOfPlace==-1 is for select all "ALL" values.
    	if (_typeOfPlace!=-1)
    	{
    			subQueryTypeOfPlace = "[" + COLUMN_LOCATION_TYPE_ID + "]="+ _typeOfPlace + " AND ";
    	}
    	
    	//Limitamos a Cataluña
    	String subQueryAutonomousRegion = "lug.[" + COLUMN_COMMUNITY_ID + "]=9 AND ";
    	
    	String where = "WHERE " + subQueryTypeOfPlace + 
    							subQueryAutonomousRegion + 
    							" lug.[" + COLUMN_LATITUDE + "] BETWEEN " + _areaValues[0] + " AND " + _areaValues[1] + 
    							" AND lug.[" + COLUMN_LONGITUDE + "] BETWEEN " + _areaValues[2] + " AND " + _areaValues[3] + " ";
    	
    	String elements = "lug.[" + COLUMN_ROWID + "] AS [" + COLUMN_ROWID + "], " +
    					  "lug.[" + COLUMN_NAME + "] AS " + COLUMN_NAME + ", " +
    					  "loc.[" + COLUMN_NAME + "] AS " + COLUMN_CITY +  ", " +
    					  "lug.[" + COLUMN_ADDRESS + "] AS " + COLUMN_ADDRESS + ", " +
    					  "lug.[" + COLUMN_LATITUDE + "], " +
    					  "lug.[" + COLUMN_LONGITUDE + "]";
    	
    	String query = "SELECT " + elements + 
    					" FROM [" + DB_TABLE_PLACES + "] as lug " +
    						"INNER JOIN [" + DB_TABLE_LOCATION + "] AS loc ON loc.[" + COLUMN_ROWID + "]=lug.[" + COLUMN_LOCATION_ID + "] " 
    					+ where;
    	
    	return mDatabase.rawQuery(query,null);
    }
    
    public Cursor getPlace(int _id, String _language)
    {
    	String columnName;
    	
    	if (_language.equalsIgnoreCase(ISO3_LANGUAGE_CATALAN))
    	{
    		columnName = COLUMN_NAME;
    	}
        else
        {
        	columnName = COLUMN_NAME_ES;
        }
    	
    	String query = "SELECT places.*, " +
    						  "location." + COLUMN_NAME + " AS " + COLUMN_LOCATION + ", "+
    						  "province." + COLUMN_NAME + " AS " + COLUMN_PROVINCE + ", "+
    						  "tol." + columnName + " AS " + COLUMN_LOCATION_TYPE + " "+
    				   "FROM " + DB_TABLE_PLACES + " AS places " +
    				   		"INNER JOIN " + DB_TABLE_LOCATION + " AS location ON places." + COLUMN_LOCATION_ID + "=location." + COLUMN_ROWID + " " +
    				   		"INNER JOIN " + DB_TABLE_PROVINCE + " AS province ON places." + COLUMN_PROVINCE_ID + "=province." + COLUMN_ROWID + " " +
    				   		"INNER JOIN " + DB_TABLE_TYPE_OF_LOCATION + " AS tol ON places." + COLUMN_LOCATION_TYPE_ID + "=tol." + COLUMN_ROWID + " " +
    				   "WHERE places." + COLUMN_ROWID + "=" + _id;
    	
    	return mDatabase.rawQuery(query,null);
    }
 
    /**
     * 
     * Helper class for the database operations create, update, open and close
     * 
     * @author Joan Fuentes
     *
     */
    private class DatabaseHelper extends SQLiteOpenHelper
    {
    	
    	public DatabaseHelper(Context _context)
    	{
    		super(_context, DB_NAME, null, DB_VERSION);
    	}
    	
    	public void createDatabase()
    	{
    		this.getReadableDatabase();
			
			try 
        	{
				//Open your local db as the input stream
		    	InputStream myInput = mContext.getAssets().open(DB_NAME);
		 
		    	// Path to the just created empty db
		    	String outFileName = DB_PATH + DB_NAME;
		 
		    	//Open the empty db as the output stream
		    	OutputStream myOutput = new FileOutputStream(outFileName);
		 
		    	//transfer bytes from the inputfile to the outputfile
		    	byte[] buffer = new byte[1024];
		    	int length;
		    	
		    	while ((length = myInput.read(buffer))>0)
		    	{
		    		myOutput.write(buffer, 0, length);
		    	}
		 
		    	//Close the streams
		    	myOutput.flush();
		    	myOutput.close();
		    	myInput.close();
    		} 
        	catch (IOException e) 
        	{
    			throw new Error(e);
        	}
    	}

		@Override
    	public void onCreate(SQLiteDatabase _database) 
    	{
			
    	}
 
    	@Override
    	public void onUpgrade(SQLiteDatabase _database, int _oldVersion, int _newVersion) 
    	{

    	}
    }
}