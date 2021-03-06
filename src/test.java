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
		
		Solver.setCounter(true, 1000);
		Solver.setDebug(false);
		Solver.setLogic(false);
		
		solver.loadGrid("testFiles/4x4/02", 16);
		System.out.println("Initial board: ");
		solver.print();
		System.out.println("=========");
		solver.solve();
		System.out.println("=========");
		System.out.println("");
		solver.print();
		Validator v = new Validator();
		System.out.print(v.validate(solver.grid, 16));
	}

}
