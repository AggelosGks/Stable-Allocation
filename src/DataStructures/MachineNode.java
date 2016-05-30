package DataStructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import MainApp.Application;

import java.util.TreeMap;

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

	public MachineNode(int id, double upper_cap) {
		super(id);
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

	public ArrayList<JobNode> getPref() {
		return pref;
	}

	public static void setDummy(MachineNode dummy) {
		MachineNode.dummy = dummy;
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

	// not used
	public boolean prefersAccRejinside(JobNode job) {
		boolean isprefered = false;
		int index_candidate = pref.indexOf(job);
		if (index_candidate <= pref_pointer) {
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
		if (time > amount) {// proposed amount is less than time on arc with lp
			Application.STEPS_IN_TEXT.add("    Machine: " + this.id + " rejects time from: " + lp.id);
			Edge.getEdge(lp, this).setCurrent_time(time - amount);
			lp.setTime_consumed(lp.getTime_consumed() - (int) amount);
			rejected.put(lp, amount);// least prefered job does not change
			Application.STEPS_IN_TEXT.add("	Least prefered job does not change");
		} else if (time == amount) {// amount and time on arc with lp are equal
			Edge.getEdge(lp, this).setCurrent_time(0);
			// remove edge from matching
			match.removeEdgeFromMatch(lp, Edge.getEdge(lp, this));
			lp.setTime_consumed(lp.getTime_consumed() - (int) amount);
			rejected.put(lp, amount);
			// least prefered job changes
			refreshPointerIndex(match, proposed);
			Application.STEPS_IN_TEXT.add("    Machine: " + this.id + " rejects time from: " + lp.id);
			
			Application.STEPS_IN_TEXT.add("	Least prefered job changes to " + this.getLeastPrefered().id);
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
				Application.STEPS_IN_TEXT.add("    Machine: " + this.id + " rejects time from: " + lp.id);
				if (assign == 0) {// if decreased to zero
					// remove edge from matching
					match.removeEdgeFromMatch(lp, Edge.getEdge(lp, this));
					// least prefered job changes or not
					change_index = refreshPointerIndex(match, proposed);
					Application.STEPS_IN_TEXT.add("	Least prefered job changes to " + this.getLeastPrefered().id);
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
			for (int i = index; i >= 0; i--) {// iterate backwards
				JobNode next = this.pref.get(i);
				if (match.getMatch_edges().get(next) != null) {// find first job
																// that an edge
																// exists
					if (match.getMatch_edges().get(next).contains(Edge.getEdge(next, this))) {
						this.pref_pointer = this.pref.indexOf(next);
						int real_index = this.pref_pointer;
						int pr_index = this.pref.indexOf(proposed);
						if (real_index < pr_index) {// if next job that is
													// assigned is most prefered
													// than proposed
							cantchange = true;// then pointer can not change
							this.pref_pointer = this.pref.indexOf(proposed);
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

	private ArrayList<JobNode> retrieveListOnMatch(Matching match) {
		ArrayList<JobNode> list = new ArrayList<JobNode>();
		TreeMap<Integer, JobNode> tree = new TreeMap<Integer, JobNode>();
		for (JobNode job : JobNode.getJobs()) {
			if (match.containsEdge(Edge.getEdge(job, this))) {
				tree.put(this.pref.indexOf(job), job);
			}
		}
		while (!tree.isEmpty()) {
			Entry<Integer, JobNode> x = tree.pollFirstEntry();
			list.add(x.getValue());
		}
		return list;

	}

	public void refreshPointerRotation(Matching m) {
		ArrayList<JobNode> list = this.retrieveListOnMatch(m);
		if (list.size() > 0) {
			this.pref_pointer = this.pref.indexOf(list.get(list.size() - 1));
		} else {
			this.pref_pointer = this.pref.indexOf(JobNode.getDummy());
		}
	}

	public void refreshPointerRotation(Matching m, JobNode job) {
		ArrayList<JobNode> list = this.retrieveListOnMatch(m);
		list.remove(job);
		if (list.size() > 0) {
			this.pref_pointer = this.pref.indexOf(list.get(list.size() - 1));
		} else {
			this.pref_pointer = this.pref.indexOf(JobNode.getDummy());
		}
		Application.STEPS_IN_TEXT.add(this.id + " new pointer " + this.pref_pointer);
	}

	public int getPref_pointer() {
		return pref_pointer;
	}

	public static void refreshMachines(Matching m) {
		for (MachineNode machine : machines) {
			machine.refreshPointerRotation(m);
		}
	}

	public boolean gotMuchBetter(RotationPair pairA, RotationPair pairB){
		boolean did=false;
		JobNode endPairA=pairA.getProposed_by();
		JobNode endPairB=pairB.getProposed_by();
		int indexA=this.pref.indexOf(endPairA);
		int indexB=this.pref.indexOf(endPairB);
		if(indexB<indexA){//most preferable for machine
			did=true;
		}
		return did;
	}
	
}
