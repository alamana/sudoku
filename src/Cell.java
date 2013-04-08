public class Cell {

	int NUM_NUMBERS = 9;

	int possibles[];
	int size;
	int value;

	Cell() {
		value = 0;
		size = NUM_NUMBERS;
		possibles = new int[NUM_NUMBERS];
		for (int i = 0; i < NUM_NUMBERS; i++) {
			possibles[i] = i + 1;
		}
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
