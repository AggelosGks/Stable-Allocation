package DataStructures;

import java.util.ArrayList;
import java.util.HashMap;

import Computation.ComputationUtil;

public class MachineNode extends Node {
	private static MachineNode dummy;
	// static fields
	private static final ArrayList<MachineNode> machines = new ArrayList<MachineNode>();// total
																						// machine
																						// population
	// instance fields
	private ArrayList<JobNode> pref;// pref list
	public final double upper_cap;// capacity
	private int pref_pointer;

	// constructor
	public MachineNode(double upper_cap) {
		super();
		this.upper_cap = upper_cap;
		if (!(this.upper_cap == Integer.MAX_VALUE)) {
			machines.add(this);
		} else {
			dummy = this;
		}
		this.pref = new ArrayList<JobNode>();// initialize pref list
		this.pref_pointer = JobNode.getJobs().size();// pointer starts at least
														// prefered choice
	}

	@Override
	public String toString() {
		String x = "";
		for (JobNode job : pref) {
			x = x + " " + job.id;
		}
		return "MachineNode [ Id: " + this.id + ",pref=" + x + ", upper_cap=" + upper_cap + ", pref_pointer="
				+ pref_pointer + "]";
	}

	public void setPref(ArrayList<JobNode> pref) {
		this.pref = pref;
	}

	public static MachineNode getDummy() {
		return dummy;
	}

	public static void createDummyMachine() {
		new MachineNode(Integer.MAX_VALUE);
	}

	public static ArrayList<MachineNode> getMachines() {
		return machines;
	}

	/**
	 * Makes the logical comparison for two job nodes. The comparison is
	 * implemented by the indexes of two jobs in the preference list. One job is
	 * indicated by the pointer and its current position.
	 * 
	 * @param job
	 *            the job node to be compared
	 * @return true if job is preferable, false otherwise.
	 */
	public boolean prefersAccRej(JobNode job) {
		boolean isprefered = false;
		int index_candidate = pref.indexOf(job);
		if (index_candidate < pref_pointer) {
			isprefered = true;
		}
		return isprefered;
	}
	
	/**
	 * Makes the logical comparison for two job nodes. The comparison is
	 * implemented by the indexes of two jobs in the preference list. One job is
	 * indicated by the pointer and its current position.
	 * 
	 * @param job
	 *            the job node to be compared
	 * @return true if job is preferable, false otherwise.
	 */
	public boolean prefersAccRejExpEll(JobNode job) {
		boolean isprefered = false;
		int index_candidate = pref.indexOf(job);
		JobNode lp=this.getLeastPrefered();
		double avail=Edge.getEdge(lp,this).computeAvailableTime();
		if (index_candidate < pref_pointer && avail>0) {
			isprefered = true;
		}
		return isprefered;
	}

	/**
	 * Return the least prefered job
	 * 
	 * @return least_pref the least prefered job.
	 */
	public JobNode getLeastPrefered() {
		JobNode least_pref = pref.get(pref_pointer);
		return least_pref;
	}
	
	

	public HashMap<Node, Double> rejectTime(double amount, Matching match, JobNode proposed) {
		HashMap<Node, Double> rejected = new HashMap<Node, Double>();
		JobNode lp = this.getLeastPrefered();
		double time = Edge.getEdge(lp, this).getCurrent_time();
		if (time > amount) {
			Edge.getEdge(lp, this).setCurrent_time(time - amount);
			lp.setTime_consumed(lp.getTime_consumed() - (int) amount);
			rejected.put(lp, amount);
		} else if (time == amount) {
			Edge.getEdge(lp, this).setCurrent_time(0);
			// remove edge from matching
			match.removeEdgeFromMatch(lp, Edge.getEdge(lp, this));
			lp.setTime_consumed(lp.getTime_consumed() - (int) amount);
			rejected.put(lp, amount);
			// least prefered job changes
			refreshPointerIndex(match, proposed);
		} else {
			boolean change_index = false;
			double rejection = amount;// total amount of rejection
			while (rejection > 0) {// until reject amount decrease to zero
				lp = this.getLeastPrefered();// get least prefered
				time = Edge.getEdge(lp, this).getCurrent_time();
				// assign the minimum value of rejection and available
				int true_rej = (int) Math.min((Double) time, rejection);
				lp.setTime_consumed(lp.getTime_consumed() - true_rej);
				// if edge is empty assign 0 value not negative
				double assign = Math.max(0, time - rejection);
				Edge.getEdge(lp, this).setCurrent_time(assign);
				rejected.put(lp, (double) true_rej);
				if (assign == 0) {// if decreased to zero
					// remove edge from matching
					match.removeEdgeFromMatch(lp, Edge.getEdge(lp, this));
					// least prefered job changes or not
					change_index = refreshPointerIndex(match, proposed);
				}
				// the new least prefered didnt change
				if (change_index) {
					rejection = 0;
				} else {
					rejection = Math.min(0, time - rejection);
					rejection = Math.abs(rejection);
				}

			}
		}
		return rejected;
	}

	/**
	 * This method is responsible for updating the point on the preference list
	 * for a machine. After an acceptance has occured and no amount of job is
	 * assigned to the least prefered job, we increase the pointer to the index
	 * of the job that proposed last.
	 * 
	 * @param job
	 *            the job that proposed
	 */
	public boolean refreshPointerIndex(Matching match, JobNode proposed) {
		boolean cantchange = false;
		JobNode lest_pref = this.getLeastPrefered();// get current least
													// prefered
		Edge edge = Edge.getEdge(lest_pref, this);// extract edge
		if (!match.getMatch_edges().get(lest_pref).contains(edge)) {// if not
																	// exists in
																	// matching
			int index = this.pref.indexOf(lest_pref);
			for (int i = index; i >= 0; i--) {//iterate backwards 
				JobNode next = this.pref.get(i);
				if (match.getMatch_edges().get(next) != null) {//find first job that an edge exists
					if (match.getMatch_edges().get(next).contains(Edge.getEdge(next, this))) {
						this.pref_pointer = this.pref.indexOf(next);
						int real_index = this.pref_pointer;
						int pr_index = this.pref.indexOf(proposed);
						if (real_index < pr_index) {
							cantchange = true;

							// xeirizesai me mia boolean an mpei to if ***
						}
						break;
					}
				}
			}
			if (this.getLeastPrefered() == lest_pref) {
				this.pref_pointer = this.pref.indexOf(proposed);
			}
		}
		return cantchange;
	}
	
	private ArrayList<JobNode> retrieveListOnMatch(Matching match){
		System.out.println("The call from   " +this.id);
		ArrayList<JobNode> list=new ArrayList<JobNode>();
		for(JobNode job : JobNode.getJobs()){
			if(match.containsEdge(Edge.getEdge(job,this))){
				list.add(job);
			}
		}
		for(JobNode m: list){
			for(JobNode n: list){
				if(!m.equals(n)){
					int m_index=this.pref.indexOf(m);
					int n_index=this.pref.indexOf(n);
					int real_mindex=list.indexOf(m);
					int real_nindex=list.indexOf(n);
					if(m_index<n_index && real_mindex>real_nindex){
						ComputationUtil.swapElements(m,n,list);
					}
				}
			}
		}
		return list;
		
	}
	
	public void refreshPointerRotation(Matching m){
		ArrayList<JobNode> list=this.retrieveListOnMatch(m);
		this.pref_pointer=this.pref.indexOf(list.get(list.size()-1));
		
		
	}

	
	public int getPref_pointer() {
		return pref_pointer;
	}

	
	
}
