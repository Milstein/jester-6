package com.sethvargo.jester;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NotExceptionTester implements Tester {
	public TestResult test(Object expectedResult, Object target, Method method, Object[] args) throws Exception {
		try {
			Object result = method.invoke(target, args);
			return new TestResult(result, true);
		} catch(InvocationTargetException e) {
			return new TestResult(null, false);
		}
	}
}
