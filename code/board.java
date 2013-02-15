import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class board {

	// rc[i][j] --> ith row, jth column
	// blocks[k][i][j] --> kth block, ith row, jth column

	private final static String ROW_DATA_FILE = "rc";
	private final static String BLOCK_DATA_FILE = "rel";
	private static int rc[][], blocks[][][];

	public static void main(String[] args) {

		// read data into arrays
		rc = new int[9][9];
		readBoard();
		blocks = new int[9][3][3];
		readBlocks();
		
		printBoard();
	}

	public static void printBoard() {
		// print out row by row
		String s = "";
		for (int j = 0; j < 9; j++) {
			for (int i = 0; i < 9; i++) {
				String temp = Integer.toString(rc[j][i]);
				if (temp.length() == 1) { // make every 'temp' 2 chars long
					temp = " " + temp;
				}
				s += temp + " ";

			}
			s = s.substring(0, 9) + " | " + s.substring(9, 18) + " | "
					+ s.substring(18, 27);
			System.out.println(s);
			s = "";
			if ((j+1) % 3 == 0 && j != 8){
				System.out.println("----------|-----------|----------");
			}
		}
	}

	public static void readBlocks() {
		File f = new File(BLOCK_DATA_FILE);
		Scanner in = null;
		try {
			in = new Scanner(f);

			int blockCounter = 0;
			while (in.hasNext()) {
				String line = in.nextLine();
				if (!line.equals("")) {
					if (line.charAt(0) != '/') {
						String lineSplit[] = line.split(" ");
						// int division rounds down
						int rowCounter = Integer.parseInt(lineSplit[0]) / 10
								- 3 * (blockCounter / 3);
						for (int j = 0; j < 3; j++) {
							// System.out.println(lineSplit[j] + " " +
							// blockCounter + " " + rowCounter);
							blocks[blockCounter / 3][rowCounter][j] = Integer
									.parseInt(lineSplit[j]);
						}
					}
				} else {
					blockCounter++;
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println(BLOCK_DATA_FILE + " not found");
		}
	}

	public static void readBoard() {
		File f = new File(ROW_DATA_FILE);
		Scanner in = null;
		try {
			in = new Scanner(f);

			for (int i = 0; i < 9; i++) { // data file has 9 lines
				String line = in.nextLine();
				String lineSplit[] = line.split(" ");
				for (int j = 0; j < 9; j++) { // nine columns
					rc[i][j] = Integer.parseInt(lineSplit[j]);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println(ROW_DATA_FILE + " not found");
		}
	} // end readBoard

}
