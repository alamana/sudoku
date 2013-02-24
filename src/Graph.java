import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Graph {

	private final int SIZE = 81; // Number of cells
	private final String BLOCK_FILE = "blocks";
	private final String ROWCOLUMN_FILE = "rc";
	private Node graph[];
	private ArrayList<Edge> edges;
	private int rc[][], blocks[][][];

	public Graph() {
		edges = new ArrayList<Edge>();

		// Read the data in so we can work with it
		rc = new int[9][9];
		readRC();
		blocks = new int[9][3][3];
		readBlocks();

		// Create the graph
		graph = new Node[SIZE];

		// Populate the graph
		for (int i = 0; i < SIZE; i++) {
			Node temp = new Node();
			int row = i / 9;
			int column = i % 9;
			int cellVal = rc[row][column];
			temp.id = cellVal; // Each node will have a unique number 1 - 88,
								// number's ending in 9 excluded
		}

		// Create the web
		for (int i = 0; i < SIZE; i++) { // Do everything from the perspective
											// of graph[i]
			int val = graph[i].value;
			int row = val / 10;
			int col = val % 10;
			// Loop through row
			for (int j = 1; j < 9; j++) { // Start one over since we don't need
											// to look at the current cell
				int id = rc[row][(col + j) % 9];
				Node temp = findNode(id); // Returns node in graph
				Edge edge = new Edge(graph[i], temp);
				edges.add(edge);
				graph[i].edges.add(edge);
			}
			// Loop through column
			for (int j = 1; j < 9; j++) { // Start one over since we don't need
				// to look at the current cell
				int id = rc[(row + j) % 9][col];
				Node temp = findNode(id); // Returns node in graph
				Edge edge = new Edge(graph[i], temp);
				edges.add(edge);
				graph[i].edges.add(edge);
			}
			// Loop through blocks
			int block = inBlock(graph[i].id);
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 3; k++) {
					int id = blocks[block][j][k];
					Node temp = findNode(id);
					if (temp.id != graph[i].id) {
						Edge edge = new Edge(graph[i], temp);
						if (!edges.contains(edge)){
							edges.add(edge);
						}
						if (!graph[i].edges.contains(edge)){
							graph[i].edges.add(edge);
						}
					}
				}
			}

		}

		// Initialize conditions here
		// E.g. given cell values

	}

	// Finds node by ID
	public Node findNode(int id) {
		Node temp = new Node();
		temp.id = id;
		for (int i = 0; i < SIZE; i++) {
			if (temp.id == graph[i].id) {
				temp = graph[i];
				break;
			}
		}
		return temp;
	}

	public int inBlock(int id) {
		int ret = 0;
		boolean found = false;
		for (int i = 0; i < 9; i++) {
			if (!found) {
				for (int j = 0; j < 3; j++) {
					for (int k = 0; k < 3; k++) {
						int cellID = blocks[i][j][k];
						if (cellID == id) {
							ret = i;
							found = true;
							break;
						}
					}
				}
			}
		}
		return ret;
	}

	public void readRC() {
		File f = new File(ROWCOLUMN_FILE);
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
			System.out.println(ROWCOLUMN_FILE + " not found");
		}
	}

	public void readBlocks() {
		File f = new File(BLOCK_FILE);
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
			System.out.println(BLOCK_FILE + " not found");
		}

	}

}
