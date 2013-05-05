/**
 * Simple object to keep track of Sudoku moves.
 * 
 * @author sjboris
 * @author alamana
 * 
 */
public class Move {
	/**
	 * Move location
	 */
	public int row, col;

	/**
	 * Move value
	 */
	public int value;

	/**
	 * True if this move was a guess.
	 */
	public boolean guess;

	/**
	 * Basic constructor. Sets loc and value to 0 and guess to false.
	 */
	public Move() {
		row = 0;
		col = 0;
		value = 0;
		guess = false;
	}
}
