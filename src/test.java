public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Validator v = new Validator();
		// SimpleSolver s = new SimpleSolver();
		// s.solve("testFiles/test01");
		// String file = s.write();
		// System.out.println("Valid = " + v.check(file));
		Solver solver = new Solver();
		solver.loadGrid("testFiles/superEasy", 9);
		solver.solve();
		solver.print();
		//System.out.println(solver.grid[0][0].name + "'s groups are: (name, value) "  + solver.grid[0][0].groups.size());
		//for (int i = 0; i < solver.grid[0][0].groups.size(); i++) {
		//	for (Cell c : solver.grid[0][0].groups.get(i)) {
		//		System.out.print("(" + c.name + ", " + c.value + "), ");
		//	}
		//	System.out.println("");
		//}
	}

}
