package Computation;

import java.util.ArrayList;
import java.util.HashMap;
import DataStructures.Edge;
import DataStructures.Matching;
import DataStructures.RotationStructure;

public class LinearProgramm {
	private final static HashMap<Edge, ArrayList<Info>> added = new HashMap<Edge, ArrayList<Info>>();
	private final static HashMap<Edge, ArrayList<Info>> abstracted = new HashMap<Edge, ArrayList<Info>>();
	private final static HashMap<Edge, Double> jopt_values = new HashMap<Edge, Double>();
	private final Matching jopt;
	private final Matching mopt;

	public LinearProgramm(Matching jopt, Matching mopt) {
		this.jopt = jopt;
		this.mopt = mopt;
	}

	public static void initInfoAddition(Edge e) {
		added.put(e, new ArrayList<Info>());
	}

	public static void initInfoAbstraction(Edge e) {
		abstracted.put(e, new ArrayList<Info>());
	}

	public static HashMap<Edge, ArrayList<Info>> getAdded() {
		return added;
	}

	public static HashMap<Edge, ArrayList<Info>> getAbstracted() {
		return abstracted;
	}

	public static void addInfoAddition(Edge e, RotationStructure rotation, Double amount) {
		added.get(e).add(new Info(rotation, amount));
	}

	public static void addInfoAbstraction(Edge e, RotationStructure rotation, Double amount) {
		abstracted.get(e).add(new Info(rotation, amount));
	}

	public static void addValuesJopt(Matching m) {
		for (Edge e : m.getEdgesListed()) {
			jopt_values.put(e, e.getCurrent_time());
		}
	}
	
	public static void clear(){
		added.clear();
		abstracted.clear();
		jopt_values.clear();
	}

	public static HashMap<Edge, Double> getJoptValues() {
		return jopt_values;
	}

	public static void printAllEdgeInfo() {
		System.out.println("ADDITION");
		for (Edge edge : added.keySet()) {
			System.out.println("J" + edge.getJob().id + " M" + edge.getMachine().id);
			for (Info info : added.get(edge)) {
				info.revealInfo();
			}
		}
		System.out.println("ABSTRACTION");
		for (Edge edge : abstracted.keySet()) {
			System.out.println("J" + edge.getJob().id + " M" + edge.getMachine().id);
			for (Info info : abstracted.get(edge)) {
				info.revealInfo();
			}
		}

	}
}
