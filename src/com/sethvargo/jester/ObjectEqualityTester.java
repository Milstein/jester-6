package com.sethvargo.jester;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class ObjectEqualityTester implements Tester {
	private String[] userTypes;
	
	public ObjectEqualityTester(String[] userTypes) {
		this.userTypes = userTypes;
	}
	
	public TestResult test(Object expectedResult, Object target) throws Exception {
		if(expectedResult == null)
			return new TestResult(expectedResult == target, expectedResult == target);

		boolean passed = false;
		if(expectedResult.getClass().isPrimitive()) {
			// primitives are compared using ==
			passed = expectedResult == target;
		} else if(expectedResult instanceof boolean[]) {
			passed = Arrays.equals((boolean[])expectedResult, (boolean[])target);
		} else if(expectedResult instanceof byte[]) {
			passed = Arrays.equals((byte[])expectedResult, (byte[])target);
		} else if(expectedResult instanceof char[]) {
			passed = Arrays.equals((char[])expectedResult, (char[])target);
		} else if(expectedResult instanceof short[]) {
			passed = Arrays.equals((short[])expectedResult, (short[])target);
		} else if(expectedResult instanceof int[]) {
			passed = Arrays.equals((int[])expectedResult, (int[])target);
		} else if(expectedResult instanceof long[]) {
			passed = Arrays.equals((long[])expectedResult, (long[])target);
		} else if(expectedResult instanceof float[]) {
			passed = Arrays.equals((float[])expectedResult, (float[])target);
		} else if(expectedResult instanceof double[]) {
			passed = Arrays.equals((double[])expectedResult, (double[])target);
		} else if(expectedResult instanceof Object[]) {
			// could be a multi-nested array
			passed = Arrays.deepEquals((Object[]) expectedResult, (Object[]) target);
		} else if(expectedResult instanceof Collection) { 
			// mainly for ArrayList<E> return types
			// where E is an (unknown) user-defined type.
			// Java sees result as an ArrayList<Object> and calls the Object.equals function
			// we can't cast because we don't know E.
			// use reflection to make sure the right method is being called.
			Collection<?> exp = (Collection<?>)expectedResult;
			Collection<?> res = (Collection<?>)target;
			passed = deepEquals(exp, res);
		} else {
			passed = userEquals(expectedResult, target);
		}

		return new TestResult(target, passed);
	}

	public TestResult test(Object expectedValue, Object target, Method method, Object[] args) throws Exception {
		throw new RuntimeException("You cannot use those parameters with this kind of tester!");
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
