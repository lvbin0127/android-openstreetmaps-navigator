# Introduction #

---

GPS usage in Android, see [LocationManager API](http://code.google.com/android/toolbox/apis/lbs.html)
##  ##
Here a **to-do-list**
> ### open an useful android tool, **Dalvik Debug monitor** ###
    * set the environment. Open a CMD (prompt), point to android dir location where you can find tools subdir. (es, D:\android\tools). A _Dalvik Debug monitor_ is now open. [more about this tool here](http://code.google.com/android/reference/ddms.html#emulator-control)
    * write command **ddms**
```
D:\android\tools>ddms
```
> ##  ##
> ### setting up a **MOCK GPS TRACKER** ###
> 
---

    * download a _.klm_ file (gps tracking), here some [documentation](http://code.google.com/apis/kml/documentation/) about flm file and samples.
    * In _Dalvik Debug monitor_ go to _emulator control_; then in this tab click on _Location Controls_ -> klm -> Load Klm an browse to your klm file
    * When you need GPS, click on play button located at the bottom of this window
> ##  ##
> ### setting up **permissions** ###
> 
---

    * add the _ACCESS\_FINE\_LOCATION_ permission to the _AndroidManifest.xml_ file, like [here](http://www.devx.com/wireless/Article/39239/1954?pf=true), before any 

&lt;application&gt;

_tag
```
<uses-permission 
   android:name="android.permission.ACCESS_FINE_LOCATION" />
```
> ##  ##
> ### code the activity to use the **Location Manager** ###
> 
---

    * obtain the LOCATION\_SERVICE service and init it. Note that the method LocationManager.requestLocationUpdates(..) require a Listener. We'll code it next step.
```
    /* obtain the SERVICE */
    private LocationManager getLocationManager() {
        return (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    }

    /* init the SERVICE */

    private void initLocation() {

	LocationManager lm = getLocationManager();

	try{

        	lm.requestLocationUpdates("gps",0,0,mLocationListener);
	
        } catch(Exception e){  
           /*... 
             Handle an exception, usually a SecurityException, see PERMISSION above
           */
        }
    }  
```
    * setting up the_Listener_, somewhere in activity class
```
    private LocationListener mLocationListener = new LocationListener(){

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
	};
```
#  #
# GPS.java handles Location, distance, angles #

---

GPS.java class uses Harvestine algorithm to calculate air-distances, waiting for some routing algorithm._

