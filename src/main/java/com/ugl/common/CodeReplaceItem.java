/**
 * 
 */
package com.ugl.common;

/**
 * @author e1042631
 *
 */
public class CodeReplaceItem {
    public String item = "";
    public String value = "";

    public String toString() {
	return String.format("CodeReplaceItem,%s,%s", item, value);
    }
}