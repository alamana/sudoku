import java.util.Random;

public class generatorTest {

	public static void main(String[] args) {
		Solver s = new Solver();
		Generator gen = new Generator();
		
		
		Cell[][] puzz = gen.generatePuzzle(9, 1);
		System.out.println("---------");
		s.loadGrid(puzz, 9);
		s.solve();
		
	}

}
