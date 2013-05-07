import javax.swing.*;
import javax.swing.text.JTextComponent;

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
	
	public static int difficulty = 2;

	/**
	 * Main grid to hold cells.
	 */
	public static JPanel grid;
	
	public static int buttonDim;

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
	public static JPanel[][] subgrid;

	public Sudoku() {
		v = new Validator();
		s = new Solver();
		g = new Generator();
		startUI();
	}

	public void startUI() {
		n = Integer.parseInt(JOptionPane
				.showInputDialog("Input n (standard Sudoku is n=3)"));
		difficulty = Integer.parseInt(JOptionPane
				.showInputDialog("Input difficulty\n 1: Easy   2: Medium   3: Hard "));
		buttonDim = (700 - (n * 10)) / (n * n);
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
				// grid.setLayout(new GridLayout(n, n, 10, 10));
				difficulty = Integer.parseInt(JOptionPane
						.showInputDialog("Input difficulty\n 1: Easy   2: Medium   3: Hard "));
				g.generatePuzzle(n * n, difficulty);
				s.loadGrid(g.getSolution(), n * n);
				s.print();
				puzzle = g.getPartial();
				solution = g.getSolution();
				current = g.getPartial();
				fillGrid(buttonDim);	
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
				JOptionPane.showMessageDialog(null, "Spot (" + newY + ","
						+ newX + ") should be " + solution[newX][newY].value);
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

		g.generatePuzzle(n * n, difficulty);
		s.loadGrid(g.getSolution(), n * n);
		s.print();
		puzzle = g.getPartial();
		solution = g.getSolution();
		current = g.getPartial();

		grid.setLayout(new GridLayout(n, n, 10, 10));

		System.out.println(buttonDim);
		subgrid = new JPanel[n][n];

		for (int ix = 0; ix < n; ix++) {
			for (int iy = 0; iy < n; iy++) {
				subgrid[ix][iy] = new JPanel();
				subgrid[ix][iy].setLayout(new GridLayout(n, n));
				for (int x = 0; x < n; x++) {
					for (int y = 0; y < n; y++) {
						final int fx = n * ix + x;
						final int fy = n * iy + y;
						final JTextField square = new JTextField(""
								+ current[fx][fy].value);
						square.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent event) {
								int temp = Integer.parseInt(square.getText());
								square.setText("" + temp);
								current[fx][fy].value = temp;
								boolean valid = v.validate(current, n * n);
								if (valid) {
									JOptionPane
											.showMessageDialog(null,
													"Congragulations! \n You completed a "
															+ n * n
															+ " sudoku puzzle!");
								}
							}
						});
						// System.out.println(button.getWidth() + " " +
						// button.getHeight());
						subgrid[ix][iy].add(square);
					}
				}
				grid.add(subgrid[ix][iy]);
				panel.add(grid, BorderLayout.CENTER);
			}
		}
	}


	public void fillGrid(int buttonDim) {
		for (int ix = 0; ix < n; ix++) {
			for (int iy = 0; iy < n; iy++) {
				for (int x = 0; x < n; x++) {
					for (int y = 0; y < n; y++) {
						int fx = n * ix + x;
						int fy = n * iy + y;
						int i = y + x*n;
						((JTextComponent) subgrid[ix][iy].getComponent(i)).setText("" + current[fx][fy].value);
					}

				}
			}
		}

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Sudoku gui = new Sudoku();
				gui.setVisible(true);
			}
		});
	}

}
