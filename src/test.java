public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Solver solver = new Solver();
		solver.loadGrid("testFiles/superEasy", 9);
		solver.fillEasyCells();
		solver.print();
		// System.out.println(solver.grid[0][6].name
		// + "'s groups are: (name, value) "
		// + solver.grid[0][6].groups.size());
		// for (int i = 0; i < solver.grid[0][6].groups.size(); i++) {
		// for (Cell c : solver.grid[0][6].groups.get(i)) {
		// System.out.print("(" + c.name + ", " + c.value + "), ");
		// }
		// System.out.println("");
		// }
		Validator v = new Validator();
		System.out.print(v.validate(solver.grid));
	}

}
