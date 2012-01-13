package com.sethvargo.jester;
import java.lang.reflect.Method;
import java.util.Arrays;

public class NotEqualityTester implements Tester {
	public TestResult test(Object expectedResult, Object target, Method method, Object[] args) throws Exception {
		if(expectedResult == null)
			throw new IllegalArgumentException("expectedResult cannot be null. Use assertNotNull(...) instead!");

		Object result = method.invoke(target, args);
		boolean passed;
		
		if(expectedResult.getClass().isPrimitive()) {
			// primitives are compared using !=
			passed = expectedResult != result;
		} else if(expectedResult instanceof boolean[]) {
			passed = !Arrays.equals((boolean[])expectedResult, (boolean[])result);
		} else if(expectedResult instanceof byte[]) {
			passed = !Arrays.equals((byte[])expectedResult, (byte[])result);
		} else if(expectedResult instanceof char[]) {
			passed = !Arrays.equals((char[])expectedResult, (char[])result);
		} else if(expectedResult instanceof short[]) {
			passed = !Arrays.equals((short[])expectedResult, (short[])result);
		} else if(expectedResult instanceof int[]) {
			passed = !Arrays.equals((int[])expectedResult, (int[])result);
		} else if(expectedResult instanceof long[]) {
			passed = !Arrays.equals((long[])expectedResult, (long[])result);
		} else if(expectedResult instanceof float[]) {
			passed = !Arrays.equals((float[])expectedResult, (float[])result);
		} else if(expectedResult instanceof double[]) {
			passed = !Arrays.equals((double[])expectedResult, (double[])result);
		} else if(expectedResult instanceof Object[]) {
			// could be a multi-nested array
			passed = !Arrays.deepEquals((Object[]) expectedResult, (Object[]) result);
		} else {
			// default to equals
			passed = !expectedResult.equals(result);
		}

		return new TestResult(result, passed);
	}
}