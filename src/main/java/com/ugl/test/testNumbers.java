package com.ugl.test;

//package test;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Set;
//
//import org.apache.commons.io.FileUtils;
//
//import com.MEUtils;
//import com.common.cout;
//import com.google.common.collect.Multimap;
//
///**
// * @author e1042631
// *
// */
//public class testNumbers {
//	private static int NotMatched = 0;
//
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		HashMap<Integer, Integer> secondary = new HashMap<Integer, Integer>();
//		HashMap<Integer, Integer> primary = new HashMap<Integer, Integer>();
//		Multimap<String, String> mapArgs = MEUtils.ScanArguments(args);
//		String primFile = mapArgs.get("p").iterator().next();
//		String secFile = mapArgs.get("s").iterator().next();
//		readInFile(primFile, primary);
//		readInFile(secFile, secondary);
//		cout.outString("Primary Size : " + Integer.toString(primary.size()));
//		cout.outString("Scondary Size : " + Integer.toString(secondary.size()));
//		ComareColl(primary, secondary);
//		ComareColl(secondary, primary);
//		cout.outString("Total Number of fields not matched: " + Integer.toString(NotMatched));
//		genReport(primary, "priamry report");
//		genReport(secondary, "secondary report");
//		cout.outString("bye!");
//	}
//
//	private static void genReport(HashMap<Integer, Integer> coll, String header) {
//		cout.outString("******************Report " + header);
//		Set<Integer> keys = coll.keySet();
//		for (Integer i : keys) {
//			if (coll.get(i) == -1) {
//				cout.outString("    Not matched Integer " + i.toString());
//			}
//		}
//	}
//
//	private static void ComareColl(HashMap<Integer, Integer> src, HashMap<Integer, Integer> dest) {
//		Set<Integer> coll = src.keySet();
//		for (Integer i : coll) {
//			if (dest.get(i) != null) {
//				src.put(i, i);
//			} else {
//				cout.outString(" Int not found : " + i.toString());
//				NotMatched++;
//			}
//		}
//	}
//
//	private static void readInFile(String inputFile, HashMap<Integer, Integer> collection) {
//		try {
//			List<String> lines = FileUtils.readLines(new File(inputFile));
//			for (String s : lines) {
//				s = s.trim();
//				if (s.isEmpty() == false) {
//					try {
//						int i = Integer.parseInt(s);
//						collection.put(new Integer(i), new Integer(-1));
//					} catch (Exception ex) {
//						ex.printStackTrace();
//					}
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}
//
//}
