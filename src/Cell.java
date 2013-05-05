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
	 * True in spot <code>i</code> means that this cell could have a value of
	 * <code>i+1</code>.
	 */
	public boolean possibles[];

	/**
	 * Number of possible values for this cell.
	 */
	public int size;

	/**
	 * Cell's value. <code>0</code> means unassigned.
	 */
	public int value;

	/**
	 * Unique. Defined using 10^floor(base 10 log of N).
	 */
	public int name;

	/**
	 * Cell's row in the board.
	 */
	public int row;

	/**
	 * Cell's column in the board.
	 */
	public int col;

	/**
	 * <code>true</code> if the cell is empty.
	 */
	public boolean empty;

	/**
	 * List of groups that this cell belongs to.
	 */
	public ArrayList<Group> groups;

	/**
	 * Initializes the cells' name to <code>0</code>, empty to <code>true</code>
	 * , value to <code>0</code>, <code>N</code> to <code>size</code>, and
	 * initializes <code>possibles[]</code> so that each of its values is
	 * <code>true</code>.
	 * 
	 */
	Cell(int N, int row, int column) {
		this.N = N;
		this.col = column;
		this.row = row;

		int shift = (int) Math.floor(Math.log10(N));
		this.name = this.row * shift + this.col;

		this.empty = true;
		this.value = 0;
		this.size = N;
		this.possibles = new boolean[N];
		for (int i = 0; i < N; i++) {
			possibles[i] = true;
		}
		groups = new ArrayList<Group>();
	}

	/**
	 * Recalculates <code>size</code> on every call. <code>size</code> is the
	 * number the possibilities for this cell.
	 */
	public void adjustSize() {
		size = 0;
		for (boolean b : possibles) {
			if (b)
				size++;
		}
	}

	/**
	 * Meant to be called when there's only one possible value for the cell.
	 * 
	 * @return <code>true</code> if a value was assigned to this cell,
	 *         <code>false</code> otherwise.
	 */
	public boolean fill() {
		if (size != 1) {
			System.out.println("Cell.fill: ERROR. SIZE != 1");
			return false;
		}
		boolean ret = false;
		if (empty) {
			/*
			 * Since there should only be 1 nonzero entry in possibles we can
			 * take the first true value
			 */
			for (int i = 0; i < possibles.length; i++) {
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
	 * Sets this cell's value to <code>n</code>. Backs up value. Sets size to
	 * <code>0</code>. Sets empty to <code>false</code>.
	 * 
	 * @param n
	 *            The value to be assigned to this cell.
	 */
	public void assignValue(int n) {
		value = n;
		size = 0;
		empty = false;
		clearPossibles();
		for (int j = 0; j < groups.size(); j++)
			if (groups.get(j).addValue(n)) {
				// System.out.println("Cells.assignValue: VALUE " + n +
				// " ALREADY IN GROUP " + groups.get(j).name);
			}
	}

	/**
	 * Sets each entry in <code>possibles[]</code> to <code>false</code>
	 */
	public void clearPossibles() {
		for (int i = 0; i < possibles.length; i++) {
			possibles[i] = false;
		}
	}

	/**
	 * Guesses a value using the first nonzero value in <code>possibles[]</code>
	 * 
	 * @return First nonzero value in <code>possibles[]</code>. <code>-1</code>
	 *         if no nonzero value is found.
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
	 * values found in that group.
	 * 
	 * @return <code>true</code> if a change was made. <code>false</code>
	 *         otherwise.
	 */
	public boolean removePossibles() {
		boolean ret = false;
		for (Group g : groups) {
			for (int i = 0; i < g.size(); i++) {
				if (g.get(i).value > 0 && g.get(i).value != this.value) {
					ret = true;
					this.removePossible(g.get(i).value);
				}
			}
		}
		return ret;
	}

	/**
	 * Checks to see if a cell value is contained an any of this cell's groups.
	 * 
	 * @param n
	 *            Value to check
	 * @return True if none of the groups contain this value, false otherwise.
	 */
	public boolean validValue(int n) {
		for (Group g : groups) {
			if (!g.valid(n))
				return false;
		}

		return true;
	}

	/**
	 * Removes a possibility from <code>possibles[]</code> and adjusts size.
	 * <code>possibles[]</code> is zero indexed, so the value set to false will
	 * be <code>n-1</code>.
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
		String ret = name + "::";
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
	 * 'value$possibles$empty$size$name$row$col'. Possibles are separated with
	 * commas
	 * 
	 */
	public String serialize() {
		String ret = "";

		ret += value + "$";

		for (int i = 0; i < possibles.length; i++) {
			ret += possibles[i] + ",";
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

		ret += row + "$" + col;

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
	 * Sets each possibility to true. Name, value, and size become
	 * <code>0</code>. Empty becomes <code>true</code>.
	 */
	public void reset() {
		resetPossibles();
		value = 0;
		size = N;
		empty = true;
	}

	/**
	 * Resets this cell. Has its groups recalculate values.
	 */
	public void unassign() {
		int x = value;
		reset();
		for (Group g : groups) {
			g.removeValue(x);
		}

	}

	/**
	 * Checks to see how many groups in this cell have n has a value. If there
	 * is only one, remove n from possibles.
	 */
	public void tentativeRemove(int n) {
		int num = 0;

		for (Group g : groups) {
			/*
			 * Valid returns true if n is not in the group.
			 */
			if (!g.valid(n)) {
				num++;
			}
		}

		if (num == 0) {
			this.removePossible(n);
		}
	}
}
