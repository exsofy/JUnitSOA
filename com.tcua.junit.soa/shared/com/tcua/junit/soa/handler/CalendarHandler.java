package com.tcua.junit.soa.handler;

import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;

import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

import com.tcua.junit.soa.ParsingStatus;
import com.tcua.junit.soa.SOAKit;

public class CalendarHandler extends AbstractHandler {

	public CalendarHandler(SOAKit soaKit) {
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

		if (obj instanceof GregorianCalendar) {
			parent.setAttribute(
					XMLName,
					((GregorianCalendar) obj).toZonedDateTime().format(
							DateTimeFormatter.ISO_OFFSET_DATE_TIME));
		} else {
			parent.setAttribute(XMLName, obj.toString());
		}

		return true;
	}

	@Override
	public boolean valueChecked(ParsingStatus currentObj,
			Attributes attributes, Locator locator) {
		return true;
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
