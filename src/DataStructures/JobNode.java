package DataStructures;

import java.util.ArrayList;

public class JobNode extends Node{
	private static final ArrayList<JobNode> jobs=new ArrayList<JobNode>();//total job population


	public final double proc_time;//time of job
	private int time_consumed;//current amount of time
	private ArrayList<MachineNode> pref;//list with preferences
	private int pref_pointer;
	
	public JobNode(double proc_time) {
		super();
		this.proc_time=proc_time;
		this.time_consumed=0;
		this.pref=new ArrayList<MachineNode>();//initialize pref list
		jobs.add(this);
		this.pref_pointer=0;//pointer start at first choice
	}
	
	
	public int getTime_consumed() {
		return time_consumed;
	}

	public void setTime_consumed(int time_consumed) {
		this.time_consumed = time_consumed;
	}

	public ArrayList<MachineNode> getPref() {
		return pref;
	}

	public void setPref(ArrayList<MachineNode> pref) {
		this.pref = pref;
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
	
	/**
	 * Return the optimal choice of a job for proposal
	 * @return
	 */
	public MachineNode getFirstChoice(){
		return this.pref.get(pref_pointer);
		
	}
	
	/**
	 * Returns the left amount of time for a job instance
	 * @return 
	 */
	public double computeLeftTime(){
		return proc_time-time_consumed;
	}

	/**
	 * Checks if job is fully assigne
	 * @return true if fully assigned, false otherwise.
	 */
	public boolean isFullyAssigned(){
		if(time_consumed==proc_time){
			return true;
		}else{
			return false;
		}
	}
	
	
}
