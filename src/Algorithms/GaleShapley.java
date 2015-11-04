package Algorithms;

import java.util.ArrayList;

import DataStructures.BipartiteGraph;
import DataStructures.JobNode;

public class GaleShapley {
	private ArrayList<JobNode> jobs_unassigned;
	private final int N;
	private final int V;
	BipartiteGraph graph;
	
	public GaleShapley(BipartiteGraph graph){
		this.graph=graph;
		this.jobs_unassigned=graph.jobs;
		this.N=graph.jobs.size();
		this.V=graph.machines.size();
	}
	
	public void execute(){
		while(hasMoreUnassigned()){
			JobNode job=extractUnassigned();
			double amount=job.computeLeftTime();
			
		}
	}
	
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
	
}
