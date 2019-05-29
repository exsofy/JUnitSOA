package com.tcua.junit.soa.handler;

import static org.junit.Assert.assertEquals;

import org.w3c.dom.Element;
import org.xml.sax.Attributes;

public abstract class ValueHandler implements ISOAClassHandler {

	public ValueHandler() {
		super();
	}

	@Override
	public boolean extend(Element parent, Object obj) {
		if (obj == null) {
			// manage null value
			parent.setAttribute("isNull", "true");
			return true;
		}

		return false;
	}

	public static boolean isValueNull(Object object, Attributes attributes) {
	
		int isNullIndex = attributes.getIndex("isNull");
		if (isNullIndex >= 0) {
			if (Boolean.valueOf(attributes.getValue(isNullIndex))) {
				// is null true
				assertEquals("NULL value", null, object);
				return true;
			}
		}
		return false;
	}

}