package DataStructures;

import java.util.ArrayList;

public class MachineNode extends Node{
	private static MachineNode dummy;
	//static fields
	private static final ArrayList<MachineNode> machines=new ArrayList<MachineNode>();//total machine population
	//instance fields
	private ArrayList<JobNode> pref;//pref list
	public final double upper_cap;//capacity
	public int pref_pointer;
	
	//constructor
	public MachineNode(double upper_cap) {
		super();
		this.upper_cap=upper_cap;
		if(!(this.upper_cap==Integer.MAX_VALUE)){
			machines.add(this);
		}else{
			dummy=this;
		}
		this.pref=new ArrayList<JobNode>();//initialize pref list
		this.pref_pointer=JobNode.getJobs().size()-1;//pointer starts at least prefered choice
	}

	
	@Override
	public String toString() {
		String x="";
		for(JobNode job : pref){
			x=x+" "+job.id;
		}
		return "MachineNode [pref=" + x + ", upper_cap=" + upper_cap + ", pref_pointer=" + pref_pointer + "]";
	}


	public void setPref(ArrayList<JobNode> pref) {
		this.pref = pref;
	}


	public static MachineNode getDummy() {
		return dummy;
	}


	public static void createDummyMachine(){
		new MachineNode(Integer.MAX_VALUE);
	}
	
	public static ArrayList<MachineNode> getMachines() {
		return machines;
	}
	
	/**
	 * Makes the logical comparison for two job nodes.
	 * The comparison is implemented by the indexes of two jobs in 
	 * the preference list. One job is indicated by the pointer and its current position.
	 * @param job the job node to be compared
	 * @return true if job is preferable, false otherwise.
	 */
	public boolean isPreferable(JobNode job){
		boolean isprefered=false;
		int index_candidate=pref.indexOf(job);
		if(index_candidate<pref_pointer){
			isprefered=true;
		}
		return isprefered;
	}

}
