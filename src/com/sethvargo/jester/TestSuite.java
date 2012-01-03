package com.sethvargo.jester;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public abstract class TestSuite {
	private HashMap<String, ArrayList<Test>> testsMap = new HashMap<String, ArrayList<Test>>();
	private ArrayList<String> ignoredMethods = new ArrayList<String>(Arrays.asList(
		"main", "setup", "tearDown", "beforeEach", "afterEach", "test", "run", "runTests",
		"assertEqual", "assertNotEqual", "assertNull", "assertNotNull", "assertException", "assertNotException", "assertMatches", "assertNotMatches",
		"wait", "equals", "toString", "hashCode", "getClass", "notify", "notifyAll"
	));
	
	public void assertEqual(Object expectedValue, Object target, String methodName, Object... args) {
		addTest(new Test(new EqualityTester(), expectedValue, target, methodName, args));
	}
	
	public void assertNotEqual(Object expectedValue, Object target, String methodName, Object... args) {
		addTest(new Test(new NotEqualityTester(), expectedValue, target, methodName, args));
	}
	
	public void assertNull(Object target, String methodName, Object... args) {
		addTest(new Test(new NullityTester(), null, target, methodName, args));
	}
	
	public void assertNotNull(Object target, String methodName, Object... args) {
		addTest(new Test(new NotNullityTester(), null, target, methodName, args));
	}
	
	public void assertException(Object exception, Object target, String methodName, Object... args) {
		addTest(new Test(new ExceptionTester(), exception, target, methodName, args));
	}
	
	public void assertNotException(Object target, String methodName, Object... args) {
		addTest(new Test(new NotExceptionTester(), null, target, methodName, args));
	}

	public void assertMatches(String regex, Object target, String methodName, Object... args) {
		addTest(new Test(new MatchTester(), regex, target, methodName, args));
	}
	
	public void assertNotMatches(String regex, Object target, String methodName, Object... args) {
		addTest(new Test(new NotMatchTester(), regex, target, methodName, args));
	}

	public void run() {
		ArrayList<Method> methods = new ArrayList<Method>(Arrays.asList(this.getClass().getMethods()));
		
		for(Method method : methods) {
			String methodName = method.getName();
			if(!ignoredMethods.contains(method.getName())) {
				try {
					beforeEach();
					method.invoke(this);
					
					ArrayList<Test> tests = testsMap.get(methodName);
					ArrayList<String> failures = new ArrayList<String>();
					int failed = 0;
					
					printHeader(methodName);
					
					for(Test test : tests) {
						if(!test.passed()) {
							failures.add(test.getFailureMessage());
							System.out.print("F");
						} else {
							System.out.print(".");
						}
					}
					
					System.out.println("\n");
					for(String failure : failures)
						System.out.println(failure);
					
					printStats(tests.size(), failed);
					afterEach();
				} catch (IllegalArgumentException e) {
					System.out.println(e.getLocalizedMessage());
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					System.out.println(e.getLocalizedMessage());
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					System.out.println(e.getLocalizedMessage());
					e.printStackTrace();
				}
			}
		}
	}
	
	public void setup() {};
	public void tearDown() {};
	public void beforeEach() {};
	public void afterEach() {};
	
	public static void runTests(TestSuite testSuite) {
		testSuite.setup();
		testSuite.run();
		testSuite.tearDown();
	}
	
	private void addTest(Test test) {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		String methodName = stackTraceElements[3].getMethodName();
		
		if(!testsMap.containsKey(methodName))
			testsMap.put(methodName, new ArrayList<Test>());
		
		ArrayList<Test> tests = testsMap.get(methodName);
		tests.add(test);
	}
	
	private void printHeader(String header) {
		int padding = 7;
		System.out.print("+");
		for(int i = 0; i < header.length() + padding*2; i++)
			System.out.print("-");
		System.out.println("+");

		System.out.print("|");
		for(int i = 0; i < padding; i++)
			System.out.print(" ");
		System.out.print(header);
		for(int i = 0; i < padding; i++)
			System.out.print(" ");
		System.out.println("|");

		System.out.print("+");
		for(int i = 0; i < header.length() + padding*2; i++)
			System.out.print("-");
		System.out.println("+");
	}
	
	private void printStats(int tests, int failed) {
		System.out.println("\nSUMMARY:");
		System.out.println("  PASSED: " + (tests - failed) + "\tFAILED: " + failed);
		System.out.println("\n\n");
	}
}
