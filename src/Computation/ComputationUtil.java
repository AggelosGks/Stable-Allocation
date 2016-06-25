package Computation;

import java.util.ArrayList;

import DataStructures.JobNode;
import DataStructures.MachineNode;
import DataStructures.Node;

public class ComputationUtil {
	public static void swapElements(MachineNode m, MachineNode n,ArrayList<MachineNode> list){
		list.set(list.indexOf(m),n);
		list.set(list.indexOf(n),m);
	}
	public static void swapElements(JobNode m, JobNode n,ArrayList<JobNode> list){
		list.set(list.indexOf(m),n);
		list.set(list.indexOf(n),m);
	}
	
	public static ArrayList<Node> getIntersectionListed(ArrayList<Node> listA, ArrayList<Node> listB){
		ArrayList<Node> intersection=new ArrayList<Node>();
		for(Node N : listA){
			if(listB.contains(N)){
				intersection.add(N);
			}
		}
		return intersection;
	}
	
	public static boolean cleanInsterSection(ArrayList<Node> intersection){
		boolean Jobexists=false;
		boolean Machineexists=false;
		for(Node node : intersection){
			if(node instanceof JobNode){
				Jobexists=true;
			}else{
				Machineexists=true;
			}
			if(Jobexists||Machineexists){
				break;
			}
		}
		return Jobexists||Machineexists;
	}
	
	public static ArrayList<MachineNode> splitMachines(ArrayList<Node> intersection){
		ArrayList<MachineNode> machines=new ArrayList<MachineNode>();
		for(Node node : intersection){
			if(node instanceof MachineNode){
				machines.add((MachineNode)node);
			}
		}
		return machines;
	}
	
	public static ArrayList<JobNode> splitJobs(ArrayList<Node> intersection){
		ArrayList<JobNode> machines=new ArrayList<JobNode>();
		for(Node node : intersection){
			if(node instanceof JobNode){
				machines.add((JobNode)node);
			}
		}
		return machines;
	}
	
	
}
