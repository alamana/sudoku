import java.util.ArrayList;

public class Cell {

	public int N = 9;

	public int possibles[];
	public int size; // number of possible values for the cell
	public int sizeBackup;
	public int value;
	public int name;
	public boolean empty;
	public ArrayList<Group> groups;
	public int checksum;

	Cell() {
		name = 0;
		empty = true;
		value = 0;
		size = N;
		sizeBackup = size;
		checksum = N * (N + 1) / 2;
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
			for (int i = 0; i < possibles.length; i++) { // since there should
															// only be 1 nonzero
															// entry in
															// possibles
				// we can take the first nonzero
				if (possibles[i] != 0) {
					this.assignValue(possibles[i]);
					ret = true;
					break;
				}
			}
		}
		return ret;
	}

	public void assignValue(int n) {
		value = n;
		sizeBackup = size;
		size = 0;
		if (value == checksum) {
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
	}

	public void assignGuess(int guess) {
		sizeBackup = size;
		size = 0;
		empty = false;
		value = guess;
		for (Group g : groups) {
			g.groupVals[value - 1] = true;
		}
	}

	public void removeGuess() {
		size = sizeBackup;
		empty = true;
		value = 0;
		for (Group g : groups)
			g.groupVals[value - 1] = false;
	}

	public int guess() {
		for (int i = 0; i < possibles.length; i++) {
			if (possibles[i] != 0)
				return possibles[i];
		}
		return -1;
	}

	public boolean removePossibles() {
		boolean ret = false;
		for (Group g : groups) {
			boolean list[] = g.groupVals;
			for (int i = 0; i < list.length; i++) {
				if (list[i]) {
					ret = true;
					checksum -= possibles[i];
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
