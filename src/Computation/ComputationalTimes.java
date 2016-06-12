package Computation;

public class ComputationalTimes {
	private  long ShapleyTimeJobs;
	private  long ShapleyTimeMachines;
	private  long RotationsTime;
	private long PosetTime;
	private  long LPtime;
	private long start;
	private long end;
	
	public ComputationalTimes() {
		
	}


	public long getShapleyTimeJobs() {
		return ShapleyTimeJobs;
	}
	
	
	
	public long getPosetTime() {
		return PosetTime;
	}


	public void setPosetTime(long posetTime) {
		PosetTime = posetTime;
	}


	public void setShapleyTimeJobs(long shapleyTimeJobs) {
		ShapleyTimeJobs = shapleyTimeJobs;
	}

	public long getShapleyTimeMachines() {
		return ShapleyTimeMachines;
	}

	public void setShapleyTimeMachines(long shapleyTimeMachines) {
		ShapleyTimeMachines = shapleyTimeMachines;
	}

	public long getRotationsTime() {
		return RotationsTime;
	}

	public void setRotationsTime(long rotationsTime) {
		RotationsTime = rotationsTime;
	}

	public long getLPtime() {
		return LPtime;
	}

	public void setLPtime(long lPtime) {
		LPtime = lPtime;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}
	
	
	
	
	
}
