package com.android.OsmNavigator.PathFinding;

// AStar Node class, Aaron Steed 2006

import java.util.ArrayList;
import java.lang.Math;

import com.android.OsmNavigator.Tag;

public class Node{

	public float x, y, z; // Location, variable dimensions
	public Node parent = null; // Parent Node setting
	public float f = 0.0f; // Sum of goal and heuristic calculations
	public float g = 0.0f; // Cost of reaching goal
	public float h = 0.0f; // Heuristic distance calculation
	public ArrayList<Connector> links = new ArrayList<Connector>(); // Connectors to other Nodes
	public boolean walkable = true; // Is this Node to be ignored?
	public String id;
    public String lat;
    public String lon;
    public ArrayList<Tag> tags = new ArrayList<Tag>();
    
    
	// Constructors

	public Node(){
		this(0.0f, 0.0f, 0.0f);
	}

	public void addTag(Tag t){
    	tags.add(t);
    }
	
	public Node(float x, float y){
		this.x = x;
		this.y = y;
		this.z = 0.0f;
	}

	public Node(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	// Undocumented constructor - used by makeCuboidNodes(int [] dim, float scale)

	public Node(float [] p){
		x = p[0];
		y = p[1];
		z = 0.0f;
		if(p.length > 2){
			z = p[2];
		}
	}

	public Node(float x, float y, ArrayList<Connector> links){
		this.x = x;
		this.y = y;
		this.z = 0.0f;
		this.links = links;
	}

	public Node(float x, float y, float z, ArrayList<Connector> links){
		this.x = x;
		this.y = y;
		this.z = z;
		this.links = links;
	}

	//
	// Field utilities
	//

	public void reset(){
		parent = null;
		f = g = h = 0;
	}

	// Calculate G

	public void setG(Connector o){
		g = parent.g + o.d;
	}

	// Euclidean field methods calculate F & H

	public void setF(Node finish){
		setH(finish);
		f = g + h;
	}

	public void setH(Node finish){
		h = dist(finish);
	}

	// Manhattan field methods calculate F & H

	public void MsetF(Node finish){
		MsetH(finish);
		f = g + h;
	}

	public void MsetH(Node finish){
		h = manhattan(finish);
	}

	//
	// Linking tools
	//

	public Node copy(){
		ArrayList<Connector> temp = new ArrayList<Connector>();
		temp.addAll(links);
		return new Node(x, y, z, temp);
	}

	public void connect(Node n){
		links.add(new Connector(n, dist(n)));
	}

	public void connect(Node n, float d){
		links.add(new Connector(n, d));
	}

	public void connect(ArrayList<Connector> links){
		this.links.addAll(links);
	}

	public void connectBoth(Node n){
		links.add(new Connector(n, dist(n)));
		n.links.add(new Connector(this, dist(n)));
	}

	public void connectBoth(Node n, float d){
		links.add(new Connector(n, d));
		n.links.add(new Connector(this, d));
	}

	public int indexOf(Node n){
		for(int i = 0; i < links.size(); i++){
			Connector c = (Connector) links.get(i);
			if(c.n == n){
				return i;
			}
		}
		return -1;
	}

	public boolean connectedTo(Node n){
		for(int i = 0; i < links.size(); i++){
			Connector c = (Connector) links.get(i);
			if(c.n == n){
				return true;
			}
		}
		return false;
	}

	public boolean connectedTogether(Node n){
		for(int i = 0; i < links.size(); i++){
			Connector c = (Connector) links.get(i);
			if(c.n == n){
				for(int j = 0; j < n.links.size(); j++){
					Connector o = (Connector) n.links.get(j);
					if(o.n == this){
						return true;
					}
				}
			}
		}
		return false;
	}

	public void mulDist(float m){
		for(int i = 0; i < links.size(); i++){
			Connector c = (Connector) links.get(i);
			c.d *= m;
		}
	}

	public void setDist(Node n, float d){
		int i = indexOf(n);
		if(i > -1){
			Connector temp = (Connector) links.get(i);
			temp.d = d;
		}
	}

	public void setDistBoth(Node n, float d){
		int i = indexOf(n);
		if(i > -1){
			Connector temp = (Connector) links.get(i);
			temp.d = d;
			int j = n.indexOf(this);
			if(j > -1){
				temp = (Connector) n.links.get(j);
				temp.d = d;
			}
		}
	}

	// Iterates thru neighbours and unlinks Connectors incomming to this - Node is
	// still linked to neighbours though

	public void disconnect(){
		for(int i = 0; i < links.size(); i++){
			Connector c = (Connector) links.get(i);
			int index = c.n.indexOf(this);
			if(index > -1){
				c.n.links.remove(index);
			}
		}
	}

	// Calculates shortest link and kills all links around the Node in that radius
	// Used for making routes around objects account for the object's size
	// Uses actual distances rather than Connector settings

	public void radialDisconnect(){
		float radius = 0.0f;
		for(int j = 0; j < links.size(); j++){
			Connector myLink = (Connector) links.get(j);
			if(straightLink(myLink.n)){
				radius = dist(myLink.n);
				break;
			}
		}
		for(int j = 0; j < links.size(); j++){
			Connector myLink = (Connector) links.get(j);
			ArrayList<Node> removeMe = new ArrayList<Node>();
			for(int k = 0; k < myLink.n.links.size(); k++){
				Connector myLinkLink = (Connector) myLink.n.links.get(k);
				float midX = (myLink.n.x + myLinkLink.n.x) * 0.5f;
				float midY = (myLink.n.y + myLinkLink.n.y) * 0.5f;
				float midZ = (myLink.n.z + myLinkLink.n.z) * 0.5f;
				Node temp = new Node(midX, midY, midZ);
				if(dist(temp) <= radius){
					removeMe.add(myLinkLink.n);
				}
			}
			for(int k = 0; k < removeMe.size(); k++){
				Node temp = (Node) removeMe.get(k);
				int index = myLink.n.indexOf(temp);
				if(index > -1){
					myLink.n.links.remove(index);
				}
			}
		}
	}

	// Checks if a Node's position differs along one dimension only

	public boolean straightLink(Node myLink){
		if(indexOf(myLink) < 0){
			return false;
		}
		int dimDelta = 0;
		if(x != myLink.x){
			dimDelta++;
		}
		if(y != myLink.y){
			dimDelta++;
		}
		if(z != myLink.z){
			dimDelta++;
		}
		if(dimDelta == 1){
			return true;
		}
		return false;
	}

	//
	// Location tools
	//

	// Euclidean distance measuring for accuracy

	public float dist(Node n){
		if(z == 0.0 && n.z == 0.0){
			return (float) Math.sqrt(((x - n.x) * (x - n.x))
					+ ((y - n.y) * (y - n.y)));
		}else{
			return (float) Math.sqrt(((x - n.x) * (x - n.x))
					+ ((y - n.y) * (y - n.y)) + ((z - n.z) * (z - n.z)));
		}
	}

	// Manhattan distance measuring for avoiding jagged paths

	public float manhattan(Node n){
		if(z == 0.0 && n.z == 0.0){
			return ((x - n.x) * (x - n.x)) + ((y - n.y) * (y - n.y))
					+ ((z - n.z) * (z - n.z));
		}else{
			return ((x - n.x) * (x - n.x)) + ((y - n.y) * (y - n.y));
		}
	}
}
