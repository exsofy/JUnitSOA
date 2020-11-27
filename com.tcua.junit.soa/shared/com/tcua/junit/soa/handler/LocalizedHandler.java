package com.tcua.junit.soa.handler;

import java.util.Locale;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;

import com.tcua.junit.soa.ParsingStatus;
import com.tcua.junit.soa.SOAKit;

public class LocalizedHandler extends AbstractHandler implements ISOAChildProvider {

	public LocalizedHandler(SOAKit soaKit) {
		super(soaKit);
	}
	
	@Override
	public boolean valueChecked(ParsingStatus currentObj, Attributes attributes, Locator locator) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object getChild(ParsingStatus currentObj, Attributes attributes, Locator locator) {

		String locale = attributes.getValue("locale");
		
		if ( locale != null && !locale.isEmpty() && locale.equals( Locale.getDefault().getLanguage() ) ) {
			// locale ok, check current object
			return currentObj.object;
		}
		
		return null;
	}

	@Override
	public String getTagName() {
		return getStaticTagName();
	}

	public String getStaticTagName() {
		return "localized";
	}

}
