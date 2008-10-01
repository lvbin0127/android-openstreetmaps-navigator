package com.android.OsmNavigator;

import android.app.Activity;

import android.app.AlertDialog;
//import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.content.Intent;
import android.os.Bundle;

import android.view.*;

import android.widget.*;
import android.widget.TextView;
import android.widget.EditText;
import android.graphics.*;


public class OsmReader extends Activity {
	
	public static final int ACTIVITY_ADD=0;
	
	public AlertDialog.Builder simpleAlertDialog;
	
	private OsmFileReader OSM;
	/* UI "main.xml" items */
	private Button CONTINUEbtn;
	private TextView msgTextView, MapInformation;
	
	//private Spinner spinner;
	
	private boolean mapParsed = false;
	
	/* menu items ID */
	static final int INSERT_ID = 0;
	static final int EXIT_ID = 1;
    static final int VIEWLOG_ID = 2;
    
	private static final int ACTIVITY_CREATE = 0;
	
	//private static final String[] mappe = {"mappa Milano - Castello", "mappa Milano - Brera"};
	
	/* graphics layout */
	Map map = null;
	private Paint   paint = new Paint();
	private  Path path = new Path();
	
	/* 
	
	private String[][] locations = { { "Area 51", "-115.800155,37.248040" },
	
	            { "Bill Gates' house", "-122.242135,47.627787" },
	
	            { "Shepshed Dynamo Football Grounds", "-1.286913,52.774472" },
	
	            { "Michael Jackson's Neverland Ranch", "-120.088012,34.745527" },
	
	            { "Leaning Tower of Pisa", "10.396473,43.723002" },
	
	            { "Airplane Graveyard", "-110.834026,32.150899" },
	
	            { "Grand Canyon", "-112.298641,36.142788" },
	
	            { "Lake Kariba", "27.990417,-17.235252" },
	
	            { "White House", "-77.036519,38.897605" },
	
	            { "World Trade Center site", "-74.012253,40.711641" },
	
	            { "Las Vegas Strip", "-115.162296,36.133347" } };
	
	 
	
	private Spinner spinner;
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
       
		super.onCreate(savedInstanceState);
		this.map = new Map(this); 
		
        setContentView(R.layout.main);
		/* SPINNER 
        spinner = (Spinner) this.findViewById(R.id.spinner1);
        
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item);
        
        for (int i = 0; i < locations.length; i++)
            adapter.add(locations[i][0]);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        
        /* END SPINNER */
        /* Background images */
        ImageView image = (ImageView)findViewById(R.id.backgroundImage);
        
        msgTextView = (TextView)findViewById(R.id.title);
        msgTextView.setText("loading maps...");
        
        //msgTextView.setText("loading maps...");
        
        MapInformation = (TextView)findViewById(R.id.mapinfo);
        MapInformation.setVisibility(View.INVISIBLE);
        simpleAlertDialog = new AlertDialog.Builder(this);
        
        // reading xml osm
        OSM = new OsmFileReader();
        OSM.readXMLfromResource(this);
        
        
        CONTINUEbtn = (Button)findViewById(R.id.ok);
        CONTINUEbtn.setOnClickListener( mCorkyListener);
        
        //
    
        //paint.setColor(Color.RED);
        //paint.setStrokeWidth(1);
        
        
       
		//path.moveTo(10,10);
		//path.lineTo(100,200);
		
		
    }
	
	
	private void parsingOpenStreetMap(){
		try{
    		if(OSM.parseStructure() == false){
    		  showSimpleAlertDialog("Parsing OSM data", OSM.IOError + "\n" + OSM.SAXError);
    		} else {
    			//showSimpleAlertDialog("Parsing OSM data","OK");
    			// show handling
    			OSM.setMessageHandler(msgTextView);
    			mapParsed = true;
    			msgTextView.setText("Map loaded");
    			MapInformation.setVisibility(View.VISIBLE);
    			//MapInformation.setText(OSM.osmHandler.openStreetMap.getXML());
    			//msgTextView.setText(OSM.osmHandler.openStreetMap.getXML());
    		}
    		//msgTextView.setText(OSM.getXMLString());
    	} catch (Exception e){
    		msgTextView.setText("ERRORE NEL PARSER");
    		showSimpleAlertDialog("Parsing Error","LocalException: " + e.getMessage() + "\n\nSAXException: " + OSM.SAXError + "\n\nIOException: " + OSM.IOError);
    	}
	}
	
	/*  override method listener keycodes */
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		msgTextView.setText("pressed" + keyCode);
		return false;
	}
	
	public void showSimpleAlertDialog(String title, String message){
		simpleAlertDialog.setMessage(message);
		simpleAlertDialog.setTitle(title);
		simpleAlertDialog.setCancelable(true);
		simpleAlertDialog.setPositiveButton("OK",onSimpleAlertListener);
		simpleAlertDialog.show();
        
	}
	
	private DialogInterface.OnClickListener onSimpleAlertListener = new DialogInterface.OnClickListener(){
		

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			// cancellazione del dialogo, accettazione
			// auto accettazione msgTextView.setText("Something");
		}
	};
	
	private View.OnClickListener mCorkyListener = new View.OnClickListener()
	{
	    public void onClick(View v)
	    {
	    	//msgTextView.setText("OK; compreso");
	    	if(mapParsed == false){
	    	parsingOpenStreetMap();
	    	} else {
	    	showSimpleAlertDialog("Attenzione","Mappa già caricata");
		    }


	    }
	};
	/* ACTIVITY RESULT: SEARCH PLACE */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
	    
		if(requestCode == ACTIVITY_CREATE && resultCode == RESULT_OK){
			
				Bundle result = data.getExtras();
				
				String tag = result.getString("tag");
				String lat = result.getString("lat");
				String lon = result.getString("lon");
				
				showSimpleAlertDialog("SOMETHING","tag : " + tag + " lat: " + lat);
			 
		}
		
	}

	@Override 
    public boolean onCreateOptionsMenu(Menu menu) { 
        super.onCreateOptionsMenu(menu); 
        
        
        
        MenuItem select = menu.add(0, INSERT_ID, 0, "FIND");
        select.setIcon(R.drawable.menu_percorsi);
        
        MenuItem viewLog = menu.add(0, VIEWLOG_ID,0,"VIEW LOG");
        viewLog.setIcon(R.drawable.menu_mapinfo);
        
        MenuItem exit = menu.add(0, EXIT_ID, 0, "EXIT");
        exit.setIcon(R.drawable.menu_pexit);
        
        return true;
       
    } 
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
	    switch(item.getItemId()) {
	    case INSERT_ID:
	    	//showSimpleAlertDialog(" ","Finding and routing");
	    	if(OSM.osmHandler.openStreetMap != null){
	    		Intent i = new Intent(this, WaySelect.class);
	    		
	    		//("openstreet",OSM.osmHandler.openStreetMap);
	    		// bundle this!
	    		Bundle bundle = new Bundle();
	    		bundle.putString("hashstring","osm:"+ OSM.osmHandler.openStreetMap.generateTagString());
	    		
	    		i.putExtras(bundle);
	    		//bundle.putParcelable("openstreetmap", OSM.osmHandler.openStreetMap)
	    		
	    		startActivityForResult(i, ACTIVITY_CREATE);
	    	} else {
	    		showSimpleAlertDialog("Fake alert message","You must create a map instance pressing 'LOAD MAP'");
	    	}
	    	
	    	
	    	
	    	return true;
	    case EXIT_ID:
	        finish();
	        return true;
	    case VIEWLOG_ID:
	    	if(OSM.osmHandler.openStreetMap != null){
	    		showSimpleAlertDialog("map data",OSM.osmHandler.openStreetMap.getXML() );
		    		
	    	} else {
	    		showSimpleAlertDialog("map data", "no map data loaded.");
	    	}
	    	
	    	return true;
	    }
	    
	       
	    return super.onMenuItemSelected(featureId, item);
	}
	
}


