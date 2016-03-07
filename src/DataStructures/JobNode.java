package DataStructures;

import java.util.ArrayList;

import Computation.ComputationUtil;

public class JobNode extends Node {
	private static final ArrayList<JobNode> jobs = new ArrayList<JobNode>();// total
																			// job
																			// population
	private static JobNode dummy;
	public final double proc_time;// time of job
	private int time_consumed;// current amount of time
	private ArrayList<MachineNode> pref;// list with preferences
	private int pref_pointer;

	public JobNode(double proc_time) {
		super();
		this.proc_time = proc_time;
		this.time_consumed = 0;
		this.pref = new ArrayList<MachineNode>();// initialize pref list
		if (!(this.proc_time == Integer.MAX_VALUE)) {
			jobs.add(this);
		} else {
			dummy = this;
		}
		this.pref_pointer = 0;// pointer start at first choice
	}

	@Override
	public String toString() {
		String x = "";
		for (MachineNode machine : pref) {
			x = x + " " + Integer.toString(machine.id);
		}
		return "JobNode [ Id: " + this.id + ", proc_time=" + proc_time + ", time_consumed=" + time_consumed + ", pref="
				+ x + ", pref_pointer=" + pref_pointer + "]";
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

	public static void createDummyJob() {
		new JobNode(Integer.MAX_VALUE);
	}

	public static ArrayList<JobNode> getJobs() {
		return jobs;
	}

	/**
	 * Return the optimal choice of a job for proposal.
	 * 
	 * @return machine
	 */
	public MachineNode getFirstChoiceForProposal() {
		MachineNode machine = pref.get(pref_pointer);
		return machine;
	}

	public int getTime_consumed() {
		return time_consumed;
	}

	/**
	 * Returns the left amount of time for a job instance
	 * 
	 * @return
	 */
	public double computeLeftTime() {
		return proc_time - time_consumed;
	}

	/**
	 * Checks if job is fully assigne
	 * 
	 * @return true if fully assigned, false otherwise.
	 */
	public boolean isFullyAssigned() {
		if (time_consumed == proc_time) {
			return true;
		} else {
			return false;
		}
	}

	public boolean proposeShapley(double amount, MachineNode machine) {
		boolean accepts = false;
		double time = Edge.getEdge(this, machine).getCurrent_time();
		if (!(time == Edge.getEdge(this, machine).max_time)) {// ensure that arc
																// has remaining
																// capacity
			if (machine.prefersAccRej(this) && amount > 0) {// if machine
															// accepts
				accepts = true;
			} else {
				accepts = false;
			}
		}
		return accepts;
	}
	
	public boolean proposeExp_Ell(double amount, MachineNode m){
		double avail=Edge.getEdge(this, m).computeAvailableTime();
		double available_amount=Math.min(amount, avail);
		if(m.prefersAccRej(this)&& available_amount>0){
			return true;
		}else{
			return false;
		}
	}

	public void refreshPointerIndex() {
		this.pref_pointer++;
	}

	/**
	 * Retrieves a sorted list (according to preferences) of connected machines on the current match.
	 * Starting from the most prefered we examine if decrease hapiness allocation exists.
	 * @param match the current match
	 * @return true if allocation exists
	 */
	public boolean canGetWorse(Matching match){
		boolean dropdown=false;
		//retrieve sorted preference list on current match
		ArrayList<MachineNode> list_OnMatch=this.getListAssignedtoMatch(match);
		int upper=0;
		if(list_OnMatch.size()==1){
			upper=1;
		}else{
			upper=list_OnMatch.size()-1;
		}
		for(int i=0; i<upper; i++){
			MachineNode from=list_OnMatch.get(i);
			double time_on_edge=Edge.getEdge(this, from).getCurrent_time();
			int index=this.pref.indexOf(from);
			MachineNode to=this.pref.get(index+1);
					if(this.proposeExp_Ell(time_on_edge, to)){
						dropdown=true;
						break;
					}
		}
		return dropdown;
	}
	
	private double extractAmount(double amount, MachineNode m){
		double avail=Edge.getEdge(this, m).computeAvailableTime();//amount available on edge job-proposal
		double available_amount=Math.min(amount, avail);//min value between amount of proposal(most prefered of job,job) and available
		double time_on_machine_lp=Edge.getEdge(m.getLeastPrefered(), m).getCurrent_time();//the amount assigned to the machine and its least prefered job
		//System.out.println(m.getLeastPrefered().id+" "+m.id);
		available_amount=Math.min(available_amount, time_on_machine_lp);
		return available_amount;//final amount of distribution
		
	}
	
	public RotationPair extractRotationPair(Matching match){
		RotationPair pair=new RotationPair();
		//retrieve sorted preference list on current match
		ArrayList<MachineNode> list_OnMatch=this.getListAssignedtoMatch(match);
		int upper=0;
		if(list_OnMatch.size()==1){
			upper=1;
		}else{
			upper=list_OnMatch.size()-1;
		}
		for(int i=0; i<upper; i++){
			MachineNode from=list_OnMatch.get(i);
			double time_on_edge=Edge.getEdge(this, from).getCurrent_time();//get amount on edge
			int index=this.pref.indexOf(from);
			MachineNode to=this.pref.get(index+1);	
				//job proposes amount
				if(this.proposeExp_Ell(time_on_edge, to)){//if accepted
					pair.setExtracted_from(from);//set node that amount was taken from
					pair.setAdded_to(to);//set node that amount will be added 
					pair.setProposed_by(this);//set node of proposal
					pair.setAmount(extractAmount(time_on_edge, to));//set final of amount distributed
					break;
				}
		}
		return pair;
	}
	
	public RotationPair extractRotationPair(Matching match,double amount){
		System.out.println("Node: "+this.id);
		boolean stop=false;
		RotationPair pair=new RotationPair();
		//code in case next pair is adjusted on match
		ArrayList<MachineNode> list_OnMatch=this.getListAssignedtoMatch(match);
		int upper=0;
		if(list_OnMatch.size()==1){
			upper=1;
		}else{
			upper=list_OnMatch.size()-1;
		}
		for(int i=0; i<upper; i++){
			MachineNode from=list_OnMatch.get(i);
			int index=this.pref.indexOf(from);
			MachineNode to=this.pref.get(index+1);
					//job proposes amount
					if(this.proposeExp_Ell(amount, to)){//if accepted
						pair.setExtracted_from(from);//set node that amount was taken from
						pair.setAdded_to(to);//set node that amount will be added 
						pair.setProposed_by(this);//set node of proposal
						pair.setAmount(extractAmount(amount, to));//set final of amount distributed
						stop=true;
					}
			if(!stop){
				index=index+2;
				for(int j=index; j<this.pref.size()-1; j++){
					
					MachineNode over_to=this.pref.get(j);
					System.out.println("DDDDDDDDDDDDDDDDDD   "+over_to.id);
					if(this.proposeExp_Ell(amount, over_to)){//if accepted
						System.out.println("aaaaaaaaaaaaaaaaaaaaaa   "+over_to.id);
						pair.setExtracted_from(from);//set node that amount was taken from
						pair.setAdded_to(over_to);//set node that amount will be added 
						pair.setProposed_by(this);//set node of proposal
						pair.setAmount(extractAmount(amount, over_to));//set final of amount distributed
						stop=true;
						break;
					}
				}
			}
		}
		
			
		return pair;
	}
	
	
	
	//Return the least prefered machine of a job that is currently assigned to job in the match
	public ArrayList<MachineNode> getListAssignedtoMatch(Matching match){
		ArrayList<Edge> edges=match.getMatch_edges().get(this);
		ArrayList<MachineNode> machines=new ArrayList<MachineNode>();
		for(Edge e : edges){
			machines.add(e.getMachine());
		}
		for(MachineNode m: machines){
			for(MachineNode n: machines){
				if(!m.equals(n)){
					int m_index=this.pref.indexOf(m);
					int n_index=this.pref.indexOf(n);
					int real_mindex=machines.indexOf(m);
					int real_nindex=machines.indexOf(n);
					if(m_index<n_index && real_mindex>real_nindex){
						ComputationUtil.swapElements(m,n,machines);
					}
				}
			}
		}
		this.pref_pointer=this.pref.indexOf(machines.get(machines.size()-1));
		return machines;
	}
	
	public void updatePointerFixedIntex(MachineNode machine){
		this.pref_pointer=this.pref.indexOf(machine);
	}
	
	
	
	public int getPref_pointer() {
		return pref_pointer;
	}

}
