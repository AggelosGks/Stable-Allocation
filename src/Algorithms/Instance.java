package Algorithms;

import java.util.ArrayList;

import DataStructures.BipartiteGraph;
import DataStructures.Edge;
import DataStructures.JobNode;
import DataStructures.MachineNode;

public class Instance {
	
	public final int j;
	public final int m;
	public final int max_time;
	public final int min_time;
	public Instance(int j, int m,int max, int min){
		this.j=j;
		this.m=m;
		this.max_time=max;
		this.min_time=min;
	}
	
	public BipartiteGraph createInstance(){
		createNodes();
		assignPreferencesJobs();
		assignPreferencesMachines();
		Edge.createAllEdges();
		return new BipartiteGraph();
	}
	
	public static int randomWithRange(int min, int max)
	{
	   int range = (max - min) + 1;     
	   return (int)(Math.random() * range) + min;
	}
	
	public void createNodes(){
		int max_value=0;
		JobNode.createDummyJob();
		MachineNode.createDummyMachine();
		for(int i=0; i<j; i++){
			int proc_time=randomWithRange(min_time,max_time);
			new JobNode(proc_time);
			if(proc_time>max_value){
				max_value=proc_time;
			}
		}
		ArrayList<Integer> capacities=new ArrayList<Integer>();
		boolean control=true;
		while(control){
			capacities.clear();
			int sum=0;
			for(int i=0; i<m; i++){
				int capacity=randomWithRange(min_time,max_time);
				capacities.add(capacity);
				sum=sum+capacity;
			}
			if(sum>max_time){
				control=false;
			}
		}
		for(Integer capacity : capacities){
			new MachineNode(capacity);
		}
	}
	public void assignPreferencesJobs(){
		ArrayList<MachineNode> machines=MachineNode.getMachines();
		for(JobNode job : JobNode.getJobs()){
			ArrayList<MachineNode> abandoned=new ArrayList<MachineNode>();
			ArrayList<MachineNode> preferences=new ArrayList<MachineNode>();
			for(int i=0; i<m; i++){
				int number=randomWithRange(0,m-1);
				MachineNode machine=machines.get(number);
				if(abandoned.contains(machine)){
					i=i-1;
				}else{
					preferences.add(machine);
					abandoned.add(machine);
				}
			}
			preferences.add(MachineNode.getDummy());
			job.setPref(preferences);
			
		}
	}
	public void assignPreferencesMachines(){
		ArrayList<JobNode> jobs=JobNode.getJobs();
		for(MachineNode machine : MachineNode.getMachines()){
			ArrayList<JobNode> abandoned=new ArrayList<JobNode>();
			ArrayList<JobNode> preferences=new ArrayList<JobNode>();
			for(int i=0; i<j-1; i++){
				int number=randomWithRange(0,j-1);
				JobNode job=jobs.get(number);
				if(abandoned.contains(job)){
					i=i-1;
				}else{
					preferences.add(job);
					abandoned.add(job);
				}
			}
			preferences.add(JobNode.getDummy());
			machine.setPref(preferences);
		}
	}
	public void testInstanceIntegration(){
		for(JobNode j : JobNode.getJobs()){
			System.out.println(j.toString());
		}
		System.out.print("-------------------");
		for(MachineNode m : MachineNode.getMachines()){
			System.out.println(m.toString());
		}
	}
}
