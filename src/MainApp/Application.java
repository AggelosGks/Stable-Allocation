package MainApp;

import Algorithms.ExposureElliminationAlgorithm;
import Algorithms.GaleShapley;
import Algorithms.Instance;
import DataStructures.BipartiteGraph;


public class Application {
	public static void main(String args[]){
		Instance i=new Instance(3,3,10,5);
		BipartiteGraph graph=i.createReadyInst();
		GaleShapley algorithm=new GaleShapley(graph);
		i.testInstanceIntegration();
		algorithm.execute();
		algorithm.getMatch().printMatching();
		ExposureElliminationAlgorithm exp=new ExposureElliminationAlgorithm(algorithm.getMatch());
		exp.execute();
		
		
	}
	
}
