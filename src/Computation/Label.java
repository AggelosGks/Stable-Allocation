package Computation;

import DataStructures.RotationStructure;

public class Label {
	private RotationStructure rotation;
	private String label;
	
	public Label(RotationStructure rotation, String label) {
		this.rotation = rotation;
		this.label = label;
	}
	
	
	public RotationStructure getRotation() {
		return rotation;
	}
	public void setRotation(RotationStructure rotation) {
		this.rotation = rotation;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	
	
}
