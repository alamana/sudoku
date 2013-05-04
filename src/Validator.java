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
	public int NUM_NUMBERS;

	/**
	 * Used to test rows, columns, and blocks.
	 */
	public char numbers[];

	/**
	 * Basic constructor. Sets NUM_NUMBERS to 9 and initializes numbers.
	 */
	Validator() {
		NUM_NUMBERS = 9;
		numbers = new char[NUM_NUMBERS];
	}

	/**
	 * Fills numbers with values 1 through N
	 */
	void fill() {
		for (int i = 1; i <= NUM_NUMBERS; i++) {
			numbers[i - 1] = (char) ('0' + i);
		}
	}

	/**
	 * Use to check if numbers if empty.
	 * 
	 * @return True if numbers is empty.
	 */
	boolean empty() {
		boolean ret = true;
		for (int i = 1; i < NUM_NUMBERS; i++) {
			if (numbers[i - 1] != '0') {
				ret = false;
				break;
			}
		}
		return ret;
	}

	/**
	 * Reads a grid from a file and checks it.
	 * 
	 * @param filename
	 *            File to read formatted grid from.
	 * @return True if the file's grid is valid.
	 */
	boolean check(String filename) {
		File f = new File(filename);
		Scanner in = null;

		try {
			in = new Scanner(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		char grid[][] = new char[NUM_NUMBERS][NUM_NUMBERS];

		// read in grid
		for (int i = 0; i < NUM_NUMBERS; i++) {
			String line = in.nextLine();
			for (int j = 0; j < NUM_NUMBERS; j++) {
				grid[i][j] = line.charAt(j);
				// System.out.print(line.charAt(j));
			}
			// System.out.println("");
		}

		// check rows
		for (int i = 0; i < NUM_NUMBERS; i++) {
			fill();
			for (int j = 0; j < NUM_NUMBERS; j++) {
				char cell = grid[i][j];
				int index = (int) (cell - '1');
				// System.out.println("j=" + j + "   " + cell + " @ " + index);
				if (index > 9 || index < 1)
					return false;
				if (numbers[index] != cell)
					return false;
				numbers[index] = '0';
			}
		}

		// check columns
		for (int j = 0; j < NUM_NUMBERS; j++) {
			fill();
			for (int i = 0; i < NUM_NUMBERS; i++) {
				char cell = grid[i][j];
				int index = (int) (cell - '1');
				if (numbers[index] != cell)
					return false;
				numbers[index] = '0';
			}
		}

		// check boxes
		int row, col;
		row = 0;
		for (int z = 0; z < 9; z++) {
			fill();
			row = 3 * (z / 3);
			for (int i = 1; i <= 3; i++) {
				col = 3 * (z % 3);
				for (int j = 1; j <= 3; j++) {
					char cell = grid[row][col];
					int index = (int) (cell - '1');
					if (numbers[index] != cell)
						return false;
					numbers[index] = '0';
					// System.out.print(grid[row][col]);
					col++;
				}
				// System.out.print("\n");
				row++;
			}
		}

		return true;
	}

	/**
	 * Checks to see if the passed in grid is valid.
	 * 
	 * @param grid
	 *            Cell[][] to read from.
	 * @return True if the grid is valid.
	 */
	boolean validate(Cell[][] grid) {
		// check rows
		for (int i = 0; i < NUM_NUMBERS; i++) {
			fill();
			for (int j = 0; j < NUM_NUMBERS; j++) {
				int cell = grid[i][j].value;
				int index = cell - 1;
				if (index == -1)
					continue;
				// System.out.println("j=" + j + "   " + cell + " @ " + index);
				if (index > 8 || index < 0)
					return false;
				if ((numbers[index] - '0') != cell)
					return false;
				numbers[index] = '0';
			}
		}

		// check columns
		for (int j = 0; j < NUM_NUMBERS; j++) {
			fill();
			for (int i = 0; i < NUM_NUMBERS; i++) {
				int cell = grid[i][j].value;
				int index = cell - 1;
				if (index == -1)
					continue;
				if ((numbers[index] - '0') != cell)
					return false;
				numbers[index] = '0';
			}
		}

		// check boxes
		int row, col;
		row = 0;
		for (int z = 0; z < 9; z++) {
			fill();
			row = 3 * (z / 3);
			for (int i = 1; i <= 3; i++) {
				col = 3 * (z % 3);
				for (int j = 1; j <= 3; j++) {
					int cell = grid[row][col].value;
					int index = cell - 1;
					if (index == -1)
						continue;
					if ((numbers[index] - '0') != cell)
						return false;
					numbers[index] = '0';
					// System.out.print(grid[row][col]);
					col++;
				}
				// System.out.print("\n");
				row++;
			}
		}
		return true;
	}
}
