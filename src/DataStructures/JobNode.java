package DataStructures;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map.Entry;

import Algorithms.ExposureElliminationAlgorithm;

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

	public JobNode(int id, double proc_time) {
		super(id);
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

	public ArrayList<MachineNode> getPref() {
		return pref;
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

	public static void setDummy(JobNode dummy) {
		JobNode.dummy = dummy;
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

	public boolean proposeExp_Ell(double amount, MachineNode to) {
		if (to.prefersAccRej(this) && amount > 0) {
			return true;
		} else {
			return false;
		}
	}

	// not used
	public boolean proposeExp_Ellinside(double amount, MachineNode to) {
		if (to.prefersAccRejinside(this) && amount > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void refreshPointerIndex() {
		this.pref_pointer++;
	}

	public boolean canGetWorse2(Matching match) {

		RotationPair pair = null;
		boolean can = false;
		ArrayList<MachineNode> machines = this.getListAssignedtoMatch(match);// get
																				// all
																				// machines
																				// that
																				// currently
																				// assigned
																				// to
																				// match
																				// with
																				// this
																				// job
		for (MachineNode machine : machines) {// iterate machines
			double amount = Edge.getEdge(this, machine).getCurrent_time();// get
																			// time
																			// on
																			// edge

			int index = this.pref.indexOf(machine);// get index on preflist
			MachineNode next = this.pref.get(index + 1);
			System.out.println("Job: " + this.id + " proposes to machine: " + next.id + " amount: " + amount+" previous "+machine.id);
			if (!next.isDummy()) {
				double available_amount = extractFeasibleAmount(next, amount);
				if (this.proposeExp_Ell(available_amount, next)) {// if machine
																	// accepts
																	// begin
																	// rotation
					System.out.println("Machine: " + next.id + " accepts");
					pair = new RotationPair(machine, next, this, available_amount);
					System.out.println("Pair: " + pair.toString());
					RotationStructure rotation = new RotationStructure();
					JobNode rejected = rotation.addPair(pair, match);
					System.out.println("Rejected from machine: " + next.id + " is Job: " + rejected.id);
					while (rotation.isOpen()) {

						MachineNode first_rejection = rotation.retrieveFirstRejectedMachine();
						RotationPair next_pair = rejected.extractNextPair(rotation.retrieveLastDistributedAmount(),match, first_rejection);
						if (next_pair == null) {// this code of if is obselet
							rotation.close();
						} else {
							System.out.println("			Next Pair: " + next_pair.toString());
							rejected = rotation.addPair(next_pair, match);
							System.out.println("			REJECTED IS : " + rejected.id);
							if (rotation.containsNode(rejected.id)) {
								rotation.close();
								System.out.println("--------------> " + rotation.revealQueueStatus());
								if (rotation.existsAandC(rejected)) {

									can = true;
									ExposureElliminationAlgorithm.setRunner(rotation);// save
																						// last
																						// rotation
								}
							}
						}
					}
				}
			}
			if (can) {
				break;
			}
		}
		return can;
	}

	public RotationPair extractNextPair(double amount, Matching match, MachineNode firstrejection) {
		RotationPair pair = null;

		int index = this.pref.indexOf(firstrejection);// get index on preflist
		for (MachineNode to : this.pref) {// iterate all next ones on preflist
											// (inside rotation a change can be
											// make 2 or 3 steps forward
			if (pref.indexOf(to) > index && to != MachineNode.getDummy()) {
				double available_amount = extractFeasibleAmount(to, amount);
				if (this.proposeExp_Ell(available_amount, to)) {
					pair = new RotationPair(firstrejection, to, this, available_amount);
					break;
				}
			}
		}
		return pair;
	}

	public double extractFeasibleAmount(MachineNode next, double amount) {
		double avail = Edge.getEdge(this, next).computeAvailableTime();
		double feasible = Math.min(amount, avail);
		double rejected = Edge.getEdge(next.getLeastPrefered(), next).getCurrent_time();
		return Math.min(feasible, rejected); // minimum of two amounts
	}

	// Return the least prefered machine of a job that is currently assigned to
	// job in the match
	public ArrayList<MachineNode> getListAssignedtoMatch(Matching match) {
		ArrayList<Edge> edges = match.getMatch_edges().get(this);
		TreeMap<Integer, MachineNode> tree = new TreeMap<Integer, MachineNode>();
		ArrayList<MachineNode> machines = new ArrayList<MachineNode>();
		for (Edge e : edges) {
			tree.put(this.pref.indexOf(e.getMachine()), e.getMachine());
		}
		while (!tree.isEmpty()) {
			Entry<Integer, MachineNode> x = tree.pollFirstEntry();
			machines.add(x.getValue());
		}
		this.pref_pointer = this.pref.indexOf(machines.get(machines.size() - 1));
		return machines;
	}

	public void updatePointerFixedIntex(MachineNode machine) {
		this.pref_pointer = this.pref.indexOf(machine);
	}

	public int getPref_pointer() {
		return pref_pointer;
	}
	
	

}
