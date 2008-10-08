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
import android.graphics.Path;
import android.graphics.Paint.Style;
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
       
    	paint_bounds.setStrokeWidth(0);
    	paint_bounds.setColor(Color.YELLOW);
    	
    	paint_ways.setStrokeWidth(1);
    	paint_ways.setStyle(Style.STROKE);
    	//paint_ways.set
    	paint_ways.setColor(getContext().getResources().getColor(R.color.panna));
    	paint_ways.setAntiAlias(true);
    }
    
    Paint paint_node = new Paint();
    Paint paint_bounds = new Paint();
    Paint paint_ways = new Paint();
    
    int updateRequested = 0;
    
    @Override protected void onDraw(Canvas canvas){
    	updateRequested++;
    	/*Path tester = new Path();
  	  
  	  tester.moveTo(0,0);
  	  tester.lineTo(200, 300);
  	  tester.lineTo(20, 50);
  	  canvas.drawPath(tester, paint_ways);*/
    	if(nodes != null){
    		
    	  canvas.drawPoints(nodes,paint_node);
    	  //canvas.drawLine(0, 100, 100, 200, paint_node);
    	  // draw bound rectangle
    	  
    	  constrainToCanvasBound(mapBounds.min);
    	  constrainToCanvasBound(mapBounds.max);
    	  
    	  canvas.drawLine(mapBounds.min.xc,mapBounds.min.yc, mapBounds.max.xc, mapBounds.max.yc, paint_bounds);
    	  /**/
    	  canvas.drawLine(0,0,canvas.getWidth(),canvas.getHeight(), paint_bounds);
    	  canvas.drawLine(0,0,320,430,paint_ways);
    	  
    	  
    	  
    	  //toastIt("\n" + mapBounds.dLongitude+ ","+     	mapBounds.dLatitude + "\n\n" + nodes[nodes.length - 2] + ","+ nodes[nodes.length - 1]);
    	  //toastIt(updateRequested + " drawn " + nodes.length + "\n"+nodes[nodes.length - 1]); 
    	  
    	}
    	if(ways != null){
    		for(int w = 0; w < ways.length; w++ ){
    			canvas.drawPath(ways[w], paint_ways);
    		}
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
    Path[] ways;
    
    MapBounds mapBounds = new MapBounds();
    CanvasBounds canvasBounds = new CanvasBounds();  
    
    public void constrainToMapBounds(String minlat, String minlon, String maxlat, String maxlon){
    	
    	mapBounds.min = convertToXY(minlat,minlon); 
    	mapBounds.max =	convertToXY(maxlat,maxlon);
    	
    	mapBounds.dLongitude = mapBounds.max.sub(mapBounds.min).x;
    	mapBounds.dLatitude = mapBounds.max.sub(mapBounds.min).y;
    	
    	/*save SurfaceView Width / Height */
    	
    	Canvas mcanvas = mHolder.lockCanvas();
    	
    	canvasBounds.width = mcanvas.getWidth();
    	canvasBounds.height = mcanvas.getHeight();
    	
    	canvasBounds.autoWrap();
    }
    public void constrainToCanvasBound(NodePoint P){
    	if(canvasBounds.CURRENT_WRAPPER == CanvasBounds.WRAP_HEIGHT){
    		double COEFF = canvasBounds.height/mapBounds.dLatitude;
    		P.yc = (float)( COEFF*(P.y - mapBounds.min.y) );
    		P.xc = (float)( (canvasBounds.width/mapBounds.dLongitude)*(P.x - mapBounds.min.x) );
    	}
    }
    
    /**
     * Return a new nodePoint according to equations of the Lambert projection
    	     * @param lat
    	     *            latitude (in radians)
    	     * @param lon 
    	     *            longitude (in radians); 
    	     *                      
    	     * @return new NodePoint(x,y)
    	     * 
    	     */
    public NodePoint convertToXY(String lat, String lon){
    	
    	float phi = (float) GPS.radians(Float.parseFloat(lat));
    	float lambda = (float) GPS.radians(Float.parseFloat(lon));
    	float q = (float) (2*Math.sin( ((Math.PI/2) - phi)/2 ));
    	
    	//float x = (float) (Float.parseFloat(lat)*( mapBounds.dLatitude/canvasBounds.height)- mapBounds.minlat);
    	//float y = (float) (Float.parseFloat(lon)*( mapBounds.dLongitude/canvasBounds.width)- mapBounds.minlon);
    	
    	float x = (float)(q*Math.sin(lambda));
    	float y = (float)(q*Math.cos(lambda));
    	
    	//canvasBounds.width
    	
    	return new NodePoint(x,y);
    	
    }
    /**
     * Set the points array nodes called by onDraw()
    	     * @param osm
    	     *            an OpenStreetMap instance that contains parsed osm data
    	     * 
    	     */
    
    public void drawOpenStreetMapWays(OpenStreetMap osm){
    	ways = new Path[osm.ways.size()];
    	int wayCounter = 0;
    	Node _n;
    	NodePoint _np;
    	Way _w;
    	for(int i = 0; i <osm.ways.size(); i++){
    		ways[wayCounter] = new Path();
    		ways[wayCounter].moveTo(0,0);
    		
    		_w = osm.ways.get(i);
    		    		
    		for(int j = 0; j < _w.nd.size(); j++){
    			_n = osm.getNode(_w.nd.get(j));
    			_np = convertToXY(_n.lat, _n.lon);
				constrainToCanvasBound(_np);
    			
				if(j == 0){
    				/* first node */
    				ways[wayCounter].moveTo(_np.xc, _np.yc);
    			} else {
    				ways[wayCounter].lineTo(_np.xc,_np.yc);
    			}
    		}
    		wayCounter++;
    	}
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
	    		constrainToCanvasBound(point);
	    		pointer++;
	    		nodes[pointer]   =  point.xc; // pX
	    		pointer++;
	    		nodes[pointer] =  point.yc; // pY	
	    		
	    	}
	    	toastIt("utlimo pointer: " + pointer + " y:" + nodes[pointer]);
	    	/*notice drawings*/
    	} catch(Exception e){
    		toastIt("Exception " + e.getMessage() + " " + osm.nodes.size());
    	}
    }
    
    
    
    class CanvasBounds{
    	int height = 0;
    	int width = 0;
    	
    	static final int WRAP_WIDTH = 0;
    	static final int WRAP_HEIGHT = 1;
    	
    	int CURRENT_WRAPPER = -1;
    	float WRAP_COEFF = 0;
    	
    	void setWrap(int W){
    		CURRENT_WRAPPER = W;
    	}
    	void autoWrap(){
    		if(height >width){
    			CURRENT_WRAPPER = WRAP_HEIGHT;
    			
    		} else {
    			CURRENT_WRAPPER = WRAP_WIDTH;
    		}
    	}
    	
    }
    class MapBounds{
    	NodePoint max, min;
    	float dLongitude, dLatitude;
    }
    class NodePoint{
    	float x;
    	float y;
    	float xc; // constrained on Canvas
    	float yc; // constrained on Canvas
    	NodePoint(float _x, float _y){
    		x = _x;
    		y = _y;
    	}
    	NodePoint sub(float _x, float _y){
    		return new NodePoint(x-_x, y-_y);
    	}
    	NodePoint sub(NodePoint n){
    		return new NodePoint(x-n.x,y-n.y);
    	}
    }
}

