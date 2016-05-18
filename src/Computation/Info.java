package Computation;


import DataStructures.RotationPair;
import DataStructures.RotationStructure;

public class Info {
	private final RotationStructure rotation;
	private final double distr_amount;
	
	public Info(RotationStructure rotation,double distr_amount){
		this.rotation=rotation;
		this.distr_amount=distr_amount;
	}

	public RotationStructure getRotation() {
		return rotation;
	}

	public double getDistr_amount() {
		return distr_amount;
	}
	
	public void revealInfo(){
		for(RotationPair pair : this.rotation.getPairs()){
			System.out.println(pair.toString());
		}
		System.out.println(this.distr_amount);
	}
	
}
