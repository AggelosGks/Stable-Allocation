package DataStructures;

import java.util.ArrayList;

public class BipartiteGraph {
	public final ArrayList<JobNode> jobs;
	public final ArrayList<MachineNode> machines;

	public BipartiteGraph() {
		this.machines = new ArrayList<MachineNode>(MachineNode.getMachines());
		this.jobs = new ArrayList<JobNode>(JobNode.getJobs());
	}

}
