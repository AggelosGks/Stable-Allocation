package MainApp;

import Algorithms.GaleShapley;
import Algorithms.Instance;
import DataStructures.BipartiteGraph;
import DataStructures.Edge;
import DataStructures.JobNode;
import DataStructures.MachineNode;

public class Application {
	public static void main(String args[]){
		Instance i=new Instance(7,5,2,10);
		BipartiteGraph graph=i.createInstance();
		GaleShapley algorithm=new GaleShapley(graph);
		algorithm.createInitialMathcing();
		algorithm.getMatch().printMatching();
		MachineNode machine= MachineNode.getMachines().get(1);
		Edge.getEdge(JobNode.getDummy(), machine).setCurrent_time(0);
		System.out.println("--------------");
		System.out.println(Edge.getEdge(JobNode.getDummy(), machine).toString());
		algorithm.getMatch().printMatching();
	}
	
}
