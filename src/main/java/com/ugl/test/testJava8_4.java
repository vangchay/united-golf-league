package com.ugl.test;

import java.util.Optional;

/**
 * Optional is a container object which is used to contain not-null objects.
 * Optional object is used to represent null with absent value. This class has
 * various utility methods to facilitate code to handle values as available or
 * not available instead of checking null values. It is introduced in Java 8
 * and is similar to what Optional is in Guava
 * 
 * @author e1042631
 *
 */
public class testJava8_4 {
    public static void sysOut(String s) {
	System.out.println(s);
    }

    public Integer sum(Optional<Integer> a, Optional<Integer> b) {
	// isPresent() checks the value is present or not
	sysOut("a is present " + a.isPresent());
	sysOut("b is present " + b.isPresent());

	// orElse() returns the value of the present otherwise returns the default value
	Integer value1 = a.orElse(new Integer(0));
	Integer value2 = b.orElse(new Integer(0));
	return value1 + value2;
    }

    public static void main(String[] args) {
	testJava8_4 test84 = new testJava8_4();
	Integer value1 = null;
	Integer value2 = new Integer(20);
	Optional<Integer> a = Optional.ofNullable(value1);
	Optional<Integer> b = Optional.ofNullable(value2);
	Integer result = test84.sum(a, b);
	sysOut(result.toString());

    }

}