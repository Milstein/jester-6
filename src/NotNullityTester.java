import java.lang.reflect.Method;

public class NotNullityTester implements Tester {
	public TestResult test(Object expectedResult, Object target, Method method, Object[] args) throws Exception {
		Object result = method.invoke(target, args);
		Boolean passed = result != expectedResult;
		return new TestResult(result, passed);
	}
}
