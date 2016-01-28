package DataStructures;

public class RotationPair {
	private Node extracted_from;
	private Node added_to;
	private Node proposed_by;
	private double amount;

	public RotationPair(Node extracted_from, Node added_to, Node proposed_by, double amount) {
		super();
		this.extracted_from = extracted_from;
		this.added_to = added_to;
		this.proposed_by = proposed_by;
		this.amount = amount;
	}
	
	public RotationPair(){
		
	}

	public Node getExtracted_from() {
		return extracted_from;
	}

	public void setExtracted_from(Node extracted_from) {
		this.extracted_from = extracted_from;
	}

	public Node getAdded_to() {
		return added_to;
	}

	public void setAdded_to(Node added_to) {
		this.added_to = added_to;
	}

	public Node getProposed_by() {
		return proposed_by;
	}

	public void setProposed_by(Node proposed_by) {
		this.proposed_by = proposed_by;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	
	
}
