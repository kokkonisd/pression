package UnitTesting;

public class UnitTestModule {

	// le nom et les tests du module
	private String name;
	private String[][] tests;
	
	// constructor, sans arguments
	UnitTestModule() {
		this.name = null;
		this.tests = null;
	}
	
	// constructor, avec arguments
	UnitTestModule(String name, String[][] tests) {
		setName(name);
		setTests(tests);
	}
	
	// ---
	// setter - getter methods
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setTests(String[][] tests) {
		this.tests = tests;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String[][] getTests() {
		return this.tests;
	}
	
	//---
	// méthode pour performer les tests
	
	public void runTests() {
		String status;
		int passed = 0;
		int failed = 0;
		
		// si le nom ou le tableau des tests n'est pas valide
		if (this.name != null && this.tests != null) {
			// on commence le test
			System.out.printf("Unit testing module %s :\n", this.name);
			for (int i = 0; i < this.tests.length; i++) {
				// on transforme tout en String (avec ""+), afin de pouvoir comparer les éléments
				if ((""+this.tests[i][0]).equals(""+this.tests[i][1])) {
					status = "PASSED";
					passed++;
				} else {
					status = "FAILED";
					failed++;
				}
				
				System.out.printf("\tTest %d : %s\n", i+1, status);
			}
			System.out.printf("---\nTest results : %d tests, %d passed, %d failed", passed+failed, passed, failed);
		} else {
			System.out.printf("Unit Testing Error : no name or tests");
		}
	}
	
	// exemple de méthode pour tester:
	public static String majuscules(String s) {
		return s.toUpperCase();
	}
	
	public static void main(String[] args) {
		// On utilise l'object comme ça:
		
		UnitTestModule myMethod = new UnitTestModule(); // on peut specifier les tests directement aussi
		// on définit le nom du module et les tests
		myMethod.setName("Majuscules");
		/* en gros on a des petits tableaux qui contiennent à la place 0
			l'entrée de la méthode qu'on veut tester et à la place 1
			la sortie qu'on attend */
		// dans ce cas, le dernier va être un fail
		String[][] mytests = {{majuscules("abc"), "ABC"}, {majuscules("3"), "3"}, {majuscules("true"), "TRUe"}};
		// puis on passe le tableau qui contient tout à notre module
		myMethod.setTests(mytests);
		// et finalement on execute les tests
		myMethod.runTests();
		
		/* donc on a l'output suivant:

		 Unit testing module Majuscules :
			Test 1 : PASSED
			Test 2 : PASSED
			Test 3 : FAILED
		---
		Test results : 3 tests, 2 passed, 1 failed
		*/
	}

}
