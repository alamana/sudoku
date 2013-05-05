import java.util.ArrayList;
import java.util.Random;

/**
 * Generates a solvable Sudoku puzzle.
 * 
 * Might generate puzzles with multiple solutions.
 * 
 * @author sjboris
 * @author alamana
 * 
 */
public class Generator {

	private static boolean debug = true;

	private Solver s;
	private Validator v;

	public Generator() {
		s = new Solver();
		v = new Validator();
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
				grid[i][j] = new Cell();
				grid[i][j].name = i * 100 + j;
			}
		}

		// make groups

		// a group for each row
		for (int i = 0; i < N; i++) {
			Group g = new Group();
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
			Group g = new Group();
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
			Group g = new Group();
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
	 * Returns a partially completed Sudoku board. If the difficulty is 1, each
	 * row will have five filled in values. If the difficulty is 2, each row
	 * will have four filled in values. If the difficulty is 3, each row will
	 * have three filled in values.
	 * 
	 * @param N
	 *            Number of cells on a side of the board.
	 * @param difficulty
	 *            1 is the easiest, 3 is the hardest
	 */
	public Cell[][] getPuzzle(int N, int difficulty) {
		Cell[][] ret = new Cell[N][N];
		ArrayList<Group> groups = new ArrayList<Group>();

		initialize(ret, groups, N);

		// give random values to the main diagonal
		Random r = new Random();

		for (int i = 0; i < N; i++) {
			Cell c = ret[i][i];

			int randVal = r.nextInt(N) + 1;
			while (!c.validValue(randVal)) {
				randVal++; // increment
				randVal %= N; // prevent from going over
				randVal++; // prevent randVal from modding to 1
			}

			c.assignValue(randVal);
		}

		// solve the board then
		s.loadGrid(ret, N);
		s.solve();

		Cell[][] solution = s.getGridCopy();
		// copy solution to ret
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				Cell r1 = ret[i][j];
				Cell s = solution[i][j];
				r1.assignValue(s.value);
			}
		}

		/*
		 * If difficulty is equal to one, reduce the number of values in each
		 * row to 5
		 */
		for (int z = N; z > 5; z--) {
			for (int row = 0; row < N; row++) {
				int col = r.nextInt(N);
				Cell c = ret[row][col];
				while (c.empty) {
					col++;
					col = col % 9;
					c = ret[row][col];
				}
				c.unassign();
			}
		}

		/*
		 * If difficulty is equal to two, reduce the number of values in each
		 * row to 4.
		 */

		if (difficulty == 2) {
			for (int row = 0; row < N; row++) {
				int col = r.nextInt(N);
				Cell c = ret[row][col];
				while (c.empty) {
					col++;
					col = col % 9;
					c = ret[row][col];
				}
				c.unassign();
			}
		}

		/*
		 * If difficulty is equal to three, reduce the number of values to 3.
		 */
		if (difficulty == 3) {
			for (int row = 0; row < N; row++) {
				int col = r.nextInt(N);
				Cell c = ret[row][col];
				while (c.empty) {
					col++;
					col = col % 9;
					c = ret[row][col];
				}
				c.unassign();
			}
		}
		return ret;
	}

}
