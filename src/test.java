public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Validator v = new Validator();
		SimpleSolver s = new SimpleSolver();
		s.solve("superEasy");
		String file = s.write();
		System.out.println(v.check(file));

	}

}
