package com.android.OsmNavigator;

import java.util.Timer;

import android.app.Activity;

import android.app.AlertDialog;
//import android.content.Context;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;

import android.widget.*;
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
	private Paint   paint = new Paint();
	private  Path path = new Path();
	Canvas mapCanvas  = new Canvas();
	
	/* GPS reading */
	private GPS gps;
	private LocationManager lm;
	private Handler mockLocationUpdater = new Handler(){
		
		public void handleMessage(Message m){
			//showToast(gps.location.toString());
			mLocationListener.onLocationChanged(gps.location);
			//lm.setTestProviderLocation(LocationManager.GPS_PROVIDER, gps.location);
		}
    }; 
    
	private static final String LOG_TAG = "Location Service";
    private LocationListener mLocationListener;

	//private View map;
	private OsmMapView osmMapView;
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
				
		setContentView(R.layout.main);
		
		//RelativeLayout.LayoutParams mapLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //mapLayoutParams.topMargin = 100;// android:layout_below
        
        //this.addContentView(map, mapLayoutParams);
		/*try{
        map = findViewById(R.id.MAP);
		//setContentView(map);
		map.setVisibility(android.view.View.INVISIBLE);//setVisibility(int)
		} catch (Exception e){
			
		}
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
        msgTextView.setText("Press LOAD MAP to begin");
        
        //msgTextView.setText("loading maps...");
        
        /*MapInformation = (TextView)findViewById(R.id.mapinfo);
        MapInformation.setVisibility(View.INVISIBLE);
        */
        simpleAlertDialog = new AlertDialog.Builder(this);
        
        // reading xml osm
        OSM = new OsmFileReader();
        OSM.readXMLfromResource(this);
        
        
        CONTINUEbtn = (Button)findViewById(R.id.ok);
        CONTINUEbtn.setOnClickListener( mCorkyListener);
        
        /* init GPS reading */
        gps = new GPS(mockLocationUpdater);
        initLocation();
        
        /* reading map view */
        osmMapView = (OsmMapView)findViewById(R.id.MAP);
        
        
    }
	
	@Override
    protected void onDestroy() {
        super.onDestroy();
        getLocationManager().removeUpdates(mLocationListener);
    }
	
	private void parsingOpenStreetMap(){
		try{
			Toast.makeText(getBaseContext(), 
                    "Parsing OSM map data", 
                    20).show();
    		if(OSM.parseStructure() == false){
    		  showSimpleAlertDialog("Parsing OSM data", OSM.IOError + "\n" + OSM.SAXError);
    		} else {
    			//showSimpleAlertDialog("Parsing OSM data","OK");
    			// show handling
    			OSM.setMessageHandler(msgTextView);
    			mapParsed = true;
    			msgTextView.setText("Location: " + gps.location.getLatitude() + ", " + gps.location.getLongitude());
    			
    			CONTINUEbtn.setVisibility(View.INVISIBLE);
    			osmMapView.drawOpenStreetMapNodes(OSM.osmHandler.openStreetMap);
    			osmMapView.drawOpenStreetMapWays(OSM.osmHandler.openStreetMap);
    			//map.setVisibility(android.view.View.VISIBLE);
    			//MapInformation.setText(OSM.osmHandler.openStreetMap.getXML());
    			//msgTextView.setText(OSM.osmHandler.openStreetMap.getXML());
    		}
    		//msgTextView.setText(OSM.getXMLString());
    	} catch (Exception e){
    		msgTextView.setText("errore generico");
    		showSimpleAlertDialog("Parsing Error","LocalException: " + e.getMessage() + "\n\nSAXException: " + OSM.SAXError + "\n\nIOException: " + OSM.IOError);
    	}
	}
	
	/*  override method listener keycodes */
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		return false;
	}
	
	public void showSimpleAlertDialog(String title, String message){
		simpleAlertDialog.setMessage(message);
		simpleAlertDialog.setTitle(title);
		simpleAlertDialog.setCancelable(true);
		simpleAlertDialog.setPositiveButton("OK",onSimpleAlertListener);
		simpleAlertDialog.show();
        
	}
	public void showToast(String text){
		Toast.makeText(this, text, 10).show();
		
	}
	
	private DialogInterface.OnClickListener onSimpleAlertListener = new DialogInterface.OnClickListener(){
		

		@Override
		public void onClick(DialogInterface dialog, int which) {
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
				
				String placeToFind = result.getString("tag");
				
				String simplePattern = "";
				
				if(placeToFind.lastIndexOf("-w-") != -1){
					simplePattern  = placeToFind.substring(0,placeToFind.lastIndexOf("-w-"));
				} else {
					simplePattern  = placeToFind.substring(0,placeToFind.lastIndexOf("-n-"));
				}
			    
				
				
				String coordinates[] = OSM.osmHandler.openStreetMap.searchNode(simplePattern);
				
				
				
				// updating gps
				gps.target.setLatitude(Double.parseDouble(coordinates[0]));
				gps.target.setLongitude(Double.parseDouble(coordinates[1]));
				
				showToast( "Target: " + simplePattern + "\n" + coordinates[0] + ", " +  coordinates[1] + "\n\n" +
				           "Location: " + "\n" + gps.location.getLatitude() + ", " + gps.location.getLongitude() +
				           "\n\nDistance: " + gps.getHaversineDistance()	   
				           );
				
				this.msgTextView.setText("Location: via Fiori Chiari \n" + "Target: " + simplePattern + 
						   "\nDistance: " + gps.getSmartHaversineDistance());
				
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
	    	gps.stopLocationUpdater();
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
	/*
	 * 
	 *  LOCATION MANAGER 
	 *  
	 *  
	 *  */
	private LocationManager getLocationManager() {
        return (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    }
	private void initLocation() {
		mLocationListener = new gpsLocationListener();
		lm = getLocationManager();
		 
		try{
			lm.requestLocationUpdates( LocationManager.GPS_PROVIDER,0,0,mLocationListener);
			gps.startLocationUpdater();
		    
			//msgTextView.setText("initial" + lm.getLastKnownLocation("gps").toString());
			//msgTextView.setText(lm.getProviders(true).toString());
		} catch(Exception e){
		    showSimpleAlertDialog(LOG_TAG, e.toString());
		}   //mLocationListener = new SampleLocationListener();
            //getLocationManager().requestLocationUpdates(PROVIDER_NAME, 0, 0, mLocationListener );
    }
	private int listenerupdate = 0;
	
	public class gpsLocationListener implements LocationListener {
        private String[] STATUS = {"Out of order", "temporary unavailable", "available"}; 
        
       
		public void onLocationChanged(Location location) {
			listenerupdate++;
        	if (location != null) {
			
			msgTextView.setText(listenerupdate+"Location changed : Lat: "+ location.getLatitude());
			
			if(gps.isTarget == true){
				// compute distance
				msgTextView.setText("Location distance: "+ gps.getSmartHaversineDistance());
			}
			//showSimpleAlertDialog(LOG_TAG, listenerupdate+"Location changed : Lat: "+ location.getLatitude() );
			/* Toast.makeText(getBaseContext(), 
                    "Location changed : Lat: " + location.getLatitude() + 
                    " Lng: " + location.getLongitude(), 
                    10).show();
			*/
			} else {
				msgTextView.setText(listenerupdate + " null message");
			}
		}

		

		@Override
		public void onProviderEnabled(String provider) {
			
			showSimpleAlertDialog(LOG_TAG, "Enabled.");
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
			
			
			Toast.makeText(getBaseContext(), 
                    "Status : "  + STATUS[status] + "\n"+extras.toString(), 
                    10).show();
		}



		@Override
		public void onProviderDisabled(String provider) {
			
			
		}
		
	};
	
   
}


