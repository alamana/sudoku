import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

/**
 * Solver solves a Sudoku board with a combination of logic and backtracking.
 * 
 * @author sjboris
 * @author alamana
 */
public class Solver extends Stack<String> {

	private static boolean debug = true;
	private static boolean logic = true;
	public int N;
	public String filename;
	public Cell grid[][];
	public Stack<Move> moveHistory;
	public ArrayList<Group> groups;
	public Move lastMove;

	/**
	 * Sets N to 9.
	 */
	public Solver() {
		groups = new ArrayList<Group>();
		moveHistory = new Stack<Move>();
		N = 9;
	}

	/**
	 * Uses a combination of guessing and backtracking to fill in a puzzle.
	 */
	public boolean solve() {
		int counter = 1000;
		if (debug)
			print();

		/*
		 * Doing logic in the beginning doesn't seem to break anything.
		 */
		fillEasyCells();

		saveBoard();
		if (debug)
			print();

		while (!solved()) {
			if (debug) {
				System.out.println(counter);
				counter--;
				if (counter < 0) {
					System.out.println("BREAKING BECAUSE OF COUNTER");
					break;
				}
			}

			// make guess
			Cell c = firstEmptyCell();
			if (c == null) {
				return true;
			}

			if (debug)
				System.out.println("first empty cell is " + c.toString());

			int guessVal = c.getGuess();
			if (guessVal == -1) { // board is no longer valid
				if (debug) {
					// System.out
					// .println("Backtracking....this is what the board looks like before backtracking");
					print();
				}
				backtrack();
				if (debug) {
					System.out
							.println("Backtracking....this is what the board looks like after backtracking");
					System.out.println(grid[0][4].toString());
					System.out.println(grid[0][1].toString());
					print();
				}
			} else {
				if (debug) {
					// System.out
					// .println("Guessing....this is what the board looks like before guessing");
					print();
				}

				/*
				 * Save before guessing and doing logic based on guess
				 */
				saveBoard();

				c.assignValue(guessVal);
				addMove(true, c.value, c.name);
				if (debug) {
					System.out
							.println("Guessing....this is what the board looks like after guessing");
					System.out.println(grid[0][4].toString());
					System.out.println(grid[0][1].toString());
					print();
				}
				if (logic) {
					fillEasyCells();
					if (debug) {
						System.out
								.println("This is what the board looks like after logic...");
						System.out.println(grid[0][4].toString());
						System.out.println(grid[0][1].toString());
						print();
					}
				}
			}

		}
		return true;
	}

	/**
	 * Finds the first empty cell.
	 */
	public Cell firstEmptyCell() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (grid[i][j].empty) {
					return grid[i][j];
				}
			}
		}
		return null;
	}

	/**
	 * cells#groups# Cells and groups are separated with '&'
	 */
	public void saveBoard() {
		String ret = "";

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				ret += grid[i][j].serialize();
				ret += "&";
			}
		}
		ret += "#";

		for (int i = 0; i < groups.size(); i++) {
			ret += groups.get(i).serialize();
			ret += "&";
		}
		ret += "#";

		this.push(ret);
	}

	/**
	 * Restores the board to the most recently saved state.
	 */
	public void restoreGrid() {
		System.out.println("STACK SIZE: " + this.size());
		String save = this.pop();
		String parts[] = save.split("#");

		// restore cells
		String cells[] = parts[0].split("&");
		for (int i = 0; i < cells.length; i++) {
			String pieces[] = cells[i].split("\\$");

			int value = Integer.parseInt(pieces[0]);

			int name = Integer.parseInt(pieces[4]);
			int row = name / 100;
			int col = name % 10;
			Cell c = grid[row][col];
			c.reset();
			c.value = value;

			String possibles[] = pieces[1].split(",");
			for (int j = 0; j < possibles.length; j++) {
				if (!possibles[j].equals("")) {
					int possible = Integer.parseInt(possibles[j]);
					c.removePossible(possible);
				}
			}

			boolean empty = false;
			if (pieces[2].equals("0")) {
				empty = true;
			}
			c.empty = empty;

			int size = Integer.parseInt(pieces[3]);
			c.size = size;
		}

		// restore groups
		String Groups[] = parts[1].split("&");
		for (int i = 0; i < Groups.length; i++) {
			String desc = Groups[i];
			String groupParts[] = desc.split("\\$");
			String groupname = groupParts[3];
			// System.out.println(groupname);
			Group g = findGroup(groupname);

			g.resetGroupVals();
			String groupvalues[] = groupParts[1].split(",");
			for (int j = 0; j < groupvalues.length; j++) {
				// System.out.println("GROUP : " + groupvalues[j]);
				if (groupvalues[j].equals("true")) {
					g.groupVals[j] = true;
				}
			}
		}
	}

	/**
	 * Searches for a group by name.
	 * 
	 * @param name
	 *            Name of group to search for
	 * @return Null if not found
	 */
	public Group findGroup(String name) {
		for (Group g : groups) {
			if (g.equals(name))
				return g;
		}
		return null;
	}

	/**
	 * Resets to the board to how it was before the last guess. Removes the
	 * guess from cell's possibles[].
	 */
	public boolean backtrack() {
		if (debug)
			System.out.println("backtracking");

		// pop stuff off the stack until we get to something that was a guess
		Move m = null;
		m = moveHistory.pop();
		if (debug) {
			System.out.println("popped off " + m.loc + " with value " + m.value
					+ " and a guess value of " + m.guess);
		}

		// revert board to state before last guess
		restoreGrid();

		int prevguess = m.value;
		int row = m.loc / 100;
		int col = m.loc % 10;
		grid[row][col].removePossible(prevguess);
		// saveBoard();

		return true;
	}

	/**
	 * A grid is solved if every cell has a value.
	 * 
	 * @return True if every cell is filled in.
	 */
	public boolean solved() {
		for (Cell[] cells : grid)
			for (Cell c : cells)
				if (c.value == 0)
					return false;
		return true;
	}

	/**
	 * Calls reducePossiblities and then fills in each cell with only one
	 * possible value. Cells are found via firstDetermined.
	 * 
	 * @return True if a cell was filled in.
	 */
	public boolean fillEasyCells() {
		boolean ret = false;
		reducePossibilites();
		Cell c;
		while ((c = firstDetermined()) != null) {
			ret = true;
			c.fill();
			// addMove(false, c.value, c.name);
		}
		return ret;
	}

	/**
	 * Creates a new move and pushes it onto the stack.
	 * 
	 * @param guess
	 *            True if the value was guessed.
	 * @param value
	 *            Value to give the cell.
	 * @param loc
	 *            Cell's location.
	 */
	public void addMove(boolean guess, int value, int loc) {
		Move move = new Move();
		move.guess = guess;
		move.value = value;
		move.loc = loc;
		moveHistory.push(move);
	}

	/**
	 * Searches through the grid and looks for the first empty cell with only 1
	 * possibility.
	 * 
	 * @return The first empty cell with size equal to 1.
	 */
	public Cell firstDetermined() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (grid[i][j].size == 1 && grid[i][j].empty) {
					return grid[i][j];
				}
			}
		}
		return null;
	}

	/**
	 * Looks at each cell in the grid and calls Cell.removePossibles()
	 * 
	 * @return true if a possibility was changed. false if nothing changed.
	 */
	public boolean reducePossibilites() {
		boolean ret = false;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				Cell curr = grid[i][j];
				ret |= curr.removePossibles();
			}
		}
		return ret;
	}

	/**
	 * Reads a passed in grid. When reading, only accepts values between 1 and N
	 * as valid cell entries.
	 * 
	 * @param matrix
	 *            Grid to solve
	 */
	public void loadGrid(Cell[][] matrix, int size) {
		N = size;
		makeGrid();

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				int val = matrix[i][j].value;
				if (0 < val && val <= N) {
					grid[i][j].assignValue(val);
				}
			}
		}
	}

	/**
	 * Reads in a grid from a file. When reading, only accepts values between 1
	 * and N as valid cell entries.
	 * 
	 * @param file
	 *            Path to file to read.
	 * @param size
	 *            Dimension of the grid. Refers to the total number of cells per
	 *            row or column.
	 * 
	 */
	public void loadGrid(String file, int size) {
		this.filename = file;
		this.N = size;

		// initialize grid
		makeGrid();

		// do IO stuff
		File f = new File(file);
		Scanner in = null;
		try {
			in = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.out.println("SimpleSolver.solve: " + filename
					+ " not found!");
		}

		// read in grid
		for (int i = 0; i < N; i++) {
			String line = in.nextLine();
			for (int j = 0; j < N; j++) {
				int val = (int) (line.charAt(j) - '0');
				if (0 < val && val <= N) {
					grid[i][j].assignValue(val);
				}
			}
		}
		// save it
		// saveBoard();
	}

	/**
	 * Sets up the grid by initializing every cell and clearing groups, move
	 * history, and the grid history.
	 */
	public void makeGrid() {
		groups.clear();
		this.clear();
		moveHistory.clear();

		grid = new Cell[N][N]; // want a new grid for each file

		// initialize grid
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				grid[i][j] = new Cell(N);
				grid[i][j].name = i * 100 + j;
			}
		}

		// make groups

		// a group for each row
		for (int i = 0; i < N; i++) {
			Group g = new Group(N);
			g.type = "row";
			g.name = "row" + i;
			for (int j = 0; j < N; j++) {
				g.addCell(grid[i][j]);
				grid[i][j].groups.add(g);
			}
			groups.add(g);
		}

		// a group for each column
		for (int j = 0; j < N; j++) {
			Group g = new Group(N);
			g.type = "col";
			g.name = "col" + j;
			for (int i = 0; i < N; i++) {
				g.addCell(grid[i][j]);
				grid[i][j].groups.add(g);
			}
			groups.add(g);
		}

		// a group for each block
		int row = 0, col = 0;
		for (int z = 0; z < N; z++) {
			Group g = new Group(N);
			g.type = "block";
			g.name = "block" + z;
			row = (int) (Math.sqrt(N) * (z / ((int) Math.sqrt(N))));
			for (int i = 1; i <= (int) Math.sqrt(N); i++) {
				col = (int) Math.sqrt(N) * (z % (int) Math.sqrt(N));
				for (int j = 1; j <= (int) Math.sqrt(N); j++) {
					g.addCell(grid[row][col]);
					grid[row][col].groups.add(g);
					col++;
				}
				row++;
			}
			groups.add(g);
		}

	}

	/**
	 * Prints out the cell values in a grid.
	 */
	public void print() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (grid[i][j].value <= 9)
					System.out.print(" ");
				System.out.print(grid[i][j].value + " ");
			}
			System.out.println("");
		}
		System.out.println("");
	}

	public void printInfo() {
		String latest = this.peek();
		String hashparts[] = latest.split("#");

		// do cells
		String cells[] = hashparts[0].split("&");
		for (int i = 0; i < cells.length; i++) {
			String cellparts[] = cells[i].split("\\$");
			System.out.println("Cell Name: " + cellparts[4]);
			System.out.println("\tValue: " + cellparts[0]);
			System.out.println("\tPossibles : " + cellparts[1]);
			System.out.println("\tEmpty: " + cellparts[2]);
			System.out.println("\tSize: " + cellparts[3]);
			System.out.println("---------------------");
		}
	}

	public Cell[][] getGridCopy() {
		Cell[][] copy = new Cell[N][N];

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				copy[i][j] = new Cell(N);
				copy[i][j].assignValue(grid[i][j].value);
			}
		}

		return copy;
	}
}
