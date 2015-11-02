package DataStructures;

import java.util.ArrayList;

public class Matching {
	private ArrayList<Edge> matching;
	
	public Matching(){
		this.matching=new ArrayList<Edge>();
	}
	
	/**
	 * Creates the initial matching, every machine is assigned to the dummy job
	 * 
	 * 
	 */
	public static Matching createInitialMatchingMachines(BipartiteGraph graph){
		Matching m=new Matching();
		ArrayList<MachineNode> machines=graph.machines;
		JobNode dummyjob=JobNode.getDummyJob();
		for(MachineNode machine : machines){
			machine.assignToJob(dummyjob);
			m.matching.add(new Edge(machine,dummyjob));
		}
		return m;
	}
}
