import java.util.ArrayList;

/**
 * Cell corresponds to a single square on the Sudoku board.
 * 
 * @author sjboris
 * @author alamana
 * @version %I%, %G%
 */
public class Cell {

	public int N = 9;

	public boolean possibles[];
	public int size; // number of possible values for the cell
	public int sizeBackup;
	public int value, oldValue;
	public int name;
	public boolean empty;
	public ArrayList<Group> groups;
	public int checksum;
	public int guessDepth;

	/**
	 * Initializes the cells' name to 0, empty to true, value to 0, size to N,
	 * and initializes possibles[].
	 */
	Cell() {
		guessDepth = -1;
		name = 0;
		empty = true;
		value = 0;
		size = N;
		sizeBackup = size;
		checksum = N * (N + 1) / 2;
		possibles = new boolean[N];
		for (int i = 0; i < N; i++) {
			possibles[i] = true;
		}
		groups = new ArrayList<Group>();
	}

	/**
	 * Prevents size from being less than 1
	 */
	public void adjustSize() {
		size = 0;
		for (boolean i : possibles) {
			if (i)
				size++;
		}
	}

	/**
	 * 
	 */
	public int getDeterminedValue() {
		int size = N;
		boolean[] temp = new boolean[N];

		// look through each of the cells groups and remove any values that the
		// group already has
		for (Group g : groups) {
			for (int i = 0; i < g.groupVals.length; i++) {
				if (g.groupVals[i]) { // if the group has the value
					if (!temp[i]) // if we haven't already seen it
						size--;
					temp[i] = true;
				}
			}
		}

		if (size == 1) {
			for (int i = 0; i < N; i++) {
				if (!temp[i]) {
					System.out.println("Cell.getDeterminedValue: returning "
							+ (i + 1));
					return i + 1; // there should only be one true cell
				}
			}
		}
		return -1;
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
				// we can take the first true value
				if (possibles[i]) {
					this.assignValue(i + 1);
					ret = true;
					break;
				}
			}
		}
		return ret;
	}

	/**
	 * Sets this cell's value to n. Backs up value. Sets size to 0. Sets empty
	 * to false.
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
		/*
		 * // this.removePossibles(); for (int i = 0; i < possibles.length; i++)
		 * { if (possibles[i]) return i + 1; }
		 */

		for (int i = 0; i < possibles.length; i++) {
			if (possibles[i]) {
				return i + 1;
			}
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
				}
			}
		}
		return ret;
	}

	/**
	 * Removes a possibility from possibles[], removes value from groups, and
	 * adjusts size
	 * 
	 * @param n
	 *            to remove from this cell's list of possible values.
	 */
	public void removePossible(int n) {
		if (n > 0) {
			possibles[n - 1] = false;
			checksum -= n - 1;
			this.adjustSize();
		} else {
			System.out.println("TRYING TO REMOVE A VALUE LESS THAN 1");
		}
	}

	public void addPossible(int n) {
		if (n > 0) {
			possibles[n - 1] = true;
			checksum += n - 1;
			this.adjustSize();
		} else {
			System.out.println("TRYING TO REMOVE A VALUE LESS THAN 1");
		}
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

	/**
	 * Returns true if n causes a conflict
	 * 
	 * @param n
	 *            Value to check
	 * 
	 */
	public boolean conflict(int n) {
		for (Group g : groups) {
			if (!g.valid(n))
				return true;
		}
		return false;
	}

	public void undoAssignment() {
		size = sizeBackup;
		// checksum += value;
		for (Group g : groups) {
			g.removeValue(value);
			// g.recalculateValues();
		}
		value = 0;
		empty = true;
	}

	/**
	 * Creates a string representing the cell. Format is
	 * 'value$possibles$empty$size$name$'. Possibles are separated with commas
	 * 
	 */
	public String serialize() {
		String ret = "";

		ret += value + "$";

		for (int i = 0; i < possibles.length; i++) {
			if (possibles[i]) {
				ret += (i + 1) + ",";
			}
		}
		ret += "$";

		if (empty) {
			ret += "0";
		} else {
			ret += "1";
		}
		ret += "$";

		ret += size;
		ret += "$";

		ret += name;
		ret += "$";

		return ret;
	}

	public void resetPossibles() {
		for (int i = 0; i < N; i++) {
			possibles[i] = true;
		}
	}

	public void reset() {
		resetPossibles();
		name = 0;
		value = 0;
		size = 0;
		empty = true;
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
