package DataStructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Matching {
	private HashMap<Node,ArrayList<Edge>> match_edges;
	
	public Matching(){
		 this.match_edges=new HashMap<Node,ArrayList<Edge>>();
	}
	
	public void addEdgeToMatch(Node source, Edge edge){
		if(match_edges.get(source)==null){
			match_edges.put(source,new ArrayList<Edge>());
			match_edges.get(source).add(edge);
		}else{
			match_edges.get(source).add(edge);
		}
	}
	
	public  void printMatching() {
		for(Map.Entry<Node,ArrayList<Edge>> entry : match_edges.entrySet()) {
			Node node = entry.getKey();
			ArrayList<Edge> edges = entry.getValue();
			System.out.println(node.id);
			for(Edge e : edges){
				System.out.println(e.toString());
			}  
		}
	}
	
	public void removeEdgeFromMatch(Node source, Edge edge){
		match_edges.get(source).remove(edge);
	}
	
	
	
	
	
	
}
