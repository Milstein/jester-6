package com.sethvargo.jester;
import java.lang.reflect.Method;
import java.util.Arrays;

public class NotEqualityTester implements Tester {
	public TestResult test(Object expectedResult, Object target, Method method, Object[] args) throws Exception {
		if(expectedResult == null)
			throw new IllegalArgumentException("expectedResult cannot be null. Use assertNotNull(...) instead!");
		
		Object result = method.invoke(target, args);
		Boolean passed;
		
		if(expectedResult instanceof Object []) {
			// it's an array, so use a different equals
			passed = !Arrays.deepEquals((Object[]) expectedResult, (Object[]) result);
		} else {
			passed = !expectedResult.equals(result);
		}
		
		return new TestResult(result, passed);
	}
}