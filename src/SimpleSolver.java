import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SimpleSolver {

	int NUM_NUMBERS = 9;
	int N = 9; // NxN dimension. Should equal NUM_NUMBERS

	Cell grid[][];

	SimpleSolver() {
	}

	String filename;

	void solve(String filename) {
		this.filename = filename;
		grid = new Cell[N][N];
		// initialize grid[][][k]
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				grid[i][j] = new Cell();
			}
		}

		File f = new File(filename);
		Scanner in = null;
		try {
			in = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.out.println("SimpleSolver.solve: " + filename
					+ " not found!");
		}

		// read in grid[i][j][]
		for (int i = 0; i < N; i++) {
			String line = in.nextLine();
			for (int j = 0; j < N; j++) {
				char cell = line.charAt(j);
				if (cell == 'x') {
					grid[i][j].size = 9;
				} else {
					int value = (int) (cell - '0');
					grid[i][j].value = value;
					grid[i][j].size = 0; // no more possibilities for that cell
				}
			}
		}
		// System.out.println("Starting solve...");
		// print();
		// while (!isSolved()) {
		// simpleRowCheck();
		// simpleColumnCheck();
		simpleBlockCheck();
		// }
		System.out.println("Done solving...");
		print();

	}

	// look at each cell and remove what's already in its block
	void simpleBlockCheck() {

		int row, col;
		row = 0;
		for (int z = 0; z < N; z++) {
			ArrayList<Cell> zeros = new ArrayList<Cell>();
			int blockVals[] = new int[NUM_NUMBERS];
			row = 3 * (z / 3);
			for (int i = 1; i <= 3; i++) {
				col = 3 * (z % 3);
				for (int j = 1; j <= 3; j++) {
					Cell current = grid[row][col];
					// compare value to everything else in cell if we have a 0
					if (current.value == 0) {
						zeros.add(current);
					} else {
						blockVals[current.value - 1] = current.value;
					}
					col++;
				}
				row++;
			}
			for (int i = 0; i < zeros.size(); i++) {
				Cell current = zeros.get(i);
				for (int n = 0; n < NUM_NUMBERS; n++) {
					if (blockVals[n] != 0) {
						current.possibles[n] = 0;
						current.size--;
					}
				}
				if (current.size == 1) {
					for (int k = 0; k < NUM_NUMBERS; k++) {
						int first_nonzero = current.possibles[k];
						if (first_nonzero != 0) {
							current.value = first_nonzero;
							break;
						}
					}
				}
			}
		}
	}

	// look at each cell and then remove what's already in the column
	void simpleColumnCheck() {
		transpose();
		simpleRowCheck();
		transpose();
	}

	void transpose() {
		// copy grid
		Cell[][] copy = grid.clone();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < i; j++) {
				Cell temp = grid[i][j];
				grid[i][j] = copy[j][i];
				grid[j][i] = temp;
			}
		}
	}

	// look at cell and then remove what's already in the row
	void simpleRowCheck() {
		for (int row = 0; row < N; row++) {
			for (int col = 0; col < N; col++) {
				int cellVal = grid[row][col].value;
				if (cellVal == 0) {
					for (int j = 0; j < N; j++) { // look at the other cells in
													// the row
						if (j == col)
							continue; // no need to look at current cell
						int val = grid[row][j].value;
						if (val != 0) {
							if (grid[row][col].possibles[val - 1] != 0) {
								grid[row][col].possibles[val - 1] = 0;
								grid[row][col].size--;
							}
						}
					}
					if (grid[row][col].size == 1) {
						for (int k = 0; k < NUM_NUMBERS; k++) {
							int first_nonzero = grid[row][col].possibles[k];
							if (first_nonzero != 0) {
								grid[row][col].value = first_nonzero;
								break;
							}
						}
					}
				}
			}
		}
	}

	void print() {
		for (int i = 1; i <= N; i++) {
			for (int j = 1; j <= N; j++) {
				System.out.print(grid[i - 1][j - 1].value);
				// if (j % 3 == 0 && j != N){
				// System.out.print("|");
				// }
			}
			System.out.println("");
			// if (i % 3 == 0 && i != N){
			// System.out.println("---|---|---");
			// }
		}
	}

	boolean isSolved() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (grid[i][j].value == 0)
					return false;
			}
		}

		return true;
	}

	String write() {
		String ret = "";
		try {
			ret = filename + "_solved";
			File file = new File(ret);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			String input = "";
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					input += grid[i][j].value;
				}
				input += "\n";
			}
			bw.write(input);
			bw.close();
		} catch (IOException e) {
			System.out.println("SimpleSolver.write: IOException");
		}
		return ret;
	}
}
