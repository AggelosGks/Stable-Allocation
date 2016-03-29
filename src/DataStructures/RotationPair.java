package DataStructures;

public class RotationPair {
	private MachineNode extracted_from;
	private MachineNode added_to;
	private JobNode proposed_by;
	private double amount;

	public RotationPair(MachineNode extracted_from, MachineNode added_to, JobNode proposed_by, double amount) {
		this.extracted_from = extracted_from;
		this.added_to = added_to;
		this.proposed_by = proposed_by;
		this.amount = amount;
	}
	
	public RotationPair(){
		
	}

	public MachineNode getExtracted_from() {
		return extracted_from;
	}

	public void setExtracted_from(MachineNode extracted_from) {
		this.extracted_from = extracted_from;
	}

	public MachineNode getAdded_to() {
		return added_to;
	}

	public void setAdded_to(MachineNode added_to) {
		this.added_to = added_to;
	}

	public JobNode getProposed_by() {
		return proposed_by;
	}

	public void setProposed_by(JobNode proposed_by) {
		this.proposed_by = proposed_by;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "RotationPair [extracted_from_machine=" + extracted_from.id + ", added_to_machine=" + added_to.id + ", proposed_by_job="
				+ proposed_by.id + ", amount=" + amount + "]";
	}
	
	
	
}
