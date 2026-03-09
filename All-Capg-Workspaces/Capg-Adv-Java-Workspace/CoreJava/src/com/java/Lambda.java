package com.java;

public abstract class Lambda  implements Person{

	public static void main(String[] args) {
		Person p1=()->{System.out.println("good food");
			};
	p1.eat();

}
}
