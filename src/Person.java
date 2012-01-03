public class Person {
	private String firstName;
	private String lastName;
	private int age;
	
	public Person(String name, int age) {
		String[] split = name.split(" ", 2);
		this.firstName = split[0];
		this.lastName = split[1];
		this.age = age;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public int getAge() {
		return this.age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public Object getNull() {
		return null;
	}
	
	public void throwSomething() {
		throw new IllegalArgumentException();
	}
	
	public void dontThrowSomething() {
		System.out.println("I clutter output!");
		System.out.println("I clutter output!");
		System.out.println("I clutter output!");
		System.out.println("I clutter output!");
		System.out.println("I clutter output!");
		System.out.println("I clutter output!");
		System.out.println("I clutter output!");
		System.out.println("I clutter output!");
		System.out.println("I clutter output!");
		System.out.println("I clutter output!");
		System.out.println("I clutter output!");
		System.out.println("I clutter output!");
		System.out.println("I clutter output!");
		System.out.println("I clutter output!");
	}
}
