package com.tcua.junit.soa.handler;

import org.w3c.dom.Element;
import org.xml.sax.Locator;

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

	/**
	 * Get formated current handler location
	 * @param locator XML parser locator
	 * @return human readable location
	 */
	@Override
	public String getLocation ( Locator locator ) {
		String URL = "";
		if ( soaKit.getURL() != null ) {
			URL = soaKit.getURL().getPath() + " ";
		}
		return URL + "at line " + locator.getLineNumber();
	}
}
