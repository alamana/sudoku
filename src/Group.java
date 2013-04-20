import java.util.ArrayList;

public class Group extends ArrayList<Cell> {

	public int N = 9;
	public boolean groupVals[];
	public String type;

	public Group() {
		super();
		type = "";
		groupVals = new boolean[N]; // initializes to false
	}

	public boolean add(Cell c) {
		int val = c.value;
		groupVals[val - 1] = true;
		super.add(c);
		return true;
	}

	public boolean remove(Cell c) {
		// if (super.contains(c)) {
		int val = c.value;
		groupVals[val - 1] = false;
		super.remove(c);
		return true;
		// }
		// return false;
	}

	public boolean valid() { // returns true if the group doesn't contain any
								// repeat values

		int possibles[] = new int[N]; // all 0

		for (int i = 0; i < this.size(); i++) {
			int val = this.get(i).value;
			if (possibles[val - 1] != 0)
				return false;
			else
				possibles[val - 1] = 1;
		}
		return true;
	}

	public boolean valid(int n) { // returns true if value n is not in the group
		return !groupVals[n - 1];
	}
}
