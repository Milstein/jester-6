import java.lang.reflect.Method;

public class EqualityTester implements Tester {
	public TestResult test(Object expectedResult, Object target, Method method, Object[] args) throws Exception {
		if(expectedResult == null)
			throw new IllegalArgumentException("expectedResult cannot be null. Use assertNull(...) instead!");
		
		Object result = method.invoke(target, args);
		Boolean passed = expectedResult.equals(result);
		return new TestResult(result, passed);
	}
}