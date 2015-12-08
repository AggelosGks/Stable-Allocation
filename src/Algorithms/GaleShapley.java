package Algorithms;

import java.util.ArrayList;
import java.util.Map;

import DataStructures.BipartiteGraph;
import DataStructures.Edge;
import DataStructures.JobNode;
import DataStructures.MachineNode;
import DataStructures.Matching;

public class GaleShapley {
	private ArrayList<JobNode> jobs_unassigned;
	private ArrayList<JobNode> jobs_assigned;
	private BipartiteGraph graph;
	private Matching match;
	
	//constructor details
	public GaleShapley(BipartiteGraph graph){
		this.graph=graph;//the graph that algorithm will be executed on.
		this.jobs_unassigned=graph.jobs;//list of jobs
		this.jobs_assigned=new ArrayList<JobNode>();
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
		JobNode dummy=JobNode.getDummy();
		for(MachineNode machine : MachineNode.getMachines()){//iterate machines
			Edge.getEdge(dummy, machine).fillEdge();;//extract relevant edge with dummy job
			//full arc
			match.addEdgeToMatch(dummy,Edge.getEdge(dummy, machine));
		}
	}

	
	public Matching getMatch() {
		return match;
	}

	/**
	 * Implements the Gale Shapley algorithm for stable allocations.
	 * The output of the algorithm is job-optimal.
	 */
	public void execute(){
		createInitialMathcing();
		match.printMatching();
		while(hasMoreUnassigned()){
			JobNode job=extractUnassigned();//extract first unassigned
			double time_left=job.computeLeftTime();//compute left time
			MachineNode proposal=job.getFirstChoiceForProposal();//compute machine for proposal
			double available=Edge.getEdge(job, proposal).computeAvailableTime();//available amount of time on arc
			double pro_amount=Math.min(time_left, available);//compute minimum amount that can flow through arc
			if(job.propose(pro_amount, proposal)){//acceptance occurs
				//reject pro_amount of time
				JobNode least_prefed=proposal.getLeastPrefered();//get the one for rejection
				if(proposal.rejectTime(pro_amount)){//if arc has decreased to zero after rejection
					match.removeEdgeFromMatch(least_prefed, Edge.getEdge(least_prefed,proposal));//remove edge from matching
					proposal.refreshPointerIndex(job);//update pointer upwards
				}
				if(!(least_prefed.isDummy())&&jobs_assigned.contains(least_prefed)){//if the rejected node was fully assigned
					jobs_assigned.remove(least_prefed);//remove from asssigned
					jobs_unassigned.add(least_prefed);//add to unassigned
				}
				job.setTime_consumed(job.getTime_consumed()+(int)pro_amount);
				
				Edge.getEdge(job, proposal).setCurrent_time(pro_amount);//set amount of time for new arc
				match.addEdgeToMatch(job, Edge.getEdge(job, proposal));//add arc to mathcing
				if(job.isFullyAssigned()){
					removeJob(job);
					jobs_assigned.add(job);
				}
			}else{//rejection occurs
				job.refreshPointerIndex();
			}
			match.printMatching();
			
			
		}
	}
}
