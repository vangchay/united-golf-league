package com.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ugl.common.Cout;
import com.ugl.handicap.Course;
import com.ugl.handicap.Hole;

/**
 * @author e1042631
 *
 */
public class testCourse {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void test() {
		List<Course> list = new ArrayList<Course>();
		// fail("Not yet implemented"); // TODO
		for (int i = 100; i > 0; i--) {
			Course test = new Course();
			test.setBack(30);
			test.setFront(30);
			test.setHoles(18);
			test.setName("course #" + Integer.toString(i));
			test.setPar(70);
			test.setRating(72);
			test.setSlope(120);
			test.setTee("blue");
			List<Hole> var = test.getCourseInfo();
			for (int h = 1; h <= 18; h++) {
				// int Number, int Par, int Yardage, int StrokeIndex
				Hole hl = new Hole(h, 4, 400, h);
				var.add(hl);
			}
			list.add(test);
		}
		// dump the course

		// sort
		Collections.sort(list);
		for (Course c : list) {
			Cout.outString(c.toString());
		}
	}

}
