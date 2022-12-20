/**
 * 
 */
package com.ugl.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * @author lily
 *
 */
public class CodeGen {

	private final static String CLASS_NAME = "codeGen.";

	private final static String MAIN = CLASS_NAME + "main()";

	private final static String ARG1 = "ARG1: BaseServiceElement code generated CSV file";

	private final static String ARG2 = "ARG2: Replacement template CSV file";

	private final static String ARG3 = "ARG3: Code template file";

	private static final int NUM_ARGS = 3;

	private static final String COMMENT = "//";

	private static String generatedCSVFile;

	private static String replaceCSVFile;

	private static String codeTemplateFile;

	private static String outputTempFile;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Cout.outString(MAIN);
		Cout.outString(ARG1);
		Cout.outString(ARG2);
		Cout.outString(ARG3);
		if (args.length >= NUM_ARGS) {
			generatedCSVFile = args[0];
			List<BaseServiceElement> elements = parseGenFile(generatedCSVFile);
			replaceCSVFile = args[1];
			List<CodeReplaceItem> replaceMappings = parseReplaceFile(replaceCSVFile);
			codeTemplateFile = args[2];
			outputTempFile = codeTemplateFile + "_outputCodeGen.txt";
			List<String> out = new ArrayList<String>();
			try {
				List<String> codeLines = FileUtils.readLines(new File(codeTemplateFile));
				for (String line : codeLines) {
					line = parseTemplateLine(line, replaceMappings, elements);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Cout.outString("Not enough arguments.");
		}
		Cout.outString("Bye!");
	}

	/**
	 * 
	 * @param line
	 * @param replaceValues
	 * @param elements
	 * @return
	 */
	public static String parseTemplateLine(String line, List<CodeReplaceItem> replaceValues,
	        List<BaseServiceElement> elements) {

		String orgLine = line;

		elements.forEach(element -> {
			// String curLine = new String(line);
			System.out.println(element);

		});
		for (CodeReplaceItem item : replaceValues) {
			line = line.replace(item.item, item.value);
		}
		return line;
	}

	/**
	 * Generate the replacement items...
	 * 
	 * @param replaceCsvFile
	 * @return
	 */
	public static List<CodeReplaceItem> parseReplaceFile(String replaceCsvFile) {
		List<CodeReplaceItem> outItems = new ArrayList<CodeReplaceItem>();
		try {
			List<String> lines = FileUtils.readLines(new File(replaceCsvFile));
			for (String line : lines) {
				if (line.isEmpty() || line.startsWith(COMMENT)) {
					continue;
				}
				String[] csvLine = line.split(",");
				CodeReplaceItem item = new CodeReplaceItem();
				item.item = csvLine[0];
				item.value = csvLine[1];
				outItems.add(item);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return outItems;
	}

	public static BaseServiceElement FindElement(int element, List<BaseServiceElement> listFields) {
		for (BaseServiceElement field : listFields) {
			if (field.id == element) {
				return field;
			}
		}
		return null;
	}

	public static BaseServiceElement findBaseServiceElement(List<BaseServiceElement> fields, int id, String className) {
		for (BaseServiceElement field : fields) {
			if (field.id == id && field.objectName.equals(className)) {
				return field;
			}
		}
		return null;
	}

	/**
	 * Read and parse the CSV file name...
	 * 
	 * @param csvFileName
	 * @return
	 */
	public static List<BaseServiceElement> parseGenFile(String csvFileName) {
		List<BaseServiceElement> outSvc = new ArrayList<BaseServiceElement>();
		try {
			List<String> lines = FileUtils.readLines(new File(csvFileName));
			for (String line : lines) {
				if (line.isEmpty()) {
					continue;
				}
				String[] csvLine = Utils.parseRecString(line);// line.split(",");
				BaseServiceElement base = new BaseServiceElement();
				base.id = Integer.parseInt(csvLine[0]);
				base.businessName = csvLine[1];
				base.shortDescription = csvLine[2];
				base.type = csvLine[3].toLowerCase();
				base.len = Integer.parseInt(csvLine[4].trim());
				base.dec = Integer.parseInt(csvLine[5].trim());
				base.inputOutput = csvLine[6];
				base.required = csvLine[8];
				base.name = csvLine[9];
				base.typeDefinition = csvLine[11];

				if (csvLine.length >= 13) {
					base.objectName = csvLine[12]; // set the structure name...
				}
				if (csvLine.length >= 14) {
					base.longDescription = csvLine[13]; // set the extended description...
				}

				base.show();
				if (base.id == 0) {
					continue;
				}
				outSvc.add(base);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outSvc;
	}

}
