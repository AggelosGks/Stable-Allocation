package MainApp;

import Algorithms.GaleShapley;
import Algorithms.Instance;
import DataStructures.BipartiteGraph;
import DataStructures.Edge;
import DataStructures.JobNode;
import DataStructures.MachineNode;

public class Application {
	public static void main(String args[]){
		Instance i=new Instance(3,3,10,5);
		BipartiteGraph graph=i.createInstance();
		GaleShapley algorithm=new GaleShapley(graph);
		i.testInstanceIntegration();
		algorithm.execute2();
		algorithm.getMatch().printMatching();
	}
	
}
