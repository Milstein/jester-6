package com.sethvargo.jester;

import java.lang.reflect.Method;

public class VerityTester implements Tester {
	public TestResult test(Object expectedResult, Object target, Method method, Object[] args) throws Exception {
		Object result = ((Boolean)method.invoke(target, args)).booleanValue();
		boolean passed = (expectedResult == result);

		return new TestResult(result, passed);
	}

}
