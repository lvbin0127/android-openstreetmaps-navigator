package com.android.OsmNavigator;

import android.app.ListActivity;
import android.database.sqlite.*;


public class OsmSQLliteProvider extends ListActivity{
	 private final String MY_DATABASE_NAME = "myCoolUserDB"; 
     private final String MY_DATABASE_TABLE = "OsmNodes"; 
     SQLiteDatabase myDB = null; 
     
     public OsmSQLliteProvider(){
    	 
     }
}
     