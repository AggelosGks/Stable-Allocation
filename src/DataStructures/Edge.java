package DataStructures;

public class Edge {
	private Node machine;
	private Node job;
	private double time;
	
	public Edge(MachineNode m,JobNode j){
		this.machine=m;
		this.job=j;
		this.time=Math.min(m.upper_cap, j.proc_time);//min value of capacity and process time
	}
	
	
	public void replaceMachine(MachineNode m){
		this.machine=m;
	}
	
	public void replaceJob(JobNode j){
		this.job=j;
	}

	public Node getMachine() {
		return machine;
	}


	public Node getJob() {
		return job;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}
	
}
