package MainApp;

import Algorithms.GaleShapley;
import Algorithms.Instance;
import DataStructures.BipartiteGraph;
import DataStructures.Edge;
import DataStructures.JobNode;
import DataStructures.MachineNode;

public class Application {
	public static void main(String args[]){
		Instance i=new Instance(3,3,2,10);
		BipartiteGraph graph=i.createInstance();
		i.testInstanceIntegration();
		GaleShapley algo=new GaleShapley(graph);
		algo.execute();
		
	}
	
}
