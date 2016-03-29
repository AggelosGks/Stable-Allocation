package Algorithms;

import java.util.ArrayList;
import java.util.Random;

import DataStructures.BipartiteGraph;
import DataStructures.Edge;
import DataStructures.JobNode;
import DataStructures.MachineNode;
import DataStructures.Node;

public class Instance {
	
	public final int j;
	public final int m;
	public final int max_time;
	public final int min_time;
	private static BipartiteGraph graph;
	

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
		graph=new BipartiteGraph();
		return graph;
	}
	
	public BipartiteGraph SwapInstance(){
		ArrayList<JobNode> or_jobs=new ArrayList<JobNode>(JobNode.getJobs());
		ArrayList<MachineNode> or_machines=new ArrayList<MachineNode>(MachineNode.getMachines());
		this.clearInstance();
		JobNode.createDummyJob();
		MachineNode.createDummyMachine();
		//swap bipartite graph
		for(MachineNode m : or_machines){
			new JobNode(m.upper_cap);	
		}
		for(JobNode j : or_jobs){
			new MachineNode(j.proc_time);
		}
		//iterate new jobs (old machines)
		for(int i=0; i<JobNode.getJobs().size(); i++){
			ArrayList<MachineNode> pref=new ArrayList<MachineNode>();//create new pref list
			JobNode job=JobNode.getJobs().get(i);//get current new job
			MachineNode mach=or_machines.get(i);//get respective old machine
			for(JobNode j: mach.getPref()){//iterate pref list of old machine
				if(!j.isDummy()){
					int id=j.id+or_machines.size();//compute new job according to changes			
					pref.add(pickMachbyId(id));
				}
			}
			pref.add(MachineNode.getDummy());
			job.setPref(pref);
		}
		for(int i=0; i<MachineNode.getMachines().size(); i++){
			ArrayList<JobNode> pref=new ArrayList<JobNode>();//create new pref list
			MachineNode m=MachineNode.getMachines().get(i);//get current new job
			JobNode j=or_jobs.get(i);//get respective old machine
			for(MachineNode l: j.getPref()){//iterate pref list of old machine
				if(!l.isDummy()){
					int id=l.id-or_jobs.size();//compute new job according to changes
					pref.add(pickJobbyId(id));
				}
			}
			pref.add(JobNode.getDummy());
			m.setPref(pref);
		}
		Edge.createAllEdges();
		graph=new BipartiteGraph();
		return graph;
	}
	
	public JobNode pickJobbyId(int id){
		JobNode r_job=null;
		for(JobNode job : JobNode.getJobs()){
			if(job.id==id){
				r_job=job;
				break;
			}
		}
		return r_job;
	}
	
	public MachineNode pickMachbyId(int id){
		MachineNode r_machine=null;
		for(MachineNode machine : MachineNode.getMachines()){
			if(machine.id==id){
				r_machine=machine;
				break;
			}
		}
		return r_machine;
	}
	
	
	
	public void clearInstance(){
		JobNode.getJobs().clear();
		MachineNode.getMachines().clear();
		Edge.getJobsMachines().clear();
		JobNode.setDummy(null);
		MachineNode.setDummy(null);
		Node.setShare(0);
		
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
	
	public static BipartiteGraph getGraph() {
		return graph;
	}
	
	public BipartiteGraph createReadyInst(){
		JobNode.createDummyJob();
		MachineNode.createDummyMachine();
		ArrayList<Integer> times=new ArrayList<Integer>();
		times.add(7);times.add(7);times.add(10);times.add(6);times.add(8);
		for(int i=0; i<5; i++){
			new JobNode(times.get(i));
		}
		ArrayList<Integer> capacities=new ArrayList<Integer>();
		capacities.add(10);capacities.add(1);capacities.add(10);capacities.add(10);capacities.add(7);
		for(int i=0; i<5; i++){
			new MachineNode(capacities.get(i));
		}
		for(JobNode j: JobNode.getJobs()){
			if(j.id==2){
				ArrayList<MachineNode> p=new ArrayList<MachineNode>();
				p.add(MachineNode.getMachines().get(1));
				p.add(MachineNode.getMachines().get(0));
				p.add(MachineNode.getMachines().get(2));
				p.add(MachineNode.getMachines().get(4));
				p.add(MachineNode.getMachines().get(3));
				p.add(MachineNode.getDummy());
				j.setPref(p);
			}else if(j.id==3){
				ArrayList<MachineNode> p=new ArrayList<MachineNode>();
				p.add(MachineNode.getMachines().get(4));
				p.add(MachineNode.getMachines().get(3));
				p.add(MachineNode.getMachines().get(0));
				p.add(MachineNode.getMachines().get(1));
				p.add(MachineNode.getMachines().get(2));
				p.add(MachineNode.getDummy());
				j.setPref(p);
			}
			else if(j.id==4){
				ArrayList<MachineNode> p=new ArrayList<MachineNode>();
				p.add(MachineNode.getMachines().get(3));
				p.add(MachineNode.getMachines().get(4));
				p.add(MachineNode.getMachines().get(2));
				p.add(MachineNode.getMachines().get(0));
				p.add(MachineNode.getMachines().get(1));
				p.add(MachineNode.getDummy());
				j.setPref(p);
			}
			else if(j.id==5){
				ArrayList<MachineNode> p=new ArrayList<MachineNode>();
				p.add(MachineNode.getMachines().get(4));
				p.add(MachineNode.getMachines().get(0));
				p.add(MachineNode.getMachines().get(3));
				p.add(MachineNode.getMachines().get(2));
				p.add(MachineNode.getMachines().get(1));
				p.add(MachineNode.getDummy());
				j.setPref(p);
			}
			else{
				ArrayList<MachineNode> p=new ArrayList<MachineNode>();
				p.add(MachineNode.getMachines().get(2));
				p.add(MachineNode.getMachines().get(0));
				p.add(MachineNode.getMachines().get(3));
				p.add(MachineNode.getMachines().get(1));
				p.add(MachineNode.getMachines().get(4));
				p.add(MachineNode.getDummy());
				j.setPref(p);
			}
		}
		
		for(MachineNode j: MachineNode.getMachines()){
			if(j.id==7){
				ArrayList<JobNode> p=new ArrayList<JobNode>();
				p.add(JobNode.getJobs().get(4));
				p.add(JobNode.getJobs().get(3));
				p.add(JobNode.getJobs().get(1));
				p.add(JobNode.getJobs().get(0));
				p.add(JobNode.getJobs().get(2));
				p.add(JobNode.getDummy());
				j.setPref(p);
			}else if(j.id==8){
				ArrayList<JobNode> p=new ArrayList<JobNode>();
				p.add(JobNode.getJobs().get(0));
				p.add(JobNode.getJobs().get(2));
				p.add(JobNode.getJobs().get(4));
				p.add(JobNode.getJobs().get(1));
				p.add(JobNode.getJobs().get(3));
				p.add(JobNode.getDummy());
				j.setPref(p);
			}
			else if(j.id==9){
				ArrayList<JobNode> p=new ArrayList<JobNode>();
				p.add(JobNode.getJobs().get(2));
				p.add(JobNode.getJobs().get(3));
				p.add(JobNode.getJobs().get(1));
				p.add(JobNode.getJobs().get(0));
				p.add(JobNode.getJobs().get(4));
				p.add(JobNode.getDummy());
				j.setPref(p);
			}
			else if(j.id==10){
				ArrayList<JobNode> p=new ArrayList<JobNode>();
				p.add(JobNode.getJobs().get(1));
				p.add(JobNode.getJobs().get(3));
				p.add(JobNode.getJobs().get(2));
				p.add(JobNode.getJobs().get(4));
				p.add(JobNode.getJobs().get(0));
				p.add(JobNode.getDummy());
				j.setPref(p);
			}
			else{
				ArrayList<JobNode> p=new ArrayList<JobNode>();
				p.add(JobNode.getJobs().get(2));
				p.add(JobNode.getJobs().get(1));
				p.add(JobNode.getJobs().get(0));
				p.add(JobNode.getJobs().get(4));
				p.add(JobNode.getJobs().get(3));
				p.add(JobNode.getDummy());
				j.setPref(p);
			}
		}
		Edge.createAllEdges();
		return new BipartiteGraph();
		
	}
}
