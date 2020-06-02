package com.tcua.junit.soa.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

import com.tcua.junit.soa.ParsingStatus;
import com.tcua.junit.soa.SOAKit;

public class MapHandler extends AbstractHandler implements ISOAChildProvider {

	public MapHandler(SOAKit soaKit) {
		super(soaKit);
	}

	@Override
	public boolean extend(Element parent, Object obj) {

		if (super.extend(parent, obj))
			return true;

		parent.setAttribute("size", Integer.toString(((Map<?, ?>) obj).size()));
		for (Entry<?, ?> mapEntry : ((Map<?, ?>) obj).entrySet()) {
			Element entry = parent.getOwnerDocument().createElement(
					ElementHandler.getHandlerTagName());
			// exporter.export(mapEntry.getKey(), entry, "key");
			ISOAClassHandler handler = soaKit.getHandler(mapEntry.getKey()
					.getClass());
			handler.extend(entry, mapEntry.getKey(), "key");

			handler = soaKit.getHandler(mapEntry.getValue().getClass());
			handler.extend(entry, mapEntry.getValue());

			parent.appendChild(entry);
		}

		return true;
	}

	@Override
	public boolean valueChecked(ParsingStatus currentObj,
			Attributes attributes, Locator locator) {
		if (isValueNull(currentObj.object, attributes, locator))
			return true;

		assertTrue("Is map " + getLocation(locator),
				Map.class.isAssignableFrom(currentObj.object.getClass()));
		int iSizeIndex = attributes.getIndex("size");
		if (iSizeIndex >= 0) {
			assertEquals("Map size " + getLocation(locator),
					Integer.parseInt(attributes.getValue(iSizeIndex)),
					((Map<?, ?>) currentObj.object).size());
		}

		return false;
	}

	@Override
	public String getTagName() {
		return getStaticTagName();
	}

	public String getStaticTagName() {
		return "map";
	}

	@Override
	public Object getChild(ParsingStatus status, Attributes attributes,
			Locator locator) {

		// in array returns sequential objects for each call
		assertTrue(Map.class.isAssignableFrom(status.object.getClass()));

		// next level by key
		int iKeyIndex = attributes.getIndex("key");
		if (iKeyIndex >= 0) {
			return ((Map<?, ?>) status.object).get(attributes
					.getValue(iKeyIndex));
		}

		// next level by value
		int iValueIndex = attributes.getIndex("value");
		if (iValueIndex >= 0) {
			String value = attributes.getValue(iValueIndex);
			for (Entry<?, ?> entry : ((Map<?, ?>) status.object).entrySet()) {
				if (entry.getValue().equals(value)) {
					return entry.getKey();
				}
			}
		}

		return null;
	}

}
