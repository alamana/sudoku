import java.util.ArrayList;
import java.util.Random;

/**
 * Generates a solvable Sudoku puzzle.
 * 
 * Note: Might generate puzzles with multiple solutions.
 * 
 * @author sjboris
 * @author alamana
 * 
 */
public class Generator {

	private Solver s;
	private Validator v;

	private static int N;

	public Cell[][] puzzle;
	public Cell[][] solution;

	public Generator() {
		s = new Solver();
		v = new Validator();
		puzzle = null;
		solution = null;
	}

	/**
	 * Used to set up the objects needed to create a puzzle.
	 * 
	 * @param grid
	 *            Array to initialize
	 * @param groups
	 *            List holding groups for the grid
	 * @param N
	 *            Number of cells on a side
	 */
	public void initialize(Cell[][] grid, ArrayList<Group> groups, int size) {
		Generator.N = size;

		// initialize cells
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				grid[i][j] = new Cell(N, i, j);
			}
		}

		// make groups

		// a group for each row
		for (int i = 0; i < N; i++) {
			Group g = new Group(N);
			g.type = "row";
			g.name = "row" + i;
			for (int j = 0; j < N; j++) { // add each cell in the row to the
											// group
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
	 * Meant to be called after <code>generatePuzzle()</code>.
	 * 
	 * @param N
	 *            of cells on a side
	 * @return A deep copy of completed puzzle
	 */
	public Cell[][] getSolution() {
		Cell[][] ret = new Cell[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				ret[i][j] = new Cell(N, i, j);
				ret[i][j].assignValue(solution[i][j].value);
			}
		}
		return ret;
	}

	/**
	 * Meant to be called after <code>generatePuzzle()</code>.
	 * 
	 * @param N
	 *            of cells on a side
	 * @return A deep copy of partially completed puzzle
	 */
	public Cell[][] getPartial() {
		Cell[][] ret = new Cell[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				ret[i][j] = new Cell(N, i, j);
				if (puzzle[i][j].value != 0) {
					ret[i][j].assignValue(puzzle[i][j].value);
				}
			}
		}
		return ret;
	}

	/**
	 * Initializes <code>puzzle</code> and <code>solution</code>. If the
	 * difficulty is 1, each row will have five filled in values. If the
	 * difficulty is 2, each row will have four filled in values. If the
	 * difficulty is 3, each row will have three filled in values.
	 * 
	 * @param N
	 *            Number of cells on a side of the board.
	 * @param difficulty
	 *            1 is the easiest, 3 is the hardest
	 */
	public void generatePuzzle(int N, int difficulty) {
		puzzle = new Cell[N][N];
		solution = new Cell[N][N];
		ArrayList<Group> groups = new ArrayList<Group>();

		initialize(puzzle, groups, N);

		// solve a blank board
		s.loadGrid(puzzle, N);

		s.solve();

		solution = s.getGridCopy();

		printSolution();
		System.out.println("Exchanging columns in section 0.");
		exchangeSolutionColumns(0);
		printSolution();

		// copy solution to ret
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				Cell r1 = puzzle[i][j];
				Cell s = solution[i][j];
				r1.assignValue(s.value);
			}
		}

		/*
		 * If difficulty is equal to one, reduce the number of values in each
		 * row to ceiling(N/2)
		 */
		Random r = new Random();

		int M = 1 + N / 2;
		for (int z = N; z > M; z--) {
			for (int row = 0; row < N; row++) {
				int col = r.nextInt(N);
				Cell c = puzzle[row][col];
				while (c.empty) {
					col++;
					col = col % N;
					c = puzzle[row][col];
				}
				c.unassign();
			}
		}

		/*
		 * If difficulty is equal to two, reduce the number of values in each
		 * row to sqrt(N).
		 */
		int sqrt = (int) Math.sqrt(N);
		int diff = M - sqrt;
		if (difficulty >= 2) {
			for (int i = 0; i < diff; i++) {
				for (int row = 0; row < N; row++) {
					int col = r.nextInt(N);
					Cell c = puzzle[row][col];
					while (c.empty) {
						col++;
						col = col % N;
						c = puzzle[row][col];
					}
					c.unassign();
				}
			}
		}

		/*
		 * return puzzle;* If difficulty is equal to three, reduce the number of
		 * values to sqrt(N)-1.
		 */
		if (difficulty == 3) {
			for (int row = 0; row < N; row++) {
				int col = r.nextInt(N);
				Cell c = puzzle[row][col];
				while (c.empty) {
					col++;
					col = col % N;
					c = puzzle[row][col];
				}
				c.unassign();
			}
		}
	}

	/**
	 * Permutes two random columns in a section.
	 */
	public void exchangeSolutionColumns(int section) {
		int sqrt = (int) Math.sqrt(N);

		int low = sqrt * section;
		int high = low + sqrt - 1;

		if (low < N && high < N) {
			// get a random value between [low, high]
			Random r = new Random();
			int col1 = r.nextInt(high + 1);

			// get another
			int col2 = r.nextInt(high + 1);
			if (col2 == col1) {
				col2 += 1;
				col2 = col2 % high;
			}

			col1 += low;
			col2 += low;

			System.out.println("Swapping " + col1 + " and " + col2);

			for (int i = 0; i < N; i++) {
				Cell c1 = solution[i][col1];
				Cell c2 = solution[i][col2];

				int val1 = c1.value;
				int val2 = c2.value;

				c1.unassign();
				c1.assignValue(val2);

				c2.unassign();
				c2.assignValue(val1);
			}
		}

	}

	/**
	 * Reflects the solution across the x-axis.
	 */
	public void reflectSolution() {
		for (int i = 0; i < N / 2; i++) {
			for (int j = 0; j < N; j++) {
				Cell from = solution[i][j];
				int fromVal = from.value;

				Cell to = solution[N - 1 - i][j];
				int toVal = to.value;

				from.unassign();
				from.assignValue(toVal);

				to.unassign();
				to.assignValue(fromVal);
			}
		}
	}

	/**
	 * Rotates the solution by 90 degrees CW.
	 */
	public void rotateSolution() {
		int shift = (int) Math.pow(10, (int) Math.floor(Math.log10(N)));

		Cell[][] temp = this.getSolution();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				Cell to = solution[j][N - i - 1];
				Cell from = temp[i][j];

				to.unassign();
				to.assignValue(from.value);
				//
				// to.row = j;
				// to.col = N - i - 1;
				//
				// to.name = j * shift + N - i - 1;
			}
		}
	}

	public void printSolution() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				Cell c = solution[i][j];
				System.out.print(c.value + " ");
			}
			System.out.println("");
		}
	}

}
