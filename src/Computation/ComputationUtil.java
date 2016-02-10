package Computation;

import java.util.ArrayList;

import DataStructures.JobNode;
import DataStructures.MachineNode;

public class ComputationUtil {
	public static void swapElements(MachineNode m, MachineNode n,ArrayList<MachineNode> list){
		list.set(list.indexOf(m),n);
		list.set(list.indexOf(n),m);
	}
	public static void swapElements(JobNode m, JobNode n,ArrayList<JobNode> list){
		list.set(list.indexOf(m),n);
		list.set(list.indexOf(n),m);
	}
}
