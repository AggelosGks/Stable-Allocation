package DataStructures;

import java.util.ArrayList;
import java.util.TreeMap;

public class Matching {
	private ArrayList<Edge> connections;
	
	public Matching(){
		 this.connections=new ArrayList<Edge>();
	}
	
	
	public void addNewEdgetoMatch(Edge edge){
		connections.add(edge);
	}
	
	
}
