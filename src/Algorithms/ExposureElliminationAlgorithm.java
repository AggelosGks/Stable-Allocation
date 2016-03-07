package Algorithms;

import java.util.ArrayList;

import DataStructures.JobNode;
import DataStructures.Matching;
import DataStructures.RotationPair;
import DataStructures.RotationStructure;

public class ExposureElliminationAlgorithm {
	private Matching match;//the matching the algorithm is applied to
	private ArrayList<RotationStructure> rotations;//list of rotationstructures
	
	public ExposureElliminationAlgorithm(Matching m){
		this.match=m;
		this.rotations=new ArrayList<RotationStructure>();
	}
	
	
	public void execute(){
		ArrayList<JobNode> jobs=JobNode.getJobs();
		while(jobDecreaseExists(jobs)){//while a job that can be feel less happy exists
			JobNode job=extractJob(jobs);//extract this job
			RotationStructure rotation=new RotationStructure();//init new rotation structure
			RotationPair pair=job.extractRotationPair(match);//execute rotation
			System.out.println("			New Rotation begins");
			System.out.println("---------------------------------");
			System.out.println(pair.toString());
			JobNode rejected=(JobNode)rotation.addPair(pair);
			boolean control=true;
			while(control){
				System.out.println("Rejected is : "+rejected.id);
				pair=rejected.extractRotationPair(match, pair.getAmount());
				if(pair.getAdded_to()!=null){
					rejected=(JobNode)rotation.addPair(pair);
					System.out.println(pair.toString());
					if(rotation.containsNode(rejected.id)){
						control=false;
						rotation.elliminateStructure(match);
					}
				}else{
					control=false;
				}
			}
			System.out.println("--------------------------------");
			
			
			//match.printMatching();
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
