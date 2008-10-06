/* 
 * Copyright (C) 2007 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.OsmNavigator;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;


import javax.microedition.khronos.egl.*;
import javax.microedition.khronos.opengles.*;

/**
 * An implementation of SurfaceView that uses the dedicated surface for
 * displaying an OpenGL animation.  This allows the animation to run in
 * a separate thread, without requiring that it be driven by the update
 * mechanism of the view hierarchy.
 */
public class OsmMapView extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder mHolder;
    private boolean mHasSurface;
    //private Cube mCube;
    private float mAngle;
    Context context;
    
    OsmMapView(Context _context) {
        super(_context);
        context = _context;
        init();
    }

    public OsmMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    private void init() {
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed 
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
    }
    
    public void surfaceCreated(SurfaceHolder holder) {
    	paint_node.setStrokeWidth(2);
    	paint_node.setAntiAlias(true);
    	paint_node.setColor(Color.RED);
       
    }
    Paint paint_node = new Paint();
    int updateRequested = 0;
    
    @Override protected void onDraw(Canvas canvas){
    	updateRequested++;
    	if(nodes != null){
    		
    	  canvas.drawPoints(nodes,paint_node);
    	  //canvas.drawLine(0, 100, 100, 200, paint_node);
    	  toastIt(updateRequested + " drawn " + nodes.length + "\n"+nodes[nodes.length - 1]); 
    	}
    }
    public void surfaceDestroyed(SurfaceHolder holder) {
        
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        
    }

    // ----------------------------------------------------------------------
    private void toastIt(String t){
   	 Toast.makeText(getContext(), t, 5).show();
   }
    
    float[] nodes;
    
    MapBounds mapBounds = new MapBounds();
    CanvasBounds canvasBounds = new CanvasBounds();  
    
    public void constrainToMapBounds(String minlat, String minlon, String maxlat, String maxlon){
    	mapBounds.minlat = Double.parseDouble(minlat);
    	mapBounds.minlon = Double.parseDouble(minlon);
    	mapBounds.maxlat = Double.parseDouble(maxlat);
    	mapBounds.maxlon = Double.parseDouble(maxlon);
    	
    	/*save SurfaceView Width / Height */
    	
    	Canvas mcanvas = mHolder.lockCanvas();
    	
    	canvasBounds.width = mcanvas.getWidth();
    	canvasBounds.height = mcanvas.getHeight();
    	
    	mapBounds.dLongitude = (mapBounds.maxlon-mapBounds.minlon);
    	mapBounds.dLatitude = (mapBounds.maxlat-mapBounds.minlat);
    	
    	toastIt(mapBounds.dLongitude+ ","+     	mapBounds.dLatitude);
    	
    }
    public NodePoint convertToXY(String lat, String lon){
    	float x = (float) (Float.parseFloat(lat)*( mapBounds.dLatitude/canvasBounds.height)) ;
    	float y = (float) (Float.parseFloat(lon)*( mapBounds.dLongitude/canvasBounds.width)) ;
    	//canvasBounds.width
    	return new NodePoint(x,y);
    	
    }
    public void drawOpenStreetMapNodes(OpenStreetMap osm){
    	/* constrain to boundaries */
    	constrainToMapBounds(osm.minlat, osm.minlon, osm.maxlat,osm.maxlon);
    	toastIt("amount: " + osm.nodes.size());
    	try{
	    	/*create points*/
	    	nodes = new float[osm.nodes.size()*2];
	    	NodePoint point;
	    	int pointer = -1;
	    	
	    	for(int i = 0; i < osm.nodes.size(); i++){
	    		point = convertToXY(osm.nodes.get(i).lat, osm.nodes.get(i).lon);
	    		
	    		pointer++;
	    		nodes[pointer]   =  point.x; // pX
	    		pointer++;
	    		nodes[pointer] =  point.y; // pY	
	    		
	    	}
	    	toastIt("utlimo pointer: " + pointer + " y:" + nodes[pointer]);
	    	/*notice drawings*/
    	} catch(Exception e){
    		toastIt("Exception " + e.getMessage() + " " + osm.nodes.size());
    	}
    }
    
    public void addNodesToDraw(Node node){
    	
    }
    
    class CanvasBounds{
    	int height = 0;
    	int width = 0;
    	double widthLongitude = 0;
    }
    class MapBounds{
    	Double minlat, maxlat, minlon, maxlon;
    	Double dLongitude, dLatitude;
    }
    class NodePoint{
    	float x;
    	float y;
    	NodePoint(float _x, float _y){
    		x = _x;
    		y = _y;
    	}
    }
}

