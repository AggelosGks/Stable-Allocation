package Algorithms;

import java.util.ArrayList;

import DataStructures.JobNode;
import DataStructures.Matching;
import DataStructures.RotationPair;
import DataStructures.RotationStructure;

public class ExposureElliminationAlgorithm {
	private Matching match;
	private ArrayList<RotationStructure> rotations;
	
	public ExposureElliminationAlgorithm(Matching m){
		this.match=m;
		this.rotations=new ArrayList<RotationStructure>();
	}
	
	public void execute(){
		ArrayList<JobNode> jobs=JobNode.getJobs();
		while(jobDecreaseExists(jobs)){
			JobNode job=extractJob(jobs);
			RotationStructure rotation=new RotationStructure();
			RotationPair pair=job.extractRotationPair(match);
			
			
			
		}
	}
	
	/**
	 * Examines whether a job node may decrease its hapiness.
	 * @param jobs
	 * @return true if at least one job exists
	 */
	private boolean jobDecreaseExists(ArrayList<JobNode> jobs){
		boolean can=false;
		for(JobNode job : jobs){
			if(job.canGetWorse(this.match)){
				can=true;
				break;
			}
		}
		return can;
	}
	

	
	/**
	 * Extracts the first feasible job node for experiencing a hapiness decrease.
	 * @param jobs
	 * @return node the node that experiences the decrease.
	 */
	private JobNode extractJob(ArrayList<JobNode> jobs){
		JobNode node=null;
		for(JobNode job : jobs){
			if(job.canGetWorse(this.match)){
				node=job;
				break;
			}
		}
		return node;
	}
	
	
}
