/**
 * This is a base service element which is 1 line of a CSV file.
 * @author Vangchay
 * @version 1.0.0 3/12/2022
 */
package com.ugl.common;

/**
 * @author Vangchay Sayaovong
 *
 */
public class BaseServiceElement {
	public static final String TAG_ID = "##ID##";
	public static final String TAG_BUSINESSNAME = "##BUSINESSNAME##";
	public static final String TAG_SHORTDESCRIPTION = "##SHORTDESCRIPTION##";
	public static final String TAG_TYPE = "##TYPE##";
	public static final String TAG_LEN = "##LEN##";
	public static final String TAG_DEC = "##DEC##";
	public static final String TAG_INPUTOUTPUT = "##INPUTOUTPUT##";
	public static final String TAG_FUNCTIONNAME = "##FUNCTIONNAME##";
	public static final String TAG_REQUIRED = "##REQUIRED##";
	public static final String TAG_MISC1 = "##MISC1##";
	public static final String TAG_NAME = "##NAME##";
	public static final String TAG_TYPEDEFINITION = "##TYPEDEFINITION##";
	public static final String TAG_OBJECTNAME = "##OBJECTNAME##";
	public static final String TAG_LONGDESCRIPTION = "##LONGDESCRIPTION##";
	public static final String CSV_HEADER = "//id,businessName,shortDescription,type,len,dec,inputOutput,functionName,required,misc1,name,typeDefinition,objectName,longDescription";

	/**
	 * ID assigned to element...
	 */
	public int id = 0;

	/**
	 * A short description or summary...
	 */
	public String shortDescription = "";

	/**
	 * A type (long, double, int, or string)...
	 */
	public String type = "string";

	/**
	 * I, O or IO...
	 */
	public String inputOutput = "IO";

	/**
	 * Name of the element...
	 */
	public String name = "";

	/**
	 * The object name in which this element belongs too...
	 */
	public String objectName = "";

	/**
	 * The data length...
	 */
	public int len = 0;

	/**
	 * The number of decimals..
	 */
	public int dec = 0;

	/**
	 * Required field?... Y/N
	 */
	public String required = "N";

	/**
	 * The business name...
	 */
	public String businessName = "";

	/**
	 * The type definition (string or numeric)...
	 */
	public String typeDefinition = "string";

	/**
	 * The long description...
	 */
	public String longDescription = "";

	/**
	 * Function name...
	 */
	public String functionName = "";

	/**
	 * MISC user defined value...
	 */
	public String misc1 = "";

	/**
	 * Replace the "Tag" with the base service element data...
	 * 
	 * @param tag
	 * @param inputLine
	 * @param element
	 * @return
	 */
	public static String replaceValue(String tag, String inputLine, BaseServiceElement element) {
		String responseVal = null;
		if (tag.equals(TAG_ID)) {
			responseVal = inputLine.replace(TAG_ID, Integer.toString(element.id));
		} else if (tag.equals(TAG_BUSINESSNAME)) {
			responseVal = inputLine.replace(TAG_BUSINESSNAME, element.businessName);
		} else if (tag.equals(TAG_SHORTDESCRIPTION)) {
			responseVal = inputLine.replace(TAG_SHORTDESCRIPTION, element.shortDescription);
		} else if (tag.equals(TAG_TYPE)) {
			responseVal = inputLine.replace(TAG_TYPE, element.type);
		} else if (tag.equals(TAG_LEN)) {
			responseVal = inputLine.replace(TAG_LEN, Integer.toString(element.len));
		} else if (tag.equals(TAG_DEC)) {
			responseVal = inputLine.replace(TAG_DEC, Integer.toString(element.dec));
		} else if (tag.equals(TAG_INPUTOUTPUT)) {
			responseVal = inputLine.replace(TAG_INPUTOUTPUT, element.inputOutput);
		} else if (tag.equals(TAG_FUNCTIONNAME)) {
			responseVal = inputLine.replace(TAG_FUNCTIONNAME, element.functionName);
		} else if (tag.equals(TAG_REQUIRED)) {
			responseVal = inputLine.replace(TAG_REQUIRED, element.required);
		} else if (tag.equals(TAG_MISC1)) {
			responseVal = inputLine.replace(TAG_MISC1, element.misc1);
		} else if (tag.equals(TAG_NAME)) {
			responseVal = inputLine.replace(TAG_NAME, element.name);
		} else if (tag.equals(TAG_TYPEDEFINITION)) {
			responseVal = inputLine.replace(TAG_TYPEDEFINITION, element.typeDefinition);
		} else if (tag.equals(TAG_OBJECTNAME)) {
			responseVal = inputLine.replace(TAG_OBJECTNAME, element.objectName);
		} else if (tag.equals(TAG_LONGDESCRIPTION)) {
			responseVal = inputLine.replace(TAG_LONGDESCRIPTION, element.longDescription);
		} else {
			responseVal = inputLine;
		}
		return responseVal;
	}

	public String toString() {
		String msg = String.format("%d,%s,%s,%s,%d,%d,%s,%s,%s,%s,%s,%s,%s,%s", id, businessName, shortDescription,
				type, len, dec, inputOutput, functionName, required, misc1, name, typeDefinition, objectName,
				longDescription);
		return msg;
	}

	public void show() {
		String msg = String.format("id=%d, description=%s, type=%s, io=%s name =%s", id, shortDescription, type,
				inputOutput, name);
		System.out.println(msg);
	}
}