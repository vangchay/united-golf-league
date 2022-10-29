package com.test;

import com.ugl.handicap.Calculate;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class testCalculate {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		// fail("Not yet implemented");
		double scores[] = { 88, 90, 94, 94, 108, 88, 92, 76, 94 };
		double handicaps[] = { 88, 90, 94, 94, 108, 88, 92, 76, 94 };
		double sum = Calculate.mean(scores);
		double med = Calculate.median(scores);
		double std = Calculate.standardDeviation(scores);
		String out = String.format("sum=%f, med=%f, std=%f", sum, med, std);
		System.out.print(out);

		int i = 0;
		for (double d : handicaps) {
			handicaps[i++] = Calculate.calculateHandicapIndex(71.2, 123, d);
		}
		sum = roundValue(Calculate.mean(handicaps));
		med = roundValue(Calculate.median(handicaps));
		std = roundValue(Calculate.standardDeviation(handicaps));
		out = String.format("\n sum=%f, med=%f, std=%f", sum, med, std);

		System.out.print(out);
	}

	private static double roundValue(double val) {
		double val2 = val * 100;
		val2 = Math.round(val2);
		val2 /= 100;
		return val2;
		// return (Math.round(val * 100)) / 100;
	}

}
