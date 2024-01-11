package com.ugl.common;

import java.awt.Font;

/**
 * @author e1042631
 *
 */
public class ToolCSS {
    public static final String VERSION = "3.02_20161130";

    /**
     * 
     */
    public ToolCSS() {
	// TODO Auto-generated constructor stub
    }

    public static Font getTitleFont() {
	return new Font("Tahoma", Font.BOLD, 14);
    }

    public static Font getFont() {
	return new Font("Tahoma", Font.PLAIN, 14);
    }

    public static Font getDebugFont() {
	return new Font("Courier New", Font.PLAIN, 16);
    }
}
