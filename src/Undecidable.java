import java.util.LinkedList;

public class Undecidable {

	private LinkedList<int[][]> saves;

	Undecidable() {
		saves = new LinkedList<int[][]>();
	}

	void takeSnapshot(int array[][]) {
		saves.push(array);
	}

	int[][] getMostRecent() {
		if (saves.size() > 0)
			return saves.getFirst();
		else
			return null;
	}

}
