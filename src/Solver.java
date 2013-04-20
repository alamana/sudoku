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
		for (int i = 0; i < 1; i++)
			fillEasyCells();

	}

	public void fillEasyCells() {
		// look at each group and remove a its values from every cell in the
		// group
		
		for (int i = 0; i < N; i++){
			for (int j = 0; j < N; j++){
				Cell curr = grid[i][j];
				curr.removePossibles();
				curr.fill();
			}
		}
	/*	for (Group g : groups) {
			System.out.println(g);
			for (int i = 0; i < g.size(); i++) { // choose a cell
				Cell a = g.get(i);
				if (!a.empty) // no need to check filled in cells
					continue;
				for (int j = 0; j < g.groupVals.length && a.empty; j++) {
					if (g.groupVals[j]) {
						a.removePossible(j + 1); // groupVals is zero indexed
					}
				}
			}
		}
		*/
	}

	public boolean validGrid() {
		for (Group g : groups) {
			if (!g.valid())
				return false;
		}
		return true;
	}

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
					for (int k = 0; k < grid[i][j].groups.size(); k++) {
						Group g = grid[i][j].groups.get(k);
						g.groupVals[grid[i][j].value - 1] = true;
					}
				}
			}
		}
	}

	public void makeMove(Move m) {
		int loc = m.loc;
		int col = loc % 10;
		int row = loc / 100;
		grid[row][col].empty = false;
		grid[row][col].value = m.value;
	}

	public void undoMove(Move m) {
		int loc = m.loc;
		int col = loc % 10;
		int row = loc / 100;
		grid[row][col].empty = true;
		grid[row][col].value = 0;
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

	public void print() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				System.out.print(grid[i][j].value);
			}
			System.out.println("");
		}
	}
}
