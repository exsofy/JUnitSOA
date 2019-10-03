package com.tcua.junit.soa.handler;

import static org.junit.Assert.assertEquals;

import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

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

	public boolean isValueNull(Object object, Attributes attributes,
			Locator locator) {
	
		int isNullIndex = attributes.getIndex("isNull");
		if (isNullIndex >= 0) {
			if (Boolean.valueOf(attributes.getValue(isNullIndex))) {
				// is null true
				assertEquals("NULL value " + getLocation(locator),
						null, object);
				return true;
			}
		}
		return false;
	}

	public String getLocation(Locator locator) {
		return " at " + locator.getLineNumber();
	}

}