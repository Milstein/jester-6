import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class NotMatchTester implements Tester {
	public TestResult test(Object expectedResult, Object target, Method method, Object[] args) throws Exception {
		Object result = method.invoke(target, args);
		Boolean passed;
		
		try {
			passed = !Pattern.matches((String) expectedResult, (CharSequence) result);
		} catch(ClassCastException e) {
			passed = false;
		}
		
		return new TestResult(result, passed);
	}
}
