package com.ugl.test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author e1042631 Nashorn javascript - jjs to execute javascript from the
 *         console.
 * 
 */
public class testJava8_5 {
	public static void sysOut(String s) {
		System.out.println(s);
	}

	/**
	 * @param args Calling javascript from Java.
	 */
	public static void main(String[] args) {
		ScriptEngineManager engManager = new ScriptEngineManager();
		ScriptEngine nashorn = engManager.getEngineByName("nashorn");
		String name = "vangchay";
		Integer result = null;

		try {
			nashorn.eval("print('" + name + "')");
			result = (Integer) nashorn.eval("10 + 2");

		} catch (ScriptException ex) {
			ex.printStackTrace();
		}
		sysOut(result.toString());
	}

}