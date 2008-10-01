package com.android.OsmNavigator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class OsmHandler extends DefaultHandler{
    
    public boolean isLoaded = false;
    public OpenStreetMap openStreetMap; 
    
    private Way way;
    private Node node;
    private Tag tag;
    
	public OsmHandler (){
		super();
		
	}
	
	public void startDocument ()
    {
		openStreetMap = new OpenStreetMap();
	}

    public void endDocument ()
    {
    	
    }
    public void startElement  (String uri, String name, String qName, Attributes atts)throws SAXException{
    	if (qName.equals ("osm")){
    		openStreetMap.setGenerator(atts.getValue("generator"));
    		openStreetMap.setVersion(atts.getValue("version"));
    		
    		
    	return;
    	}
    	else if (qName.equals("bounds")){
    		openStreetMap.setMinlat(atts.getValue("minlat"));
    		openStreetMap.setMaxlat(atts.getValue("maxlat"));
    		openStreetMap.setMinlon(atts.getValue("minlon"));
    		openStreetMap.setMaxlon(atts.getValue("maxlon"));
    		
    	return;
    	} else if(qName.equals("node")){
    		//increment node counter
    		node = new Node();
    	    
    		return;	
    	} else if(qName.equals("relation")){
    		openStreetMap.addRelation();
    		return;
    	} else if(qName.equals("way")){
    		way = new Way();
    		return;
    	} else if(qName.equals("nd")){
    		way.addNode(atts.getValue("ref"));
    		return;
    	
    	} else if(qName.equals("tag")){
    		tag = new Tag();
    		tag.k = atts.getValue("k");
    		tag.v = atts.getValue("v");
    		if(way!=null)way.addTag(tag);
    		else node.addTag(tag);
    		return;
    	}
    }
    
    public void endElement (String uri, String name, String qName){
    	if(qName.equals("way")){
    		
    		openStreetMap.addWay(way);
    		return;
    	} else if(qName.equals("node")){
    		openStreetMap.addNode(node);
    		
    		return;
    	}
    }
    
    public void characters (char ch[], int start, int length){
    	
    }

}
