package com.sethvargo.jester;

public class TestResult {
	private Object result;
	private boolean passed;
	
	public TestResult(Object result, boolean passed) {
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
