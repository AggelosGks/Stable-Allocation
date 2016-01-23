package DataStructures;

import java.util.ArrayList;
import java.util.HashMap;

public class MachineNode extends Node{
	private static MachineNode dummy;
	//static fields
	private static boolean control_pref;
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
	
	
	

			
			
			
	//tha girnaei treemap me value to amount pou ekane reject
	public HashMap<Node,Double> rejectTime(double amount,Matching match,JobNode proposed){
		HashMap<Node,Double> rejected=new HashMap<Node,Double>();
		JobNode lp=this.getLeastPrefered();
		double time=Edge.getEdge(lp,this).getCurrent_time();
		if(time>amount){//THA DIWKSEI AMOUNT
			Edge.getEdge(lp,this).setCurrent_time(time-amount);
			lp.setTime_consumed(lp.getTime_consumed()-(int)amount);
			rejected.put(lp,amount);
		}else if(time==amount){//THA DIWKSEI AMOUNT
			Edge.getEdge(lp,this).setCurrent_time(0);
			match.removeEdgeFromMatch(lp, Edge.getEdge(lp,this));//remove edge from matching
			lp.setTime_consumed(lp.getTime_consumed()-(int)amount);
			rejected.put(lp,amount);
			refreshPointerIndex(match,proposed);//least prefered job changes
		}else{
			boolean change_index=false;
			double rejection=amount;//total amount of rejection
			while(rejection>0){//until reject amount decrease to zero
				lp=this.getLeastPrefered();//get least prefered
				time=Edge.getEdge(lp,this).getCurrent_time();
				int true_rej=(int) Math.min((Double)time, rejection);//assign the minimum value of rejection and available
				lp.setTime_consumed(lp.getTime_consumed()-true_rej);
				double assign=Math.max(0,time-rejection);//if edge is empty assign 0 value not negative
				Edge.getEdge(lp,this).setCurrent_time(assign);
				rejected.put(lp,(double)true_rej);
				if(assign==0){//if decreased to zero 
					match.removeEdgeFromMatch(lp, Edge.getEdge(lp,this));//remove edge from matching
					change_index=refreshPointerIndex(match,proposed);//least prefered job changes or not
					//*** an i boolean parei false tote spaei to loop midenizei to rejection
				}
				if(change_index){//the new least prefered didnt change
					rejection=0;
				}else{
					rejection=Math.min(0, time-rejection);
					rejection=Math.abs(rejection);
				}
				
			}
		}
		return rejected;
	}
	
	/**
	 * This method is responsible for updating the point on the preference list
	 * for a machine. After an acceptance has occured and no amount of job is assigned
	 * to the least prefered job, we increase the pointer to the index of the 
	 * job that proposed last.
	 * @param job the job that proposed
	 */
	public boolean refreshPointerIndex(Matching match,JobNode proposed){
			boolean cantchange=false;
			JobNode lest_pref=this.getLeastPrefered();//get current least prefered
			Edge edge=Edge.getEdge(lest_pref, this);//extract edge
			if(!match.getMatch_edges().get(lest_pref).contains(edge)){//if not exists in matching
				int index=this.pref.indexOf(lest_pref);
				for(int i=index; i>=0; i--){
					JobNode next=this.pref.get(i);
					if(match.getMatch_edges().get(next)!=null){
						if(match.getMatch_edges().get(next).contains(Edge.getEdge(next, this))){
							this.pref_pointer=this.pref.indexOf(next);
							int real_index=this.pref_pointer;
							int pr_index=this.pref.indexOf(proposed);
							if(real_index<pr_index){
								cantchange=true;
								System.out.println("Machine is: "+this.id+" proposed is: "+proposed.id+" swap job is: "+this.getLeastPrefered().id);
								//xeirizesai me mia boolean an mpei to if ***
							}
							break;
						}
					}
				}
				if(this.getLeastPrefered()==lest_pref){
					this.pref_pointer=this.pref.indexOf(proposed);
				}
			}
			return cantchange;
		}
	
	

}
