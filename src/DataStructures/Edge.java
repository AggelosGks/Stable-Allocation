package DataStructures;

import java.util.ArrayList;
import java.util.TreeMap;

public class Edge {
	
	//instance fields
	private Node machine;
	private Node job;
	public final double max_time;
	private double current_time;
	private static final TreeMap<Integer,ArrayList<Edge>> jobs_machines=new TreeMap<Integer,ArrayList<Edge>>();
	
	
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
	
	
	public boolean isFull(){
	 if(current_time==max_time){
		 return true;
	 }else{
		 return false;
	 }
	}
	
	public void fillEdge(){
		this.current_time=this.max_time;
	}
	
	/**
	 * Return the available left capacity of time for a specific edge
	 * @return time
	 */
	public double computeAvailableTime(){
		return max_time-current_time;
	}
	
	/**
	 * Creates and stores all the edges of the bipartite graph.
	 * We indicate to an edge by the id of each node.
	 * 
	 */
	public static void createAllEdges(){
		for(JobNode job : JobNode.getJobs()){
			jobs_machines.put(job.id,new ArrayList<Edge>());
			for(MachineNode machine : MachineNode.getMachines()){
				jobs_machines.get(job.id).add(new Edge(job,machine));
			}
		}
	}
	
	/**
	 * Retrieves the specific edge connecting the two parameters.
	 * @param j the job
	 * @param m the machine
	 * @return edge
	 */
	public static Edge getEdge(JobNode j,MachineNode m){
		Edge edge=null;
		for(Edge e : jobs_machines.get(j.id)){
			if(e.getMachine().equals(m)){
				edge=e;
				break;
			}
		}
		return edge;
	}
	
}
