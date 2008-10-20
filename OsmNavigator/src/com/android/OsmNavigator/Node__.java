package com.android.OsmNavigator;

import java.util.ArrayList;

public class Node__ {
    public String id;
    public String lat;
    public String lon;
    public ArrayList<Tag> tags = new ArrayList<Tag>();
    
    public void addTag(Tag t){
    	tags.add(t);
    }
}
