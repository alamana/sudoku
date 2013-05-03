public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Solver solver = new Solver();
		solver.loadGrid("testFiles/backtrackingTest", 9);
		solver.solve();
		solver.print();
		Validator v = new Validator();
		System.out.print(v.validate(solver.grid));
	}

}
