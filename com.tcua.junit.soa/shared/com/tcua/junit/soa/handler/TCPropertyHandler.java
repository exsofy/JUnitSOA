package com.tcua.junit.soa.handler;

import static org.junit.Assert.assertTrue;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;

import com.tcua.junit.soa.ParsingStatus;
import com.tcua.junit.soa.SOAKit;
import com.teamcenter.rac.kernel.TCProperty;

public class TCPropertyHandler extends StringValueHandler {

	public TCPropertyHandler(SOAKit soaKit) {
		super();
	}

	@Override
	public boolean valueChecked(ParsingStatus currentObj,
			Attributes attributes, Locator locator) {
		assertTrue("Is TCProperty", currentObj.object instanceof TCProperty);

		return super.valueChecked(currentObj, attributes, locator);
	}

	@Override
	public String getTagName() {
		return getHandlerTagName();
	}

	public static String getHandlerTagName() {
		return "tcproperty";
	}

}
