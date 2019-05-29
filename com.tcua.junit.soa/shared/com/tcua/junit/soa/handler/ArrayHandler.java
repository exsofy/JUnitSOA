package com.tcua.junit.soa.handler;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.w3c.dom.Element;
import org.xml.sax.Attributes;

import com.tcua.junit.soa.ParsingStatus;
import com.tcua.junit.soa.SOAKit;

public class ArrayHandler extends AbstractHandler implements ISOAChildProvider {

	public ArrayHandler(SOAKit soaKit) {
		super(soaKit);
	}

	@Override
	public boolean extend(Element parent, Object obj) {

		if (super.extend(parent, obj))
			return true;

		parent.setAttribute("size", Integer.toString(((Object[]) obj).length));
		for (Object arrayElem : (Object[]) obj) {
			Element entry = parent.getOwnerDocument().createElement(
					ElementHandler.getHandlerTagName());
			ISOAClassHandler handler = soaKit.getHandler(obj.getClass()
					.getComponentType());
			handler.extend(entry, arrayElem);
			parent.appendChild(entry);
		}

		return true;
	}

	@Override
	public boolean valueChecked(ParsingStatus currentObj, Attributes attributes) {
		if (isValueNull(currentObj.object, attributes))
			return true;
		
		assertTrue("Is array", currentObj.object.getClass().isArray());
		int iSizeIndex = attributes.getIndex("size");
		if ( iSizeIndex >=0 ) {
			assertEquals ( "Array size", Integer.parseInt(attributes.getValue(iSizeIndex)), ((Object[])currentObj.object).length );
		}
		
		return false;
	}

	@Override
	public String getTagName() {
		return "array";
	}

	@Override
	public Object getChild(ParsingStatus status, Attributes attributes) {

		// in array returns sequential objects for each call
		assertTrue(status.object.getClass().isArray());

		int index;
		if (status.handlerState == null
				|| !(status.handlerState instanceof Integer)) {
			// no previous status, start with 0 create new next value with 1
			index = 0;
			status.handlerState = Integer.valueOf(1);
		} else {
			// use next value and increment it
			index = ((Integer) status.handlerState).intValue();
			status.handlerState = Integer.valueOf(index + 1);
		}

		if (((Object[]) status.object).length > index) {
			return ((Object[]) status.object)[index];
		}

		return null;
	}

}
