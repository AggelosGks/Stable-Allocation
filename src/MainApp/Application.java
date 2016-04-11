package MainApp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Algorithms.ExposureElliminationAlgorithm;
import Algorithms.GaleShapley;
import Algorithms.Instance;
import DataStructures.BipartiteGraph;

import DataStructures.JobNode;
import DataStructures.MachineNode;
import DataStructures.Matching;

public class Application {

	public static void main(String args[]) throws CloneNotSupportedException {
		execute(4, 4, 20, 5, false);
	}

	public static ArrayList<Integer> readInstance() {

		String Path = "C:\\Users\\aggelos\\Desktop\\Aggelos\\Eclipse\\StableAllocations\\Instances\\instance.txt";
		File file = new File(Path);
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String line;

		ArrayList<Integer> values = new ArrayList<Integer>();
		try {
			while ((line = in.readLine()) != null) {
				values.add(Integer.parseInt(line));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return values;
	}

	public static void writeInstance() {
		ArrayList<Integer> values = new ArrayList<Integer>();
		for (JobNode job : JobNode.getJobs()) {
			values.add(job.id);
			values.add((int) job.proc_time);
			for (MachineNode machine : job.getPref()) {
				values.add(machine.id);
			}
		}
		for (MachineNode mach : MachineNode.getMachines()) {
			values.add(mach.id);
			values.add((int) mach.upper_cap);
			for (JobNode job : mach.getPref()) {
				values.add(job.id);
			}
		}

		try {
			File file = new File(
					"C:\\Users\\aggelos\\Desktop\\Aggelos\\Eclipse\\StableAllocations\\Instances\\instance3.txt");
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for (int i : values) {
				bw.write(Integer.toString(i));
				bw.newLine();
			}
			bw.close();
			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void execute(int jobs, int machines, int max_time, int min_time, boolean print) {
		Instance i = new Instance(jobs, machines, max_time, min_time);
		BipartiteGraph graph = i.createInstance();
		i.testInstanceIntegration();
		Matching job_optimal = executeShapleyJobOrieented(graph, print);
		job_optimal = executeRotations(job_optimal, print);
		Matching machine_optimal = executeShapleyMachinesOrieented(i, print);
		testCorrectness(job_optimal, machine_optimal);

	}

	public static Matching executeShapleyJobOrieented(BipartiteGraph graph, boolean print) {
		GaleShapley algorithm = new GaleShapley(graph);

		algorithm.execute();

		System.out.println("---------------JOB OPTIMAL SHAPLEY---------------");
		algorithm.getMatch().printMatching();
		System.out.println("--------------------------------------------------");
		System.out.println("                       ");
		System.out.println("                       ");
		return algorithm.getMatch();
	}

	public static Matching executeRotations(Matching job_optimal, boolean print) {
		ExposureElliminationAlgorithm exp = new ExposureElliminationAlgorithm(job_optimal);

		exp.execute();

		System.out.println("---------------MACHINE OPTIMAL ROTATIONS---------------");
		exp.getMatch().printMatching();
		System.out.println("--------------------------------------------------");
		System.out.println("                       ");
		System.out.println("                       ");
		return exp.getMatch();
	}

	public static Matching executeShapleyMachinesOrieented(Instance instance, boolean print) {
		GaleShapley inversed = new GaleShapley(instance.SwapInstance());
		instance.testInstanceIntegration();
		inversed.execute();
		inversed.getMatch().printMatchingSwaped();
		return inversed.getMatch();
	}

	public static void testCorrectness(Matching job_optimal, Matching machine_optimal) {
		if (machine_optimal.areEqual(job_optimal)) {
			System.out.println("Yes");
		} else {
			writeInstance();
			System.out.println("No");
		}
	}

}
