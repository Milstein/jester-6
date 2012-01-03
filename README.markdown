Jester
======
Jester is a Java micro Unit-Testing framework. I hate JUnit. It's too large and bloated for simple cases. Furthermore, it's poorly documented (in terms of usage and installation). I wanted a drop-in Unit-Testing framework that the most novice Java developers could use without thinking.

Fore-warnings
-----------
I'm sure that JUnit (or other frameworks) are more efficient. If you're writing code where testing time plays a major factor, then you probably shouldn't be using Jester. That being said, if you want to fork and submit a speed enhancement patch, I'll mostly likely accept.

This is still in early development. Most is undocumented at the moment, but eventually it will be. I promise.

It's currently just source. Eventually it will be a package.

Installation
------------
For now, just clone and create your own package. I have yet to package Jester.

Usage
-----
 - Create a public class named `[Model]Tester`

```java
public class PersonTester { ... }
```
 
 - Extend `TestSuite`

```java
public class PersonTester extends TestSuite { ... }
```

 - Add the `main(String args[])` method

```java
public static void main(String args[]) {
  // this must match the name of the current class
  runTests(new PersonTester());
}
```

 - Write tests! For a full list of methods and documentation, see the [Wiki](wiki).

Features
--------
 - Super simple setup (see above)
 - Suppresses output from tested classes (great if testing other people's code)
 - Simple class methods (assert*) make a great DSL
 - Highly extensible (just create a new class that implements Tester)
 - Setup/TearDown methods