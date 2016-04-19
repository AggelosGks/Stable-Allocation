package Algorithms;

import java.util.ArrayList;
import java.util.HashMap;

import DataStructures.BipartiteGraph;
import DataStructures.Edge;
import DataStructures.JobNode;
import DataStructures.MachineNode;
import DataStructures.Matching;
import DataStructures.Node;

public class GaleShapley {
	private ArrayList<JobNode> jobs_unassigned;
	private ArrayList<JobNode> jobs_assigned;
	private final BipartiteGraph graph;
	private Matching match;

	// constructor details
	public GaleShapley(BipartiteGraph graph) {
		this.graph = graph;// the graph that algorithm will be executed on.
		this.jobs_unassigned = graph.jobs;// list of jobs
		this.jobs_assigned = new ArrayList<JobNode>();
		this.match = new Matching();
	}

	/**
	 * Removes the indicated job given as parameter.
	 * 
	 * @param job
	 *            the fully assigned job to be removed.
	 */
	public void removeJob(JobNode job) {
		jobs_unassigned.remove(job);
	}

	/**
	 * Extracts the first unassigned job
	 * 
	 * @return false if all jobs are fully assigned, true otherwise.
	 */
	public JobNode extractUnassigned() {
		JobNode job = null;
		for (JobNode j : jobs_unassigned) {
			if (!j.isFullyAssigned()) {
				job = j;
				break;
			}
		}
		return job;
	}

	/**
	 * Checks if all jobs in list are fully assigned
	 * 
	 * @return false if all jobs are fully assigned, true otherwise.
	 */
	public boolean hasMoreUnassigned() {
		boolean hasMore = false;
		for (JobNode j : jobs_unassigned) {
			if (!j.isFullyAssigned()) {
				hasMore = true;
				break;
			}
		}
		return hasMore;
	}

	/**
	 * Creates the initial matching before execution. All machines fully
	 * assigend to the dummy job.
	 */
	public void createInitialMathcing() {
		JobNode dummy = JobNode.getDummy();
		for (MachineNode machine : MachineNode.getMachines()) {// iterate
																// machines
			Edge.getEdge(dummy, machine).fillEdge();
			;// extract relevant edge with dummy job
				// full arc
			match.addEdgeToMatch(dummy, Edge.getEdge(dummy, machine));
		}
	}

	public Matching getMatch() {
		return match;
	}

	/**
	 * Implenents the algorithm procedure.
	 */
	public void execute() {
		createInitialMathcing();
		while (hasMoreUnassigned()) {
			JobNode job = extractUnassigned();// extract first unassigned

			// compute machine for proposal
			double time_left = job.computeLeftTime();// compute left time
			MachineNode proposal = job.getFirstChoiceForProposal();
			// available amount of time on arc
			double available = Edge.getEdge(job, proposal).computeAvailableTime();
			// compute minimum amount that can flow through arc
			double pro_amount = Math.min(time_left, available);
			System.out.println("Job: " + job.id + " proposes to " + proposal.id + " amount of : " + pro_amount);
			if (job.proposeShapley(pro_amount, proposal)) {// acceptance occurs
				System.out.println("Accepts");
				HashMap<Node, Double> rejected = proposal.rejectTime(pro_amount, match, job);

				ArrayList<Node> rejections = new ArrayList<Node>();
				double total = 0;
				for (Node key : rejected.keySet()) {
					rejections.add(key);
					total = total + rejected.get(key);
				}
				for (Node rej : rejections) {
					// if the rejected node was fully assigned
					if (!(rej.isDummy()) && jobs_assigned.contains(rej)) {
						// remove from asssigned
						jobs_assigned.remove(rej);
						// add to unassigned
						jobs_unassigned.add((JobNode) rej);
					}
				}
				job.setTime_consumed(job.getTime_consumed() + (int) total);
				if (match.containsEdge(Edge.getEdge(job, proposal))) {
					double current = Edge.getEdge(job, proposal).getCurrent_time();
					Edge.getEdge(job, proposal).setCurrent_time(current + total);
				} else {// set amount of time for new arc
					Edge.getEdge(job, proposal).setCurrent_time(total);
					// add arc to mathcing
					match.addEdgeToMatch(job, Edge.getEdge(job, proposal));
				}
				if (job.isFullyAssigned()) {
					removeJob(job);
					jobs_assigned.add(job);
				}
			} else {// rejection occurs
				job.refreshPointerIndex();
				System.out.println("Rejection! Job : " + job.id + " refreshes index to "
						+ job.getPref().get(job.getPref_pointer()).id);
			}
			this.match.printMatching();
			System.out.println(" ");
			for (MachineNode m : MachineNode.getMachines()) {

				System.out.println("Machine: " + m.id + "LP: " + m.getLeastPrefered().id);

			}
			System.out.println(" ");
		}
	}

	

	public BipartiteGraph getGraph() {
		return graph;
	}

}
