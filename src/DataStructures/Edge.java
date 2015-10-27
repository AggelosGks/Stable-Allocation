package DataStructures;

public class Edge {
	private MachineNode machine;
	private JobNode job;
	
	public Edge(MachineNode m,JobNode j){
		this.machine=m;
		this.job=j;
	}
	
	public void replaceMachine(MachineNode m){
		this.machine=m;
	}
	
	public void replaceJob(JobNode j){
		this.job=j;
	}
}
