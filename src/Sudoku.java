import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class Sudoku  extends JFrame{

	public static int n = 6;
	public static JPanel grid;
	public static Cell[][] solution, puzzle, current;
	public static JPanel panel;
	public static JPanel[] subgrid;
	
	public Sudoku() {
		startUI();
	}
	
	public void startUI() {
		panel = new JPanel();
	    getContentPane().add(panel);
	    setTitle("NxN Sudoku");
	    setSize(900, 700);
	    setLocationRelativeTo(null);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    panel.setLayout(new BorderLayout(50,0));
	    
	    grid = new JPanel();
	    
	    
	    JPanel buttonList = new JPanel();
	    buttonList.setLayout(new GridLayout(3,1));
	    
	    panel.add(buttonList, BorderLayout.EAST);
	    
	    JButton generateButton = new JButton("Generate");
	    generateButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent event) {
	    		n = Integer.parseInt(JOptionPane.showInputDialog("Input n (standard Sudoku is n=3)"));
	    		grid.setLayout(new GridLayout(n, n, 10, 10));
	    		puzzle = new Cell[n*n][n*n];
	    		solution = new Cell[n*n][n*n];
	    		current = new Cell[n*n][n*n];
	    		for(int x = 0; x < n*n; x++) {
	    			for(int y = 0; y < n*n; y++) {
	    				puzzle[x][y] = new Cell();
	    				solution[x][y] = new Cell();
	    				current[x][y] = new Cell();
	    			}
	    		}
	    		//fillGrid((700-(n*20))/(n*n));
	    		/*panel.add(grid, BorderLayout.CENTER);
	    		for(int i = 0; i < n*n; i++) {
	    			grid.add(new JButton(""+i));
	    		}*/
	    	}
	    });
	    
	    JButton hintButton = new JButton("Hint");
	    hintButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent event) {
	    		System.exit(0);
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
	    
	    puzzle = new Cell[n*n][n*n];
		solution = new Cell[n*n][n*n];
		current = new Cell[n*n][n*n];
	    for(int x = 0; x < n*n; x++) {
			for(int y = 0; y < n*n; y++) {
				puzzle[x][y] = new Cell();
				solution[x][y] = new Cell();
				current[x][y] = new Cell();
			}
		}
	    
	    grid.setLayout(new GridLayout(n, n, 10, 10));
	    
	    int buttonDim = (700-(n*10))/(n*n);
	    System.out.println(buttonDim);
	    subgrid = new JPanel[n*n];
		
		for(int i = 0; i < n*n; i++) {
			subgrid[i] = new JPanel();
			subgrid[i].setLayout(new GridLayout(n,n));
			for(int x = 0; x < n; x++) {
				for(int y = 0; y < n; y++) {
					final int fx = n*((i)%n)+x;
					final int fy = n*((i)/n)+y;
					final JTextField square = new JTextField(""+current[fx][fy].value);
					//button.setSize(buttonDim, buttonDim);
				    square.addActionListener(new ActionListener() {
				    	public void actionPerformed(ActionEvent event) {
				    		//int temp = Integer.parseInt(JOptionPane.showInputDialog("Enter new value"));
				    		int temp = Integer.parseInt(square.getText());
				    		System.out.println("INSIDE FIELD: " + temp);
				    		square.setText(""+temp);
				    		current[fx][fy].value = temp;
				    	}
				    });
				    //System.out.println(button.getWidth() + " " + button.getHeight());
				    subgrid[i].add(square);
				}
			}
			grid.add(subgrid[i]);
			System.out.println("worked " + i);
			panel.add(grid, BorderLayout.CENTER);
		}
	}
	
	public void fillGrid(int buttonDim) {
		subgrid = new JPanel[n*n];
		
		for(int i = 0; i < n*n; i++) {
			subgrid[i] = new JPanel();
			subgrid[i].setLayout(new GridLayout(n,n));
			for(int x = 0; x < n; x++) {
				for(int y = 0; y < n; y++) {
					final int fx = 3*((i)%3)+x;
					final int fy = 3*((i)/3)+y;
					final JButton button = new JButton(""+current[fx][fy].value);
					button.setSize(buttonDim, buttonDim);
				    button.addActionListener(new ActionListener() {
				    	public void actionPerformed(ActionEvent event) {
				    		int temp = Integer.parseInt(JOptionPane.showInputDialog("Enter new value"));
				    		button.setText(""+temp);
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
