package DataStructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.PriorityQueue;

//Each rotation structure instance actually implements a full rotation exposure and ellimination.
//It contains a list of RotationPairs and a priorityqueue that manipulates the termination criteria.
public class RotationStructure {

	private PriorityQueue<RotationNode> queue;
	private ArrayList<RotationPair> pairs;
	private int elements;
	private boolean open;
	
	
	public RotationStructure(){
		this.pairs=new ArrayList<RotationPair>();
		this.queue=new PriorityQueue<RotationNode>();
		this.elements=1;
		this.open=true;
	}
	
	public double retrieveLastDistributedAmount(){
		int size_of_pairs=this.pairs.size()-1;	
		return this.pairs.get(size_of_pairs).getAmount();
	}
	
	public MachineNode retrieveFirstRejectedMachine(){
		int size_of_pairs=this.pairs.size()-1;	
		return this.pairs.get(size_of_pairs).getAdded_to();
	}
	
	public boolean isOpen(){
		return this.open;
	}
	
	
	
	public void close(){
		this.open=false;
	}
	/**
	 * This method adds a pair in the list of the instance, and adds the respective node of the pair 
	 * to the queue.Afterwards, 
	 * @param pair
	 * @return
	 */
	public JobNode addPair(RotationPair pair,Matching m) {
		if(pair==null){
			System.err.println("Pair arrived empty");
		}
		JobNode rejected=null;
		JobNode to_stack=pair.getProposed_by();//get job that got worst
		this.queue.add(new RotationNode(to_stack.id,this.elements));
		this.elements++;
		this.pairs.add(pair);
		rejected=((MachineNode)pair.getAdded_to()).getLeastPrefered();
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
					
				}
				
				current_time=Edge.getEdge(proposed_by, added).getCurrent_time();//get time one edge between job and added
				Edge.getEdge(proposed_by, added).setCurrent_time(current_time+minimum_amount);//add new amount
				if(!m.containsEdge(Edge.getEdge(proposed_by, added))){//if edge not in match add it
					m.addEdgeToMatch(Edge.getEdge(proposed_by, added).getJob(),Edge.getEdge(proposed_by, added));
				}
				added.refreshPointerIndex(m,proposed_by);
				//abstracted.refreshPointerRotation(m);
		}
	}
	
	public boolean existsAandC(){
		boolean exists=false;
		double minimum_amount=Integer.MAX_VALUE;
		for(RotationPair pair : this.pairs){
			if(minimum_amount>pair.getAmount()){
				minimum_amount=pair.getAmount();
			}
		}
		if(minimum_amount>0){
			exists=true;
		}
	
		boolean cycle=createsCycle();
		
		return (exists&&cycle);
	}
	
	public String revealQueueStatus(){
		String x="";
		for(RotationNode n: this.queue){
			x=x+" "+n.node_id;
		}
		return x;
	}

	private boolean createsCycle(){
		HashMap<MachineNode,Integer> m_v=new HashMap<MachineNode,Integer>();
		for(RotationPair pair : this.pairs){
			MachineNode extracted=pair.getExtracted_from();
			if(m_v.get(extracted)==null){
				m_v.put(extracted, 1);
			}else{
				m_v.replace(extracted, m_v.get(extracted)+1);
			}
			MachineNode added=pair.getAdded_to();
			if(m_v.get(added)==null){
				m_v.put(added, 1);
			}else{
				m_v.replace(added, m_v.get(added)+1);
			}
		}
		boolean unfeasible=true;
		for(Entry<MachineNode, Integer> x : m_v.entrySet()){
			if(x.getValue()!=2){
				unfeasible=false;
				break;
			}
		}
		return unfeasible;
	}
	
	
	public void clearstack(){
		this.queue.clear();
	}
	
	

	@Override
	public String toString() {
		String x="";
		for(RotationPair pair : this.pairs){
			System.out.println(pair.toString());
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
