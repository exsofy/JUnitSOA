package com.tcua.junit.soa.handler;


import org.w3c.dom.Element;

import com.tcua.junit.soa.SOAKit;


public abstract class AbstractHandler extends ValueHandler implements ISOAClassHandler {

	protected final SOAKit soaKit;

	public AbstractHandler(SOAKit soaKit) {
		this.soaKit = soaKit;
	}

	@Override
	public boolean extend(Element parent, Object obj, String XMLName) {
		return extend(parent, obj);
	}

	public abstract String getTagName();

	@Override
	public boolean hasEntry() {
		return true;
	}
}
