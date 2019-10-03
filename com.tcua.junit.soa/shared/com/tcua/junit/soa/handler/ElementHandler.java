package com.tcua.junit.soa.handler;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;

import com.tcua.junit.soa.ParsingStatus;
import com.tcua.junit.soa.SOAKit;

public class ElementHandler extends AbstractHandler {

	public ElementHandler(SOAKit soaKit) {
		super(soaKit);
	}

	@Override
	public String getTagName() {
		return getHandlerTagName();
	}

	public static String getHandlerTagName() {
		return "element";
	}

	@Override
	public boolean valueChecked(ParsingStatus currentObj,
			Attributes attributes, Locator locator) {
		if (isValueNull(currentObj.object, attributes, locator))
			return true;

		return false;
	}

	@Override
	public boolean hasEntry() {
		// element does not have SOA representation
		return false;
	}

}
