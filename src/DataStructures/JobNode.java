package DataStructures;

import java.util.ArrayList;

public class JobNode extends Node{
	
	private static final ArrayList<JobNode> jobs=new ArrayList<JobNode>();//total job population
	private MachineNode assigned;//pointer to current assignment
	public final double proc_time;//time of job
	private ArrayList<MachineNode> pref;//list with preferences
	
	public JobNode(double proc_time) {
		super();
		this.proc_time=proc_time;
		this.pref=new ArrayList<MachineNode>();//initialize pref list
		jobs.add(this);
	}
	
	/**
	 * Makes a logical comparison according to preference list between two machine nodes
	 * @return true if node argument is preferable tha current
	 */
	public boolean isPreferable(MachineNode candidate){
		boolean prefer=false;
		int current_index=this.pref.indexOf(assigned);
		int candidate_index=this.pref.indexOf(candidate);
		if(current_index>candidate_index){//candidate is prefered than current
			prefer=true;
		}
		return prefer;
	}
	
	public static void createDummyJob(){
		new JobNode(Double.MAX_VALUE);
	}

	public static ArrayList<JobNode> getJobs() {
		return jobs;
	}

	public static JobNode getDummyJob(){
		int size=jobs.size();
		return jobs.get(size-1);
	}
	
	public void assignToMachine(MachineNode m){
		this.assigned=m;
	}

	
	
}
