# Introduction #



Our OsmHandler.java is a simple handler class that interprete xml data
The SAX parser reads our xml file line by line and launch some default methods when it finds any opening tag like 

&lt;somecontent&gt;

_and any closing tag like_

Unknown end tag for &lt;/somecontents&gt;

_or_/>_.
We will simply write these methods.
See [SAX parsing](http://www.onjava.com/pub/a/onjava/2002/06/26/xml.html) in O'Reilly article to understand more about SAX parsing._

## Basic class: what we should consider before beginning ##

---

Synopsis of our OsmHandler class
```
  package com.android.OsmNavigator;
  import org.xml.sax.Attributes;
  import org.xml.sax.SAXException;
  import org.xml.sax.helpers.DefaultHandler;

  public class OsmHandler extends DefaultHandler{
  
      public OsmHandler (){
          // simply this.
          super();	
      }  

      public void startDocument (){
          /* Called at the beginning. Parser has read only tag <?xml >
             e.g., create a class instance
 
             myosm = new OpenStreetMap();
          
             where we can store informations about how many nodes
             there are or stuff like this
          */
      }
      
      public void endDocument (){
          /*  Finished reading
 
          */
      }
      
      public void startElement  (String uri, String name, String qName, Attributes    atts)throws SAXException{
          if(qName.equals("node")){
          /* Called by parser when it finds opening tag <node>
             e.g., 
             
             myosm.addSomeNodeLatitude(atts.getValue("lat"));
            
          */
          }
      }
      
      public void endElement(String uri, String name, String qName){{
          if(qName.equals("way")){
          /*
          
          */
          }
      }   


  }

```

## Our data manipuation ##

---


An osm file has a structure like [this](http://wiki.openstreetmap.org/index.php/Data_Primitives).
So we have a long list of each osm node 

&lt;node&gt;

_with the couple attribute_lat_and_lon_, dealing with latitude and longitude of each **GPS survey**.
  * First, we create an **instance** of a generic class OpenStreetMap that will store all the information about the osm file. We do this in method **startDocument()**
```
  OpenStreetMap openStreetMap;
 
  public void startDocument ()
      {
		openStreetMap = new OpenStreetMap();
      }
```
  * Then, we start storing information writing method **startElement()**, that would call methods OpenStreetMap.setGenerator(String g) and OpenStreetMap.setVersion(String v) when parser finds_

&lt;osm&gt;

_tag
```
  public void startElement  (String uri, String name, String qName, Attributes atts)throws   SAXException{
    	if (qName.equals ("osm")){
    		openStreetMap.setGenerator(atts.getValue("generator"));
    		openStreetMap.setVersion(atts.getValue("version"));
    		
    		
    	return;
    	}
        /*
        ... see next step
        */
  }
```
  * Then, other tags (we are writing the same methods **startElement()** )
```
        if (qName.equals ("osm")){
    		openStreetMap.setGenerator(atts.getValue("generator"));
    		openStreetMap.setVersion(atts.getValue("version"));
    	return;
    	} else if (qName.equals("bounds")){
    		openStreetMap.setMinlat(atts.getValue("minlat"));
    		openStreetMap.setMaxlat(atts.getValue("maxlat"));
    		openStreetMap.setMinlon(atts.getValue("minlon"));
    		openStreetMap.setMaxlon(atts.getValue("maxlon"));
    		
    	return;
    	} else if(qName.equals("node")){
    		//increment node counter
    		openStreetMap.addNode(atts.getValue("id"), atts.getValue("lat"),atts.getValue("lon"));// = OpenStreetMap.nodes + 1;
    	return;	
    	} else if(qName.equals("relation")){
    		openStreetMap.addRelation();
    		return;
    	}
        
```
  * Now we would like to store information about_

&lt;way&gt;

_tag. But_

&lt;way&gt;

_is not like_

&lt;node&gt;

_, it has got child nodes like_

&lt;nd&gt;

_and_

&lt;tag&gt;

_. Thus, in each Way instance, we would appreciate to find relates_

&lt;nd&gt;

_and_

&lt;tag&gt;

_. note that we are writing **startElement()** method again.
```
    Way way; /* create way instance outside methods. The same var way is created eash time parser finds new <way> node*/

    public void startElement( ... ){
        /*
        ...
        else if(qName.equals("relation")){
    		openStreetMap.addRelation();
    		return;
    	}
        */ else if(qName.equals("way")){
    		way = new Way(); // we create there a new 
    		return;
    	} else if(qName.equals("nd")){
    		way.addNode(atts.getValue("ref"));
    		return;
    	} else if(qName.equals("tag")){
    		tag = new Tag();
    		tag.k = atts.getValue("k");
    		tag.v = atts.getValue("v");
    		if(way!=null)way.addTag(tag);
                /*
                utilizziamo il metodo Way.addTag() per aggiungere un'instanza 
                tag
                */
    		return;
    	}
    } // finally, we close startElement()
```
  * Finally, we could write the method **endElement()**. When the_

&lt;way&gt;

_tag is closed, the method OpenStreetMap.addWay(Way w) is called and the_way_instance is pushed in the_ArrayList

&lt;Way&gt;

