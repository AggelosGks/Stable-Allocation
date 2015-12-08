package DataStructures;

import java.util.ArrayList;

public class JobNode extends Node{
	private static final ArrayList<JobNode> jobs=new ArrayList<JobNode>();//total job population
	private static JobNode dummy;

	public final double proc_time;//time of job
	private int time_consumed;//current amount of time
	private ArrayList<MachineNode> pref;//list with preferences
	private int pref_pointer;
	
	public JobNode(double proc_time) {
		super();
		this.proc_time=proc_time;
		this.time_consumed=0;
		this.pref=new ArrayList<MachineNode>();//initialize pref list
		if(!(this.proc_time==Integer.MAX_VALUE)){
			jobs.add(this);
		}else{
			dummy=this;
		}
		this.pref_pointer=0;//pointer start at first choice
	}
	


	@Override
	public String toString() {
		String x="";
		for(MachineNode machine : pref){
			x=x+" "+Integer.toString(machine.id);
		}
		return "JobNode [ Id: "+this.id+", proc_time=" + proc_time + ", time_consumed=" + time_consumed + ", pref=" + x
				+ ", pref_pointer=" + pref_pointer + "]";
	}



	public void setPref(ArrayList<MachineNode> pref) {
		this.pref = pref;
	}



	public static JobNode getDummy() {
		return dummy;
	}

	public void setTime_consumed(int time_consumed) {
		this.time_consumed = time_consumed;
	}


	public static void createDummyJob(){
		new JobNode(Integer.MAX_VALUE);
	}

	public static ArrayList<JobNode> getJobs() {
		return jobs;
	}

	
	/**
	 * Return the optimal choice of a job for proposal.
	 * @return machine
	 */
	public MachineNode getFirstChoiceForProposal(){
		MachineNode machine=pref.get(pref_pointer);
		return machine;
	}
	
	public int getTime_consumed() {
		return time_consumed;
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
	
	public boolean propose(double amount,MachineNode machine){
		boolean accepts=false;
		double time=Edge.getEdge(this,machine).getCurrent_time();
		if(!(time==Edge.getEdge(this, machine).max_time)){//ensure that arc has remaining capacity
			if(machine.prefersAccRej(this)&&amount>0){//if machine accepts
				accepts=true;
			}else{
				accepts=false;
			}
		}
		return accepts;
	}
	
	public void refreshPointerIndex(){
		this.pref_pointer++;
	}
	
}
