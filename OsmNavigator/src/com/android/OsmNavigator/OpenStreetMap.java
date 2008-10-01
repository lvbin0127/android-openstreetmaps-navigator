package com.android.OsmNavigator;

import java.util.ArrayList;
import android.graphics.*;

//import org.xml.sax.Attributes;


public class OpenStreetMap {
	/* basic */
	String minlat, minlon, maxlat, maxlon;
	String version, generator;
	public int nodesnum = 0;
	public int relations = 0;
	ArrayList<Node> nodes = new ArrayList<Node>();
	ArrayList<Way> ways = new ArrayList<Way>();
	
	public void drawNodes(){
		
	}
	public String generateTagString(){
		String separator="__";
		String deepContent = "";
		Way w;
		Node n;
		for(int i = 0; i < ways.size();i++){
			w = ways.get(i);
			
			for(int j = 0; j < w.tags.size(); j++){
				
				if(w.tags.get(j).k.equals("name") || w.tags.get(j).k.equals("amenity")){
					deepContent += separator + w.tags.get(j).v;
				} else continue;
			}
		}
		for(int ii = 0; ii < nodes.size();ii++){
			n = nodes.get(ii);
			
			for(int jj = 0; jj < n.tags.size(); jj++){
				//eepContent += w.tags.get(j).k +"\n";
				if(n.tags.get(jj).k.equals("name")){
					deepContent += separator + n.tags.get(jj).v;
					//deepContent += separator + w.tags.get(j).v;
				} else continue;
			}
		}
		return deepContent;
	}
	public String getXML(){
		return ("version : " + version + "\n"+
		        "generator : " + generator	+"\n\n"+
		        "bounds : \n" +
		        " - min Lat : " + minlat + "\n" +
		        " - min Lon : " + minlon + "\n" +
		        " - max Lat : " + maxlat + "\n" +
		        " - max Lon : " + maxlon + "\n\n" +
		        "nodes : " + nodes.size() + "\n" +
		        "relations : " + relations + "\n" +
		        "ways : " + ways.size() + "( way 1 : rel " + ways.get(1).getNdSize()+ ", tag " + ways.get(1).getTagsSize()+")"
		);
	}
	public void addNode(Node n){
		nodes.add(n);
	}
	public void addRelation(){
		relations++;
	}
	public void addWay(Way w){
		ways.add(w);
		
	}
	public String getMinlat() {
		return minlat;
	}
	public void setMinlat(String minlat) {
		this.minlat = minlat;
	}
	public String getMinlon() {
		return minlon;
	}
	public void setMinlon(String minlon) {
		this.minlon = minlon;
	}
	public String getMaxlat() {
		return maxlat;
	}
	public void setMaxlat(String maxlat) {
		this.maxlat = maxlat;
	}
	public String getMaxlon() {
		return maxlon;
	}
	public void setMaxlon(String maxlon) {
		this.maxlon = maxlon;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getGenerator() {
		return generator;
	}
	public void setGenerator(String generator) {
		this.generator = generator;
	}
	
}

