/**
 * 
 */
package com.test;

import com.ugl.common.BaseServiceElement;

import org.junit.Test;

/**
 * @author lily
 *
 */
public class TestBaseServiceElement extends BaseServiceElement {

	@Test
	public void test() {
		BaseServiceElement element = new BaseServiceElement();
		element.id = 999;
		element.businessName = "test base service element";
		element.len = 10;
		element.dec = 0;
		element.inputOutput = "I";
		element.longDescription = "base service element test long description";
		element.functionName = "myFunctionName";
		element.misc1 = "misc1";
		element.name = "baseId";
		element.required = "N";
		element.type = "long";
		element.typeDefinition = "numeric";
		element.objectName = "myObject";
		element.functionName = "myFunction";
		element.shortDescription = "short description";
		System.out.println(BaseServiceElement.CSV_HEADER);
		System.out.println(element.toString());
		element.show();
	}

}
