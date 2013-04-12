public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Validator v = new Validator();
		SimpleSolver s = new SimpleSolver();
		s.solve("test01");
		String file = s.write();
		System.out.println("Valid = " + v.check(file));

	}

}
