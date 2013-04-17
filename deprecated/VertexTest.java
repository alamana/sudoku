import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class VertexTest {

	/**
	 * @param args
	 */
	static ArrayList<Vertex> list;
	
	public static void main(String[] args) {
		int N = 9;
		list = new ArrayList<Vertex>();

		String filename = "testFiles/easy1";

		Vertex grid[][] = new Vertex[9][9];

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				grid[i][j] = new Vertex();
				grid[i][j].name = i * 100 + j;
				list.add(grid[i][j]);
			}
		}

		File f = new File(filename);
		Scanner in = null;
		try {
			in = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.out.println("SimpleSolver.solve: " + filename
					+ " not found!");
		}

		// read in grid[i][j]
		for (int i = 0; i < N; i++) {
			String line = in.nextLine();
			for (int j = 0; j < N; j++) {
				char cell = line.charAt(j);
				if (cell == 'x') {
					continue;
				} else {
					int value = (int) (cell - '0');
					grid[i][j].value = value;
					grid[i][j].labelled = true;
				}
			}
		}

		// set connections

		// rows
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				for (int k = 0; k < N; k++) {
					if (j != k) {
						grid[i][j].add(grid[i][k]);
					}
				}
			}
		}

		// columns
		for (int j = 0; j < N; j++) {
			for (int i = 0; i < N; i++) {
				for (int k = 0; k < N; k++) {
					if (i != k) {
						grid[i][j].add(grid[k][j]);
					}
				}
			}
		}
		
		// // add 10 nodes, make each one connect to the three previous ones
		// for (int i = 0; i < 15; i++) {
		// Vertex temp = new Vertex();
		// if (i - 1 >= 0) {
		// temp.add(list.get(i - 1));
		// }
		// if (i - 2 >= 0) {
		// temp.add(list.get(i - 2));
		// }
		// if (i - 3 >= 0) {
		// temp.add(list.get(i - 3));
		// }
		// // System.out.println("i=" + i);
		// list.add(temp);
		// }

		// print out the values in the array
		//printList(list);

		// color
		while (notColored(list)) {
			printList(list);
			Vertex mostPopular = findMax(list);
			System.out.println("Mostpopular=" + mostPopular.name);
			mostPopular.value = mostPopular.lowest();
			System.out.println("Mostpopular.lowestValue()=" + mostPopular.value);
			mostPopular.labelled = true;
			//System.out.println("After doing mostpopular stuff");
			//printList(list);
			//color(mostPopular); // start from mostPopular
		}
		// print again
		printList(list);
	}

	static boolean notColored(ArrayList<Vertex> list) {
		for (int i = 0; i < list.size(); i++) {
			if (!list.get(i).labelled) {
				return true;
			}
		}
		return false;
	}

	static void color(Vertex vertex) {
		System.out.println("Entering color....Looking at " + vertex.name);
		Vertex maxNeighbour = vertex.getMax();
		if (maxNeighbour != null) {
			System.out.print("Max neighbour is..." + maxNeighbour.name);
			maxNeighbour.value = maxNeighbour.lowest();
			System.out.println(", settings it's value to " + maxNeighbour.value);
			maxNeighbour.labelled = true;
			System.out.println("Recusing...");
			printList(list);
			color(maxNeighbour);
		}
	}

	static Vertex findMax(ArrayList<Vertex> list) {
		Vertex ret = null;
		int max = -1;
		for (int i = 0; i < list.size(); i++) {
			// System.out.println("Looking at " + list.get(i).value
			// + " with size of " + list.get(i).getSize() + ", max = "
			// + max);
			if (!list.get(i).labelled) { // only look at unlabelled
				if (list.get(i).getSize() > max) {
					ret = list.get(i);
					max = list.get(i).getSize();
				}
			}
		}
		return ret;
	}

	static void printList(ArrayList<Vertex> list) {
		for (int i = 1; i <= list.size(); i++) {
			System.out.print(list.get(i - 1).value + " ");
			if (i % 9 == 0) {
				System.out.println("");
			}
		}
		System.out.println("");
	}

}