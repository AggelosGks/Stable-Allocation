package MainApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


import Algorithms.ExposureElliminationAlgorithm;
import Algorithms.GaleShapley;
import Algorithms.Instance;
import DataStructures.BipartiteGraph;
import DataStructures.Edge;
import DataStructures.JobNode;
import DataStructures.MachineNode;
import DataStructures.Matching;
import DataStructures.Node;



public class Application {
	
	
	public static void main(String args[]) throws CloneNotSupportedException{
		
	
		Instance i=new Instance(4,4,20,2);
			BipartiteGraph graph=i.createReadyInst3();
			
			//shapley jobs orieented
			GaleShapley algorithm=new GaleShapley(graph);
			i.testInstanceIntegration();
			algorithm.execute();
			System.out.println("---------------JOB OPTIMAL SHAPLEY---------------");
			algorithm.getMatch().printMatching();
			System.out.println("--------------------------------------------------");
			System.out.println("                       ");
			System.out.println("                       ");
			//
			
			
			//exposure ellimination
			ExposureElliminationAlgorithm exp=new ExposureElliminationAlgorithm(algorithm.getMatch());
			exp.execute();
			Matching job_opt=algorithm.getMatch();
			job_opt.printMatching();
			System.out.println("---------------MACHINE OPTIMAL ROTATIONS---------------");
			algorithm.getMatch().printMatching();
			System.out.println("--------------------------------------------------");
			System.out.println("                       ");
			System.out.println("                       ");
			//
			
			//shapley machines orieented
			System.out.println("SWAPED");
			GaleShapley inversed=new GaleShapley(i.SwapInstance());
			i.testInstanceIntegration();
			inversed.execute();
			inversed.getMatch().printMatchingSwaped();
			Matching mach_opt=inversed.getMatch();
			if(mach_opt.areEqual(job_opt)){
				System.out.println("Yes");
			}else{
				System.out.println("No");
			}
				 
		
		
	}
	public static void readInstance(){
		HashMap<Integer,String> preferences=new HashMap<Integer,String>();
		String Path="C:\\Users\\aggelos\\Desktop\\instance.txt";
		File file=new File(Path);
		BufferedReader in=null;
		try {
			in = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int counter=2;
		String line;
		try {
			while((line = in.readLine()) != null)
			{
			   if(line.startsWith("-")){
				   preferences.put(counter,extractInfoMachines(line));
			   }else{
				   preferences.put(counter,extractInfoJobs(line));
			   }
			   counter++;  
			 
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String extractInfoJobs(String line){
		int id=0;
		double time=0;
		String pref="";
		id=Integer.parseInt(line.substring(0,1));
		time=Double.parseDouble(line.substring(2, 5));
		pref=line.substring(6,10);
		System.out.println(id+" "+time+" "+pref);
		//new JobNode(id,time);
		return pref;
		
	}
	
	public static String extractInfoMachines(String line){
		int id=0;
		double time=0;
		String pref="";
		id=Integer.parseInt(line.substring(1,2));
		time=Double.parseDouble(line.substring(3, 6));
		pref=line.substring(7,11);
		System.out.println(id+" "+time+" "+pref);
		//new MachineNode(id,time);
		return pref;
		
	}
	
	
}
