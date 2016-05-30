package Algorithms;

import java.util.ArrayList;
import java.util.HashMap;

import Computation.ComputationUtil;
import DataStructures.JobNode;
import DataStructures.MachineNode;
import DataStructures.Node;
import DataStructures.RotationPair;
import DataStructures.RotationStructure;


public class PosetGraphAlgorithm {
	private final ArrayList<RotationEdge> poset;
	private static  ArrayList<RotationStructure> rotations_order;
	
	public PosetGraphAlgorithm(){
		this.poset=new ArrayList<RotationEdge>(); 
		execute();
		for(RotationEdge edge : poset){
			String x="";
			x=Integer.toString(edge.first.id)+" "+Integer.toString(edge.second.id);
			System.out.println(x);
		}
	}
	
	
	
	public void execute(){
		for(RotationStructure R: rotations_order){
			for(RotationStructure r : rotations_order){
				ArrayList<Node> inter=ComputationUtil.getIntersectionListed(R.getPartitionListed(),r.getPartitionListed());
				if(ComputationUtil.cleanInsterSection(inter)){
					ArrayList<MachineNode> machines=ComputationUtil.splitMachines(inter);
					ArrayList<JobNode> jobs=ComputationUtil.splitJobs(inter);
					CheckRuleMachines(machines,R,r);
					CheckRuleJobs(jobs,R,r);
				}
			}
		}
	}
	
	public  void CheckRuleMachines(ArrayList<MachineNode> machines,RotationStructure Ra,RotationStructure Rb){
		for(MachineNode machine : machines){
			RotationPair pairA=Ra.extractPairOfAdded(machine);
			RotationPair pairB=Rb.extractPairOfAdded(machine);
			if(machine.gotMuchBetter(pairA, pairB)){
				boolean exists_inversed=false;
				for(RotationEdge e : poset){
					if(e.first.equals(Rb)&&e.second.equals(Ra)){
						exists_inversed=true;
					}
				}
				if(!exists_inversed){
					poset.add(new RotationEdge(Ra,Rb));
				}
				
			}
		}
	}
	
	public  void CheckRuleJobs(ArrayList<JobNode> jobs,RotationStructure Ra,RotationStructure Rb){
		for(JobNode job : jobs){
			RotationPair pairA=Ra.extractPairOfProposed(job);
			RotationPair pairB=Rb.extractPairOfProposed(job);
			if(job.gotMuchWorse(pairA, pairB)){
				boolean exists_inversed=false;
				for(RotationEdge e : poset){
					if(e.first.equals(Rb)&&e.second.equals(Ra)){
						exists_inversed=true;
					}
				}
				if(!exists_inversed){
					poset.add(new RotationEdge(Ra,Rb));
				}
			}
		}
	}
	
	
	
	public static void setRotations_order(ArrayList<RotationStructure> rotations_order) {
		PosetGraphAlgorithm.rotations_order = rotations_order;
	}



	private class RotationEdge  {

		public final RotationStructure first;
		public final RotationStructure second;

		public RotationEdge (RotationStructure a, RotationStructure b) {
			this.first = a;
			this.second = b;
		}

	}
}
