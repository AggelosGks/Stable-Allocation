package DataStructures;

public class Node {
	
	private static int share=0;
	public final int id;
	
	public Node(){
		this.id=share;
		share++;
	}
	
	public boolean isDummy(){
		boolean isDummy=false;
		if(this.id==0||this.id==1){
			isDummy=true;
		}
		
		return isDummy;
	}
	

}
