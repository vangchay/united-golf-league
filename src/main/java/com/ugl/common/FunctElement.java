/**
 * 
 */
package com.ugl.common;

/**
 * @author e1042631
 *
 */
public class FunctElement {
	private Integer Element;

	private String Description;

	public FunctElement(int id, String desc) {
		Element = new Integer(id);
		Description = desc;
	}

	/**
	 * 
	 */
	public FunctElement() {
		// TODO Auto-generated constructor stub
	}

	public Integer getElement() {
		return Element;
	}

	public void setElement(Integer element) {
		Element = element;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

}