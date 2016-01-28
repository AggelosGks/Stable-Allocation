package DataStructures;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class RotationStructure {

	private PriorityQueue<RotationNode> queue;
	private ArrayList<RotationPair> pairs;
	private int elements;
	
	public RotationStructure(){
		this.pairs=new ArrayList<RotationPair>();
		this.queue=new PriorityQueue<RotationNode>();
		this.elements=1;
	}
	
	
	public Node addPair(RotationPair pair) {
		Node rejected=null;
		Node to_stack=pair.getProposed_by();
		if(containsNode(to_stack.id)){
			//finish rotation
		}
		else{
			this.queue.add(new RotationNode(to_stack.id,this.elements));
			this.elements++;
			this.pairs.add(pair);
			MachineNode machine=((MachineNode)pair.getAdded_to());
			double amount=pair.getAmount();
			JobNode lp=machine.getLeastPrefered();
			double amount_on_edge=Edge.getEdge(lp, machine).getCurrent_time();
			double amount_after=Math.max(0,amount_on_edge-amount);
			if(amount_after==0){
				rejected=null;
			}else{
				rejected=lp;
			}
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
