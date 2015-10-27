package DataStructures;

import java.util.ArrayList;

public class JobNode extends Node{
	
	private static ArrayList<JobNode> jobs;
	
	private double proc_time;
	
	public JobNode(int id,double proc_time) {
		super(id);
		this.proc_time=proc_time;
	}

	
	
}
