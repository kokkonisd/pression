package UnitTesting;

public class UnitTestModule {

	// the name and tests of the module
	private String name;
	
	// variables to keep track of the tests
	private String status;
	private int passed;
	private int failed;
	
	private int[][] intTests;
	private double[][] doubleTests;
	private char[][] charTests;
	private String[][] StringTests;
	
	private String active_tests = null;
	
	// constructor, without argument
	public UnitTestModule() {
		this.name = null;
	}
	
	// constructor, with argument
	public UnitTestModule(String name) {
		setName(name);
	}
	
	// ---
	// setter - getter methods
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setTests(int[][] tests) {
		this.intTests = tests;
		this.active_tests = "int";
	}
	
	public void setTests(double[][] tests) {
		this.doubleTests = tests;
		this.active_tests = "double";
	}
	
	public void setTests(char[][] tests) {
		this.charTests = tests;
		this.active_tests = "char";
	}
	
	public void setTests(String[][] tests) {
		this.StringTests = tests;
		this.active_tests = "String";
	}
	
	public String getName() {
		return this.name;
	}
	
	//---
	// method that handles input test array and
	// runs the tests accordingly
	public void runTests() {
		// start the test
		System.out.printf("Unit testing module %s :\n", this.name);
		this.passed = 0;
		this.failed = 0;
		switch(active_tests) {
			case "int":
				parseAndRun(this.intTests);
				break;
			case "double":
				parseAndRun(this.doubleTests);
				break;
			case "char":
				parseAndRun(this.charTests);
				break;
			case "String":
				parseAndRun(this.StringTests);
				break;
			default:
				System.out.println("No tests specified.");
		}
		if (active_tests != null) {
			System.out.printf("---\nTest results : %d tests, %d passed, %d failed\n", 
					this.passed + this.failed, this.passed, this.failed);
		}
	}
	
	
	// overloading the parseAndRun method to support all basic types
	private void parseAndRun(int[][] tests) {
		for (int i = 0; i < tests.length; i++) {
			// compare the elements
			if(tests[i][0] == tests[i][1]) {
				status = "PASSED";
				passed++;
			} else {
				status = "FAILED";
				failed++;
			}

			System.out.printf("\tTest %d : %s\n", i+1, status);
		}
	}
	
	private void parseAndRun(double[][] tests) {
		for (int i = 0; i < tests.length; i++) {
			// compare the elements
			if(tests[i][0] == tests[i][1]) {
				status = "PASSED";
				passed++;
			} else {
				status = "FAILED";
				failed++;
			}

			System.out.printf("\tTest %d : %s\n", i+1, status);
		}
	}
	
	private void parseAndRun(char[][] tests) {
		for (int i = 0; i < tests.length; i++) {
			// compare the elements
			if(tests[i][0] == tests[i][1]) {
				status = "PASSED";
				passed++;
			} else {
				status = "FAILED";
				failed++;
			}

			System.out.printf("\tTest %d : %s\n", i+1, status);
		}
	}
	
	private void parseAndRun(String[][] tests) {
			for (int i = 0; i < tests.length; i++) {
				// compare the elements
				if((tests[i][0]).equals(tests[i][1])) {
					status = "PASSED";
					passed++;
				} else {
					status = "FAILED";
					failed++;
				}

				System.out.printf("\tTest %d : %s\n", i+1, status);
			}
	}
	
	// example methods to run tests on
	public static String majuscules(String s) {
		return s.toUpperCase();
	}
	
	public static int power(int i) {
		return i * i;
	}
	
	public static double power(double d) {
		return Math.pow(d, 2);
	}
	
	public static void main(String[] args) {
		// here's how to use the object
		
		UnitTestModule myTester = new UnitTestModule(); // we can also specify the tests in the constructor
		// define the name of the module
		myTester.setName("Majuscules");
		
		// make a 2D array, containing smaller 2 element 1D arrays
		// the first element of the 1D arrays is the input, the second is the expected output
		String[][] mytests = {{majuscules("abc"), "ABC"}, {majuscules("3"), "3"}, {majuscules("true"), "TRUe"}};
		// pass the test array to the module
		myTester.setTests(mytests);
		// run the tests
		myTester.runTests();
		
		/* here's the output we get for this one

		 Unit testing module Majuscules :
			Test 1 : PASSED
			Test 2 : PASSED
			Test 3 : FAILED
		---
		Test results : 3 tests, 2 passed, 1 failed
		*/
		
		// now let's test the second method
		myTester.setName("Power");
		myTester.setTests(new int[][] {{power(1), 1}, {power(2), 4}, {power(3), 9}});
		System.out.println();
		myTester.runTests();
		
		// and finally the third method (which is really just overloading the second)
		myTester.setName("Double Power");
		myTester.setTests(new double[][] {{power(2.0), 4.0}, {power(5.0), 25.0}});
		System.out.println();
		myTester.runTests();
	}

}
