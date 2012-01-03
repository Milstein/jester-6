import java.lang.reflect.*;

public class ExceptionTester implements Tester {
	public TestResult test(Object expectedResult, Object target, Method method, Object[] args) throws Exception {
		Object result;
		Boolean passed = false;
		try {
			result = method.invoke(target, args);
		} catch(InvocationTargetException e) {
			Object targetException = e.getTargetException();
			result = targetException;
			passed = targetException.getClass() == expectedResult.getClass();
		}
		
		return new TestResult(result, passed);
	}
}
