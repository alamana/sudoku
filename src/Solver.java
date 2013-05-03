import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Solver {

	public int N;
	public String filename;
	public Cell grid[][];
	public Stack<Move> moves;
	public ArrayList<Group> groups;

	public Solver() {
		groups = new ArrayList<Group>();
		moves = new Stack<Move>();
		N = 9;
	}

	public void solve() {
		int counter = 0;
		while (!solved() && counter++ < 500) {
			reducePossibilites();
			// we've done goofed if there's an empty cell with 0 possiblities
			if (!sanityCheck()) {
				backtrack();
			}
			// while (fillEasyCells()) { // fillEasyCells returns false if
			// // there's
			// // nothing else to fill in
			// System.out.println("------");
			// print();
			// System.out.println("------");
			// }
			// make a guess
			guess();
		}
	}

	public void backtrack() {
		System.out.println("backtracking");
		// pop stuff off the stack until we get to something that was a guess
		Move m = moves.pop();
		System.out.println("popped off " + m.loc);
		int guessSave = m.value;
		undoMove(m);
		while (!m.guess) {
			m = moves.pop();
			System.out.println("popped off " + m.loc);
			guessSave = m.value;
			undoMove(m);
		}
		int row = m.loc / 100;
		int col = m.loc % 10;
		Cell c = grid[row][col];
		c.removePossible(guessSave);
		System.out.println("going to give " + c.name + " a value of "
				+ c.getGuess());
		c.assignValue(c.getGuess());

		addMove(true, c.value, c.name);
	}

	/**
	 * Loops through the grid and sees if any cells are empty and have a size of
	 * 0.
	 * 
	 * @return True if every cell passes the sanity check.
	 */
	public boolean sanityCheck() {
		for (Cell[] cells : grid) {
			for (Cell c : cells) {
				if (c.empty && c.size == 0) {
					System.out.println("sanityCheck: " + c.name);
					return false;
				}
			}
		}
		System.out.println("sanity check returning true");
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
			addMove(false, c.value, c.name);
		}
		return ret;
	}

	/**
	 * Called when there are no more easy cells to fill in. Takes the first
	 * undetermined cell and has it guess a value for itself.
	 */
	public void guess() {
		Cell c = firstUndetermined();
		if (c != null) {
			System.out.println("guess: " + c.name);
			c.assignValue(c.getGuess());
			Move m = new Move();
			m.guess = true;
			m.value = c.value;
			m.loc = c.name;
			moves.push(m);
		} else {
			System.out.println("Solver.guess: firstUndetermined was null");
		}
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
		moves.push(move);
	}

	/**
	 * Searches through the grid and looks for the first empty cell.
	 * 
	 * @return The first empty cell.
	 */
	public Cell firstUndetermined() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (grid[i][j].size > 0 && grid[i][j].empty) {
					return grid[i][j];
				}
			}
		}
		return null;
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
	 * Removes all of the easy possibilities from cells in the grid.
	 */
	public void easyReduce() {
		boolean reduced = true;
		while (reduced) {
			reduced = reducePossibilites();
		}
	}

	/**
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
	 * Looks at each group in groups and calls Group.valid()
	 * 
	 * @return true if every group in this grid is valid. False if at least one
	 *         is not.
	 */
	public boolean validGrid() {
		for (Group g : groups) {
			if (!g.valid())
				return false;
		}
		return true;
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
		moves.clear();
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
					grid[i][j].value = val;
					grid[i][j].empty = false;
					addMove(false, grid[i][j].value, grid[i][j].name);
					for (int k = 0; k < grid[i][j].groups.size(); k++) {
						Group g = grid[i][j].groups.get(k);
						g.groupVals[grid[i][j].value - 1] = true;
					}
				}
			}
		}
	}

	/**
	 * Parses a move and then calls the corresponding cell's removeGuess()
	 * method.
	 * 
	 * @param m
	 *            Move to undo.
	 */
	public void undoMove(Move m) {
		int loc = m.loc;
		int col = loc % 10;
		int row = loc / 100;
		grid[row][col].removeGuess();
	}

	public void makeGrid() {
		groups.clear();
		grid = new Cell[N][N]; // want a new grid for each file

		// initialize grid
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				grid[i][j] = new Cell();
				grid[i][j].name = i * 100 + j;
			}
		}

		// make groups

		// a group for each row
		for (int i = 0; i < N; i++) {
			Group g = new Group();
			g.type = "row";
			for (int j = 0; j < N; j++) { // add each cell in the row to the
											// group
				g.add(grid[i][j]);
				grid[i][j].groups.add(g);
			}
			groups.add(g);
		}

		// a group for each column
		for (int j = 0; j < N; j++) {
			Group g = new Group();
			g.type = "col";
			for (int i = 0; i < N; i++) {
				g.add(grid[i][j]);
				grid[i][j].groups.add(g);
			}
			groups.add(g);
		}

		// a group for each block
		int row = 0, col = 0;
		for (int z = 0; z < N; z++) {
			Group g = new Group();
			g.type = "block";
			row = 3 * (z / 3);
			for (int i = 1; i <= 3; i++) {
				col = 3 * (z % 3);
				for (int j = 1; j <= 3; j++) {
					// System.out.println(z + " " + row + " " + col);
					g.add(grid[row][col]);
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
				System.out.print(grid[i][j].value);
			}
			System.out.println("");
		}
	}
}
