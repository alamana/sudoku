import java.util.ArrayList;


public class Node {

	public int id;
	public int value;
	public ArrayList<Edge> edges;
	public int possibles[];
	
	
	public Node(){
		edges = new ArrayList<Edge>();
		possibles = new int[9];
		for (int i = 1; i <= 9; i++){
			possibles[i-1] = i;
		}
		id = 0;
		value = 0;
	}
	
	//Two nodes are equal if they have the same ID
	public boolean equals(Object other){
		boolean ret = false;
		Node temp = (Node) other;
		if (temp.id == this.id) ret = true;
		return ret;
	}
	
}
