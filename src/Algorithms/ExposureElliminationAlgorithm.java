package Algorithms;

import java.util.ArrayList;

import DataStructures.JobNode;
import DataStructures.MachineNode;
import DataStructures.Matching;
import DataStructures.RotationStructure;
import MainApp.Application;

public class ExposureElliminationAlgorithm {
	private Matching match;// the matching the algorithm is applied to
	private ArrayList<RotationStructure> rotations;// list of rotationstructures
	private static RotationStructure runner;

	public ExposureElliminationAlgorithm(Matching m) {
		this.match = m;
		this.rotations = new ArrayList<RotationStructure>();
	}

	
	/**
	 * Implements the rotation procedure.
	 * The method execution stops when no more jobs can get less happier.
	 */
	public void execute() {
		//get jobs
		ArrayList<JobNode> jobs = new ArrayList<JobNode>(JobNode.getJobs());
		Application.STEPS_IN_TEXT.add("                       ROTATIONS                  ");
		Application.STEPS_IN_TEXT.add(" ");
		MachineNode.refreshMachines(match);
		while (decreaseExists(jobs, this.match)) {
			System.out.println("--------------------------------------------------------------");
			Application.STEPS_IN_TEXT.add("-------------------------------------------------------------------");
			Application.STEPS_IN_TEXT.add(runner.toString());
			System.out.println("--------------------------------------------------------------");
			Application.STEPS_IN_TEXT.add("-------------------------------------------------------------------");
			System.out.println(" ");
			Application.STEPS_IN_TEXT.add(" ");
			System.out.println(" ");
			Application.STEPS_IN_TEXT.add(" ");
			runner.elliminateStructure(match);
			rotations.add(runner);
			runner = null;
			for (MachineNode mach : MachineNode.getMachines()) {
				mach.refreshPointerRotation(match);
			}
		}
		for(RotationStructure rot : rotations){
			int id=rotations.indexOf(rot)+1;
			rot.setId(id);
		}
	}

	/**
	 * Return the match of the instace
	 * @return match
	 */

	public Matching getMatch() {
		return match;
	}

	
	public static void setRunner(RotationStructure runner) {
		ExposureElliminationAlgorithm.runner = runner;
	}

	/**
	 * Examines wether a decrease of happiness exists for one or more jobs.
	 * 
	 * @param jobs
	 * @param match
	 * @return true if job can get less happy
	 */
	public boolean decreaseExists(ArrayList<JobNode> jobs, Matching match) {
		boolean decrease = false;
		for (JobNode job : jobs) {
			if (job.canGetWorse(match)) {
				decrease = true;
				break;
			}
		}
		return decrease;
	}

}
