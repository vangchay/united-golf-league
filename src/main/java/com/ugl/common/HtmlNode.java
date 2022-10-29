/**
 * 
 */
package com.ugl.common;

/**
 * @author e1042631
 *
 */
public class HtmlNode {
	public String Name = "";

	public StringBuilder Value = new StringBuilder();

	public String HeaderData = "";

	public String Line = "";

	public boolean hasXMLEnd;

	public String toString() {
		String out = "";
		out = String.format("<%s %s>%s</%s>", Name, HeaderData, Value.toString(), Name);
		return out;
	}

	public static HtmlNode ParseHtmlNode(String line) {
		HtmlNode n = new HtmlNode();
		line = line.trim();
		n.Line = line;
		char[] tbl = n.Line.toCharArray();
		int state = 0;
		int name1 = 0, name2 = 0, header1 = 0, header2 = 0, value1 = 0, value2 = 0;

		for (int i = 0; i < tbl.length; i++) {
			char c = tbl[i];
			switch (state) {
			case 0:
				if (c == '<') {

					name1 = i + 1;
					char c2 = tbl[name1];
					if (c2 == '/') {
						state = 0;
					} else {
						state++;
					}
				}
				break;
			case 1:

				if (c == ' ' || c == '>') {
					name2 = i;
					n.Name = line.substring(name1, name2);
					Cout.outString(n.Name);
					// if (n.Name.equals("h3")) {
					// cout.outString("h3");
					// }
					if (c == ' ') {
						state++;
						header1 = i + 1;
					} else {
						state = 3;
					}
				}
				break;
			case 2:
				if (c == '>') {
					header2 = i;
					value1 = i + 1;
					n.HeaderData = line.substring(header1, header2);
					state++;

					// testcode(vs)
					String xmlEnd = "</" + n.Name + ">";
					if (line.contains(xmlEnd)) {
						n.hasXMLEnd = true;
					} else {
						n.Value.append(line.substring(value1));
						state = -1;
					}
				}
				break;
			case 3: // value
				if (c == '<') {
					value2 = i;
					n.Value.append(line.substring(value1, value2));
					state++;
				}
				break;

			case -1:
			default:
				break;
			}
		}
		return n;
	}
}