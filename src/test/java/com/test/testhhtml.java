package com.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ugl.common.Cout;
import com.ugl.handicap.HtmlUtil;

/**
 * @author e1042631
 *
 */
public class testhhtml extends HtmlUtil {

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
		// fail("Not yet implemented"); // TODO
		List<String> str = new ArrayList<String>();
		HtmlUtil.startHTML(str);
		HtmlUtil.endHTML();
		HtmlUtil.bodyStartHTML();
		HtmlUtil.bodyEndHTML();
		HtmlUtil.paraStartHTML("test");
		HtmlUtil.breakHTML();
		HtmlUtil.tableStartHTML();
		HtmlUtil.tableEndHTML();
		HtmlUtil.tableRowStartHTML();
		HtmlUtil.tableRowEndHTML();
		HtmlUtil.tableColumnHTML("cell");
		str.add(HtmlUtil.format("this is a test", 5));
		str.add(HtmlUtil.format("this is a test", 7));

		for (String s : str) {
			Cout.outString(s);
		}

	}

}
