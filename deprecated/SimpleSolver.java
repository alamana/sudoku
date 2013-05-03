import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.util.TreeSet;

public class SimpleSolver {

	int NUM_NUMBERS = 9;
	int N = 9; // NxN dimension. Should equal NUM_NUMBERS

	Cell grid[][];
	Stack<Move> moves;
	Validator v;

	SimpleSolver() {
		moves = new Stack<Move>();
	}

	String filename;

	void solve(String filename) {
		moves.clear();
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
					grid[i][j].size = 0; // no more possibilities for that
											// cell
				}
			}
		}
		int counter = 0;
		while (!isSolved() && counter < 25) {
			fillEasyCells(); // exits when there are no more easy fills
			// make guess
			makeGuess();
			// print();
			// System.out.println("");
			// System.out.println("------------------------------");
			counter++;
		}
		//

		System.out.println("Done solving...");
		print();
		// for (int i = 0; i < N; i++) {
		// System.out.println(grid[8][i].toString());
		// }

	}

	public void makeGuess() {
		Move move = new Move();
		move.guess = true;
		// find first zero cell
		Cell firstZero = null;
		int row = 0, col = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (grid[i][j].value == 0) {
					firstZero = grid[i][j];
					row = i;
					col = j;
					break;
				}
			}
			if (firstZero != null)
				break;
		}
		if (firstZero != null) {
			move.loc = row * 100 + col;
			// check to see what else is in the rows, columns, and blocks
			int nextVal = 0;
			for (int i = 0; i < firstZero.possibles.length; i++) {
				if (firstZero.possibles[i]) {
					nextVal = i+1;
					break;
				}
			}
			System.out.println("nonzeroval = " + nextVal);
			move.value = nextVal;
			// make move
			moves.push(move);
			firstZero.value = nextVal;
			firstZero.empty = false;
		} else {
			System.out.println("It's NULL");
		}
	}

	public void fillEasyCells() {
		boolean changed = true;
		while (!isSolved() && changed) {
			boolean c1 = simpleRowCheck();
			boolean c2 = simpleColumnCheck();
			boolean c3 = simpleBlockCheck();
			// boolean c4 = nakedPair(); // don't know if this works
			boolean c4 = false;
			changed = c1 || c2 || c3 || c4;
		}
	}

	boolean nakedPair() {
		boolean c1 = nakedRowPair();
		boolean c2 = nakedColumnPair();
		return (c2 || c1);
	}

	boolean nakedColumnPair() {
		transpose();
		boolean ret = nakedRowPair();
		transpose();
		return ret;
	}

	boolean nakedRowPair() {
		boolean ret = false;
		ArrayList<TreeSet<Integer>> sets = new ArrayList<TreeSet<Integer>>();
		for (int i = 0; i < N; i++) {
			sets.clear();
			// creates a set for each possibles[j]
			for (int j = 0; j < N; j++) {
				TreeSet<Integer> temp = new TreeSet<Integer>();
				for (int z = 0; z < NUM_NUMBERS; z++) {
					temp.add(z+1);
				}
				temp.remove(0); // we don't care about 0
				sets.add(temp);
			}
			// see if any of the sets are the same
			ArrayList<TreeSet<Integer>> groups = new ArrayList<TreeSet<Integer>>();
			ArrayList<TreeSet<Integer>> places = new ArrayList<TreeSet<Integer>>();
			for (int a = 0; a < sets.size(); a++) {
				TreeSet<Integer> temp = new TreeSet<Integer>();
				int counter = 1;
				temp.add(a);
				for (int b = 0; b < sets.size(); b++) {
					if (a != b) {
						TreeSet<Integer> setA = sets.get(a);
						TreeSet<Integer> setB = sets.get(b);
						if (setA.containsAll(setB) && setB.containsAll(setA)) {
							temp.add(b);
							counter++;
						}
					}
				}
				if (counter == sets.get(a).size()) {
					groups.add(sets.get(a));
					places.add(temp);
				}
			}
			// we now have sets in groups
			for (int a = 0; a < groups.size(); a++) {
				TreeSet<Integer> temp = groups.get(a);
				TreeSet<Integer> correspondingPlaces = places.get(a);
				// remove the values in temp from every cell in this row
				for (int q = 0; q < temp.size(); q++) {
					int first = temp.pollFirst();
					for (int j = 0; j < N; j++) {
						if (!correspondingPlaces.contains(j)) {
							// System.out.println("Trying to remove " + first
							// + " from (" + i + ", " + j + ")");
							if (grid[i][j].possibles[first - 1] != 0) {
								grid[i][j].possibles[first - 1] = 0;
								if (grid[i][j].size != 0)
									grid[i][j].size--;
							}
						}
					}
				}
			}
		}
		return ret;
	}

	// look at each cell and remove what's already in its block
	boolean simpleBlockCheck() {
		boolean ret = false;
		int row, col = 0;
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
						if (current.possibles[n] != 0) {
							current.possibles[n] = 0;
							current.size--;
						}
					}
				}
				if (current.size == 1) {
					for (int k = 0; k < NUM_NUMBERS; k++) {
						int first_nonzero = current.possibles[k];
						if (first_nonzero != 0) {
							ret = true;
							current.value = first_nonzero;
							current.empty = false;
							// add move to stack
							Move temp = new Move();
							temp.guess = false;
							temp.loc = row * 100 + col;
							temp.value = first_nonzero;
							moves.push(temp);
							break;
						}
					}
				}
			}
		}
		return ret;
	}

	// look at each cell and then remove what's already in the column
	boolean simpleColumnCheck() {
		transpose();
		boolean ret = simpleRowCheck();
		transpose();
		return ret;
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
	boolean simpleRowCheck() {
		boolean ret = false;
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
								ret = true;
								grid[row][col].value = first_nonzero;
								grid[row][col].empty = false;
								// add move to stack
								Move temp = new Move();
								temp.guess = false;
								temp.loc = row * 100 + col;
								temp.value = first_nonzero;
								moves.push(temp);
								break;
							}
						}
					}
				}
			}
		}
		return ret;
	}

	void print() {
		for (int i = 1; i <= N; i++) {
			for (int j = 1; j <= N; j++) {
				System.out.print(grid[i - 1][j - 1].value);
			}
			System.out.println("");
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
