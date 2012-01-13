package com.sethvargo.jester;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class Test {
	private Tester tester;
	private Object expectedResult;
	private Object actualResult;
	private Object target;
	private String methodName;
	private Object[] args;
	
	private String failureMessage;
	
	private TestResult testResult;
	
	public Test(Tester tester, Object expectedResult, Object target, String methodName, Object... args) {
		this.tester = tester;
		this.expectedResult = expectedResult;
		this.target = target;
		this.methodName = methodName;
		this.args = args;
	}
	
	public Boolean passed() {
		if(testResult == null)
			throw new IllegalArgumentException("You have not run the test yet!");
		
		return testResult.passed();
	}

	public String getFailureMessage() {
		if(testResult == null)
			throw new IllegalArgumentException("You have not run the test yet!");
		
		if(testResult.passed())
			throw new IllegalArgumentException("Cannot call failureMessage on a test that passed!");
		
		if(failureMessage != null)
			return failureMessage;
		
		return "[failed]: " + methodWithParameters() + " expected " + (tester.getClass().toString().indexOf("Not") == -1 ? "" : "NOT ") + "<" + toString(expectedResult) + ">, but got <" + toString(actualResult) + ">";
	}
	
	private String methodSignature() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < args.length; i++) {
			sb.append(args[i].getClass().getSimpleName());
			if(i != args.length-1) {
				sb.append(", ");
			}
		}
		
		return methodName + "(" + sb.toString() + ")";
	}
	
	private String methodWithParameters() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < args.length; i++) {
			sb.append(args[i]);
			if(i != args.length-1) {
				sb.append(", ");
			}
		}
		
		return methodName + "(" + sb.toString() + ")";
	}
	
	private Class<?>[] argClasses(Object... args) {
		Class<?>[] argClasses = new Class<?>[args.length];
		for(int i = 0; i < args.length; i++) {
			argClasses[i] = args[i].getClass();
		}
		return argClasses;
	}
	
	public void run() {
		PrintStream printStreamOriginal = System.out;
		System.setOut(new PrintStream(new OutputStream(){ public void write(int b) { } }));
		
		testResult = new TestResult(expectedResult, false);

		try {
			testResult = tester.test(expectedResult, target, getMethod(), args);
			actualResult = testResult.getResult();
		} catch(IllegalArgumentException e) {
			failureMessage = "[tester error]: illegal number or type of arguments passed to " + methodSignature();
		} catch(NoSuchMethodException e) {
			failureMessage = "[tester error]: method " + methodSignature() + " does not exist";
		} catch(IllegalAccessException e) {
			failureMessage = "[tester error]: tried to access private or protected method " + methodSignature();
		} catch(SecurityException e) {
			failureMessage = "[tester error]: tried accessing private or protected method" + methodSignature();
		} catch(InvocationTargetException e) {
			// errors produced by the actual code
			failureMessage = "[error]: method " + methodWithParameters() + " expected <" + toString(expectedResult) + ">, but got <" + e.getTargetException().getClass().toString() + ">";
		} catch(Exception e) {
			failureMessage = "[tester error]: uncaught exception " + e.getLocalizedMessage();
			e.printStackTrace();
		} finally {
			System.setOut(printStreamOriginal);
		}
	}
	
	private Method getMethod() throws Exception {
		return target.getClass().getMethod(methodName, argClasses(args));
	}
	
	private String toString(Object obj) {
		if(obj == null)
			return "null";
		
		if(obj.getClass().isArray()) {
			ArrayList<Object> objectList = new ArrayList<Object>();
			objectList.addAll(Arrays.asList(obj));
			Object[] objs = objectList.toArray();
			String result = Arrays.deepToString(objs);
			return result.substring(1, result.length()-1);
		}
		
		return obj.toString();
	}
}
