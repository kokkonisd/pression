package UnitTesting;

public class UnitTestModule {

	private String name;
	private String[][] tests;
	
	UnitTestModule() {
		this.name = null;
		this.tests = null;
	}
	
	UnitTestModule(String name, String[][] tests) {
		setName(name);
		setTests(tests);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setTests(String[][] tests) {
		System.arraycopy(tests, 0, this.tests, 0, tests.length);
	}
	
	public String getName() {
		return this.name;
	}
	
	public String[][] getTests() {
		return this.tests;
	}
	
	public void runTests() {
		String status;
		int passed = 0;
		int failed = 0;
		
		System.out.printf("Unit testing module %s :\n", this.name);
		if (this.name != null && this.tests != null) {
			for (int i = 0; i < this.tests.length; i++) {
				if ((""+this.tests[i][0]).equals(""+this.tests[i][1])) {
					status = "PASSED";
					passed++;
				} else {
					status = "FAILED";
					failed++;
				}
				
				System.out.printf("\tTest %d : %s\n", i, status);
			}
			System.out.printf("---\nTest results : %d tests, %d passed, %d failed", passed+failed, passed, failed);
		}
	}
	
	public static void main(String[] args) {
		// On utilise l'object comme ça:
		
		UnitTestModule myMethod = new UnitTestModule(); // on peut specifier les tests directement aussi
		myMethod.setName("My Method");
		String[][] mytests = {{"abc", "abc"}, {"3", "7"}, {"true", "true"}};
		myMethod.setTests(mytests);
		myMethod.runTests();
	}

}
