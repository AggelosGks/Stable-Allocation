package DataStructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Matching {
	private HashMap<Node, ArrayList<Edge>> match_edges;

	public Matching() {
		this.match_edges = new HashMap<Node, ArrayList<Edge>>();
	}

	public HashMap<Node, ArrayList<Edge>> getMatch_edges() {
		return match_edges;
	}

	public void addEdgeToMatch(Node source, Edge edge) {
		if (match_edges.get(source) == null) {
			match_edges.put(source, new ArrayList<Edge>());
			match_edges.get(source).add(edge);
		} else {
			match_edges.get(source).add(edge);
		}
	}

	public boolean containsEdge(Edge e) {
		boolean contains = false;
		JobNode job = (JobNode) e.getJob();
		if (this.match_edges.get(job) != null) {
			if (this.match_edges.get(job).contains(e)) {
				contains = true;
			}
		}
		return contains;
	}

	public void printMatching() {
		for (Map.Entry<Node, ArrayList<Edge>> entry : match_edges.entrySet()) {
			Node node = entry.getKey();
			ArrayList<Edge> edges = entry.getValue();
			System.out.println("J " + node.id);
			for (Edge e : edges) {
				System.out.println(e.toString());
			}
		}
	}

	public void printMatchingSwaped() {
		System.out.println(" ");
		System.out.println("---------------MACHINE OPTIMAL SHAPLEY---------------");
		for (MachineNode mach : MachineNode.getMachines()) {
			int past_id = mach.id - JobNode.getJobs().size();
			System.out.println("M " + mach.id + " (J" + Integer.toString(past_id) + ")");
			for (Map.Entry<Node, ArrayList<Edge>> entry : match_edges.entrySet()) {
				ArrayList<Edge> edges = entry.getValue();
				for (Edge e : edges) {
					if (e.getMachine().id == mach.id) {
						System.out.println(e.toStringSwaped());
					}
				}
			}
		}

		System.out.println("--------------------------------------------------");
	}

	public void removeEdgeFromMatch(Node source, Edge edge) {
		match_edges.get(source).remove(edge);
	}

	public ArrayList<Edge> getEdgesListed() {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		HashMap<Node, ArrayList<Edge>> match_edges = this.getMatch_edges();
		for (Map.Entry<Node, ArrayList<Edge>> entry : match_edges.entrySet()) {
			for (Edge edge : entry.getValue()) {
				edges.add(edge);
			}
		}
		return edges;
	}

	public boolean areEqual(Matching other) {
		int jobs = MachineNode.getMachines().size();
		int machines = JobNode.getJobs().size();
		ArrayList<Edge> edges = this.getEdgesListed();
		ArrayList<Boolean> array = new ArrayList<Boolean>();
		for (Edge edge : edges) {
			boolean found = false;
			for (Edge ed : other.getEdgesListed()) {
				if (edge.isEqualReversed(ed, jobs, machines)) {
					found = true;
				}
			}
			if (!found) {
				int past_id = edge.getMachine().id - jobs;
				System.out.println("M " + edge.getMachine().id + " (J" + Integer.toString(past_id) + ")");
				System.out.println(edge.toStringSwaped());
			}
			array.add(found);
		}
		if (array.contains(false)) {
			return false;
		} else {
			return true;
		}
	}

}
