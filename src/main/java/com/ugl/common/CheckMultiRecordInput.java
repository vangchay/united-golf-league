package com.ugl.common;

///**
// * 
// */
//package com.common;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.List;
//
//import org.apache.commons.io.FileUtils;
//
///**
// * @author Vangchay Sayaovong
// *
// */
//public class CheckMultiRecordInput {
//
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		if (args.length > 0) {
//			String inputFile = args[0];
//			CheckMultiRecordInput check = new CheckMultiRecordInput();
//			if (check.checkForMultiCustomerTag(inputFile)) {
//				Cout.outString("is multicustomer input" + inputFile);
//			}
//		}
//	}
//
//	/**
//	 * Check the input file for Multi-Customer tags. The tag is in
//	 * <!FILE multiRecordFile=""/> The multiRecordFile attribute should be found in
//	 * the first few lines of the input file.
//	 * 
//	 * @param fullInputFileName
//	 */
//	private boolean checkForMultiCustomerTag(String fullInputFileName) {
//		try {
//			@SuppressWarnings("unchecked")
//			List<String> lines = FileUtils.readLines(new File(fullInputFileName), "UTF-8");
//			int size = lines.size();
//			for (int line = 0; line < size && line < 10; line++) {
//				String currentLine = lines.get(line);
//				if (currentLine.contains("<FILE")) {
//					String[] token = currentLine.split(" ");
//					int tokenSize = token.length;
//					for (int i = 0; i < tokenSize; i++) {
//						String currentToken = token[i];
//						if (currentToken.contains("multiRecordFile=")) {
//							return true;
//						}
//					}
//				}
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return false;
//	}
//
//}