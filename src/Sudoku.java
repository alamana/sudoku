import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * GUI for Sudoku solver.
 * 
 * @author sjboris
 * @author alamana
 * 
 */
public class Sudoku extends JFrame {

	/**
	 * Size of the minor boxes. n^2 is the length of a side of the grid.
	 */
	public static int n = 3;

	/**
	 * Main grid to hold cells.
	 */
	public static JPanel grid;

	/**
	 * Cell[][] grid containing the solution/puzzle/current.
	 */
	public static Cell[][] solution, puzzle, current;

	/**
	 * Validates a solution.
	 */
	private static Validator v;

	/**
	 * Fills in cells on a grid.
	 */
	private static Solver s;
	
	/**
	 * Makes puzzles
	 */
	private static Generator g;

	/*
	 * contains all empty cells of the grid in the form 1000*x + y (cannot
	 * handle values where n*n >= 1000)
	 */

	public static ArrayList<Integer> empty;
	public static JPanel panel;
	public static JPanel[] subgrid;

	public Sudoku() {
		v = new Validator();
		s = new Solver();
		g = new Generator();
		startUI();
	}

	public void startUI() {
		n = Integer.parseInt(JOptionPane
				.showInputDialog("Input n (standard Sudoku is n=3)"));
		panel = new JPanel();
		getContentPane().add(panel);
		setTitle("NxN Sudoku");
		setSize(900, 700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		panel.setLayout(new BorderLayout(50, 0));

		grid = new JPanel();

		JPanel buttonList = new JPanel();
		buttonList.setLayout(new GridLayout(3, 1));

		panel.add(buttonList, BorderLayout.EAST);

		empty = new ArrayList<Integer>();
		for (int x = 0; x < n; x++) {
			for (int y = 0; y < n; y++) {
				empty.add(1000 * x + y);
			}
		}

		JButton generateButton = new JButton("Generate");
		generateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				grid.setLayout(new GridLayout(n, n, 10, 10));
				puzzle = new Cell[n * n][n * n];
				solution = new Cell[n * n][n * n];
				current = new Cell[n * n][n * n];
				for (int x = 0; x < n * n; x++) {
					for (int y = 0; y < n * n; y++) {
						puzzle[x][y] = new Cell();
						solution[x][y] = new Cell();
						current[x][y] = new Cell();
					}
				}
			}
		});

		JButton hintButton = new JButton("Hint");
		hintButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int spot = (int) (Math.random() * empty.size());
				int temp = empty.get(spot);
				empty.remove(spot);
				int newX = temp / 1000;
				int newY = temp % 1000;
				JOptionPane.showMessageDialog(null, "Spot (" + newX + ","
						+ newY + ") should be " + solution[newX][newY].value);
			}
		});

		JButton quitButton = new JButton("Quit");
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});

		buttonList.add(generateButton);
		buttonList.add(hintButton);
		buttonList.add(quitButton);

		puzzle = new Cell[n * n][n * n];
		solution = new Cell[n * n][n * n];
		current = new Cell[n * n][n * n];
		for (int x = 0; x < n * n; x++) {
			for (int y = 0; y < n * n; y++) {
				puzzle[x][y] = new Cell();
				solution[x][y] = new Cell();
				current[x][y] = new Cell();
			}
		}
		s.loadGrid(puzzle, n * n);
		s.solve();
		solution = s.getGridCopy();

		if (n > 1) {
			for (int i = 0; i < (n * n) - n; i++) {
				int spot = (int) (Math.random() * empty.size());
				int temp = empty.get(spot);
				empty.remove(spot);
				int newX = temp / 1000;
				int newY = temp % 1000;
				current[newX][newY].value = solution[newX][newY].value;
			}
		}

		grid.setLayout(new GridLayout(n, n, 10, 10));

		int buttonDim = (700 - (n * 10)) / (n * n);
		System.out.println(buttonDim);
		subgrid = new JPanel[n * n];

		for (int i = 0; i < n * n; i++) {
			subgrid[i] = new JPanel();
			subgrid[i].setLayout(new GridLayout(n, n));
			for (int x = 0; x < n; x++) {
				for (int y = 0; y < n; y++) {
					final int fx = n * ((i) % n) + x;
					final int fy = n * ((i) / n) + y;
					final JTextField square = new JTextField(""
							+ current[fx][fy].value);
					square.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent event) {
							int temp = Integer.parseInt(square.getText());
							square.setText("" + temp);
							current[fx][fy].value = temp;
							boolean valid = v.validate(current);
							if (valid) {
								JOptionPane.showConfirmDialog(null,
										"Congragulations! \n You completed a "
												+ n * n + " sudoku puzzle!");
							}
						}
					});
					// System.out.println(button.getWidth() + " " +
					// button.getHeight());
					subgrid[i].add(square);
				}
			}
			grid.add(subgrid[i]);
			System.out.println("worked " + i);
			panel.add(grid, BorderLayout.CENTER);
		}
	}

	/*
	 * Does nothing right now
	 */
	public void fillGrid(int buttonDim) {
		subgrid = new JPanel[n * n];

		for (int i = 0; i < n * n; i++) {
			subgrid[i] = new JPanel();
			subgrid[i].setLayout(new GridLayout(n, n));
			for (int x = 0; x < n; x++) {
				for (int y = 0; y < n; y++) {
					final int fx = 3 * ((i) % 3) + x;
					final int fy = 3 * ((i) / 3) + y;
					final JButton button = new JButton(""
							+ current[fx][fy].value);
					button.setSize(buttonDim, buttonDim);
					button.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent event) {
							int temp = Integer.parseInt(JOptionPane
									.showInputDialog("Enter new value"));
							button.setText("" + temp);
							current[fx][fy].value = temp;
						}
					});
					subgrid[i].add(button);
				}
			}
			grid.add(subgrid[i]);
			System.out.println("worked " + i);
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Sudoku gui = new Sudoku();
				gui.setVisible(true);
			}
		});
	}

}
