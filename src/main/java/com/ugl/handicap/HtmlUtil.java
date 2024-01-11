package com.ugl.handicap;

import java.util.List;

public class HtmlUtil {
    public final static String HTML_DOCTYPE = "<!DOCTYPE html><html lang=\"en-US\">";

    public final static String HTML_START = "<html>";

    public final static String HTML_END = "</html>";

    public final static String HTML_BODY = "<body>";

    public final static String HTML_BODY_END = "</body>";

    public final static String HTML_P = "<p>";

    public final static String HTML_P_END = "</p>";

    public final static String HTML_BREAK = "<br>";

    public final static String HTML_TABLE_START = "<table>";

    public final static String HTML_TABLE_END = "</table>";

    public final static String HTML_TABLE_ROW_START = "<tr>";

    public final static String HTML_TABLE_ROW_END = "</tr>";

    public final static String HTML_TABLE_COL_START = "<td>";

    public final static String HTML_TABLE_COL_END = "</td>";

    private static int tabIndex = 0;

    private static List<String> output = null;

    private final static String header[] = { "    <head>", "    <style>", "        table, td, tr {",
	    "            border: 1px solid black;", "            padding: 1px", "        }", "    </style>",
	    "    </head>" };

    static public void startHTML(List<String> out) {
	output = out;
	tabIndex = 0;
	output.add(HTML_DOCTYPE);
	output.add(HTML_START);
	for (String s : header) {
	    out.add(s);
	}
    }

    static public void endHTML() {
	output.add(HTML_END);
    }

    static public void bodyStartHTML() {
	tabIndex++;
	String tab = format(" ", tabIndex * 4);
	output.add(tab + HTML_BODY);
    }

    static public void bodyEndHTML() {
	String tab = format(" ", tabIndex * 4);
	output.add(tab + HTML_BODY_END);
	tabIndex--;
    }

    static public void paraStartHTML(String in) {

	String tab = format(" ", tabIndex * 4);
	output.add(tab + HTML_P + in + HTML_P_END);
    }

    static public void breakHTML() {
	String tab = format(" ", tabIndex * 4);
	output.add(tab + HTML_BREAK);
    }

    static public void tableStartHTML() {
	tabIndex++;
	String tab = format(" ", tabIndex * 4);
	output.add(tab + HTML_TABLE_START);
    }

    static public void tableEndHTML() {
	String tab = format(" ", tabIndex * 4);
	output.add(tab + HTML_TABLE_END);
	tabIndex--;
    }

    static public void tableRowStartHTML() {
	tabIndex++;
	String tab = format(" ", tabIndex * 4);
	output.add(tab + HTML_TABLE_ROW_START);
    }

    static public void tableRowEndHTML() {
	String tab = format(" ", tabIndex * 4);
	output.add(tab + HTML_TABLE_ROW_END);
	tabIndex--;
    }

    static public void tableColumnHTML(String cell) {
	String tab = format(" ", tabIndex * 4);
	output.add(tab + HTML_TABLE_COL_START + cell + HTML_TABLE_COL_END);
    }

    /**
     * Format size of string.
     * 
     * @param input
     * @param size
     * @return formated string.
     */
    static public String format(String input, int size) {
	if (size <= 0) {
	    return input;
	}
	char[] arrOutput = new char[size];
	char[] arrInput = input.toCharArray();

	// put spaces in array
	for (int index = 0; index < size; index++) {
	    arrOutput[index] = ' ';
	}

	// set the new array
	for (int index = 0;;) {
	    arrOutput[index] = arrInput[index];
	    index++;
	    if (index == size || index == arrInput.length) {
		break;
	    }
	}

	return String.valueOf(arrOutput);
    }

    public HtmlUtil() {
    }

}
