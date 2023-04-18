package com.test;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ugl.handicap.HcpUtils;

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
		double sum = HcpUtils.mean(scores);
		double med = HcpUtils.median(scores);
		double std = HcpUtils.standardDeviation(scores);
		String out = String.format("sum=%f, med=%f, std=%f", sum, med, std);
		System.out.print(out);

		int i = 0;
		for (double d : handicaps) {
			handicaps[i++] = HcpUtils.calculateHandicapIndex(71.2, 123, d);
		}
		sum = HcpUtils.roundValue(HcpUtils.mean(handicaps));
		med = HcpUtils.roundValue(HcpUtils.median(handicaps));
		std = HcpUtils.roundValue(HcpUtils.standardDeviation(handicaps));
		out = String.format("\n sum=%f, med=%f, std=%f", sum, med, std);

		System.out.print(out);
	}

}
