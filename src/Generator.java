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
	public void initialize(Cell[][] grid, ArrayList<Group> groups, int N) {
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
	public Cell[][] getSolution(int N) {
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
	public Cell[][] getPartial(int N) {
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

		// give random values to the main diagonal
		Random r = new Random();

		// if (N <= 9) {
		for (int i = 0; i < N; i++) {
			Cell c = puzzle[i][i];

			int randVal = r.nextInt(N) + 1;
			boolean broke = false;
			int counter = 0;
			while (!c.validValue(randVal) && counter < 10) {
				randVal++; // increment
				randVal %= N; // prevent from going over
				randVal++; // prevent randVal from modding to 1
				counter++;
				if (counter == 10)
					broke = true;
			}
			if (broke)
				System.out.println("GENERATOR CONFLICT");
			c.assignValue(randVal);
		}

		// give a random value to the other diagonal
		// for (int i = 0; i < N; i++) {
		// Cell c = puzzle[i][N - i - 1];
		//
		// int randVal = r.nextInt(N) + 1;
		// boolean broke = false;
		// int counter = 0;
		// while (!c.validValue(randVal) && counter < 10) {
		// randVal++; // increment
		// randVal %= N; // prevent from going over
		// randVal++; // prevent randVal from modding to 1
		// counter++;
		// if (counter == 10)
		// broke = true;
		// }
		// if (broke)
		// System.out.println("GENERATOR CONFLICT");
		//
		// c.assignValue(randVal);
		// }
		// } else {
		// // do one side
		// for (int j = 0; j < N; j += 3) {
		// for (int i = 0; i + j < N; i++) {
		// Cell c = puzzle[j + i][i];
		//
		// int randVal = r.nextInt(N) + 1;
		// boolean broke = false;
		// int counter = 0;
		// while (!c.validValue(randVal) && counter < 10) {
		// randVal++; // increment
		// randVal %= N; // prevent from going over
		// randVal++; // prevent randVal from modding to 1
		// counter++;
		// if (counter == 10)
		// broke = true;
		// }
		// if (broke)
		// System.out.println("GENERATOR CONFLICT");
		// c.assignValue(randVal);
		// }
		// }
		//
		// // do the other
		// for (int j = 3; j < N; j += 3) {
		// for (int i = 0; i + j < N; i++) {
		// Cell c = puzzle[i][j + i];
		//
		// int randVal = r.nextInt(N) + 1;
		// boolean broke = false;
		// int counter = 0;
		// while (!c.validValue(randVal) && counter < 10) {
		// randVal++; // increment
		// randVal %= N; // prevent from going over
		// randVal++; // prevent randVal from modding to 1
		// counter++;
		// if (counter == 10)
		// broke = true;
		// }
		// if (broke)
		// System.out.println("GENERATOR CONFLICT");
		// c.assignValue(randVal);
		// }
		// }
		// }

		// solve the board then
		s.loadGrid(puzzle, N);

		System.out.println("Came up with: ");
		s.print();

		System.out.println(s.solve());

		solution = s.getGridCopy();
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

}
