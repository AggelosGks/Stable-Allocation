package DataStructures;

public class Node {

	private static int share = 0;
	public final int id;

	public Node() {
		this.id = share;
		share++;
	}

	public Node(int id) {
		this.id = id;

	}

	public boolean isDummy() {
		boolean isDummy = false;
		if (this.id == 0 || this.id == 1) {
			isDummy = true;
		}

		return isDummy;
	}

	public static void setShare(int share) {
		Node.share = share;
	}

}
