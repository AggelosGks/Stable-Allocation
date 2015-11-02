package DataStructures;

public class Node {
	
	private static int share=0;
	public final int id;
	
	public Node(){
		share++;
		this.id=share;
	}

}
