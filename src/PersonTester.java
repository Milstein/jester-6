import com.sethvargo.jester.TestSuite;


public class PersonTester extends TestSuite {
	private Person seth;
	private Person rick;
	
	public void setup() {
		seth = new Person("Seth Vargo", 20);
		rick = new Person("Richard Stormer", 40);
	}
	
	public void tearDown() {
		seth = null;
		rick = null;
	}
	
	public void testFirstName() {
		assertEqual("Seth", seth, "getFirstName");
		assertEqual("Richard", rick, "getFirstName");
		assertNotEqual("Seth", rick, "getFirstName");
		assertNotEqual("Richard", seth, "getFirstName");
	}
	
	public void testLastName() {
		assertEqual("Vargo", seth, "getLastName");
		assertEqual("Stormer", rick, "getLastName");
	}
	
	public void testThrowable() {
		assertException(new IllegalArgumentException(), seth, "throwSomething");
	}
	
	public void testNotThrowable() {
		assertNotException(seth, "dontThrowSomething");
	}
	
	public static void main(String args[]) {
		runTests(new PersonTester());
	}
}
