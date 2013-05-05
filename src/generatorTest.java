import java.util.Random;

public class generatorTest {

	public static void main(String[] args) {
		Solver s = new Solver();
		Validator v = new Validator();
		Generator gen = new Generator();

		int N = 9;
		System.out.println("Generating...");
		gen.generatePuzzle(N, 1);
		Cell[][] puzz = gen.getPartial(N);
		System.out.println(v.validate(puzz, N));
		System.out.println("---------");
		s.loadGrid(puzz, N);
		//s.print();
		s.solve();
		//s.print();

	}

}