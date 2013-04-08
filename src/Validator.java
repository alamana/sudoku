import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Validator {

	int NUM_NUMBERS = 9;

	char numbers[];

	Validator() {
		numbers = new char[NUM_NUMBERS];
	}

	void fill() {
		for (int i = 1; i <= NUM_NUMBERS; i++) {
			numbers[i - 1] = (char) ('0' + i);
		}
	}

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

}
