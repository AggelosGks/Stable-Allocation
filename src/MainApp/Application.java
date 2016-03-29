package MainApp;

import Algorithms.ExposureElliminationAlgorithm;
import Algorithms.GaleShapley;
import Algorithms.Instance;
import DataStructures.BipartiteGraph;
import DataStructures.JobNode;
import DataStructures.MachineNode;


public class Application {
	public static void main(String args[]){
		Instance i=new Instance(3,3,10,5);
		BipartiteGraph graph=i.createReadyInst();
		GaleShapley algorithm=new GaleShapley(graph);
		i.testInstanceIntegration();
		algorithm.execute();
		algorithm.getMatch().printMatching();
		
		for(MachineNode mach : MachineNode.getMachines()){
			mach.refreshPointerRotation(algorithm.getMatch());
			
		}
		ExposureElliminationAlgorithm exp=new ExposureElliminationAlgorithm(algorithm.getMatch());
		exp.execute();
		/**
		 * System.out.println("                       ");
		System.out.println("                       ");
		System.out.println("TEST");
		GaleShapley algorithm1=new GaleShapley(i.SwapInstance());
		i.testInstanceIntegration();
		algorithm1.execute();
		algorithm1.getMatch().printMatchingSwaped();
		 * 
		 * 
		 * 
		 */
		
		
		
		
		
		 
	
		
		
	}
	
}
