package Algorithms;

import DataStructures.BipartiteGraph;

public class GaleShapley {
	private boolean[] jobs_matched;//true if job is fully assigned false otherwise
	private boolean[] machines_matched;
	private int N,V;//number of vertices on each set
	
	public GaleShapley(BipartiteGraph graph){
		this.N=graph.jobs.size();
		this.V=graph.machines.size();
		this.jobs_matched=new boolean[N];
		this.machines_matched=new boolean[V];
	}
}
