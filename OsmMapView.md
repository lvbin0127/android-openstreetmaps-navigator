# Introduction #

---

**OsmMapView** is the map handler class.
This is how import this View into our activity.
##  ##
In OsmReader.java, this is how to get View instance (then you can switch visiblity, write animation)
```
@Override
    public void onCreate(Bundle savedInstanceState) {
        /* ... */
        setContentView(R.layout.main);

        osmMapView = (OsmMapView)findViewById(R.id.MAP);
    }
```

In _res/layout/main.xml_, simply called in our class by the resource Identifier _R.layout.main_, we call class **com.android.OsmNavigator.OsmMapView**
```
   <FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
       android:layout_width="fill_parent"
       android:layout_height="fill_parent">
    
       <com.android.OsmNavigator.OsmMapView
           android:id="@+id/MAP"
           android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:background="@color/verde_erba"
       />
       
       <RelativeLayout  .../> // "traditional" layout
```


# Details #

---

##  ##
## onDraw ##

---

The onDraw method, displayed as:
```
@Override protected void onDraw(Canvas canvas){...}
```
allow drawing on our surface. We need a Paint first, see [android.graphics.Paint](http://code.google.com/android/reference/android/graphics/Paint.html) and we set up it in SurfaceView implemented **surfaceCreated**
```
//setting up paint 
public void surfaceCreated(SurfaceHolder holder) {
        paint_node.setStrokeWidth(2);
    	paint_node.setAntiAlias(true);
    	paint_node.setColor(Color.RED);
}
```
Then, we can draw almost everything inside **onDraw** overriden method
```
if(nodes != null){
    		
    	  canvas.drawPoints(nodes,paint_node);
}

```
where nodes is a float array of _pixel coordinates_ like this
```
float nodes[] = {x1,y1,x2,y2 ...,xn,yn};
```
##  ##
## Transforming Latitude / longitude angles into pixel ##

---

We use the [Equations of the Lambert projection](http://lazarus.elte.hu/~guszlev/vet/equation/dslambe.htm) as explayed below
##  ##
![http://lazarus.elte.hu/~guszlev/vet/equation/lambsik.gif](http://lazarus.elte.hu/~guszlev/vet/equation/lambsik.gif)
##  ##
implementation:
```
public NodePoint convertToXY(String lat, String lon){
    	
    	float phi = (float) GPS.radians(Float.parseFloat(lat));
    	float lambda = (float) GPS.radians(Float.parseFloat(lon));
    	float q = (float) (2*Math.sin( ((Math.PI/2) - phi)/2 ));
    	
    	   	
    	float x = (float)(q*Math.sin(lambda));
    	float y = (float)(q*Math.cos(lambda));
    	
    	
    	return new NodePoint(x,y);
    	
    }
```