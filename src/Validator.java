import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Validates a Sudoku board. A board is considered valid if each row, column,
 * and block do not contain any repeated values.
 * 
 * @author sjboris
 * @author alamana
 * 
 */
public class Validator {

	/**
	 * Number of values for a group of cells.
	 */
	public int N;

	/**
	 * Used to test rows, columns, and blocks.
	 */
	public int numbers[];

	/**
	 * Basic constructor. Sets NUM_NUMBERS to 9 and initializes numbers.
	 */
	Validator() {
	}

	/**
	 * Fills numbers with values 1 through N
	 */
	void fill() {
		for (int i = 0; i < N; i++) {
			numbers[i] = i + 1;
		}
	}

	/**
	 * Use to check if numbers if empty.
	 * 
	 * @return True if numbers is empty.
	 */
	boolean empty() {
		boolean ret = true;
		for (int i = 1; i < N; i++) {
			if (numbers[i - 1] != '0') {
				ret = false;
				break;
			}
		}
		return ret;
	}

	/**
	 * Checks to see if the grid is valid. N is set here. numbers[] is
	 * initialized here.
	 * 
	 * @param grid
	 *            Cell[][] to read from.
	 * @return True if the grid is valid.
	 */
	boolean validate(Cell[][] grid, int size) {
		N = size;
		numbers = new int[N];

		// check rows
		for (int i = 0; i < N; i++) {
			fill();
			for (int j = 0; j < N; j++) {
				int cell = grid[i][j].value;
				int index = cell - 1;
				if (index == -1)
					continue;
				// System.out.println("j=" + j + "   " + cell + " @ " + index);
				if (index > N || index < 0) {
					// System.out.println("Failed at " + i + " " + j + "\t"
					// + numbers[index] + (numbers[index] != cell) + cell);
					return false;
				}
				if (numbers[index] != cell) {
					// System.out.println("Failed at " + i + " " + j + "\t"
					// + numbers[index] + (numbers[index] != cell) + cell);
					return false;
				}
				numbers[index] = 0;
			}
		}

		// check columns
		for (int j = 0; j < N; j++) {
			fill();
			for (int i = 0; i < N; i++) {
				int cell = grid[i][j].value;
				int index = cell - 1;
				if (index == -1)
					continue;
				if ((numbers[index]) != cell) {
					// System.out.println("Failed at " + i + " " + j + "\t"
					// + numbers[index] + " != " + cell);
					return false;
				}
				numbers[index] = 0;
			}
		}

		// check boxes
		int row, col;
		row = 0;
		for (int z = 0; z < N; z++) {
			fill();
			row = (int) (Math.sqrt(N) * (z / (int) Math.sqrt(N)));
			for (int i = 1; i <= (int) Math.sqrt(N); i++) {
				col = (int) Math.sqrt(N) * (z % (int) Math.sqrt(N));
				for (int j = 1; j <= (int) Math.sqrt(N); j++) {
					int cell = grid[row][col].value;
					int index = cell - 1;
					if (index == -1)
						continue;
					if ((numbers[index]) != cell) {
						// System.out.println("Failed at " + i + " " + j + "\t"
						// + numbers[index] + " != " + cell);
						return false;
					}
					numbers[index] = 0;
					col++;
				}
				// System.out.print("\n");
				row++;
			}
		}
		return true;
	}

	public boolean isSolved(Cell[][] grid, int size) {
		if (this.validate(grid, size)) {
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					if (grid[i][j].empty) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}
}
