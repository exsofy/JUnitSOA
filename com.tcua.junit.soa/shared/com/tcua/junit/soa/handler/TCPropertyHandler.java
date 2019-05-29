package com.tcua.junit.soa.handler;

import static org.junit.Assert.assertTrue;

import org.xml.sax.Attributes;

import com.tcua.junit.soa.ParsingStatus;
import com.tcua.junit.soa.SOAKit;
import com.teamcenter.rac.kernel.TCProperty;

public class TCPropertyHandler extends StringValueHandler {

	public TCPropertyHandler(SOAKit soaKit) {
		super();
	}

	@Override
	public boolean valueChecked(ParsingStatus currentObj, Attributes attributes) {
		assertTrue("Is TCProperty", currentObj.object instanceof TCProperty);

		return super.valueChecked(currentObj, attributes);
	}

	@Override
	public String getTagName() {
		return getHandlerTagName();
	}

	public static String getHandlerTagName() {
		return "tcproperty";
	}

}
