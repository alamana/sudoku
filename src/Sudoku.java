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

	public static ArrayList<Cell> empty;
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
		difficulty = Integer
				.parseInt(JOptionPane
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

		empty = new ArrayList<Cell>();

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
				if (!v.validate(current, n * n))
					JOptionPane.showMessageDialog(null,
							"A spot is filled incorrectly.");
				else {

					/*
					 * Have to resolve the puzzle every time since not every
					 * puzzle has a unique solution.
					 */
					s.loadGrid(current, n * n);
					try {

						s.solve();
						Cell[][] temps = s.grid;

						/*
						 * Look to see if any values have been filled
						 * incorrectly.
						 */
						boolean incorrect = false;
						int row = -1;
						int col = -1;
						for (int i = 0; i < n * n && !incorrect; i++) {
							for (int j = 0; j < n * n && !incorrect; j++) {
								if ((current[i][j].value != temps[i][j].value)
										&& !current[i][j].empty) {
									incorrect = true;
									row = i;
									col = j;
								}
							}
						}
						if (incorrect) {
							JOptionPane.showMessageDialog(null, "Spot (" + row
									+ "," + col + ") is filled incorrectly.");
						} else {
							empty.clear();
							for (int i = 0; i < n * n; i++) {
								for (int j = 0; j < n * n; j++) {
									if (current[i][j].empty) {
										empty.add(temps[i][j]);
									}
								}
							}
							int spot = (int) (Math.random() * empty.size());
							Cell temp = empty.get(spot);
							int newX = temp.row;
							int newY = temp.col;
							JOptionPane.showMessageDialog(null, "Spot ("
									+ (newY + 1) + "," + (newX + 1)
									+ ") should be "
									+ solution[newX][newY].value);
						}
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null,
								"A spot is filled incorrectly.");
					}
				}
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
								if (!square.getBackground()
										.equals(Color.orange)) {
									square.setBackground(Color.ORANGE);
								} else {
									square.setBackground(Color.BLUE);
								}
								current[fx][fy].unassign();
								current[fx][fy].assignValue(temp);
								boolean solved = v.isSolved(current, n * n);
								if (solved) {
									JOptionPane.showMessageDialog(null,
											"Congragulations! \n You completed a "
													+ n * n + "x" + n * n
													+ " sudoku puzzle!");
								}
							}
						});
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
						int i = y + x * n;
						((JTextComponent) subgrid[ix][iy].getComponent(i))
								.setText("" + current[fx][fy].value);
						((JTextComponent) subgrid[ix][iy].getComponent(i))
								.setBackground(Color.WHITE);
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
