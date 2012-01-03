Jester
======
Jester is a Java micro Unit-Testing framework. I hate JUnit. It's too large and bloated for simple cases. Furthermore, it's poorly documented (in terms of usage and installation). I wanted a drop-in Unit-Testing framework that the most novice Java developers could use without thinking.

Fore-warnings
-----------
I'm sure that JUnit (or other frameworks) are more efficient. If you're writing code where testing time plays a major factor, then you probably shouldn't be using Jester. That being said, if you want to fork and submit a speed enhancement patch, I'll mostly likely accept.

This is still in early development. Most is undocumented at the moment, but eventually it will be. I promise.

It's currently just source. Eventually it will be a package.

Usage
-----
Imagine you have a simple class that represents a `Person` object:

```java
public class Person {
	public Person(String name, int age) { ... }
	
	public String getFirstName() { ... }
	public void setFirstName(String firstName) { ... }
	public String getLastName() { ... }
	public void setLastName(String lastName) { ... }
	public int getAge() { ... }
	public void setAge(int age) { ... }
```

It's pretty basic, and I've removed all the method bodies for readability. Supposed you have a bazillion Exceptions that you need to test for. It would be very cumbersome to surround each test with a `try-catch` block. Do I need to check if the constructor throws an exception? What about a NullPointerException? IllegalArgumentException? So many unanswered questions!

With Jester, just write happy code (remember, I'm a Ruby developer - forced to write Java in my spare time):

```java
public class PersonTester extends TestSuite {
	private Person seth;
	private Person joe;
	
	public void setup() {
		seth = new Person("Seth Vargo", 20);
		joe = new Person("Joe Frick", 21);
	}
	
	public void testFirstName() {
		assertEqual("Seth", seth, "getFirstName");
		assertEqual("Joe", joe, "getFirstName");
		
		assertNotEqual("Seth", joe, "getFirstName");
		assertNotEqual("Joe", seth, "getFirstName");
	}
	
	public void testLastName() {
		assertEqual("Vargo", seth, "getLastName");
		assertEqual("Frick", joe, "getLastName");
	}
	
	public void tearDown() {
		seth = null;
		joe = null;
	}
	
	public static void main(String args[]) {
		runTests(new PersonTester());
	}
}
```

Run the class and you'll get nice, pretty-printed output. A full list of available assertions can be found in the source or in the document I have yet to create.

Features
--------
 - Super simple setup
 - Suppresses output from tested classes (great if testing other people's code)
 - Simple class methods (assert*) make a great DSL
 - Highly extensible (just create a new class that implements Tester)
 - Setup/TearDown methods