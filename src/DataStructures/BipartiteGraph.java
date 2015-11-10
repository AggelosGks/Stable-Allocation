package DataStructures;

import java.util.ArrayList;
import java.util.TreeMap;

public class BipartiteGraph {
	public final ArrayList<JobNode> jobs;
	public final ArrayList<MachineNode> machines;
	
	
	
	public BipartiteGraph(){
		this.machines=MachineNode.getMachines();
		this.jobs=JobNode.getJobs();
	}

	
	
}
