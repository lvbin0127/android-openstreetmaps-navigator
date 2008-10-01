package com.android.OsmNavigator;

import java.util.ArrayList;

public class Node {
    public String id,lat,lon;
    public ArrayList<Tag> tags = new ArrayList<Tag>();
    
    public void addTag(Tag t){
    	tags.add(t);
    }
}
