package DataStructures;

import java.util.ArrayList;
import java.util.TreeMap;

public class BipartiteGraph {
	public final ArrayList<JobNode> jobs;
	public final ArrayList<MachineNode> machines;
	public final TreeMap<Integer,ArrayList<Edge>> jobs_machines;
	
	
	public BipartiteGraph(){
		this.machines=MachineNode.getMachines();
		this.jobs=JobNode.getJobs();
		this.jobs_machines=new TreeMap<Integer,ArrayList<Edge>>();
		this.createAllEdges();
	}
	
	public void createAllEdges(){
		for(JobNode job : jobs){
			jobs_machines.put(job.id,new ArrayList<Edge>());
			for(MachineNode machine : machines){
				jobs_machines.get(job.id).add(new Edge(job,machine));
			}
		}
	}
	
	public Edge getEdge(JobNode j,MachineNode m){
		Edge edge=null;
		for(Edge e : jobs_machines.get(j.id)){
			if(e.getMachine().equals(m)){
				edge=e;
				break;
			}
		}
		return edge;
	}
	
	/**
	 *Creates the initial matching, every machine fully assigned to dummy job 
	 *
	 * @param graph containing two set of vertices
	 * @return 
	 */
	public Matching createIniitalMatchingJobs(BipartiteGraph graph){
		Matching match=new Matching();
		JobNode dum=JobNode.getDummyJob();
		ArrayList<Edge> edges=jobs_machines.get(dum.id);
		for(Edge edge : edges){
			edge.fillEdge();
			match.addNewEdgetoMatch(edge);
		}
		return match;
	}
	
}
