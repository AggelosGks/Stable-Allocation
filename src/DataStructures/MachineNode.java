package DataStructures;

import java.util.ArrayList;

public class MachineNode extends Node{
	
	//static fields
	private static final ArrayList<MachineNode> machines=new ArrayList<MachineNode>();//total machine population
	//instance fields
	private JobNode assigned;//pointer to current assignment
	private ArrayList<JobNode> pref;//pref list
	public final double upper_cap;//capacity
	
	//constructor
	public MachineNode(double upper_cap) {
		super();
		this.upper_cap=upper_cap;
		machines.add(this);
		this.pref=new ArrayList<JobNode>();//initialize pref list
	}
	
	/**
	 * Makes a logical comparison according to preference list between two machine nodes
	 * @return true if node argument is preferable tha current
	 */
	public boolean isPreferable(JobNode candidate){
		boolean prefer=false;
		int current_index=this.pref.indexOf(assigned);
		int candidate_index=this.pref.indexOf(candidate);
		if(current_index>candidate_index){//candidate is prefered than current
			prefer=true;
		}
		return prefer;
	}
	
	public static void createDummyMachine(){
		new MachineNode(Double.MAX_VALUE);
	}
	
	public static ArrayList<MachineNode> getMachines() {
		return machines;
	}
	
	public void assignToJob(JobNode j){
		this.assigned=j;
	}
}
