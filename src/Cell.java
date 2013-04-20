import java.util.ArrayList;

public class Cell {

	public int N = 9;

	public int possibles[];
	public int size;
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

	public void decSize() { // prevents size from being negative
		if (size > 0)
			size--;
	}

	public void fill() {
		if (size == 1 && empty) {
			size = 0;
			for (int i = 0; i < possibles.length; i++) {
				if (possibles[i] != 0) {
					value = possibles[i];
					empty = false;
					for (int j = 0; j < groups.size(); j++){
						groups.get(j).groupVals[value - 1] = true;
					}
					break;
				}
			}
		}
	}

	public void removePossible(int x) {
		this.fill();
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
