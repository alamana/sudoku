import java.util.Random;

public class generatorTest {

	public static void main(String[] args) {
		Solver s = new Solver();
		Validator v = new Validator();
		Generator gen = new Generator();
		
		
		Cell[][] puzz = gen.getSolvable(9, 1);
		System.out.println(v.validate(puzz));
		System.out.println("---------");
		s.loadGrid(puzz, 9);
		s.print();
		s.solve();
		s.print();
		
	}

}