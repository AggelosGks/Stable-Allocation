package Computation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import Algorithms.ExposureElliminationAlgorithm;
import Algorithms.PosetGraphAlgorithm.RotationEdge;
import DataStructures.Edge;
import DataStructures.JobNode;
import DataStructures.MachineNode;
import DataStructures.RotationPair;
import DataStructures.RotationStructure;


public class CsvCreation {
 private static int share=26;  
  public final String name; 
  private final ComputationalTimes times;
   private final int no_J;
   private final int no_M;
   private final int no_Rot;
   private final ArrayList<RotationEdge> poset;
   
	
	
	public CsvCreation(int no_J, int no_M, int no_Rot,ArrayList<RotationEdge> poset,ComputationalTimes times) {
	share++;
	this.name=Integer.toString(no_J)+"X"+Integer.toString(no_M)+"-"+Integer.toString(share)+".csv";
	this.no_J = no_J;
	this.no_M = no_M;
	this.no_Rot = no_Rot;
	this.poset=new ArrayList<RotationEdge>(poset);
	this.times=times;
	try {
		execute();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}



	private void execute()throws FileNotFoundException{
		 PrintWriter pw=null;
		try {
			pw = new PrintWriter(new File(name));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			ArrayList<RotationStructure> rotations=new ArrayList<RotationStructure>(ExposureElliminationAlgorithm.rots);
	        StringBuilder sb = new StringBuilder();
	        sb.append("NUMBER OF JOBS");
	        sb.append(',');
	        sb.append(Integer.toString(no_J));
	        sb.append('\n');

	        sb.append("NUMBER OF MACHINES");
	        sb.append(',');
	        sb.append(Integer.toString(no_M));
	        sb.append('\n');
	        
	        sb.append("NUMBER OF ROTATIONS");
	        sb.append(',');
	        sb.append(Integer.toString(no_Rot));
	        sb.append('\n');
	        sb.append("VALUE OF ROTATIONS");
	        sb.append(',');
	        for(RotationStructure r : rotations){
	        	sb.append("R"+Integer.toString(r.id));
	        	sb.append(',');
	        }
	        sb.append('\n');
	        sb.append("VALUES");
	        sb.append(',');
	        for(RotationStructure r : rotations){
	        	double minimum_amount = Integer.MAX_VALUE;
	    		for (RotationPair pair : r.getPairs()) {
	    			if (minimum_amount > pair.getAmount()) {
	    				minimum_amount = pair.getAmount();
	    			}
	    		}
	    		sb.append(Double.toString(minimum_amount));
	        	sb.append(',');
	        }
	        sb.append('\n');
	        sb.append("ROTATION POSET GRAPH CONSTR");
	        sb.append(',');
	        for(RotationStructure r : rotations){
	        	sb.append("R"+Integer.toString(r.id));
	        	sb.append(',');
	        }
	        sb.append('\n');
	        sb.append(',');
	        for(RotationEdge edge : poset){
	        	for(RotationStructure r: rotations){
		        	if(r.id==edge.first.id){
		        		if(edge.first.id>edge.second.id){
		        			sb.append("-1");
			        		 sb.append(',');
		        		}else{
		        			sb.append("1");
			        		 sb.append(',');
		        		}
		        	}else if(r.id==edge.second.id){
		        		 if(edge.first.id>edge.second.id){
			        			sb.append("1");
				        		 sb.append(',');
			        		}else{
			        			sb.append("-1");
				        		 sb.append(',');
			        		}
		        	}else{
		        		 sb.append("0");
		        		 sb.append(',');
		        	}
		        }
	        	sb.append('\n');
	        	sb.append(',');
	        }
	        sb.append('\n');
	        sb.append("JOB MACHINE PAIRS");
	        sb.append(',');
	        sb.append('\n');
	        HashMap<Edge, ArrayList<Info>> added = LinearProgramm.getAdded();
	    	HashMap<Edge, ArrayList<Info>> abstracted = LinearProgramm.getAbstracted();
	    	HashMap<Edge, Double> jopt_values = LinearProgramm.getJoptValues();
	        for(RotationStructure r : rotations){
    			sb.append(',');
    			sb.append("R"+Integer.toString(r.id));
	        }
	        sb.append(',');
			sb.append("Uij");
	        for(JobNode j : JobNode.getJobs()){
	        	for(MachineNode m : MachineNode.getMachines()){
	        		Edge e=Edge.getEdge(j, m);
	        		sb.append('\n');
	        		sb.append("J"+e.getJob().id+" "+"M"+e.getMachine().id);
	        		sb.append(',');
	        		if(abstracted.containsKey(e)){//means that arc is on jopt
	        			for(RotationStructure r : rotations){
	        				for(Info info : abstracted.get(e)){
	        					if(r.id==info.rotation.id){
	        						sb.append("-1");
					        		sb.append(',');
	        					}else{
	        						sb.append("0");
					        		sb.append(',');
	        					}
	        				}
	        			}
	        			if(jopt_values.get(e)!=null){
	        				sb.append(Double.toString(jopt_values.get(e)));
			        		sb.append(',');
	        			}
	        		}
	        		if(added.containsKey(e)){
	        				for(RotationStructure r : rotations){
		        				for(Info info : added.get(e)){
		        					if(r.id==info.rotation.id){
		        						sb.append("1");
						        		sb.append(',');
		        					}else{
		        						sb.append("0");
						        		sb.append(',');
		        					}
		        				}
		        			}
	        		}
	        	}
	        }
	        sb.append('\n');
	        sb.append("JOB OPTIMAL");
	        sb.append(',');
	        sb.append(Double.toString(times.getShapleyTimeJobs()/1000000000.0)+" seconds");
	        sb.append('\n');
	        sb.append("MACHINE OPTIMAL");
	        sb.append(',');
	        sb.append(Double.toString(times.getShapleyTimeMachines()/1000000000.0)+" seconds");
	        sb.append('\n');
	        sb.append("ROTATIONS");
	        sb.append(',');
	        sb.append(Double.toString(times.getRotationsTime()/1000000000.0)+" seconds");
	        sb.append('\n');
	        sb.append("POSET");
	        sb.append(',');
	        sb.append(Double.toString(times.getPosetTime()/1000000000.0)+" seconds");
	        pw.write(sb.toString());
	        pw.close();
	        System.out.println("done!");
	}
}