package Algorithms;

import java.util.ArrayList;

import DataStructures.JobNode;
import DataStructures.MachineNode;
import DataStructures.Matching;
import DataStructures.RotationPair;
import DataStructures.RotationStructure;

public class ExposureElliminationAlgorithm {
	private Matching match;//the matching the algorithm is applied to
	private ArrayList<RotationStructure> rotations;//list of rotationstructures
	private static RotationStructure runner;
	
	public ExposureElliminationAlgorithm(Matching m){
		this.match=m;
		this.rotations=new ArrayList<RotationStructure>();
	}
	
	public void execute(){
		ArrayList<JobNode> jobs=new ArrayList<JobNode>(JobNode.getJobs());//get all jobs
		while(decreaseExists(jobs,this.match)){
			System.out.println("CCCCCCCCCCCCC");
			runner.elliminateStructure(match);
			match.printMatching();
			rotations.add(runner);
			for(MachineNode mach : MachineNode.getMachines()){
				mach.refreshPointerRotation(match);
			}
			
		}
	}
	
	
	public static void setRunner(RotationStructure runner) {
		ExposureElliminationAlgorithm.runner = runner;
	}

	/**
	 * Examines wether a decrease of happiness exists for one or more jobs.
	 * @param jobs
	 * @param match
	 * @return true if job can get less happy
	 */
	public boolean decreaseExists(ArrayList<JobNode> jobs,Matching match){
		boolean decrease=false;
		for(JobNode job: jobs){
			if(job.canGetWorse(match)){
				decrease=true;
				break;
			}
		}
		return decrease;
	}
	

	
	
	

	


	
	
}
