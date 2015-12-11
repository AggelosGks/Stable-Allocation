package Algorithms;

import java.util.ArrayList;
import java.util.Random;

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
		int total_proc_time=0;
		//create dummy nodes
		JobNode.createDummyJob();
		MachineNode.createDummyMachine();
		ArrayList<Integer> data=new ArrayList<Integer>();//save times and capacities
		boolean control=true;
		//generate j integers 
		while(control){
			for(int i=0; i<j; i++){
				int proc_time=randomWithRange(min_time,max_time);
				data.add(proc_time);
				total_proc_time=total_proc_time+proc_time;
				if(proc_time>max_value){
					max_value=proc_time;//save longest time
				}
			}
			if(total_proc_time>m){
				control=false;
				for(int time : data){
					new JobNode(time);
				}
			}
		}
		data.clear();//clear list to save capacities
		int[] result=sumNumbers(total_proc_time,m);
		for(Integer capacity : result){
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
			for(int i=0; i<j; i++){
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
		System.out.println("-------------------------------");
		for(MachineNode m : MachineNode.getMachines()){
			System.out.println(m.toString());
		}
	}
	
	public static int[] sumNumbers(int number, int parts) {
	    int[] result = new int[parts];
	    int sum = 0;
	    Random random = new Random();
	    for (int i = 1; i < result.length; i++) {
	        // here is the uneffecient part:
	        int rand = random.nextInt(number);
	        if (sum + rand < number) {
	            result[i] = rand;
	            sum += rand;
	        } else {
	            i--;
	        }
	    }
	    result[0] = number - sum;
	    for(int i=0; i<result.length; i++){
	    	if(result[i]==0||result[i]==1){
	    		result[i]=result[i]+2;
	    		for(int j=0; j<result.length; j++){
	    			if(result[j]!=0&&result[j]!=1&&result[j]!=2){
	    				result[j]=result[j]-2;
	    				break;
	    			}
	    		}
	    	}
	    }
	    return result;
	}
}
