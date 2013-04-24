import java.util.ArrayList;

public class Cell {

	public int N = 9;

	public int possibles[];
	public int size; // number of possible values for the cell
	public int value;
	public int name;
	public boolean empty;
	public ArrayList<Group> groups;
	public int sum;

	Cell() {
		name = 0;
		empty = true;
		value = 0;
		size = N;
		sum = N * (N + 1) / 2;
		possibles = new int[N];
		for (int i = 0; i < N; i++) {
			possibles[i] = i + 1;
		}
		groups = new ArrayList<Group>();
	}

	public void decSize() { // prevents size from being less than 1
		if (size > 1)
			size--;
	}

	public boolean fill() { // meant to be called when there's only 1 possible
							// value for the cell
		boolean ret = false;
		if (empty) {
			size = 0;
			for (int i = 0; i < possibles.length; i++) { // since there should
															// only be 1 nonzero
															// entry in
															// possibles
				// we can take the first nonzero
				if (possibles[i] != 0) {
					value = possibles[i];
					if (value == sum) {
						ret = true;
						empty = false;
						for (int j = 0; j < groups.size(); j++) { // set flags
																	// for
																	// groups
							groups.get(j).groupVals[value - 1] = true; // groupVals
																		// is
																		// zero
																		// indexed
						}
					}
					break;
				}
			}
		}
		return ret;
	}

	public boolean removePossibles() {
		boolean ret = false;
		for (Group g : groups) {
			boolean list[] = g.groupVals;
			for (int i = 0; i < list.length; i++) {
				if (list[i]) {
					ret = true;
					sum -= possibles[i];
					possibles[i] = 0;
					this.decSize();
				}
			}
		}
		return ret;
	}

	public void removePossible(int x) {
		if (x != 0) {
			possibles[x - 1] = 0;
			this.decSize();
			if (size == 1) {
				this.fill();
			}
		}
	}

	@Override
	public boolean equals(Object o) { // two cells are equal if they have the
										// same name
		Cell c = (Cell) o;
		return (this.name == c.name);
	}

	@Override
	public String toString() {
		String ret = "";
		ret += value + ":" + size + ":[";
		for (int i = 0; i < N; i++) {
			if (i != 0)
				ret += ",";
			ret += possibles[i];
		}
		ret += "]";
		return ret;
	}
}
