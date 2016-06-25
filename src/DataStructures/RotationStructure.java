package DataStructures;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import Algorithms.Instance;
import Computation.Label;
import Computation.LinearProgramm;
import MainApp.Application;

//Each rotation structure instance actually implements a full rotation exposure and ellimination.
//It contains a list of RotationPairs and a priorityqueue that manipulates the termination criteria.
public class RotationStructure {
	public int id;
	private PriorityQueue<RotationNode> queue;
	private ArrayList<RotationPair> pairs;
	private int elements;
	private boolean open;

	public RotationStructure() {
		this.id=0;
		this.pairs = new ArrayList<RotationPair>();
		this.queue = new PriorityQueue<RotationNode>();
		this.elements = 1;
		this.open = true;
	}

	public double retrieveLastDistributedAmount() {
		int size_of_pairs = this.pairs.size() - 1;
		return this.pairs.get(size_of_pairs).getAmount();
	}

	public MachineNode retrieveFirstRejectedMachine() {
		int size_of_pairs = this.pairs.size() - 1;
		return this.pairs.get(size_of_pairs).getAdded_to();
	}

	public boolean isOpen() {
		return this.open;
	}

	public void close() {
		this.open = false;
	}

	/**
	 * This method adds a pair in the list of the instance, and adds the
	 * respective node of the pair to the queue.Afterwards,
	 * 
	 * @param pair
	 * @return
	 */
	public JobNode addPair(RotationPair pair, Matching m) {
		if (pair == null) {
			System.err.println("Pair arrived empty");
		}
		JobNode rejected = null;
		JobNode to_stack = pair.getProposed_by();// get job that got worst
		this.queue.add(new RotationNode(to_stack.id, this.elements));
		this.elements++;
		this.pairs.add(pair);
		rejected = ((MachineNode) pair.getAdded_to()).getLeastPrefered();
		return rejected;
	}

	public boolean containsNode(int node_id) {
		boolean contains = false;
		for (RotationNode node : this.queue) {
			if (node.node_id == node_id) {
				contains = true;
				break;
			}
		}
		return contains;
	}

	public void elliminateStructure(Matching m) {
		double minimum_amount = Integer.MAX_VALUE;
		for (RotationPair pair : this.pairs) {
			if (minimum_amount > pair.getAmount()) {
				minimum_amount = pair.getAmount();
			}
		}
		// get job that got worst
		for (RotationPair pair : this.pairs) {
			JobNode proposed_by = pair.getProposed_by();
			// get machinenode that amount was extracted from
			MachineNode abstracted = pair.getExtracted_from();
			// get machinenode that  amount was added to
			MachineNode added = pair.getAdded_to();
			// get current amount of time between job and abstracted
			double current_time = Edge.getEdge(proposed_by, abstracted).getCurrent_time();
			//set new amount
			Edge.getEdge(proposed_by, abstracted).setCurrent_time(current_time - minimum_amount);
			//add rotation for edge
			LinearProgramm.initInfoAbstraction(Edge.getEdge(proposed_by, abstracted));
			LinearProgramm.addInfoAbstraction(Edge.getEdge(proposed_by, abstracted),this,minimum_amount);
			//delete if empty
			if (Edge.getEdge(proposed_by, abstracted).getCurrent_time() == 0) {
				m.removeEdgeFromMatch(Edge.getEdge(proposed_by, abstracted).getJob(),
				Edge.getEdge(proposed_by, abstracted));
			}
			
			//get time on edge between job and added
			current_time = Edge.getEdge(proposed_by, added).getCurrent_time();
			//add new amount
			Edge.getEdge(proposed_by, added).setCurrent_time(current_time + minimum_amount);
			//if edge not in match add it
			LinearProgramm.initInfoAddition(Edge.getEdge(proposed_by, added));
			LinearProgramm.addInfoAddition(Edge.getEdge(proposed_by, added),this,minimum_amount);
			if (!m.containsEdge(Edge.getEdge(proposed_by, added))) {
				m.addEdgeToMatch(Edge.getEdge(proposed_by, added).getJob(), Edge.getEdge(proposed_by, added));
				
			}
			added.refreshPointerIndex(m, proposed_by);
			addLabels(m,pair);
			// abstracted.refreshPointerRotation(m);
		}
	}
	
	public void addLabels(Matching m,RotationPair pair){
		addLabelPlus(pair);
		addLabelNon(pair);
		addLabelMinus(m,pair);
	}
	
	public void addLabelPlus(RotationPair pair){
		for(MachineNode m : pair.getProposed_by().getPref()){
			if(m.id==pair.getAdded_to().id){
				System.out.println(m.id);
				if(m.getLabel().get(pair.getProposed_by())==null){//init first if null
					m.getLabel().put(pair.getProposed_by(), new ArrayList<Label>());
				}
				m.getLabel().get(pair.getProposed_by()).add(new Label(this,"+"));
			}
		}
		
	}
	public void addLabelNon(RotationPair pair){
		for(JobNode j : pair.getExtracted_from().getPref()){
			if(j.id==pair.getProposed_by().id){
				if(j.getLabel().get(pair.getExtracted_from())==null){
					j.getLabel().put(pair.getExtracted_from(), new ArrayList<Label>());
				}
				j.getLabel().get(pair.getExtracted_from()).add(new Label(this,"X"));
			}
		}
	}
	
	public void addLabelMinus(Matching match,RotationPair pair){
			MachineNode extracted=pair.getExtracted_from();
			JobNode terminal=null;
			JobNode start=pair.getProposed_by();
			for(RotationPair pairS : this.pairs){
				if(pair!=pairS){
					if(pairS.getAdded_to().id==extracted.id){//find the next pair that extracted is added
						terminal=pairS.getProposed_by();
					}
				}
			}
			int start_index=extracted.getPref().indexOf(start);
			int last_index=extracted.getPref().indexOf(terminal);
			for(int i=start_index-1; i>last_index; i--){
				JobNode job=extracted.getPref().get(i);
				if(!match.containsEdge(Edge.getEdge(job,extracted))){
					for(MachineNode mach : job.getPref()){
						if(mach.id==extracted.id){
							if(mach.getLabel().get(pair.getProposed_by())==null){
								mach.getLabel().put(pair.getProposed_by(), new ArrayList<Label>());
							}
							mach.getLabel().get(pair.getProposed_by()).add(new Label(this,"-"));
						}
					}
				}
			}
		
	}

	public boolean existsAandC(JobNode rejected) {
		boolean exists = false;
		double minimum_amount = Integer.MAX_VALUE;
		for (RotationPair pair : this.pairs) {
			if (minimum_amount > pair.getAmount()) {
				minimum_amount = pair.getAmount();
			}
		}
		if (minimum_amount > 0) {
			exists = true;
		}

		boolean cycle = createsCycle(rejected);

		return (exists && cycle);
	}

	public String revealQueueStatus() {
		String x = "";
		PriorityQueue<RotationNode> q = new PriorityQueue<RotationNode>(this.queue);
		while (!q.isEmpty()) {
			x = x + " " + Integer.toString(q.remove().node_id);
		}
		return x;
	}

	private boolean createsCycle(JobNode rejected) {
		boolean control = true;
		ArrayList<JobNode> partition = new ArrayList<JobNode>();
		while (control) {
			int id = this.queue.remove().node_id;
			JobNode job = Instance.pickJobbyId(id);
			partition.add(job);
			if (job.id == rejected.id) {
				control = false;
			}
		}
		ArrayList<RotationPair> deleted = new ArrayList<RotationPair>();
		for (RotationPair pair : this.pairs) {
			boolean delete = true;
			for (JobNode p : partition) {
				if (pair.getProposed_by().id == p.id) {
					delete = false;
					break;
				}
			}
			if (delete) {
				deleted.add(pair);
			}
		}
		for (RotationPair pair : deleted) {
			this.pairs.remove(pair);
		}
		HashMap<MachineNode, Integer> m_v = new HashMap<MachineNode, Integer>();
		for (RotationPair pair : this.pairs) {
			MachineNode extracted = pair.getExtracted_from();
			if (m_v.get(extracted) == null) {
				m_v.put(extracted, 1);
			} else {
				m_v.replace(extracted, m_v.get(extracted) + 1);
			}
			MachineNode added = pair.getAdded_to();
			if (m_v.get(added) == null) {
				m_v.put(added, 1);
			} else {
				m_v.replace(added, m_v.get(added) + 1);
			}
		}
		boolean unfeasible = true;
		for (Entry<MachineNode, Integer> x : m_v.entrySet()) {
			if (x.getValue() != 2) {
				unfeasible = false;
				break;
			}
		}
		return unfeasible;
	}

	public void clearstack() {
		this.queue.clear();
	}


	public ArrayList<RotationPair> getPairs() {
		return pairs;
	}

	@Override
	public String toString() {
		String x = "";
		for (RotationPair pair : this.pairs) {
			System.out.println(pair.toString());
			Application.STEPS_IN_TEXT.add(pair.toString());
		}
		return x;
	}

	public ArrayList<Node> getPartitionListed(){
		ArrayList<Node> partition=new ArrayList<Node>();
		for(RotationPair pair : this.pairs){
			partition.add(pair.getAdded_to());
			partition.add(pair.getProposed_by());
		}
		return partition;
	}
	
	public RotationPair extractPairOfAdded(MachineNode machine){
		RotationPair pair_r=null;
		for(RotationPair pair:this.pairs){
			if(pair.getAdded_to().id==machine.id){
				pair_r=pair;
				break;
			}
		}
		return pair_r;
	}
	
	public RotationPair extractPairOfProposed(JobNode job){
		RotationPair pair_r=null;
		for(RotationPair pair:this.pairs){
			if(pair.getProposed_by().id==job.id){
				pair_r=pair;
				break;
			}
		}
		return pair_r;
	}
	
	
	
	public void setId(int id) {
		this.id = id;
	}



	private class RotationNode implements Comparable<RotationNode> {

		public final int node_id;
		private int added;

		public RotationNode(int node_id, int elements) {
			this.node_id = node_id;
			this.added = elements;
		}

		@Override
		public int compareTo(RotationNode o) {
			// TODO Auto-generated method stub
			if (this.added < o.added) {
				return 1;
			} else {
				return -1;
			}
		}

	}

}
