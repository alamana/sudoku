import java.util.ArrayList;
import java.util.Collections;

public class Vertex {

	public int value;
	public boolean labelled;
	public ArrayList<Vertex> neighbours;
	public int name;

	public Vertex() {
		neighbours = new ArrayList<Vertex>();
		value = 0;
		labelled = false;
		name = 0;
	}
	
	@Override
	public boolean equals(Object o){
		Vertex temp  = (Vertex) o;
		if (this.name == temp.name){
			return true;
		}
		else {
			return false;
		}
	}

	public int getSize() {
		int ret = 0;

		for (int i = 0; i < neighbours.size(); i++) {
			if (!neighbours.get(i).labelled) { // only increment if node isn't
												// filled
				ret++;
			}
		}

		return ret;
	}

	public Vertex getMax() { // returns the neighbour with the most unlabelled
								// neighbours
		Vertex maxV = null;
		int max = -1;
		// look at neighbours
		for (int i = 0; i < neighbours.size(); i++) {
			Vertex temp = neighbours.get(i);
			if (!temp.labelled) { // only look at unlabelled nodes
				if (temp.getSize() > max) {
					max = temp.getSize();
					maxV = temp;
				}
			}
		}
		return maxV;
	}

	public void add(Vertex vertex) {
		if (!neighbours.contains(vertex)) {
			neighbours.add(vertex);
			vertex.neighbours.add(this);
		}
	}

	public int lowest() {
		int ret = 1; // start with 1

		// get neighbours' values and sort them
		ArrayList<Integer> vals = new ArrayList<Integer>();
		for (int i = 0; i < neighbours.size(); i++) {
			if (!vals.contains(neighbours.get(i).value)) {
				if (neighbours.get(i).labelled) { // only look at labelled ones
					vals.add(neighbours.get(i).value);
				}
			}
		}

		// find the smallest value not being used by the neighbours
		while (true) {
			if (vals.contains(ret)) {
				ret++;
				continue;
			} else {
				break;
			}
		}
		return ret;
	}
}
