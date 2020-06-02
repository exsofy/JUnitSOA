package com.tcua.junit.soa.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

import com.tcua.junit.soa.ParsingStatus;
import com.tcua.junit.soa.SOAKit;

public class RootHandler extends SOAEntryHandler {

	public RootHandler(SOAKit soaKit) {
		super(soaKit);
	}

	@Override
	public Object getChild(ParsingStatus currentObj, Attributes attributes,
			Locator locator) {
		return currentObj.object;
	}

	@Override
	public String getTagName() {
		return getHandlerTagName();
	}

	@Override
	public boolean extend(Element parent, Object obj) {
		parent.setAttribute("version", "1.0");
		parent.setAttribute("xmlns", "http://www.tcua.com/xmlschema/JUnitSOA");
		parent.setAttribute("xmlns:xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		parent.setAttribute("xsi:schemaLocation",
				"http://www.tcua.com/xmlschema/JUnitSOA JUnitSOA_1.0.xsd");

		return super.extend(parent, obj);
	}

	@Override
	public boolean hasEntry() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean valueChecked(ParsingStatus currentObj,
			Attributes attributes, Locator locator) {

		int iVersionIndex = attributes.getIndex("version");
		if ( iVersionIndex >= 0 ) {
			assertEquals("1.0", attributes.getValue(iVersionIndex));
		} else {
			fail("XML version not set");
		}

		return false;
	}

	public static String getHandlerTagName() {
		return "soaentry";
	}
}
