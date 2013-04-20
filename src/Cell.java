import java.util.ArrayList;

public class Cell {

	public int N = 9;

	public int possibles[];
	public int size; // number of possible values for the cell
	public int value;
	public int name;
	public boolean empty;
	public ArrayList<Group> groups;

	Cell() {
		name = 0;
		empty = true;
		value = 0;
		size = N;
		possibles = new int[N];
		for (int i = 0; i < N; i++) {
			possibles[i] = i + 1;
		}
		groups = new ArrayList<Group>();
	}

	public void decSize() { // prevents size from being less than 1
		if (size > 0)
			size--;
	}

	public void fill() { // meant to be called when there's only 1 possible value for the cell
		if (size == 1 && empty) {
			size = 0;
			for (int i = 0; i < possibles.length; i++) { // since there should only be 1 nonzero entry in possibles
								// we can take the first nonzero
				if (possibles[i] != 0) {
					value = possibles[i];
					empty = false;
					for (int j = 0; j < groups.size(); j++){ // set flags for groups
						groups.get(j).groupVals[value - 1] = true; // groupVals is zero indexed
					}
					break;
				}
			}
		}
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
