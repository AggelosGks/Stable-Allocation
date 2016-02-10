package DataStructures;

import java.util.ArrayList;
import java.util.PriorityQueue;

//Each rotation structure instance actually implements a full rotation exposure and ellimination.
//It contains a list of RotationPairs and a priorityqueue that manipulates the termination criteria.
public class RotationStructure {

	private PriorityQueue<RotationNode> queue;
	private ArrayList<RotationPair> pairs;
	private int elements;
	
	public RotationStructure(){
		this.pairs=new ArrayList<RotationPair>();
		this.queue=new PriorityQueue<RotationNode>();
		this.elements=1;
	}
	
	/**
	 * This method adds a pair in the list of the instance, and adds the respective node of the pair 
	 * to the queue.Afterwards, 
	 * @param pair
	 * @return
	 */
	public Node addPair(RotationPair pair) {
		Node rejected=null;
		Node to_stack=pair.getProposed_by();
		if(containsNode(to_stack.id)){
			rejected=null;
		}
		else{
			this.queue.add(new RotationNode(to_stack.id,this.elements));
			this.elements++;
			this.pairs.add(pair);
			rejected=((MachineNode)pair.getAdded_to()).getLeastPrefered();
		}
		return rejected;
	}

	public boolean containsNode(int node_id){
		boolean contains=false;
		for(RotationNode node : this.queue){
			if(node.node_id==node_id){
				contains=true;
				break;
			}
		}
		return contains;
	}

	public void elliminateStructure(Matching m){
		double minimum_amount=Integer.MAX_VALUE;
		for(RotationPair pair : this.pairs){
			if(minimum_amount>pair.getAmount()){
				minimum_amount=pair.getAmount();
			}
		}
		for(RotationPair pair :this.pairs){
			JobNode proposed_by=pair.getProposed_by();//get job that got worst
			MachineNode abstracted=pair.getExtracted_from();//get machinenode that amount was extracted from
			MachineNode added=pair.getAdded_to();//get machinenode that amount was added to
			double current_time=Edge.getEdge(proposed_by, abstracted).getCurrent_time();//get current amount of time between job and abstracted
			Edge.getEdge(proposed_by, abstracted).setCurrent_time(current_time-minimum_amount);//set new amount
			if(Edge.getEdge(proposed_by, abstracted).getCurrent_time()==0){//delete if empty
				m.removeEdgeFromMatch(Edge.getEdge(proposed_by, abstracted).getJob(), Edge.getEdge(proposed_by, abstracted));
				//abstracted.refreshPointerRotation(m);
			}
			
			current_time=Edge.getEdge(proposed_by, added).getCurrent_time();//get time one edge between job and added
			Edge.getEdge(proposed_by, added).setCurrent_time(current_time+minimum_amount);//add new amount
			if(!m.containsEdge(Edge.getEdge(proposed_by, added))){//if edge not in match add it
				m.addEdgeToMatch(Edge.getEdge(proposed_by, added).getJob(),Edge.getEdge(proposed_by, added));
			}
			added.refreshPointerIndex(m,proposed_by);
		}
	}
	
	public String revealQueueStatus(){
		String x="";
		for(RotationNode n: this.queue){
			x=x+" "+n.node_id;
		}
		return x;
	}




	private class RotationNode implements Comparable<RotationNode>{

		public final int node_id;
		private int added;
		
		public RotationNode(int node_id, int elements){
			this.node_id=node_id;
			this.added=elements;
		}
		
		@Override
		public int compareTo(RotationNode o) {
			// TODO Auto-generated method stub
			if(this.added<o.added){
				return -1;
			}else{
				return 1;
			}
		}
		
	}
	
}
