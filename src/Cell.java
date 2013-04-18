import java.util.ArrayList;

public class Cell {

	public int NUM_NUMBERS = 9;

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
		size = NUM_NUMBERS;
		possibles = new int[NUM_NUMBERS];
		for (int i = 0; i < NUM_NUMBERS; i++) {
			possibles[i] = i + 1;
		}
		groups = new ArrayList<Group>();
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
		for (int i = 0; i < NUM_NUMBERS; i++) {
			if (i != 0)
				ret += ",";
			ret += possibles[i];
		}
		ret += "]";
		return ret;
	}
}
