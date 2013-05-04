import java.util.ArrayList;

/**
 * Cell corresponds to a single square on the Sudoku board.
 * 
 * @author sjboris
 * @author alamana
 */
public class Cell {

	/**
	 * Max number of possible choices for this cell.
	 */
	public int N;

	/**
	 * True in the ith spot means that this cell could have a value of i+1.
	 */
	public boolean possibles[];

	/**
	 * Number of possible values for this cell.
	 */
	public int size;

	/**
	 * Cell's value. 0 means unassigned.
	 */
	public int value;

	/**
	 * Unique. Defined as 100*row + column.
	 */
	public int name;

	/**
	 * True if the cell is empty.
	 */
	public boolean empty;

	/**
	 * List of groups that this cell belongs to.
	 */
	public ArrayList<Group> groups;

	/**
	 * Initializes the cells' name to 0, empty to true, value to 0, size to N,
	 * and initializes possibles[]. Sets N to 9.
	 */
	Cell() {
		N = 9;
		name = 0;
		empty = true;
		value = 0;
		size = N;
		possibles = new boolean[N];
		for (int i = 0; i < N; i++) {
			possibles[i] = true;
		}
		groups = new ArrayList<Group>();
	}

	/**
	 * Recalculates size on every call. Size is the number the possibilities for
	 * this cell.
	 */
	public void adjustSize() {
		size = 0;
		for (boolean b : possibles) {
			if (b)
				size++;
		}
	}

	/**
	 * Meant to be called when there's only 1 possible value for the cell.
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
		value = n;
		size = 0;
		empty = false;
		for (int j = 0; j < groups.size(); j++)
			groups.get(j).addValue(n);
	}

	/**
	 * Returns a guess value using the first nonzero value in possibles[].
	 * Returns -1 if no nonzero value is found.
	 */
	public int getGuess() {
		for (int i = 0; i < possibles.length; i++) {
			if (possibles[i]) {
				return (i + 1);
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
	 * Creates a string representing the cell. Format is
	 * 'value$possibles$empty$size$name$'. Possibles are separated with commas
	 * 
	 */
	public String serialize() {
		String ret = "";

		ret += value + "$";

		for (int i = 0; i < possibles.length; i++) {
			if (!possibles[i]) {
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

	/**
	 * Sets each possibility to true.
	 */
	public void resetPossibles() {
		for (int i = 0; i < N; i++) {
			possibles[i] = true;
		}
	}

	/**
	 * Sets each possibility to true. Name, value, and size become 0. Empty
	 * becomes true.
	 */
	public void reset() {
		resetPossibles();
		name = 0;
		value = 0;
		size = 0;
		empty = true;
	}
}
