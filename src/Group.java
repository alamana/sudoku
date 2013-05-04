import java.util.ArrayList;

/**
 * Group is meant to be used for dividing up the Sudoku grid. Checks to see if a
 * board conforms to Sudoku rules should be done through this class.
 * 
 * @author sjboris
 * @author alamana
 * @version %I%, %G%
 */
public class Group extends ArrayList<Cell> {

	public int N = 9;
	public boolean groupVals[]; // true if group contains value
	public String type;
	public String name;

	public Group() {
		super();
		type = "";
		name = "";
		groupVals = new boolean[N]; // initializes to false
	}

	/**
	 * type$groupVals$cellNames$name$ Cells and group values are separated with
	 * commas
	 */
	public String serialize() {
		String ret = "";

		ret += type + "$";

		for (int i = 0; i < groupVals.length; i++) {
			if (groupVals[i]) {
				ret += i + ",";
			}
		}
		ret += "$";

		for (int i = 0; i < this.size(); i++) {
			Cell c = this.get(i);
			ret += c.name + ",";
		}
		ret += "$";

		ret += name;
		ret += "$";

		return ret;
	}

	/**
	 * Add the passed in cell to this group. Adjusts groupVals and then calls
	 * super.add. Always returns true.
	 * 
	 * @param c
	 *            Cell to be added
	 * @return true
	 */
	public boolean add(Cell c) {
		boolean status = this.addValue(c.value);
		if (!status && c.value > 0)
			System.out.println("Group.add: invalid add with cell " + c.name);
		super.add(c);
		return true;
	}

	/**
	 * Removes a cell from this group. Always returns true.
	 * 
	 * @param c
	 *            Cell to be removed from this group.
	 * @return Always returns true.
	 */
	public boolean remove(Cell c) {
		boolean status = this.removeValue(c.value);
		if (!status)
			System.out.println("Group.add: invalid remove");
		super.remove(c);
		return true;
	}

	public void resetGroupVals() {
		for (boolean b : groupVals) {
			b = false;
		}
	}

	/**
	 * Returns true if the group doesn't contain any repeat values.
	 */
	public boolean valid() {
		boolean possibles[] = new boolean[N]; // all false

		for (int i = 0; i < this.size(); i++) {
			int val = this.get(i).value;
			if (val > 0) {
				if (possibles[val - 1])
					return false;
				else
					possibles[val - 1] = true;
			} else if (val < 0)
				return false;
		}
		return true;
	}

	/**
	 * Returns true if value n is not in the group.
	 * 
	 * @param n
	 *            Value to check
	 */
	public boolean valid(int n) {
		return !groupVals[n - 1];
	}

	/**
	 * Returns true if n was not a value of this group. False if it was.
	 */
	public boolean addValue(int n) {
		boolean ret = false;
		if (n > 0) {
			ret = groupVals[n - 1];
			if (true) { //!ret) {
				for (Cell c : this) {
					// if (c.value != n)
					c.removePossible(n);
				}
				groupVals[n - 1] = true;
				ret = true;
			}
		} else {
			// System.out.println("Group.addValue: n less than 1");
		}
		return ret;
	}

	/**
	 * Returns true if n was a value of this group.
	 */
	public boolean removeValue(int n) {
		boolean ret = false;
		if (n > 0) {
			ret = groupVals[n - 1];
			if (ret) {
				groupVals[n - 1] = false;
			}
		}
		return ret;
	}

	@Override
	public String toString() {
		String ret = type + ": ";
		for (int i = 0; i < this.size(); i++) {
			ret += "(" + this.get(i).name + ", " + this.get(i).value + ") ";
		}
		return ret;
	}

	@Override
	public boolean equals(Object o) {
		Group other = (Group) o;
		if (this.name.equals(other.name))
			return true;
		else
			return false;
	}

	public boolean equals(String s) {
		if (this.name.equals(s))
			return true;
		else
			return false;
	}
}
