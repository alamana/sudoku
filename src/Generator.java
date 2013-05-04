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

	private static boolean debug = false;

	private Solver s;
	private Validator v;

	public Generator() {
		s = new Solver();
		v = new Validator();
	}

	/**
	 * Works on the assumption that puzzles with more blanks are harder to
	 * solve. Difficult of one means that
	 * 
	 * @param N
	 *            Number of cells in one edge of the grid
	 * @param difficulty
	 *            1=easy, 2=medium, 3=hard
	 * @return NxN array
	 */
	public Cell[][] generatePuzzle(int N, int difficulty) {
		Cell[][] ret = new Cell[N][N];
		ArrayList<Group> groups = new ArrayList<Group>();

		// initialize grid
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				ret[i][j] = new Cell();
				ret[i][j].name = i * 100 + j;
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
				g.add(ret[i][j]);
				ret[i][j].groups.add(g);
			}
			groups.add(g);
		}

		// a group for each column
		for (int j = 0; j < N; j++) {
			Group g = new Group();
			g.type = "col";
			g.name = "col" + j;
			for (int i = 0; i < N; i++) {
				g.add(ret[i][j]);
				ret[i][j].groups.add(g);
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
					g.add(ret[row][col]);
					ret[row][col].groups.add(g);
					col++;
				}
				row++;
			}
			groups.add(g);
		}

		/*
		 * Make each group have three values.
		 */

		Random rand = new Random();

		for (Group g : groups) {
			while (g.size() < 3) {
				int randVal = rand.nextInt(N);
				randVal++; // rand returns between [0,N)

				int randIndex = rand.nextInt(N);
				Cell randCell = g.get(randIndex);

				/*
				 * Increment until we get a good value.
				 */
				if (debug)
					System.out.println(randVal);
				while (!g.valid(randVal)) {
					randVal = (randVal + 1) % N;
					randVal++;
					if (debug)
						System.out.println("Inside Loop: " + randVal);
				}
				randCell.assignValue(randVal);
			}
		}

		/*
		 * Add one more value if difficulty is 2 or less
		 */

		if (difficulty <= 2) {
			for (Group g : groups) {
				while (g.size() < 4) {
					int randVal = rand.nextInt(N);
					randVal++; // rand returns between [0,N)

					int randIndex = rand.nextInt(N);
					Cell randCell = g.get(randIndex);

					/*
					 * Increment until we get a good value.
					 */
					while (!g.valid(randVal)) {
						randVal = (randVal + 1) % N;
						randVal++;
					}
					randCell.assignValue(randVal);
				}
			}

		}

		/*
		 * Add one more additional value if difficulty is 1
		 */

		if (difficulty == 1) {
			for (Group g : groups) {
				while (g.size() < 5) {
					int randVal = rand.nextInt(N);
					randVal++; // rand returns between [0,N)

					int randIndex = rand.nextInt(N);
					Cell randCell = g.get(randIndex);

					/*
					 * Increment until we get a good value.
					 */
					while (!g.valid(randVal)) {
						randVal = (randVal + 1) % N;
						randVal++;
					}
					randCell.assignValue(randVal);
				}
			}

		}

		/*
		 * Print it out if debugging.
		 */

		if (debug) {
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					System.out.print(ret[i][j].value);
				}
				System.out.println("");
			}
		}

		return ret;
	}

}
