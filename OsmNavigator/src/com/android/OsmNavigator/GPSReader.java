package com.android.OsmNavigator;

public class GPSReader {
    
	
	public double latitude;
    public double longitude;
    int Radius = 6371;
    
    public GPSReader(){
    	GPS();
    }
    private void GPS(){
    	latitude = 45.42222222;
    	longitude = 29.356;
    }
    
    private double toRad(double n) {
    	  return n * Math.PI / 180;
    }
    public double Haversine(double lat1, double lon1, double lat2, double lon2){
   	 // km
   	double dLat = toRad(lat2-lat1);
   	double dLon = toRad(lon2-lon1); 
   	double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
   	        Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) * 
   	        Math.sin(dLon/2) * Math.sin(dLon/2); 
   	double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
   	return Radius * c;
   	
   }
    //public getGPSHeading(){}
    public double getlocalBearing(double flatitude, double flongitude ){
    	double dLon = (flongitude-longitude);
    	//double dLat = (flatitude-latitude);
    	
    	double dPhi = Math.log(Math.tan(flatitude/2+Math.PI/4)/Math.tan(latitude/2+Math.PI/4));
    	//double q = (Math.abs(flatitude - latitude) > 1e-10) ? (flatitude - latitude)/dPhi : Math.cos(latitude);
    	// if dLon over 180° take shorter rhumb across 180° meridian:
    	
    	if (Math.abs(dLon) > Math.PI) {
    		dLon = dLon>0 ? -(2*Math.PI-(dLon)) : (2*Math.PI+(dLon));
    	}
    	//double d = Math.sqrt(dLat*dLat + q*q*dLon*dLon) * Radius;
    	return Math.atan2(dLon, dPhi);
    }
    /*
     * Calculate geodesic distance (in m) between two points specified by latitude/longitude (in numeric degrees)
     * using Vincenty inverse formula for ellipsoids
     */
    
    public double Vincenty(double lat1, double lon1, double lat2, double lon2) {
      int a = 6378137;
      double b = 6356752.3142;
      double f = 1/298.257223563;  // WGS-84 ellipsiod
      double L = toRad(lon2-lon1);
      double U1 = Math.atan((1-f) * Math.tan(toRad(lat1)));
      double U2 = Math.atan((1-f) * Math.tan(toRad(lat2)));
      double sinU1 = Math.sin(U1), cosU1 = Math.cos(U1);
      double sinU2 = Math.sin(U2), cosU2 = Math.cos(U2);
      
      double lambda = L;
      double lambdaP = 2 * Math.PI;
      int iterLimit = 20;
      
      double cosSqAlpha = 0,sinSigma = 0,sigma = 0;
      Double cos2SigmaM = 0.0,cosSigma = 0.0;
      
      while (Math.abs(lambda-lambdaP) > 1e-12 && --iterLimit>0) {
        double sinLambda = Math.sin(lambda), cosLambda = Math.cos(lambda);
        sinSigma = Math.sqrt((cosU2*sinLambda) * (cosU2*sinLambda) + 
          (cosU1*sinU2-sinU1*cosU2*cosLambda) * (cosU1*sinU2-sinU1*cosU2*cosLambda));
        if (sinSigma==0) return 0;  // co-incident points
        cosSigma = sinU1*sinU2 + cosU1*cosU2*cosLambda;
        sigma = Math.atan2(sinSigma, cosSigma);
        double sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
        cosSqAlpha = 1 - sinAlpha*sinAlpha;
        cos2SigmaM = cosSigma - 2*sinU1*sinU2/cosSqAlpha;
        if (cos2SigmaM.isNaN() ) cos2SigmaM = 0.0;  // equatorial line: cosSqAlpha=0 (§6)
        double C = f/16*cosSqAlpha*(4+f*(4-3*cosSqAlpha));
        lambdaP = lambda;
        lambda = L + (1-C) * f * sinAlpha *
          (sigma + C*sinSigma*(cos2SigmaM+C*cosSigma*(-1+2*cos2SigmaM*cos2SigmaM)));
      }
      if (iterLimit==0) return java.lang.Double.NaN;// formula failed to converge

      double uSq = cosSqAlpha * (a*a - b*b) / (b*b);
      double A = 1 + uSq/16384*(4096+uSq*(-768+uSq*(320-175*uSq)));
      double B = uSq/1024 * (256+uSq*(-128+uSq*(74-47*uSq)));
      double deltaSigma = B*sinSigma*(cos2SigmaM+B/4*(cosSigma*(-1+2*cos2SigmaM*cos2SigmaM)-
        B/6*cos2SigmaM*(-3+4*sinSigma*sinSigma)*(-3+4*cos2SigmaM*cos2SigmaM)));
      double s = b*A*(sigma-deltaSigma);
      
      //s = s.toFixed(3); // round to 1mm precision
      return s;
    }
}
