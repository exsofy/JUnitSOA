package com.tcua.junit.soa.handler;

import static org.junit.Assert.fail;

import java.lang.reflect.Field;

import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

import com.tcua.junit.soa.ParsingStatus;
import com.tcua.junit.soa.SOAKit;

public class SOAEntryHandler extends AbstractHandler implements ISOAChildProvider {

	public SOAEntryHandler(SOAKit soaKit) {
		super(soaKit);
	}

	@Override
	public String getTagName() {
		return getHandlerTagName();
	}

	@Override
	public boolean extend(Element parent, Object obj) {

		if (super.extend(parent, obj))
			return true;

		for (Field field : obj.getClass().getDeclaredFields()) {
			// export object fields

			Class<?> type = field.getType();

			try {
				Object value = field.get(obj);

				ISOAClassHandler fieldHandler = soaKit.getHandler(type);

				Element child = parent.getOwnerDocument().createElement(
						fieldHandler.getTagName());
				child.setAttribute("name", field.getName());

				fieldHandler.extend(child, value);

				parent.appendChild(child);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return true;
	}

	@Override
	public boolean valueChecked(ParsingStatus currentObj,
			Attributes attributes, Locator locator) {
		if (isValueNull(currentObj.object, attributes, locator))
			return true;
		return false;
	}

	public static String getHandlerTagName() {
		return "entry";
	}

	@Override
	public Object getChild(ParsingStatus currentObj, Attributes attributes,
			Locator locator) {
		int iNameIndex = attributes.getIndex("name");
		if (iNameIndex >= 0) {
			String name = attributes.getValue(iNameIndex);

			try {
				Field field = currentObj.object.getClass().getField(name);
				return field.get(currentObj.object);
			} catch (NoSuchFieldException | SecurityException
					| IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				fail(e.getMessage() + " at " + getLocation(locator));
			}
		}
		return null;
	}

}
