package Algorithms;

import java.util.ArrayList;

import DataStructures.BipartiteGraph;
import DataStructures.Edge;
import DataStructures.JobNode;
import DataStructures.MachineNode;
import DataStructures.Matching;

public class GaleShapley {
	private ArrayList<JobNode> jobs_unassigned;
	private BipartiteGraph graph;
	private Matching match;
	
	//constructor details
	public GaleShapley(BipartiteGraph graph){
		this.graph=graph;//the graph that algorithm will be executed on.
		this.jobs_unassigned=graph.jobs;//list of jobs
		this.match=new Matching();
	}
	
	/**
	 * Removes the indicated job given as parameter.
	 * @param job the fully assigned job to be removed.
	 */
	public void removeJob(JobNode job){
		jobs_unassigned.remove(job);
	}
	
	/**
	 * Extracts the first unassigned job
	 * @return false if all jobs are fully assigned, true otherwise.
	 */
	public JobNode extractUnassigned(){
		JobNode job=null;
		for(JobNode j : jobs_unassigned){
			if(!j.isFullyAssigned()){
				job=j;
				break;
			}
		}
		return job;
	}
	
	/**
	 * Checks if all jobs in list are fully assigned
	 * @return false if all jobs are fully assigned, true otherwise.
	 */
	public boolean hasMoreUnassigned(){
		boolean hasMore=false;
		for(JobNode j : jobs_unassigned){
			if(!j.isFullyAssigned()){
				hasMore=true;
				break;
			}
		}
		return hasMore;
	}
	
	/**
	 * Creates the initial matching before execution.
	 * All machines fully assigend to the dummy job.
	 */
	public void createInitialMathcing(){
		JobNode dummy=JobNode.getDummyJob();
		for(MachineNode machine : MachineNode.getMachines()){
			Edge.getEdge(dummy, machine).fillEdge();
			match.addNewEdgetoMatch(Edge.getEdge(dummy, machine));
		}
	}
	
	/**
	 * Implements the Gale Shapley algorithm for stable allocations.
	 * The output of the algorithm is job-optimal.
	 */
	public void execute(){
		createInitialMathcing();
		while(hasMoreUnassigned()){
			JobNode job=extractUnassigned();//extract first unassigned
			double time_left=job.computeLeftTime();//
		}
	}
}
