import java.lang.reflect.Method;

public interface Tester {
	public TestResult test(Object expectedValue, Object target, Method method, Object[] args) throws Exception;
}
