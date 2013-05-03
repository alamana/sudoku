import java.util.ArrayList;

public class Cell {

	public int N = 9;

	public int possibles[];
	public int size; // number of possible values for the cell
	public int sizeBackup;
	public int value, oldValue;
	public int name;
	public boolean empty;
	public ArrayList<Group> groups;
	public int checksum;

	/**
	 * Initializes the cells' name to 0, empty to true, value to 0, size to N,
	 * and initializes possibles[].
	 */
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

	/**
	 * Prevents size from being less than 1
	 */
	public void adjustSize() {
		size = 0;
		for (int i : possibles) {
			if (i != 0)
				size++;
		}
	}

	/**
	 * Meant to be called when there's only 1 possible value for the cell
	 */
	public boolean fill() {
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

	/**
	 * Sets this cell's value to n.
	 * 
	 * @param n
	 *            The value to be assigned to this cell.
	 */
	public void assignValue(int n) {
		oldValue = value;
		value = n;
		sizeBackup = size;
		size = 0;
		empty = false;
		for (int j = 0; j < groups.size(); j++)
			groups.get(j).addValue(n);
	}

	/**
	 * Removes a guess that was made. Resets this cell's value to 0.
	 */
	public void removeGuess() {
		size = sizeBackup;
		this.removePossible(value);
		checksum += value;
		value = 0;
		empty = true;
	}

	/**
	 * Returns a guess value using the first nonzero value in possibles[].
	 * Returns -1 if no nonzero value is found.
	 */
	public int getGuess() {
		for (int i = 0; i < possibles.length; i++) {
			if (possibles[i] != 0)
				return possibles[i];
		}
		return -1;
	}

	/**
	 * Looks the value each group this cell belongs to contains and removes any
	 * values found in that group. Returns true if a change was made. False
	 * otherwise.
	 */
	public boolean removePossibles() {
		boolean ret = false;
		for (Group g : groups) {
			boolean list[] = g.groupVals;
			for (int i = 0; i < list.length; i++) {
				if (list[i]) {
					ret = true;
					this.removePossible(i + 1);
					// checksum -= possibles[i];
					// possibles[i] = 0;
					// this.adjustSize();
				}
			}
		}
		return ret;
	}

	/**
	 * Removes a possibility from possibles[], removes value from groups, and
	 * adjusts size
	 * 
	 * @param Value
	 *            to remove from this cell's list of possible values.
	 */
	public void removePossible(int n) {
		for (Group g : groups) {
			g.removeValue(n);
		}
		possibles[n - 1] = 0;
		checksum -= n - 1;
		this.adjustSize();
	}

	/**
	 * Two cells are defined to be equal if they have the same name.
	 * 
	 * @param o
	 *            Another cell
	 * @return true if the cells have the same name. false if they do not.
	 */
	@Override
	public boolean equals(Object o) {
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

	// Looks the same as removeGuess.

	// public void undo() {
	// possibles[value - 1] = value;
	// empty = true;
	// size = sizeBackup;
	// checksum += value;
	// for (Group g : groups) {
	// g.groupVals[value - 1] = true;
	// }
	// value = 0;
	// }
}
