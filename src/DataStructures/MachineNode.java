package DataStructures;

import java.util.ArrayList;

public class MachineNode extends Node{
	private static MachineNode dummy;
	//static fields
	private static final ArrayList<MachineNode> machines=new ArrayList<MachineNode>();//total machine population
	//instance fields
	private ArrayList<JobNode> pref;//pref list
	public final double upper_cap;//capacity
	public int pref_pointer;
	
	//constructor
	public MachineNode(double upper_cap) {
		super();
		this.upper_cap=upper_cap;
		if(!(this.upper_cap==Integer.MAX_VALUE)){
			machines.add(this);
		}else{
			dummy=this;
		}
		this.pref=new ArrayList<JobNode>();//initialize pref list
		this.pref_pointer=JobNode.getJobs().size();//pointer starts at least prefered choice
	}

	
	@Override
	public String toString() {
		String x="";
		for(JobNode job : pref){
			x=x+" "+job.id;
		}
		return "MachineNode [ Id: "+this.id+",pref=" + x + ", upper_cap=" + upper_cap + ", pref_pointer=" + pref_pointer + "]";
	}


	public void setPref(ArrayList<JobNode> pref) {
		this.pref = pref;
	}


	public static MachineNode getDummy() {
		return dummy;
	}


	public static void createDummyMachine(){
		new MachineNode(Integer.MAX_VALUE);
	}
	
	public static ArrayList<MachineNode> getMachines() {
		return machines;
	}
	
	/**
	 * Makes the logical comparison for two job nodes.
	 * The comparison is implemented by the indexes of two jobs in 
	 * the preference list. One job is indicated by the pointer and its current position.
	 * @param job the job node to be compared
	 * @return true if job is preferable, false otherwise.
	 */
	public boolean prefersAccRej(JobNode job){
		boolean isprefered=false;
		int index_candidate=pref.indexOf(job);
		if(index_candidate<pref_pointer){
			isprefered=true;
		}
		return isprefered;
	}
	
	/**
	 * Return the least prefered job
	 * @return least_pref the least prefered job.
	 */
	public JobNode getLeastPrefered(){
		JobNode least_pref=pref.get(pref_pointer);
		return least_pref;
	}
	
	/**
	 * This method is responsible for updating the point on the preference list
	 * for a machine. After an acceptance has occured and no amount of job is assigned
	 * to the least prefered job, we increase the pointer to the index of the 
	 * job that proposed last.
	 * @param job the job that proposed
	 */
	public void refreshPointerIndex(Matching match){
		JobNode lest_pref=this.getLeastPrefered();//get current least prefered
		Edge edge=Edge.getEdge(lest_pref, this);//extract edge
		if(!match.getMatch_edges().get(lest_pref).contains(edge)){//if not exists in matching 
			int index=this.pref.indexOf(lest_pref);
			for(int i=index; i<=0; i--){
				JobNode next=this.pref.get(i);
				if(match.getMatch_edges().get(next)!=null){
					if(match.getMatch_edges().get(next).contains(Edge.getEdge(next, this))){
						this.pref_pointer=this.pref.indexOf(next);
						
						break;
					}
				}
			}
		}
		
	}
	
	public double rejectTime(double amount,Matching match){
		double remain=0;
		JobNode job_reject=this.getLeastPrefered();
		double current_time=Edge.getEdge(job_reject, this).getCurrent_time();
		if(current_time>amount){
			Edge.getEdge(job_reject,this).setCurrent_time(current_time-amount);
			remain=current_time-amount;
		}else{
			Edge.getEdge(job_reject,this).setCurrent_time(0);
			amount=Math.abs(current_time-amount);
			remain=current_time-amount;
			match.removeEdgeFromMatch(job_reject, Edge.getEdge(job_reject,this));//remove edge from matching
		}
		
		return remain;
	}
	
	public ArrayList<Node> rejectTime2(double amount,Matching match){
		ArrayList<Node> rejected=new ArrayList<Node>();
		JobNode lp=this.getLeastPrefered();
		double time=Edge.getEdge(lp,this).getCurrent_time();
		if(time>amount){
			Edge.getEdge(lp,this).setCurrent_time(time-amount);
			rejected.add(lp);
		}else if(time==amount){
			Edge.getEdge(lp,this).setCurrent_time(0);
			match.removeEdgeFromMatch(lp, Edge.getEdge(lp,this));//remove edge from matching
			rejected.add(lp);
			refreshPointerIndex(match);//least prefered job changes
		}else{
			double rejection=amount;//total amount of rejection
			while(rejection>0){//until reject amount decrease to zero
				System.out.println("Rejection amount= "+rejection);
				lp=this.getLeastPrefered();//get least prefered
				System.out.println("Least prefered: "+lp.id);
				time=Edge.getEdge(lp,this).getCurrent_time();
				double assign=Math.max(0,time-rejection);
				Edge.getEdge(lp,this).setCurrent_time(assign);
				if(assign==0){
					match.removeEdgeFromMatch(lp, Edge.getEdge(lp,this));//remove edge from matching
					refreshPointerIndex(match);//least prefered job changes
				}
				rejected.add(lp);
				rejection=Math.min(0, time-rejection);
				rejection=Math.abs(rejection);
			}
		}
		return rejected;
	}
	
	

}
