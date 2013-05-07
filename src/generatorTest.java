import java.util.Random;

public class generatorTest {

	public static void main(String[] args) {
		Solver s = new Solver();
		Validator v = new Validator();
		Generator gen = new Generator();

		Solver.setDebug(false);
		Solver.setCounter(false, 35);
		Solver.setLogic(true);
		
		int N = 16;
		System.out.println("Generating...");
		long startTime = System.nanoTime();
		gen.generatePuzzle(N, 1);
		long endTime = System.nanoTime();
		Cell[][] puzz = gen.getPartial();
		
		for (int i = 0; i < 3*N-1; i++)
			System.out.print("=");
		System.out.println("");
		
		System.out.println("Puzzle generated in\n" + 1E-9*(endTime - startTime) + " seconds.");
		
		for (int i = 0; i < 3*N-1; i++)
			System.out.print("=");
		System.out.println("");
		s.loadGrid(puzz, N);
		s.print();
		
		Cell[][] solution = gen.getSolution();
		for (int i = 0; i < N; i++){
			for (int j = 0; j < N; j++){
				System.out.print(solution[i][j].value + " ");
			}
			System.out.println("");
		}
		
		System.out.println(v.validate(s.grid, N));

	}

}