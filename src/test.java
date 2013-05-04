public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Solver solver = new Solver();
		solver.loadGrid("testFiles/backtrackingTest2", 9);
//		System.out.println("*********");
//		solver.printInfo();
//		System.out.println("*********");
		solver.solve();
		System.out.println("=========");
		solver.print();
		Validator v = new Validator();
		System.out.print(v.validate(solver.grid));
	}

}
