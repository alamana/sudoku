import java.util.Random;

public class generatorTest {

	public static void main(String[] args) {
		Solver s = new Solver();
		Validator v = new Validator();
		Generator gen = new Generator();
		
		System.out.println("Generating...");
		Cell[][] puzz = gen.getPuzzle(4, 3);
		System.out.println(v.validate(puzz, 4));
		System.out.println("---------");
		s.loadGrid(puzz, 4);
		s.print();
		s.solve();
		s.print();
		
	}

}