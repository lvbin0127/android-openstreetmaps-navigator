package com.android.OsmNavigator;

import java.util.ArrayList;

public class Way {
    private ArrayList<String> nd = new ArrayList<String>();
    public ArrayList<Tag> tags = new ArrayList<Tag>();
    
    
    public void addNode(String nodeId){
    	nd.add(nodeId);
    }
    public void addTag(Tag t){
       	tags.add(t);
    }
    public int getNdSize(){
    	return nd.size();
    	
    }
    public int getTagsSize(){
    	return tags.size();
    }
    
}

