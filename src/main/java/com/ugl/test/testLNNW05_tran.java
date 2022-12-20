package com.ugl.test;

//package test;
//
//import static org.junit.Assert.fail;
//
//import java.util.List;
//import java.util.function.Consumer;
//
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import IntuitionServiceProviders.Utility;
//import ibs.com.fis.cw.pub.cons.LNNNW05_tran;
//
///**
// * @author e1042631
// *
// */
//public class testLNNW05_tran extends LNNNW05_tran {
//
//	private final static String CLASS_NAME = "testLNNW05_tran.";
//
//	/**
//	 * Test Staging file.
//	 */
//	private final static String CONSUMER_INPUT_FILE = "c:\\users\\e1042631\\desktop\\stage\\org.txt";
//
//	/**
//	 * Consumers.
//	 */
//	private static List<Consumer> testConsumers = null;
//
//	/**
//	 * LNNNW05_tran
//	 */
//	private static LNNNW05_tran testLNNNW05_tran = null;
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@BeforeClass
//	public static void setUpBeforeClass() throws Exception {
//	}
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@AfterClass
//	public static void tearDownAfterClass() throws Exception {
//	}
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@Before
//	public void setUp() throws Exception {
//		String[] args = new String[1];
//		args[0] = CONSUMER_INPUT_FILE;
//		Consumer.main(args);
//		testConsumers = Consumer.getInstance();
//		for (Consumer c : testConsumers) {
//			testLNNNW05_tran = new LNNNW05_tran();
//			testLNNNW05_tran.set(c);
//			Utility.showError(CLASS_NAME, testLNNNW05_tran.debug());
//		}
//	}
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@After
//	public void tearDown() throws Exception {
//	}
//
//	@Test
//	public void test() {
//		fail("Not yet implemented");
//	}
//
//}
