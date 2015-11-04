package DataStructures;


public class Edge {
	
	//instance fields
	private Node machine;
	private Node job;
	public final double max_time;
	private double current_time;
	
	
	
	public Edge(JobNode j,MachineNode m){
		this.machine=m;
		this.job=j;
		this.max_time=Math.min(m.upper_cap, j.proc_time);//min value of capacity and process time
		this.current_time=0;
	}
	
	
	public double getCurrent_time() {
		return current_time;
	}


	public void setCurrent_time(double current_time) {
		this.current_time = current_time;
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
	
	
	public boolean isEdgeFull(){
	 if(current_time==max_time){
		 return true;
	 }else{
		 return false;
	 }
	}
	
	public void fillEdge(){
		this.current_time=this.max_time;
	}
	
}
