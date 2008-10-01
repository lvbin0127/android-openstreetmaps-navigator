package com.android.OsmNavigator;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
//import org.xml.sax.helpers.XMLReaderFactory;

import android.content.Context;
import android.widget.TextView;

public class OsmFileReader {
	private String xmlfile = null;
	private InputStream xmlInputStream = null;
	public OsmHandler osmHandler;
	public String IOError =  "";
	public String SAXError = "";
	public Context context;
	private TextView messageHandler;
	
	public OsmFileReader(){
		// instantiate our handler
        osmHandler = new OsmHandler();
	}
	
	public void readXMLfromResource(Context _context){
		context = _context;
		/*try{
			xmlInputStream = context.getResources().openRawResource(R.raw.milano); 
			int i=0;
			 
			while((i=xmlInputStream.read())!=-1)
			{
				xmlfile +=(char)i;
			}
		} catch(Exception e){
			IOError = e.getMessage();
		}*/
		
	}
	public void setMessageHandler(TextView tv){
		messageHandler = tv;
	}
	public String getXMLString(){
		if(xmlInputStream != null){
		   return xmlfile;
		} 
		return null;
	}
	public boolean parseStructure() throws ParserConfigurationException{
		try {
			   // create the factory
	           SAXParserFactory factory = SAXParserFactory.newInstance();
	           // create a parser
	           SAXParser parser = factory.newSAXParser();

	           // create the reader (scanner)
	           XMLReader xmlreader = parser.getXMLReader();
	           
	           // assign our handler
	           xmlreader.setContentHandler(osmHandler);
	           
	           xmlInputStream = context.getResources().openRawResource(R.raw.milano);
	           
	           xmlreader.parse(new InputSource(xmlInputStream));
	           osmHandler.isLoaded=true;
	           
			 } catch (SAXException e) {
				 SAXError = e.getMessage();
				 return false;
			 } catch(IOException e){
				 IOError =  e.getMessage();
				 return false;
			 }
			 
		     return true; 

		
	}
	
	
}


