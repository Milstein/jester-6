package com.sethvargo.jester;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class NotEqualityTester implements Tester {
	private String[] userTypes;
	
	public NotEqualityTester(String userTypes[]) {
		this.userTypes = userTypes;
	}
	
	public TestResult test(Object expectedResult, Object target, Method method, Object[] args) throws Exception {
		if(expectedResult == null)
			throw new IllegalArgumentException("expectedResult cannot be null. Use assertNotNull(...) instead!");

		Object result = method.invoke(target, args);
		boolean passed;
		
		if(expectedResult.getClass().isPrimitive()) {
			// primitives are compared using !=
			passed = expectedResult != result;
		} else if(expectedResult instanceof boolean[]) {
			passed = !Arrays.equals((boolean[])expectedResult, (boolean[])result);
		} else if(expectedResult instanceof byte[]) {
			passed = !Arrays.equals((byte[])expectedResult, (byte[])result);
		} else if(expectedResult instanceof char[]) {
			passed = !Arrays.equals((char[])expectedResult, (char[])result);
		} else if(expectedResult instanceof short[]) {
			passed = !Arrays.equals((short[])expectedResult, (short[])result);
		} else if(expectedResult instanceof int[]) {
			passed = !Arrays.equals((int[])expectedResult, (int[])result);
		} else if(expectedResult instanceof long[]) {
			passed = !Arrays.equals((long[])expectedResult, (long[])result);
		} else if(expectedResult instanceof float[]) {
			passed = !Arrays.equals((float[])expectedResult, (float[])result);
		} else if(expectedResult instanceof double[]) {
			passed = !Arrays.equals((double[])expectedResult, (double[])result);
		} else if(expectedResult instanceof Object[]) {
			// could be a multi-nested array
			passed = Arrays.deepEquals((Object[]) expectedResult, (Object[]) result);
		} else if(expectedResult instanceof Collection) { 
			// mainly for ArrayList<E> return types
			// where E is an (unknown) user-defined type.
			// Java sees result as an ArrayList<Object> and calls the Object.equals function
			// we can't cast because we don't know E.
			// use reflection to make sure the right method is being called.
			Collection<?> exp = (Collection<?>)expectedResult;
			Collection<?> res = (Collection<?>)result;
			passed = deepEquals(exp, res);
		} else {
			passed = userEquals(expectedResult, result);
		}

		return new TestResult(result, passed);
	}
	
	private boolean userEquals(Object expected, Object result) throws Exception	{
		for(int i = 0; i < this.userTypes.length; i++) {
			String type = this.userTypes[i];
			
			if(Class.forName(type).isInstance(expected)) {
				try	{
					Method equals = Class.forName(type).getMethod("equals", new Class[]{Class.forName(type)});
					boolean r = ((Boolean)equals.invoke(expected, result)).booleanValue();
					return r;
				} catch(Exception e) {
					Method equals = Class.forName(type).getMethod("equals", new Class[]{Class.forName("java.lang.Object")});
					return ((Boolean)equals.invoke(expected, result)).booleanValue();
				}
			}
		}
		
		// not equivalent to a user type
		return expected.equals(result);
	}
	
	private boolean deepEquals(Collection<?> exp, Collection<?> res) throws Exception {
		if(exp == null && res == null)
			return true;
		
		if((exp == null && res != null) || (exp != null && res == null))
			return false;
		
		if(exp.size() == 0 && res.size() == 0)
			return true;
		
		if(exp.size() != res.size())
			return false;
		
		Object next = exp.iterator().next();
		
		if(Class.forName("java.util.Collection").isInstance(next)) {
			Iterator<?> i = exp.iterator();
			Iterator<?> j = exp.iterator();
			
			while(i.hasNext() && j.hasNext()) {
				if(!deepEquals((Collection<?>)i.next(), (Collection<?>)j.next())) {
					return false;
				}
			}
			
			return true;
		} 
		
		
		if(this.userTypes != null) {
			Iterator<?> i = exp.iterator();
			Iterator<?> j = exp.iterator();

			while(i.hasNext() && j.hasNext()) {
				if(!userEquals(i.next(), j.next())) {
					return false;
				}
			}
			
			return true;
		} 
		
		return exp.equals(res);
	}
}