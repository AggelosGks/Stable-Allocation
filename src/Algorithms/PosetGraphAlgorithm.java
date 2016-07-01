package Algorithms;

import java.util.ArrayList;
import Computation.Label;
import DataStructures.JobNode;
import DataStructures.MachineNode;
import DataStructures.RotationStructure;

public class PosetGraphAlgorithm {
	private final ArrayList<RotationEdge> poset;

	public PosetGraphAlgorithm() {
		this.poset = new ArrayList<RotationEdge>();
	}

	public void execute() {
		ArrayList<Label> source = new ArrayList<Label>();
		ArrayList<Label> sink = new ArrayList<Label>();
		for (JobNode job : JobNode.getJobs()) {
			for (MachineNode mach : job.getPref()) {
				if (source.isEmpty()) {
					if (mach.getLabel().get(job) != null) {
						for (Label l : mach.getLabel().get(job)) {
							source.add(l);
						}
					}
				} else {
					boolean found = false;
					if (mach.getLabel().get(job) != null) {
						for (Label l : mach.getLabel().get(job)) {
							sink.add(l);
							found = true;
						}
					}
					if (found) {
						for (Label L : source) {
							for (Label l : sink) {
								RotationEdge e = new RotationEdge(L.getRotation(), l.getRotation());
								if (!containsTheEdge(e)) {
									poset.add(e);
								}
							}
						}
						source.clear();
						for (Label l : sink) {
							source.add(l);
						}
						sink.clear();
					}
				}
			}
		}
		clearDuplicReverse();
	}

	private void clearDuplicReverse() {
		ArrayList<RotationEdge> del = new ArrayList<RotationEdge>();
		for (RotationEdge e : poset) {
			if (e.first.id == e.second.id) {
				del.add(e);
			}
		}
		for (RotationEdge e : del) {
			poset.remove(e);
		}
		
		
	}
	
	private void reveal(){
		for (RotationEdge edge : poset) {
			String x = "";
			x = Integer.toString(edge.first.id) + " " + Integer.toString(edge.second.id);
			System.out.println(x);
		}
	}

	private boolean containsTheEdge(RotationEdge edge) {
		boolean contains = false;
		for (RotationEdge e : poset) {
			boolean thesame = e.first.equals(edge.first) && e.second.equals(edge.second);
			boolean inversed = e.second.equals(edge.first) && e.first.equals(edge.second);
			if (thesame || inversed) {
				contains = true;
				break;
			}
		}
		return contains;
	}
	
	

	public ArrayList<RotationEdge> getPoset() {
		return poset;
	}



	public class RotationEdge {

		public final RotationStructure first;
		public final RotationStructure second;

		public RotationEdge(RotationStructure a, RotationStructure b) {
			this.first = a;
			this.second = b;
		}

	}
}
