/**
 * Used to test Solver.java.
 * 
 * @author sjboris
 * @author alamana
 */
public class test {

	/**
	 * Basic test method.
	 */
	public static void main(String[] args) {
		Solver solver = new Solver();
		solver.loadGrid("testFiles/hardest", 9);
		solver.solve();
		System.out.println("=========");
		System.out.println("");
		solver.print();
		Validator v = new Validator();
		System.out.print(v.validate(solver.grid));
	}

}
