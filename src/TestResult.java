
public class TestResult {
	private Object result;
	private Boolean passed;
	
	public TestResult(Object result, Boolean passed) {
		this.result = result;
		this.passed = passed;
	}
	
	public Object getResult() {
		return this.result;
	}
	
	public Boolean passed() {
		return this.passed;
	}
}
