package DataStructures;

import java.util.ArrayList;

public class MachineNode extends Node{
	private static ArrayList<MachineNode> machines;
	
	private ArrayList<JobNode> pref;
	private double upper_cap;
	
	public MachineNode(int id,double upper_cap) {
		super(id);
		this.upper_cap=upper_cap;
	}
	
	
	
}
