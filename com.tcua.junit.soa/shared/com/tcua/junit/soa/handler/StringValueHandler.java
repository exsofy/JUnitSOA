package com.tcua.junit.soa.handler;

import static org.junit.Assert.assertEquals;

import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

import com.tcua.junit.soa.ParsingStatus;
import com.tcua.junit.soa.SOAKit;

public class StringValueHandler extends AbstractHandler implements
		ISOAClassHandler {

	public StringValueHandler(SOAKit soaKit) {
		super(soaKit);
	}

	@Override
	public boolean extend(Element parent, Object obj) {
		return extend(parent, obj, "value");
	}

	@Override
	public boolean extend(Element parent, Object obj, String XMLName) {
		// manage null general
		if (super.extend(parent, obj))
			return true;

		parent.setAttribute(XMLName, obj.toString());

		return true;
	}

	@Override
	public boolean valueChecked(ParsingStatus currentObj,
			Attributes attributes, Locator locator) {
		if (isValueNull(currentObj.object, attributes, locator))
			return true;

		int iAttr = attributes.getIndex("value");
		if (iAttr >= 0) {
			// value
			if (!attributes.getValue(iAttr)
					.equals(currentObj.object.toString())) {
				assertEquals(
"Attribute value " + getLocation(locator),
						attributes.getValue(iAttr),
						currentObj.object.toString());
			}
		} else if ( ( iAttr = attributes.getIndex("regex") ) >= 0 ) {
			// regex
			assertTrue(
					"Attribute matches " + getLocation(locator),
					currentObj.object.toString()
					.matches(attributes.getValue(iAttr)));
		}
		// neither null, value nor regex

		// attribute is leaf
		return true;
	}

	private void assertTrue(String string, boolean matches) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getTagName() {
		return "attribute";
	}

	@Override
	public boolean hasEntry() {
		return false;
	}

}
